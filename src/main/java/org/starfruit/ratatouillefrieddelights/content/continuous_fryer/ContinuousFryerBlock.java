package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.neoforge.capabilities.Capabilities;

// TODO: 替换为你自己的 BE 类型注册类
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;

/**
 * 连续油炸器 Block（雏形）
 * - 支持向内部油槽灌油（FluidHelper.tryEmptyItemIntoBE）
 * - 不支持 GenericItemEmptying（炸锅不“倒液体”）
 * - 空手右键可取回当前 held 物品
 * - 比较器输出 = 内部油槽液位
 */
public class ContinuousFryerBlock extends Block implements IWrenchable, IBE<ContinuousFryerBlockEntity> {

    public ContinuousFryerBlock(Properties properties) {
        super(properties);
    }

    /**
     * 右键交互（参照 ItemDrainBlock 的 useItemOn 写法）
     * - 若手持是纯方块物品且本身不带流体能力，交给默认交互
     * - 尝试把手上容器内的油灌入 BE（成功则消耗动作）
     * - 否则，若锅里正“卡着”一个物品，空手右键可把它退回背包
     */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {

        // 与 Drain 一致：方块物品（且该物品没有流体能力）交给默认交互
        if (stack.getItem() instanceof BlockItem && stack.getCapability(Capabilities.FluidHandler.ITEM) == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        return onBlockEntityUseItemOn(level, pos, be -> {
            // 1) 玩家手持容器尝试往油槽灌油
            if (!stack.isEmpty()) {
                ItemInteractionResult tryFill = tryFillOil(level, player, hand, stack, be);
                if (tryFill.consumesAction())
                    return tryFill;
            }

            // 2) 空手右键：把 held 物品退回玩家背包（仅服务端）
            ItemStack heldItemStack = be.getHeldItemStack();
            if (!level.isClientSide && !heldItemStack.isEmpty()) {
                player.getInventory().placeItemBackInInventory(heldItemStack);
                be.heldItem = null;
                be.notifyUpdate();
            }
            return ItemInteractionResult.SUCCESS;
        });
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
        super.updateEntityAfterFallOn(world, entity);
        if (!(entity instanceof ItemEntity itemEntity))
            return;
        if (!entity.isAlive())
            return;
        if (entity.level().isClientSide)
            return;

        DirectBeltInputBehaviour input = BlockEntityBehaviour.get(world, entity.blockPosition(), DirectBeltInputBehaviour.TYPE);
        if (input == null)
            return;

        Vec3 delta = entity.getDeltaMovement().multiply(1, 0, 1).normalize();
        Direction nearest = Direction.getNearest(delta.x, delta.y, delta.z);
        ItemStack remainder = input.handleInsertion(itemEntity.getItem(), nearest, false);
        itemEntity.setItem(remainder);
        if (remainder.isEmpty())
            itemEntity.discard();
    }

    /**
     * 只处理“向 BE 内灌油”的交互。
     * 不做 GenericItemEmptying（炸锅不从物品里倒液体）。
     */
    protected ItemInteractionResult tryFillOil(Level level, Player player, InteractionHand hand,
                                               ItemStack heldItem, ContinuousFryerBlockEntity be) {
        // 允许插入 → 尝试把玩家手上的流体物品“倒入”锅内油槽
        be.internalTank.allowInsertion();
        ItemInteractionResult res = FluidHelper.tryEmptyItemIntoBE(level, player, hand, heldItem, be)
                ? ItemInteractionResult.SUCCESS
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        be.internalTank.forbidInsertion(); // 还原为默认策略（如你在 BE 里设置了只读/只写，可以按需调整）
        return res;
    }

    // 形状：沿用 Create 的 13px 壳体
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return AllShapes.CASING_13PX.get(Direction.UP);
    }

    /**
     * 方块被移除时，掉落锅内正在处理/等待的物品
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;

        withBlockEntityDo(level, pos, be -> {
            ItemStack held = be.getHeldItemStack();
            if (!held.isEmpty())
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), held);
        });

        level.removeBlockEntity(pos);
    }

    // ---- IBE 接口实现 ----

    @Override
    public Class<ContinuousFryerBlockEntity> getBlockEntityClass() {
        return ContinuousFryerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ContinuousFryerBlockEntity> getBlockEntityType() {
        return RFDBlockEntityTypes.CONTINUOUS_FRYER.get();
    }

    // 放置时打点成就（与 Drain 一致）
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        AdvancementBehaviour.setPlacedBy(level, pos, placer);
    }

    // ---- 比较器输出：油槽液位 ----

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return ComparatorUtil.levelOfSmartFluidTank(level, pos);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}