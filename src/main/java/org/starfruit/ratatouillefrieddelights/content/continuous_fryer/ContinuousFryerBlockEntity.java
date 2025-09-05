package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;

import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

public class ContinuousFryerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    // === 可调参数 ===
    public static final int FRYING_TIME_TICKS = 20;    // 处理总时长（1秒）
    public static final int OIL_PER_ITEM_MB   = 100;   // 每个物品消耗油量（示例值）
    public static final int TANK_CAPACITY_MB  = 4000;  // 内置油槽容量（示例值）
    private static final float SPEED_PER_TICK = 1f / 8f;

    // === 内部状态 ===
    protected SmartFluidTankBehaviour oilTank;
    protected TransportedItemStack heldItem;
    protected int processingTicks;

    // 水平面每个方向的物品能力（用于带/漏斗对接）
    protected final Map<Direction, FryerItemHandler> itemHandlers = new IdentityHashMap<>();

    public ContinuousFryerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        for (Direction d : Iterate.horizontalDirections) {
            itemHandlers.put(d, new FryerItemHandler(this, d));
        }
    }

    // === 能力注册：物品（水平）、流体（除UP） ===
    public static void registerCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<? extends ContinuousFryerBlockEntity> beType) {
        //event.registerBlockEntity(
        //        Capabilities.ItemHandler.BLOCK,
        //        RFDBlockEntityTypes.CONTINUOUS_FRYER.get(),
        //        (be, ctx) -> (ctx != null && ctx.getAxis().isHorizontal()) ? be.itemHandlers.get(ctx) : null
        //);

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                RFDBlockEntityTypes.CONTINUOUS_FRYER.get(),
                (be, ctx) -> (ctx != Direction.UP) ? be.oilTank.getCapability() : null
        );
    }

    // === 行为注册 ===
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this)
                .allowingBeltFunnels()
                .setInsertionHandler(this::tryInsertingFromSide));

        // 连续油炸器油槽：允许插入与抽取（可按需改成只插入）
        behaviours.add(oilTank = SmartFluidTankBehaviour.single(this, TANK_CAPACITY_MB)
                .allowInsertion()
                .allowExtraction());
    }

    // === 传送带物品从侧面插入时的处理 ===
    private ItemStack tryInsertingFromSide(TransportedItemStack transported, Direction side, boolean simulate) {
        ItemStack in = transported.stack;
        ItemStack remainder = ItemStack.EMPTY;

        // 正在占用就不接
        if (!getHeldItemStack().isEmpty())
            return in;

        boolean fryable = canFry(in);
        if (in.getCount() > 1 && fryable) {
            // 只处理单个；多余返回
            remainder = in.copyWithCount(in.getCount() - 1);
            in = in.copyWithCount(1);
        }

        if (simulate)
            return remainder;

        TransportedItemStack copy = transported.copy();
        copy.stack = in.copy();
        copy.beltPosition = side.getAxis().isVertical() ? .5f : 0f;
        copy.prevSideOffset = copy.sideOffset;
        copy.prevBeltPosition = copy.beltPosition;

        setHeldItem(copy, side);
        setChanged();
        sendData();
        return remainder;
    }

    public ItemStack getHeldItemStack() {
        return heldItem == null ? ItemStack.EMPTY : heldItem.stack;
    }

    public void setHeldItem(TransportedItemStack held, Direction insertedFrom) {
        this.heldItem = held;
        this.heldItem.insertedFrom = insertedFrom;
    }

    // === 每tick逻辑：移动→中点触发→处理→输出 ===
    @Override
    public void tick() {
        super.tick();

        if (heldItem == null) {
            processingTicks = 0;
            return;
        }

        boolean client = level.isClientSide && !isVirtual();

        // 正在处理阶段
        if (processingTicks > 0) {
            heldItem.prevBeltPosition = .5f;
            boolean wasAtStart = processingTicks == FRYING_TIME_TICKS;
            if (!client || processingTicks < FRYING_TIME_TICKS)
                processingTicks--;

            if (!continueProcessing()) {
                processingTicks = 0;
                notifyUpdate();
                return;
            }
            if (wasAtStart != (processingTicks == FRYING_TIME_TICKS))
                sendData();
            return;
        }

        // 移动阶段
        heldItem.prevBeltPosition = heldItem.beltPosition;
        heldItem.prevSideOffset   = heldItem.sideOffset;

        heldItem.beltPosition += SPEED_PER_TICK;

        if (heldItem.beltPosition > 1f) {
            heldItem.beltPosition = 1f;

            if (client) return;

            Direction side = heldItem.insertedFrom;

            // 1) 优先尝试交给对侧 Belt Funnel
            ItemStack afterFunnel = getBehaviour(DirectBeltInputBehaviour.TYPE)
                    .tryExportingToBeltFunnel(heldItem.stack, side.getOpposite(), false);
            if (afterFunnel != null) {
                if (afterFunnel.getCount() != heldItem.stack.getCount()) {
                    if (afterFunnel.isEmpty()) heldItem = null;
                    else heldItem.stack = afterFunnel;
                    notifyUpdate();
                    return;
                }
                if (!afterFunnel.isEmpty())
                    return;
            }

            // 2) 尝试交给前方具备 DirectBeltInput 的目标
            BlockPos nextPos = worldPosition.relative(side);
            DirectBeltInputBehaviour next = BlockEntityBehaviour.get(level, nextPos, DirectBeltInputBehaviour.TYPE);

            if (next == null) {
                // 3) 前方非实体面 → 抛出实体
                if (!BlockHelper.hasBlockSolidSide(level.getBlockState(nextPos), level, nextPos, side.getOpposite())) {
                    ItemStack ejected = heldItem.stack;
                    Vec3 outPos = VecHelper.getCenterOf(worldPosition)
                            .add(Vec3.atLowerCornerOf(side.getNormal()).scale(.75));
                    float v = SPEED_PER_TICK;
                    Vec3 outMotion = Vec3.atLowerCornerOf(side.getNormal()).scale(v).add(0, 1/8f, 0);
                    outPos.add(outMotion.normalize());
                    ItemEntity entity = new ItemEntity(level, outPos.x, outPos.y + 6 / 16f, outPos.z, ejected);
                    entity.setDeltaMovement(outMotion);
                    entity.setDefaultPickUpDelay();
                    entity.hurtMarked = true;
                    level.addFreshEntity(entity);

                    heldItem = null;
                    notifyUpdate();
                }
                return;
            }

            if (!next.canInsertFromSide(side))
                return;

            ItemStack returned = next.handleInsertion(heldItem.copy(), side, false);
            if (returned.isEmpty()) {
                heldItem = null;
                notifyUpdate();
                return;
            }
            if (returned.getCount() != heldItem.stack.getCount()) {
                heldItem.stack = returned;
                notifyUpdate();
            }
            return;
        }

        // 到达中点，若可炸则进入处理
        if (heldItem.prevBeltPosition < .5f && heldItem.beltPosition >= .5f) {
            if (!canFry(heldItem.stack))
                return;
            heldItem.beltPosition = .5f;
            if (client) return;
            processingTicks = FRYING_TIME_TICKS;
            sendData();
        }
    }

    /**
     * 处理阶段核心：
     * - >5tick：模拟消耗油，油不够则回拨等待（processingTicks=FRYING_TIME_TICKS）
     * - ≤5tick：真实消耗油并替换物品
     */
    protected boolean continueProcessing() {
        if (level.isClientSide && !isVirtual())
            return true;

        if (processingTicks < 5)
            return true;

        ItemStack in = heldItem.stack;
        if (!canFry(in))
            return false;

        // 先拿到“油炸后结果”，用于最终替换；>5tick 阶段不替换，只做油量检查
        ItemStack result = getFryResult(in);
        if (result.isEmpty())
            return false;

        FluidStack need = new FluidStack(getOilFluidKey(), OIL_PER_ITEM_MB); // getOilFluidKey(): 只需提供流体类型标识
        if (processingTicks > 5) {
            // 模拟检查是否有足够油
            int drainedSim = oilTank.getPrimaryHandler().drain(need, FluidAction.SIMULATE).getAmount();
            if (drainedSim < OIL_PER_ITEM_MB) {
                processingTicks = FRYING_TIME_TICKS; // 油不够，等待补充
                return true;
            }
            return true;
        }

        // ≤5tick：真实消耗+替换
        int drained = oilTank.getPrimaryHandler().drain(need, FluidAction.EXECUTE).getAmount();
        if (drained < OIL_PER_ITEM_MB) {
            processingTicks = FRYING_TIME_TICKS; // 边界情况：有人把油抽走了，再等
            return true;
        }

        // 替换 Held 物品为炸后结果；无副产物
        heldItem.stack = result.copy();
        notifyUpdate();

        // 可选：授予成就（如果有自定义的）或使用 Create 的某个成就
        // award(AllAdvancements.SOME_ADVANCEMENT);

        return true;
    }

    // === 工具方法 ===

    private float itemMovementPerTick() {
        return SPEED_PER_TICK;
    }

    /**
     * 占位：判断是否可油炸
     * 之后可改为：查询配方表（RecipeManager）、物品标签、JSON 数据等
     */
    protected boolean canFry(ItemStack in) {
        if (in.isEmpty()) return false;
        ItemStack out = getFryResult(in);
        return !out.isEmpty() && !ItemStack.isSameItemSameComponents(out, in);
    }

    /**
     * 占位：获取油炸结果物
     * TODO：接入你的配方系统（例如 RecipeType<ContinuousFryerRecipe>）
     * 这里先写一个空实现：默认不可炸（返回 ItemStack.EMPTY）
     */
    protected ItemStack getFryResult(ItemStack in) {
        // 示例：在这里根据自定义配方返回结果物
        // return ContinuousFryerRecipes.getResult(level, in);
        return RFDItems.FRENCH_FRIES.asStack();
    }

    /**
     * 占位：指定“油”的流体身份（用于构造需要量的 FluidStack）
     * TODO：改成你的油流体（如 RFDFluids.COOKING_OIL.get().getSource() 或注册的 FluidKey）
     */
    protected net.minecraft.world.level.material.Fluid getOilFluidKey() {
        // return YourFluids.COOKING_OIL.get().getSource();
        return net.minecraft.world.level.material.Fluids.WATER; // 占位：先用水占位，务必替换
    }

    // === 数据保存同步 ===
    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        tag.putInt("ProcessingTicks", processingTicks);
        if (heldItem != null)
            tag.put("HeldItem", heldItem.serializeNBT(registries));
        super.write(tag, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        heldItem = null;
        processingTicks = tag.getInt("ProcessingTicks");
        if (tag.contains("HeldItem"))
            heldItem = TransportedItemStack.read(tag.getCompound("HeldItem"), registries);
        super.read(tag, registries, clientPacket);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    // === 护目镜显示：复用 Create 的 containedFluidTooltip ===
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(
                tooltip,
                isPlayerSneaking,
                level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null)
        );
    }

    // === 简单的 ItemHandler 占位（如需从外部插入/取出）===
    // 如果你不打算让外部直接从方块侧面"取/塞"物品，这个可以保持空壳或只读。
    public static class FryerItemHandler /* implements IItemHandler */ {
        final ContinuousFryerBlockEntity be;
        final Direction side;

        public FryerItemHandler(ContinuousFryerBlockEntity be, Direction side) {
            this.be = be;
            this.side = side;
        }

        // 你可以在这里实现 IItemHandler 逻辑，或保持由传送带行为接管。
        // 建议：像 Create Drain 一样，仅用于传送带/漏斗交互，不作为通用背包。
    }
}
