package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import java.util.*;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.drain.ItemDrainItemHandler;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
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
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

public class ContinuousFryerBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public static final int FILLING_TIME = 20;

    SmartFluidTankBehaviour internalTank;
    TransportedItemStack heldItem;
    protected int processingTicks;
    Map<Direction, ContinuousFryerItemHandler> itemHandlers;
    private FryingRecipe lastRecipe;

    public int fryerLength;
    protected BlockPos controller;

    public ContinuousFryerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        controller = pos;

        itemHandlers = new IdentityHashMap<>();
        for (Direction d : Iterate.horizontalDirections) {
            itemHandlers.put(d, new ContinuousFryerItemHandler(this, d));
        }
    }

    public boolean shouldRenderAxis() {
        BlockState state = getBlockState();
        if (!state.hasProperty(ContinuousFryerBlock.PART)) {
            return false;
        }
        FryerPart part = state.getValue(ContinuousFryerBlock.PART);

        return part == FryerPart.END || part == FryerPart.SINGLE;
    }

    public ContinuousFryerBlockEntity getControllerBE() {
        if (controller == null)
            return null;
        if (!level.isLoaded(controller))
            return null;
        BlockEntity be = level.getBlockEntity(controller);
        if (be == null || !(be instanceof ContinuousFryerBlockEntity))
            return null;
        return (ContinuousFryerBlockEntity) be;
    }

    public void setController(BlockPos controller) {
        this.controller = controller;
    }

    public BlockPos getController() {
        return controller == null ? worldPosition : controller;
    }

    public boolean isController() {
        return controller != null && worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }

    public void updateConnectivity() {
        if (level == null || level.isClientSide) return;
        if (!isController())
            return;

        Direction.Axis axis = getBlockState()
                .getValue(ContinuousFryerBlock.HORIZONTAL_FACING)
                .getAxis();

        List<BlockPos> chain = new ArrayList<>();
        chain.add(worldPosition);

        BlockPos cursor = worldPosition;
        while (true) {
            cursor = axis == Direction.Axis.X ? cursor.east() : cursor.south();
            BlockState state = level.getBlockState(cursor);
            if (!(state.getBlock() instanceof ContinuousFryerBlock))
                break;

            Direction.Axis otherAxis = state.getValue(ContinuousFryerBlock.HORIZONTAL_FACING).getAxis();
            if (otherAxis != axis)
                break;

            chain.add(cursor);
        }

        cursor = worldPosition;
        while (true) {
            cursor = axis == Direction.Axis.X ? cursor.west() : cursor.north();
            BlockState state = level.getBlockState(cursor);
            if (!(state.getBlock() instanceof ContinuousFryerBlock))
                break;

            Direction.Axis otherAxis = state.getValue(ContinuousFryerBlock.HORIZONTAL_FACING).getAxis();
            if (otherAxis != axis)
                break;

            chain.addFirst(cursor);
        }

        int length = chain.size();
        this.fryerLength = length;
        for (int i = 0; i < length; i++) {
            BlockPos pos = chain.get(i);
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof ContinuousFryerBlockEntity fryer))
                continue;

            fryer.setController(chain.getFirst());
            fryer.fryerLength = length;

            FryerPart part;
            if (length == 1) {
                part = FryerPart.SINGLE;
            } else if (i == 0 || i == length - 1) {
                part = FryerPart.END;
            } else {
                part = FryerPart.MIDDLE;
            }
            BlockState old = level.getBlockState(pos);
            BlockState newState = old;

            if (old.getValue(ContinuousFryerBlock.PART) != part) {
                newState = newState.setValue(ContinuousFryerBlock.PART, part);
            }

            if (part == FryerPart.END) {
                if (i == 0) {
                    Direction facing = (axis == Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;
                    newState = newState.setValue(ContinuousFryerBlock.HORIZONTAL_FACING, facing);
                } else if (i == length - 1) {
                    Direction facing = (axis == Direction.Axis.X) ? Direction.WEST : Direction.NORTH;
                    newState = newState.setValue(ContinuousFryerBlock.HORIZONTAL_FACING, facing);
                }
            }

            if (newState != old) {
                level.setBlock(pos, newState, 6);
                fryer.requestModelDataUpdate();
                fryer.notifyUpdate();
            }
        }
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                RFDBlockEntityTypes.CONTINUOUS_FRYER.get(),
                (be, context) -> {
                    if (context != null && context.getAxis().isHorizontal())
                        return be.itemHandlers.get(context);
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                RFDBlockEntityTypes.CONTINUOUS_FRYER.get(),
                (be, context) -> {
                    if (context != Direction.UP)
                        return be.internalTank.getCapability();
                    return null;
                }
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this).allowingBeltFunnels()
                .setInsertionHandler(this::tryInsertingFromSide));
        behaviours.add(internalTank = SmartFluidTankBehaviour.single(this, 1500)
                .allowExtraction()
                .forbidInsertion());
        registerAwardables(behaviours, AllAdvancements.DRAIN, AllAdvancements.CHAINED_DRAIN);
    }

    private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
        ItemStack inserted = transportedStack.stack;
        ItemStack returned = ItemStack.EMPTY;

        if (!getHeldItemStack().isEmpty())
            return inserted;

        if (inserted.getCount() > 1 && GenericItemEmptying.canItemBeEmptied(level, inserted)) {
            returned = inserted.copyWithCount(inserted.getCount() - 1);
            inserted = inserted.copyWithCount(1);
        }

        if (simulate)
            return returned;

        transportedStack = transportedStack.copy();
        transportedStack.stack = inserted.copy();
        transportedStack.beltPosition = side.getAxis()
                .isVertical() ? .5f : 0;
        transportedStack.prevSideOffset = transportedStack.sideOffset;
        transportedStack.prevBeltPosition = transportedStack.beltPosition;
        setHeldItem(transportedStack, side);
        setChanged();
        sendData();

        return returned;
    }

    public ItemStack getHeldItemStack() {
            return heldItem == null ? ItemStack.EMPTY : heldItem.stack;
        }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) return;

        if (heldItem == null) {
            processingTicks = 0;
            return;
        }

        // 正在烹饪
        if (processingTicks > 0) {
            processingTicks--;
            if (processingTicks == 0) {
                process();
            }
            return;
        }

        // 空闲 → 尝试查找 Frying 配方
        ItemStack in = heldItem.stack;
        if (in.isEmpty()) return;

        Optional<RecipeHolder<FryingRecipe>> match = RFDRecipeTypes.FRYING.find(new SingleRecipeInput(in), level);

        if (match.isPresent()) {
            FryingRecipe recipe = match.get().value();

            // 检查油量是否足够
//            if (internalTank.getPrimaryHandler().getFluidAmount() >= recipe.getOilCost()) {
                lastRecipe = recipe;
                processingTicks = recipe.getProcessingDuration();
                sendData();
//            }
        }
    }

    // 在处理完成时调用
    private void process() {
        if (lastRecipe == null || heldItem == null) return;

        ItemStack in = heldItem.stack;
        if (!lastRecipe.getIngredients().get(0).test(in)) return;

        // 消耗 1 个输入
        in.shrink(1);

        // 消耗油
        internalTank.allowExtraction();
//        internalTank.getPrimaryHandler().drain(lastRecipe.getOilCost(), FluidAction.EXECUTE);
        internalTank.forbidExtraction();

        // 产物
        ItemStack result = lastRecipe.getResultItem(level.registryAccess()).copy();
        heldItem.stack = result;

        notifyUpdate();
        setChanged();
    }


    private float itemMovementPerTick() {
        return 1 / 8f;
    }

        @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public void setHeldItem(TransportedItemStack heldItem, Direction insertedFrom) {
        this.heldItem = heldItem;
        this.heldItem.insertedFrom = insertedFrom;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (controller != null)
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        compound.putBoolean("IsController", isController());
        compound.putInt("Length", fryerLength);

        compound.putInt("ProcessingTicks", processingTicks);
        if (heldItem != null)
            compound.put("HeldItem", heldItem.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (compound.getBoolean("IsController"))
            controller = worldPosition;
        if (!isController())
            controller = NBTHelper.readBlockPos(compound, "Controller");
        fryerLength = compound.getInt("Length");

        heldItem = null;
        processingTicks = compound.getInt("ProcessingTicks");
        if (compound.contains("HeldItem"))
            heldItem = TransportedItemStack.read(compound.getCompound("HeldItem"), registries);
        super.read(compound, registries, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null));
    }

}
