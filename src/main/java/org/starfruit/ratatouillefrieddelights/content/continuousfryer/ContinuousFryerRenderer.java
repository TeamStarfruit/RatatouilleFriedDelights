package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

public class ContinuousFryerRenderer extends KineticBlockEntityRenderer<ContinuousFryerBlockEntity> {

    public ContinuousFryerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(ContinuousFryerBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacingVertical(RFDPartialModels.FRYER_AXIS, state, state.getValue(ContinuousFryerBlock.HORIZONTAL_FACING));
    }
}
