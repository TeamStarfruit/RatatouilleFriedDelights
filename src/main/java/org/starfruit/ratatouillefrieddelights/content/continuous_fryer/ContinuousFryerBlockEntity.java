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

    private FryingRecipe lastRecipe;

    public int fryerLength;
    protected BlockPos controller;

    public ContinuousFryerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        controller = pos;
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

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo,
                                     BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target instanceof ContinuousFryerBlockEntity fryer && !connectedViaAxes) {
            if (!getController().equals(fryer.getController()))
                return 0;

            Direction.Axis axis1 = stateFrom.getValue(ContinuousFryerBlock.HORIZONTAL_FACING).getAxis();
            Direction.Axis axis2 = stateTo.getValue(ContinuousFryerBlock.HORIZONTAL_FACING).getAxis();
            if (axis1 != axis2)
                return 0;

            return 1;
        }
        return 0;
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

            level.setBlock(pos, newState, 6);
            fryer.attachKinetics();
            fryer.requestModelDataUpdate();
            fryer.notifyUpdate();
        }
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public Direction getFacingDirection() {
        return getBlockState()
                .getValue(ContinuousFryerBlock.HORIZONTAL_FACING);
    }

    public void updateNeighbours() {
        if (level == null) return;

        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos neighbourPos = worldPosition.relative(dir);
            BlockState neighbourState = level.getBlockState(neighbourPos);

            if (neighbourState.getBlock() instanceof ContinuousFryerBlock) {
                BlockEntity fryerBe = level.getBlockEntity(neighbourPos);
                if (fryerBe instanceof ContinuousFryerBlockEntity fryer && !fryer.isRemoved()) {
                    fryer.setController(fryer.getBlockPos());
                    fryer.updateConnectivity();
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) return;
        if (getControllerBE() == null || getFacingDirection().getAxis() != getControllerBE().getFacingDirection().getAxis()) {
            setController(worldPosition);
            updateConnectivity();
            updateNeighbours();
        }
    }

        @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }


    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (controller != null)
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        compound.putBoolean("IsController", isController());
        compound.putInt("Length", fryerLength);

        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (compound.getBoolean("IsController"))
            controller = worldPosition;
        if (!isController())
            controller = NBTHelper.readBlockPos(compound, "Controller");
        fryerLength = compound.getInt("Length");

        super.read(compound, registries, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null));
    }
}
