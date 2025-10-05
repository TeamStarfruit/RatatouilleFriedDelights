package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import static com.simibubi.create.content.kinetics.belt.transport.BeltTunnelInteractionHandler.flapTunnel;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.FryingItemStackHandlerBehaviour.FryingResult;

import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.FryerProcessingBehaviour.ProcessingResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FryerInventory {

    final ContinuousFryerBlockEntity fryer;
    private final List<FryingItemStack> items;
    final List<FryingItemStack> toInsert;
    final List<FryingItemStack> toRemove;
    boolean fryerMovementPositive;
    final float SEGMENT_WINDOW = .75f;

    FryingItemStack lazyClientItem;

    public FryerInventory(ContinuousFryerBlockEntity be) {
        this.fryer = be;
        items = new LinkedList<>();
        toInsert = new LinkedList<>();
        toRemove = new LinkedList<>();
    }

    public void tick() {

        // Residual item for "smooth" transitions
        if (lazyClientItem != null) {
            if (lazyClientItem.locked)
                lazyClientItem = null;
            else
                lazyClientItem.locked = true;
        }

        // Added/Removed items from previous cycle
        if (!toInsert.isEmpty() || !toRemove.isEmpty()) {
            toInsert.forEach(this::insert);
            toInsert.clear();
            items.removeAll(toRemove);
            toRemove.clear();
            fryer.notifyUpdate();
        }

        if (fryer.getSpeed() == 0)
            return;

        // Reverse item collection if belt just reversed
        if (fryerMovementPositive != fryer.getDirectionAwareBeltMovementSpeed() > 0) {
            fryerMovementPositive = !fryerMovementPositive;
            Collections.reverse(items);
            fryer.notifyUpdate();
        }

        // Assuming the first entry is furthest on the belt
        FryingItemStack stackInFront = null;
        FryingItemStack currentItem = null;
        Iterator<FryingItemStack> iterator = items.iterator();

        // Useful stuff
        float beltSpeed = fryer.getDirectionAwareBeltMovementSpeed();
        Direction movementFacing = fryer.getMovementFacing();
        float spacing = 1;
        Level world = fryer.getLevel();
        boolean onClient = world.isClientSide && !fryer.isVirtual();

        // resolve ending only when items will reach it this tick
        Ending ending = Ending.UNRESOLVED;

        // Loop over items
        while (iterator.hasNext()) {
            stackInFront = currentItem;
            currentItem = iterator.next();
            currentItem.prevFryerPosition = currentItem.fryerPosition;
            currentItem.prevSideOffset = currentItem.sideOffset;

            if (currentItem.stack.isEmpty()) {
                iterator.remove();
                currentItem = null;
                continue;
            }

            float movement = beltSpeed;
            if (onClient)
                movement *= ServerSpeedProvider.get();

            // Don't move if held by processing (client)
            if (world.isClientSide && currentItem.locked)
                continue;

            // Don't move if held by external components
            if (currentItem.lockedExternally) {
                currentItem.lockedExternally = false;
                continue;
            }

            // Don't move if other items are waiting in front
            boolean noMovement = false;
            float currentPos = currentItem.fryerPosition;
            if (stackInFront != null) {
                float diff = stackInFront.fryerPosition - currentPos;
                if (Math.abs(diff) <= spacing)
                    noMovement = true;
                movement =
                        fryerMovementPositive ? Math.min(movement, diff - spacing) : Math.max(movement, diff + spacing);
            }

            // Don't move beyond the edge
            float diffToEnd = fryerMovementPositive ? fryer.fryerLength - currentPos : -currentPos;
            if (Math.abs(diffToEnd) < Math.abs(movement) + 1) {
                if (ending == Ending.UNRESOLVED)
                    ending = resolveEnding();
                diffToEnd += fryerMovementPositive ? -ending.margin : ending.margin;
            }
            float limitedMovement =
                    fryerMovementPositive ? Math.min(movement, diffToEnd) : Math.max(movement, diffToEnd);
            float nextOffset = currentItem.fryerPosition + limitedMovement;

            // Belt item processing
            if (!onClient) {
                ItemStack item = currentItem.stack;
                if (handleBeltProcessingAndCheckIfRemoved(currentItem, nextOffset, noMovement)) {
                    iterator.remove();
                    fryer.notifyUpdate();
                    continue;
                }
                if (item != currentItem.stack)
                    fryer.notifyUpdate();
                if (currentItem.locked)
                    continue;
            }

            // Belt Funnels
//            if (BeltFunnelInteractionHandler.checkForFunnels(this, currentItem, nextOffset))
//                continue;

            if (noMovement)
                continue;

            // Belt Tunnels
//            if (BeltTunnelInteractionHandler.flapTunnelsAndCheckIfStuck(this, currentItem, nextOffset))
//                continue;

            // Horizontal Crushing Wheels
//            if (BeltCrusherInteractionHandler.checkForCrushers(this, currentItem, nextOffset))
//                continue;

            // Apply Movement
            currentItem.fryerPosition += limitedMovement;
            float diffToMiddle = currentItem.getTargetSideOffset() - currentItem.sideOffset;
            currentItem.sideOffset += Mth.clamp(diffToMiddle * Math.abs(limitedMovement) * 6f, -Math.abs(diffToMiddle),
                    Math.abs(diffToMiddle));
            currentPos = currentItem.fryerPosition;

            // Movement successful
            if (limitedMovement == movement || onClient)
                continue;

            // End reached
            int lastOffset = fryerMovementPositive ? fryer.fryerLength - 1 : 0;
            BlockPos nextPosition = FryerHelper.getPositionForOffset(fryer, fryerMovementPositive ? fryer.fryerLength : -1);

            if (ending == Ending.FUNNEL)
                continue;

            if (ending == Ending.INSERT) {
                DirectBeltInputBehaviour inputBehaviour =
                        BlockEntityBehaviour.get(world, nextPosition, DirectBeltInputBehaviour.TYPE);
                if (inputBehaviour == null)
                    continue;
                if (!inputBehaviour.canInsertFromSide(movementFacing))
                    continue;

//                ItemStack remainder = inputBehaviour.handleInsertion(currentItem, movementFacing, false);
                ItemStack remainder = inputBehaviour.handleInsertion(currentItem.stack, movementFacing, false);
                if (ItemStack.matches(remainder, currentItem.stack))
                    continue;

                currentItem.stack = remainder;
                if (remainder.isEmpty()) {
                    lazyClientItem = currentItem;
                    lazyClientItem.locked = false;
                    iterator.remove();
                } else
                    currentItem.stack = remainder;

//                flapTunnel(this, lastOffset, movementFacing, false);
                fryer.notifyUpdate();
                continue;
            }

            if (ending == Ending.BLOCKED)
                continue;

            if (ending == Ending.EJECT) {
                eject(currentItem);
                iterator.remove();
//                flapTunnel(this, lastOffset, movementFacing, false);
                fryer.notifyUpdate();
                continue;
            }
        }
    }

    protected boolean handleBeltProcessingAndCheckIfRemoved(FryingItemStack currentItem, float nextOffset,
                                                            boolean noMovement) {
        int currentSegment = (int) currentItem.fryerPosition;

        // Continue processing if held
        if (currentItem.locked) {
            FryerProcessingBehaviour processingBehaviour = getBeltProcessingAtSegment(currentSegment);
            FryingItemStackHandlerBehaviour stackHandlerBehaviour =
                    getFryingItemStackHandlerAtSegment(currentSegment);

            if (stackHandlerBehaviour == null)
                return false;
            if (processingBehaviour == null) {
                currentItem.locked = false;
                fryer.notifyUpdate();
                return false;
            }

            ProcessingResult result = processingBehaviour.handleHeldItem(currentItem, stackHandlerBehaviour);
            if (result == ProcessingResult.REMOVE)
                return true;
            if (result == ProcessingResult.HOLD)
                return false;

            currentItem.locked = false;
            fryer.notifyUpdate();
            return false;
        }

        if (noMovement)
            return false;

        // See if any new belt processing catches the item
        if (currentItem.fryerPosition > .5f || fryerMovementPositive) {
            int firstUpcomingSegment = (int) (currentItem.fryerPosition + (fryerMovementPositive ? .5f : -.5f));
            int step = fryerMovementPositive ? 1 : -1;

            for (int segment = firstUpcomingSegment; fryerMovementPositive ? segment + .5f <= nextOffset
                    : segment + .5f >= nextOffset; segment += step) {

                FryerProcessingBehaviour processingBehaviour = getBeltProcessingAtSegment(segment);
                FryingItemStackHandlerBehaviour stackHandlerBehaviour =
                        getFryingItemStackHandlerAtSegment(segment);

                if (processingBehaviour == null)
                    continue;
                if (stackHandlerBehaviour == null)
                    continue;
                if (FryerProcessingBehaviour.isBlocked(fryer.getLevel(), FryerHelper.getPositionForOffset(fryer, segment)))
                    continue;

                ProcessingResult result = processingBehaviour.handleReceivedItem(currentItem, stackHandlerBehaviour);
                if (result == ProcessingResult.REMOVE)
                    return true;

                if (result == ProcessingResult.HOLD) {
                    currentItem.fryerPosition = segment + .5f + (fryerMovementPositive ? 1 / 512f : -1 / 512f);
                    currentItem.locked = true;
                    fryer.notifyUpdate();
                    return false;
                }
            }
        }

        return false;
    }

    protected FryerProcessingBehaviour getBeltProcessingAtSegment(int segment) {
        return BlockEntityBehaviour.get(fryer.getLevel(), FryerHelper.getPositionForOffset(fryer, segment)
                .above(2), FryerProcessingBehaviour.TYPE);
    }

    protected FryingItemStackHandlerBehaviour getFryingItemStackHandlerAtSegment(int segment) {
        return BlockEntityBehaviour.get(fryer.getLevel(), FryerHelper.getPositionForOffset(fryer, segment),
                FryingItemStackHandlerBehaviour.TYPE);
    }

    private enum Ending {
        UNRESOLVED(0), EJECT(0), INSERT(.25f), FUNNEL(.5f), BLOCKED(.45f);

        private float margin;

        Ending(float f) {
            this.margin = f;
        }
    }

    private Ending resolveEnding() {
        Level world = fryer.getLevel();
        BlockPos nextPosition = FryerHelper.getPositionForOffset(fryer, fryerMovementPositive ? fryer.fryerLength : -1);

//		if (AllBlocks.BRASS_BELT_FUNNEL.has(world.getBlockState(lastPosition.up())))
//			return Ending.FUNNEL;

        DirectBeltInputBehaviour inputBehaviour =
                BlockEntityBehaviour.get(world, nextPosition, DirectBeltInputBehaviour.TYPE);
        if (inputBehaviour != null)
            return Ending.INSERT;

        if (BlockHelper.hasBlockSolidSide(world.getBlockState(nextPosition), world, nextPosition,
                fryer.getMovementFacing()
                        .getOpposite()))
            return Ending.BLOCKED;

        return Ending.EJECT;
    }

    //

    public boolean canInsertAt(int segment) {
        return canInsertAtFromSide(segment, Direction.UP);
    }

    public boolean canInsertAtFromSide(int segment, Direction side) {
        float segmentPos = segment;
        if (fryer.getMovementFacing() == side.getOpposite())
            return false;
        if (fryer.getMovementFacing() != side)
            segmentPos += .5f;
        else if (!fryerMovementPositive)
            segmentPos += 1f;

        for (FryingItemStack stack : items)
            if (isBlocking(segment, side, segmentPos, stack))
                return false;
        for (FryingItemStack stack : toInsert)
            if (isBlocking(segment, side, segmentPos, stack))
                return false;

        return true;
    }

    private boolean isBlocking(int segment, Direction side, float segmentPos, FryingItemStack stack) {
        float currentPos = stack.fryerPosition;
        if (stack.insertedAt == segment && stack.insertedFrom == side
                && (fryerMovementPositive ? currentPos <= segmentPos + 1 : currentPos >= segmentPos - 1))
            return true;
        return false;
    }

    public void addItem(FryingItemStack newStack) {
        toInsert.add(newStack);
    }

    private void insert(FryingItemStack newStack) {
        if (items.isEmpty())
            items.add(newStack);
        else {
            int index = 0;
            for (FryingItemStack stack : items) {
                if (stack.compareTo(newStack) > 0 == fryerMovementPositive)
                    break;
                index++;
            }
            items.add(index, newStack);
        }
    }

    public FryingItemStack getStackAtOffset(int offset) {
        float min = offset;
        float max = offset + 1;
        for (FryingItemStack stack : items) {
            if (toRemove.contains(stack))
                continue;
            if (stack.fryerPosition > max)
                continue;
            if (stack.fryerPosition > min)
                return stack;
        }
        return null;
    }

    public void read(CompoundTag nbt, HolderLookup.Provider registries, Level level) {
        items.clear();
        nbt.getList("Items", Tag.TAG_COMPOUND)
                .forEach(inbt -> items.add(FryingItemStack.read((CompoundTag) inbt, registries, level)));
        if (nbt.contains("LazyItem"))
            lazyClientItem = FryingItemStack.read(nbt.getCompound("LazyItem"), registries, level);
        fryerMovementPositive = nbt.getBoolean("PositiveOrder");
    }

    public CompoundTag write(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        ListTag itemsNBT = new ListTag();
        items.forEach(stack -> itemsNBT.add(stack.serializeNBT(registries)));
        nbt.put("Items", itemsNBT);
        if (lazyClientItem != null)
            nbt.put("LazyItem", lazyClientItem.serializeNBT(registries));
        nbt.putBoolean("PositiveOrder", fryerMovementPositive);
        return nbt;
    }

    public void eject(FryingItemStack stack) {
        ItemStack ejected = stack.stack;
        Vec3 outPos = FryerHelper.getVectorForOffset(fryer, stack.fryerPosition);
        float movementSpeed = Math.max(Math.abs(fryer.getFryerMovementSpeed()), 1 / 8f);
        Vec3 outMotion = Vec3.atLowerCornerOf(fryer.getFryerChainDirection())
                .scale(movementSpeed)
                .add(0, 1 / 8f, 0);
        outPos = outPos.add(outMotion.normalize()
                .scale(0.001));
        ItemEntity entity = new ItemEntity(fryer.getLevel(), outPos.x, outPos.y + 6 / 16f, outPos.z, ejected);
        entity.setDeltaMovement(outMotion);
        entity.setDefaultPickUpDelay();
        entity.hurtMarked = true;
        fryer.getLevel()
                .addFreshEntity(entity);
    }

    public void ejectAll() {
        items.forEach(this::eject);
        items.clear();
    }

    public void applyToEachWithin(float position, float maxDistanceToPosition,
                                  Function<FryingItemStack, FryingResult> processFunction) {
        boolean dirty = false;
        for (FryingItemStack transported : items) {
            if (toRemove.contains(transported))
                continue;
            ItemStack stackBefore = transported.stack.copy();
            if (Math.abs(position - transported.fryerPosition) >= maxDistanceToPosition)
                continue;
            FryingResult result = processFunction.apply(transported);
            if (result == null || result.didntChangeFrom(stackBefore))
                continue;

            dirty = true;
            if (result.hasHeldOutput()) {
                FryingItemStack held = result.getHeldOutput();
                held.fryerPosition = ((int) position) + .5f - (fryerMovementPositive ? 1 / 512f : -1 / 512f);
                toInsert.add(held);
            }
            toInsert.addAll(result.getOutputs());
            toRemove.add(transported);
        }
        if (dirty) {
            fryer.notifyUpdate();
        }
    }

    public List<FryingItemStack> getTransportedItems() {
        return items;
    }

    @Nullable
    public FryingItemStack getLazyClientItem() {
        return lazyClientItem;
    }

}
