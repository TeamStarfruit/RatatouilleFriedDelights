package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class FryingItemStackHandlerBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<FryingItemStackHandlerBehaviour> TYPE = new BehaviourType<>();

    private FryingItemStackHandlerBehaviour.ProcessingCallback processingCallback;
    private FryingItemStackHandlerBehaviour.PositionGetter positionGetter;

    public static class FryingResult {
        List<FryingItemStack> outputs;
        FryingItemStack heldOutput;

        private static final FryingItemStackHandlerBehaviour.FryingResult DO_NOTHING = new FryingItemStackHandlerBehaviour.FryingResult(null, null);
        private static final FryingItemStackHandlerBehaviour.FryingResult REMOVE_ITEM = new FryingItemStackHandlerBehaviour.FryingResult(ImmutableList.of(), null);

        public static FryingItemStackHandlerBehaviour.FryingResult doNothing() {
            return DO_NOTHING;
        }

        public static FryingItemStackHandlerBehaviour.FryingResult removeItem() {
            return REMOVE_ITEM;
        }

        public static FryingItemStackHandlerBehaviour.FryingResult convertTo(FryingItemStack output) {
            return new FryingItemStackHandlerBehaviour.FryingResult(ImmutableList.of(output), null);
        }

        public static FryingItemStackHandlerBehaviour.FryingResult convertTo(List<FryingItemStack> outputs) {
            return new FryingItemStackHandlerBehaviour.FryingResult(outputs, null);
        }

        public static FryingItemStackHandlerBehaviour.FryingResult convertToAndLeaveHeld(List<FryingItemStack> outputs,
                                                                                                   FryingItemStack heldOutput) {
            return new FryingItemStackHandlerBehaviour.FryingResult(outputs, heldOutput);
        }

        private FryingResult(List<FryingItemStack> outputs, FryingItemStack heldOutput) {
            this.outputs = outputs;
            this.heldOutput = heldOutput;
        }

        public boolean doesNothing() {
            return outputs == null;
        }

        public boolean didntChangeFrom(ItemStack stackBefore) {
            return doesNothing()
                    || outputs.size() == 1 && ItemStack.matches(outputs.get(0).stack, stackBefore) && !hasHeldOutput();
        }

        public List<FryingItemStack> getOutputs() {
            if (outputs == null)
                throw new IllegalStateException("Do not call getOutputs() on a Result that doesNothing().");
            return outputs;
        }

        public boolean hasHeldOutput() {
            return heldOutput != null;
        }

        @Nullable
        public FryingItemStack getHeldOutput() {
            if (heldOutput == null)
                throw new IllegalStateException(
                        "Do not call getHeldOutput() on a Result with hasHeldOutput() == false.");
            return heldOutput;
        }

    }

    public FryingItemStackHandlerBehaviour(SmartBlockEntity be, FryingItemStackHandlerBehaviour.ProcessingCallback processingCallback) {
        super(be);
        this.processingCallback = processingCallback;
        positionGetter = t -> VecHelper.getCenterOf(be.getBlockPos());
    }

    public FryingItemStackHandlerBehaviour withStackPlacement(FryingItemStackHandlerBehaviour.PositionGetter function) {
        this.positionGetter = function;
        return this;
    }

    public void handleProcessingOnAllItems(Function<FryingItemStack, FryingItemStackHandlerBehaviour.FryingResult> processFunction) {
        handleCenteredProcessingOnAllItems(.51f, processFunction);
    }

    public void handleProcessingOnItem(FryingItemStack item, FryingItemStackHandlerBehaviour.FryingResult processOutput) {
        handleCenteredProcessingOnAllItems(.51f, t -> {
            if (t == item)
                return processOutput;
            return null;
        });
    }

    public void handleCenteredProcessingOnAllItems(float maxDistanceFromCenter,
                                                   Function<FryingItemStack, FryingItemStackHandlerBehaviour.FryingResult> processFunction) {
        this.processingCallback.applyToAllItems(maxDistanceFromCenter, processFunction);
    }

    public Vec3 getWorldPositionOf(FryingItemStack transported) {
        return positionGetter.getWorldPositionVector(transported);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @FunctionalInterface
    public interface ProcessingCallback {
        public void applyToAllItems(float maxDistanceFromCenter,
                                    Function<FryingItemStack, FryingItemStackHandlerBehaviour.FryingResult> processFunction);
    }

    @FunctionalInterface
    public interface PositionGetter {
        public Vec3 getWorldPositionVector(FryingItemStack transported);
    }

}