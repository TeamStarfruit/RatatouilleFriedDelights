package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class FryerProcessingBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<FryerProcessingBehaviour> TYPE = new BehaviourType<>();

    public static enum ProcessingResult {
        PASS, HOLD, REMOVE;
    }

    private FryerProcessingBehaviour.ProcessingCallback onItemEnter;
    private FryerProcessingBehaviour.ProcessingCallback continueProcessing;

    public FryerProcessingBehaviour(SmartBlockEntity be) {
        super(be);
        onItemEnter = (s, i) -> FryerProcessingBehaviour.ProcessingResult.PASS;
        continueProcessing = (s, i) -> FryerProcessingBehaviour.ProcessingResult.PASS;
    }

    public FryerProcessingBehaviour whenItemEnters(FryerProcessingBehaviour.ProcessingCallback callback) {
        onItemEnter = callback;
        return this;
    }

    public FryerProcessingBehaviour whileItemHeld(FryerProcessingBehaviour.ProcessingCallback callback) {
        continueProcessing = callback;
        return this;
    }

    public static boolean isBlocked(BlockGetter world, BlockPos processingSpace) {
        BlockState blockState = world.getBlockState(processingSpace.above());
        if (AbstractFunnelBlock.isFunnel(blockState))
            return false;
        return !blockState.getCollisionShape(world, processingSpace.above())
                .isEmpty();
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public FryerProcessingBehaviour.ProcessingResult handleReceivedItem(FryingItemStack stack,
                                                                       FryingItemStackHandlerBehaviour inventory) {
        return onItemEnter.apply(stack, inventory);
    }

    public FryerProcessingBehaviour.ProcessingResult handleHeldItem(FryingItemStack stack, FryingItemStackHandlerBehaviour inventory) {
        return continueProcessing.apply(stack, inventory);
    }

    @FunctionalInterface
    public interface ProcessingCallback {
        public FryerProcessingBehaviour.ProcessingResult apply(FryingItemStack stack, FryingItemStackHandlerBehaviour inventory);
    }

}