package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import java.util.*;
import java.util.function.Function;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import org.starfruit.ratatouillefrieddelights.util.Lang;

import static com.simibubi.create.content.fluids.tank.FluidTankBlockEntity.getCapacityMultiplier;
import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel.*;
import static net.minecraft.core.Direction.AxisDirection.NEGATIVE;
import static net.minecraft.core.Direction.AxisDirection.POSITIVE;

public class ContinuousFryerBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    public Map<Entity, FryerMovementHandler.FringEntityInfo> passengers;
    private FryingRecipe lastRecipe;

    public int fryerLength;
    public int index;
    protected BlockPos controller;

    protected IFluidHandler fluidHandler;
    protected FluidTank tankInventory;

    protected FryerInventory itemInventory;
    protected IItemHandler itemHandler;

    public VersionedInventoryTrackerBehaviour invVersionTracker;
    private LerpedFloat fluidLevel;
    protected boolean forceFluidLevelUpdate;

    private boolean updateConnectivity = true;
    public ContinuousFryerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
//        controller = pos;
        tankInventory = createTankInventory();
        forceFluidLevelUpdate = true;
    }

    protected SmartFluidTank createTankInventory() {
        return new SmartFluidTank(getCapacityMultiplier(), this::onFluidStackChanged);
    }

    @Override
    public AABB createRenderBoundingBox() {
        if (!isController())
            return super.createRenderBoundingBox();
        else
            return super.createRenderBoundingBox().inflate(fryerLength + 1);
    }

    public void applyFluidTankSize(int blocks) {
        tankInventory.setCapacity(blocks * getCapacityMultiplier());
        int overflow = tankInventory.getFluidAmount() - tankInventory.getCapacity();
        if (overflow > 0)
            tankInventory.drain(overflow, IFluidHandler.FluidAction.EXECUTE);
        forceFluidLevelUpdate = true;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        var controllerBE = getControllerBE();
        if (controllerBE == null || level == null)
            return false;

        containedFluidTooltip(tooltip, isPlayerSneaking, controllerBE.tankInventory);

        BlazeBurnerBlock.HeatLevel heatLevel = controllerBE.getHeatLevel();
        Lang.translate("fryer.heat_level").forGoggles(tooltip);
        Lang.translate("fryer."+heatLevel.getSerializedName()).style(ChatFormatting.GOLD).forGoggles(tooltip, 1);

        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    private List<ContinuousFryerBlockEntity> getConnectedChain() {
        if (level == null) return List.of(this);
        ContinuousFryerBlockEntity controller = getControllerBE();
        if (controller == null) return List.of(this);

        Direction.Axis axis = controller.getFryerFacing().getAxis();
        List<ContinuousFryerBlockEntity> chain = new ArrayList<>();

        BlockPos cursor = controller.getBlockPos();
        for (int i = 0; i < controller.fryerLength; i++) {
            BlockEntity be = level.getBlockEntity(cursor);
            if (be instanceof ContinuousFryerBlockEntity fryer) {
                chain.add(fryer);
            }
            cursor = axis == Direction.Axis.X ? cursor.east() : cursor.south();
        }
        return chain;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (level == null || level.isClientSide)
            return;
        split(true);
    }

    public void split(boolean dropContents) {
        ContinuousFryerBlockEntity controllerBE = getControllerBE();
        if (controllerBE == null)
            return;

        var itemInv = controllerBE.getItemInventory();
        if (itemInv == null)
            return;

        List<FryingItemStack> allItems = itemInv.getTransportedItems();
        if (allItems == null)
            return;
        allItems = new ArrayList<>(allItems);
        FluidStack totalFluid = controllerBE.tankInventory.getFluid();
        int totalAmount = totalFluid.getAmount();

        if (dropContents) ItemHelper.dropContents(level, worldPosition, new ItemHandlerFryerSegment(itemInv, index));

        List<ContinuousFryerBlockEntity> chain = controllerBE.getConnectedChain();
        for (ContinuousFryerBlockEntity fryer : chain) {
            fryer.itemInventory = new FryerInventory(fryer);
            fryer.tankInventory = fryer.createTankInventory();
            if (totalAmount > 0 && !(dropContents && fryer == this)) {
                int fillAmount = Math.min(totalAmount, fryer.tankInventory.getCapacity());
                fryer.tankInventory.setFluid(new FluidStack(totalFluid.getFluid(), fillAmount));
                totalAmount -= fillAmount;
            }
        }

        for (FryingItemStack item : allItems) {
            float globalPos = item.fryerPosition;
            int segmentIndex = Mth.clamp((int) globalPos, 0, chain.size() - 1);
            float localPos = globalPos - segmentIndex;
            ContinuousFryerBlockEntity target = chain.get(segmentIndex);

            target.setController(target.worldPosition);

            item.fryerPosition = localPos;
            item.prevFryerPosition = localPos;
            item.insertedAt = 0;
            item.locked = false;
            item.lockedExternally = false;

            FryerInventory targetInv = target.getItemInventory();
            targetInv.getTransportedItems().add(item);
            targetInv.getTransportedItems()
                    .sort((a, b) -> Float.compare(b.fryerPosition, a.fryerPosition));
        }

        updateNeighbours();
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

        fluidHandler = tankInventory;
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
//        applyFluidTankSize(1);
        onFluidStackChanged(tankInventory.getFluid());
        refreshCapability();
    }

    public LerpedFloat getFluidLevel() {
        return fluidLevel;
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
        updateConnectivity = false;
//        if (!isController())
//            return;

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

        List<FryingItemStack> mergedItems = new ArrayList<>();
        int length = chain.size();
        for (int i = 0; i < length; i++) {
            BlockPos pos = chain.get(i);
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof ContinuousFryerBlockEntity fryer))
                continue;

            fryer.fryerLength = length;
            fryer.index = i;
            fryer.setController(chain.getFirst());

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

            FryerInventory inv = fryer.itemInventory;
            if (inv != null) {
                List<FryingItemStack> stacks = inv.getTransportedItems();
                if (stacks != null && !stacks.isEmpty()) {
                    for (FryingItemStack stack : stacks) {
                        float localPos = stack.fryerPosition;
                        float globalPos = i + localPos;
                        stack.fryerPosition = globalPos;
                        stack.prevFryerPosition = globalPos;

                        stack.locked = false;
                        stack.lockedExternally = false;
                        stack.insertedAt = i;
                        mergedItems.add(stack);
                    }
                }
            }
            fryer.itemInventory = null;

            level.setBlock(pos, newState, 6);
            fryer.detachKinetics();
            fryer.clearKineticInformation();
            fryer.attachKinetics();
            fryer.refreshCapability();
            fryer.requestModelDataUpdate();
            fryer.notifyUpdate();
        }

        ContinuousFryerBlockEntity controllerBE =
                (ContinuousFryerBlockEntity) level.getBlockEntity(chain.getFirst());
        if (controllerBE != null) {
            FryerInventory controllerInv = controllerBE.getItemInventory();
            controllerInv.getTransportedItems().clear();
            controllerInv.getTransportedItems().addAll(mergedItems);

            FluidStack mergedFluid = FluidStack.EMPTY;
            for (var fryerPos : chain) {
                BlockEntity be = level.getBlockEntity(fryerPos);
                if (!(be instanceof ContinuousFryerBlockEntity fryer))
                    continue;
                FluidTank tank = fryer.tankInventory;
                if (tank != null && !tank.getFluid().isEmpty()) {
                    FluidStack fluid = tank.getFluid();
                    if (mergedFluid.isEmpty()) {
                        mergedFluid = fluid.copy();
                    } else if (mergedFluid.getFluid().isSame(fluid.getFluid())) {
                        mergedFluid.grow(fluid.getAmount());
                    }
                    tank.setFluid(FluidStack.EMPTY);
                }
            }

            controllerBE.applyFluidTankSize(length);
            if (!mergedFluid.isEmpty()) {
                int capacity = controllerBE.tankInventory.getCapacity();
                if (mergedFluid.getAmount() > capacity)
                    mergedFluid.setAmount(capacity);
                controllerBE.tankInventory.setFluid(mergedFluid);
            }

            controllerBE.notifyUpdate();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new DirectBeltInputBehaviour(this).onlyInsertWhen(this::canInsertFrom)
                .setInsertionHandler(this::tryInsertingFromSide).considerOccupiedWhen(this::isOccupied));
        behaviours.add(new FryingItemStackHandlerBehaviour(this, this::applyToAllItems)
                .withStackPlacement(this::getWorldPositionOf));
        behaviours.add(invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
    }

    private Vec3 getWorldPositionOf(FryingItemStack transported) {
        ContinuousFryerBlockEntity controllerBE = getControllerBE();
        if (controllerBE == null)
            return Vec3.ZERO;
        return FryerHelper.getVectorForOffset(controllerBE, transported.fryerPosition);
    }

    private void applyToAllItems(float maxDistanceFromCenter,
                                 Function<FryingItemStack, FryingItemStackHandlerBehaviour.FryingResult> processFunction) {
        ContinuousFryerBlockEntity controller = getControllerBE();
        if (controller == null)
            return;
        FryerInventory inventory = controller.getItemInventory();
        if (inventory != null)
            inventory.applyToEachWithin(index + .5f, maxDistanceFromCenter, processFunction);
    }

    private boolean canInsertFrom(Direction side) {
        if (getSpeed() == 0)
            return false;
        return getMovementFacing() != side.getOpposite();
    }

    private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
        ContinuousFryerBlockEntity nextBeltController = getControllerBE();
        ItemStack inserted = transportedStack.stack;
        ItemStack empty = ItemStack.EMPTY;

        if (nextBeltController == null)
            return inserted;
        FryerInventory nextInventory = nextBeltController.getItemInventory();
        if (nextInventory == null)
            return inserted;

        if (isOccupied(side))
            return inserted;
        if (simulate)
            return empty;

        transportedStack = transportedStack.copy();
        transportedStack.beltPosition = index + .5f - Math.signum(getDirectionAwareBeltMovementSpeed()) / 16f;

        Direction movementFacing = getMovementFacing();
        if (!side.getAxis()
                .isVertical()) {
            if (movementFacing != side) {
                transportedStack.sideOffset = side.getAxisDirection()
                        .getStep() * .675f;
                if (side.getAxis() == Direction.Axis.X)
                    transportedStack.sideOffset *= -1;
            } else {
                // This creates a smoother transition from belt to belt
                float extraOffset = transportedStack.prevBeltPosition != 0
                        && FryerHelper.getSegmentBE(level, worldPosition.relative(movementFacing.getOpposite())) != null
                        ? .26f
                        : 0;
                transportedStack.beltPosition =
                        getDirectionAwareBeltMovementSpeed() > 0 ? index - extraOffset : index + 1 + extraOffset;
            }
        }

        transportedStack.prevSideOffset = transportedStack.sideOffset;
        transportedStack.insertedAt = index;
        transportedStack.insertedFrom = side;
        transportedStack.prevBeltPosition = transportedStack.beltPosition;

        nextInventory.addItem(new FryingItemStack(transportedStack));
        nextBeltController.setChanged();
        nextBeltController.sendData();
        return empty;
    }

    private boolean isOccupied(Direction side) {
        ContinuousFryerBlockEntity nextBeltController = getControllerBE();
        if (nextBeltController == null)
            return true;
        FryerInventory nextInventory = nextBeltController.getItemInventory();
        if (nextInventory == null)
            return true;
        if (getSpeed() == 0)
            return true;
        if (getMovementFacing() == side.getOpposite())
            return true;
        if (!nextInventory.canInsertAtFromSide(index, side))
            return true;
        return false;
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
//                    fryer.setController(fryer.getBlockPos());
                    fryer.updateConnectivity();
                }
            }
        }
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (level == null)
            return;
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }

        if (isVirtual()) {
            if (fluidLevel == null)
                fluidLevel = LerpedFloat.linear()
                        .startWithValue(getFillState());
            fluidLevel.chase(getFillState(), .5f, LerpedFloat.Chaser.EXP);
        }
    }

    public float getFillState() {
        return (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();
    }

    @Override
    public void tick() {
        if ((level != null && !level.isClientSide) && (getControllerBE() == null || getFryerFacing().getAxis() != getControllerBE().getFryerFacing().getAxis())) {
            setController(worldPosition);
            updateConnectivity();
            updateNeighbours();
        }
        if (fluidLevel != null)
            fluidLevel.tickChaser();
        if (updateConnectivity)
            updateConnectivity();

        super.tick();
        if (!isController())
            return;

        invalidateRenderBoundingBox();
        getItemInventory().tick();

        this.tickPassengers();
        this.tickFryingItems();
    }

    private void tickPassengers() {
        if (getSpeed() == 0)
            return;

        if (passengers == null)
            passengers = new HashMap<>();

        List<Entity> toRemove = new ArrayList<>();
        passengers.forEach((entity, info) -> {
            boolean canBeTransported = FryerMovementHandler.canBeTransported(entity);
            boolean leftTheBelt = info.getTicksSinceLastCollision() > 1;
            if (!canBeTransported || leftTheBelt) {
                toRemove.add(entity);
                return;
            }

            info.tick();
            FryerMovementHandler.transportEntity(this, entity, info);
        });
        toRemove.forEach(passengers::remove);
    }

    public BlazeBurnerBlock.HeatLevel getHeatLevel() {
        ContinuousFryerBlockEntity controller = getControllerBE();
        if (controller == null || controller.level == null)
            return BlazeBurnerBlock.HeatLevel.NONE;

        Level level = controller.level;
        BlazeBurnerBlock.HeatLevel strongest = BlazeBurnerBlock.HeatLevel.NONE;

        for (ContinuousFryerBlockEntity fryer : controller.getConnectedChain()) {
            BlockPos below = fryer.getBlockPos().below();
            BlazeBurnerBlock.HeatLevel local = getHeatLevelAt(level, below);
            if (local.ordinal() > strongest.ordinal())
                strongest = local;
        }

        return strongest;
    }

    private static BlazeBurnerBlock.HeatLevel getHeatLevelAt(Level level, BlockPos pos) {
        if (level == null)
            return BlazeBurnerBlock.HeatLevel.NONE;

        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof BlazeBurnerBlock) {
            return state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
        }
        return BlazeBurnerBlock.HeatLevel.NONE;
    }

    private void tickFryingItems() {
        FryerInventory inventory = getItemInventory();
        if (inventory == null) return;

        List<FryingItemStack> items = inventory.getTransportedItems();
        if (items == null || items.isEmpty()) return;

        for (FryingItemStack item : items) {
            var heatLevel = getHeatLevel();
            if (item.lastRecipe == null || !item.lastRecipe.matches(item.stack, tankInventory.getFluid(), heatLevel)) {
                FryingRecipe recipe = RFDRecipeTypes.findFryingRecipe(level, item.stack, tankInventory.getFluid(), heatLevel);
                if (recipe != null) {
                    if (item.lastRecipe != recipe) {
                        item.processingTime = 0;
                    }
                    item.lastRecipe = recipe;
                }
            }

            item.processingTime++;

            if (item.processingTime % 5 == 0 && level != null && !level.isClientSide) {
                level.playSound(
                        null,
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5,
                        SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
                        SoundSource.BLOCKS,
                        1.0f,
                        0.9f + level.random.nextFloat() * 0.2f
                );
            }

            // TODO: use another overcooked item
            if (item.lastRecipe == null) {
                if (item.processingTime > 100 && !item.stack.is(Items.CHARCOAL)) {
                    item.processingTime = 0;
                    item.stack = new ItemStack(Items.CHARCOAL, item.stack.getCount());
                }
                continue;
            }

            if (item.processingTime >= item.lastRecipe.getProcessingDuration()) {
                item.stack = RecipeApplier.applyRecipeOn(level, item.stack, item.lastRecipe).getFirst();
                tankInventory.drain(item.lastRecipe.getFluidIngredients().getFirst().getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);

                item.clearFryerProcessingData();
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }


    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
//        compound.putBoolean("Uninitialized", updateConnectivity);
        if (controller != null)
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        compound.putBoolean("IsController", isController());
        compound.putInt("Length", fryerLength);
        compound.putInt("Index", index);
        if (isController()) {
            compound.put("ItemInventory", getItemInventory().write(registries));
            compound.put("TankContent", tankInventory.writeToNBT(registries, new CompoundTag()));
        }
        if (!clientPacket)
            return;
        if (forceFluidLevelUpdate)
            compound.putBoolean("ForceFluidLevel", true);
        forceFluidLevelUpdate = false;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

//        updateConnectivity = compound.getBoolean("Uninitialized");

        BlockPos controllerBefore = controller;
        int prevLength = fryerLength;

        if (compound.getBoolean("IsController")) {
            controller = worldPosition;
        } else {
            controller = NBTHelper.readBlockPos(compound, "Controller");
        }
        fryerLength = compound.getInt("Length");
        index = compound.getInt("Index");

        if (isController()){
            getItemInventory().read(compound.getCompound("ItemInventory"), registries, level);
            tankInventory.setCapacity(fryerLength * getCapacityMultiplier());
            tankInventory.readFromNBT(registries, compound.getCompound("TankContent"));
            if (tankInventory.getSpace() < 0)
                tankInventory.drain(-tankInventory.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }

        if (compound.contains("ForceFluidLevel") || fluidLevel == null)
            fluidLevel = LerpedFloat.linear()
                    .startWithValue(getFillState());


        if (!clientPacket)
            return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevLength != fryerLength) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            if (isController())
                tankInventory.setCapacity(getCapacityMultiplier() * fryerLength);
            invalidateRenderBoundingBox();
        }
        if (isController()) {
            float fillState = getFillState();
            if (compound.contains("ForceFluidLevel") || fluidLevel == null)
                fluidLevel = LerpedFloat.linear()
                        .startWithValue(fillState);
            fluidLevel.chase(fillState, 0.5f, LerpedFloat.Chaser.EXP);
        }
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

    public boolean shouldRenderNormally() {
        if (level == null)
            return isController();
        BlockState state = getBlockState();
        return state != null && state.hasProperty(ContinuousFryerBlock.PART) && state.getValue(ContinuousFryerBlock.PART) != FryerPart.MIDDLE;
    }
}
