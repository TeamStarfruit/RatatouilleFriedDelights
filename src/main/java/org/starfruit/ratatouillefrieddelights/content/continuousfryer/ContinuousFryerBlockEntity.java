package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import java.util.*;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;

import static com.simibubi.create.content.fluids.tank.FluidTankBlockEntity.getCapacityMultiplier;
import static net.minecraft.core.Direction.AxisDirection.NEGATIVE;
import static net.minecraft.core.Direction.AxisDirection.POSITIVE;

public class ContinuousFryerBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public static final int FILLING_TIME = 20;

    private FryingRecipe lastRecipe;

    public int fryerLength;
    public int index;
    protected BlockPos controller;

    protected IFluidHandler fluidHandler;
    protected FluidTank tankInventory;

    protected FryerInventory itemInventory;
    protected IItemHandler itemHandler;

    public ContinuousFryerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        controller = pos;
        tankInventory = createTankInventory();
    }

    protected SmartFluidTank createTankInventory() {
        return new SmartFluidTank(getCapacityMultiplier(), $->{});
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                RFDBlockEntityTypes.CONTINUOUS_FRYER.get(),
                (be, context) -> {
                    be.initCapability();
                    return be.fluidHandler;
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                RFDBlockEntityTypes.CONTINUOUS_FRYER.get(),
                (be, context) -> {
                    be.initCapability();
                    return be.itemHandler;
                }
        );
    }

    void refreshCapability() {
        fluidHandler = null;
        itemHandler = null;
        invalidateCapabilities();
    }

    void initCapability() {
        if (itemHandler != null && fluidHandler != null || level == null) return;

        if (!isController()) {
            ContinuousFryerBlockEntity controllerBE = getControllerBE();
            if (controllerBE == null) return;

            controllerBE.initCapability();
            this.itemHandler = new ItemHandlerFryerSegment(controllerBE.getItemInventory(), index);
            this.fluidHandler = controllerBE.fluidHandler;
            return;
        }

        Direction.Axis axis = getFryerFacing().getAxis();
        BlockPos cursor = getBlockPos();
        List<FluidTank> chain = new ArrayList<>();

        for (int i = 0; i < fryerLength; i++) {
            BlockEntity be = level.getBlockEntity(cursor);
            if (be instanceof ContinuousFryerBlockEntity fryer) {
                if (fryer.tankInventory == null) {
                    fryer.tankInventory = createTankInventory();
                }
                chain.add(fryer.tankInventory);
            }
            cursor = axis == Direction.Axis.X ? cursor.east() : cursor.south();
        }

        fluidHandler = new CombinedTankWrapper(chain.toArray(new FluidTank[0]));
        itemHandler = new ItemHandlerFryerSegment(getItemInventory(), index);
    }

    public FryerInventory getItemInventory() {
        if (!isController()) {
            ContinuousFryerBlockEntity controllerBE = getControllerBE();
            if (controllerBE != null)
                return controllerBE.getItemInventory();
            return null;
        }
        if (itemInventory == null) {
            itemInventory = new FryerInventory(this);
        }
        return itemInventory;
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
        if (controller == null || level == null)
            return null;
        if (!level.isLoaded(controller))
            return null;
        BlockEntity be = level.getBlockEntity(controller);
        if (!(be instanceof ContinuousFryerBlockEntity))
            return null;
        return (ContinuousFryerBlockEntity) be;
    }

    public void setController(BlockPos controller) {
        if (level == null || level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
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
            fryer.refreshCapability();
            fryer.requestModelDataUpdate();
            fryer.notifyUpdate();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public Direction getFryerFacing() {
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
        if (getControllerBE() == null || getFryerFacing().getAxis() != getControllerBE().getFryerFacing().getAxis()) {
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

    public Direction getMovementFacing() {
        Direction.Axis axis = getFryerFacing().getAxis();
        return Direction.fromAxisAndDirection(axis, getFryerMovementSpeed() < 0 ^ axis == Direction.Axis.X ? NEGATIVE : POSITIVE);
    }

    protected Vec3i getMovementDirection(boolean firstHalf, boolean ignoreHalves) {
        if (getSpeed() == 0)
            return BlockPos.ZERO;

        final BlockState blockState = getBlockState();
        final Direction beltFacing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        final Direction.Axis axis = beltFacing.getAxis();

        Direction movementFacing = Direction.get(axis == Direction.Axis.X ? NEGATIVE : POSITIVE, axis);
        if (getSpeed() < 0)
            movementFacing = movementFacing.getOpposite();

        return movementFacing.getNormal();
    }


    public Vec3i getMovementDirection(boolean firstHalf) {
        return this.getMovementDirection(firstHalf, false);
    }

    public Vec3i getFryerChainDirection() {
        return this.getMovementDirection(true, true);
    }

    public float getFryerMovementSpeed() {
        return getSpeed() / 480f;
    }

    public float getDirectionAwareBeltMovementSpeed() {
        int offset = getFryerFacing().getAxisDirection()
                .getStep();
        if (getFryerFacing().getAxis() == Direction.Axis.X)
            offset *= -1;
        return getFryerMovementSpeed() * offset;
    }

}
