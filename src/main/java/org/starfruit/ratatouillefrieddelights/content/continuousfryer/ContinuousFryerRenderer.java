package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.forsteri.ratatouille.content.thresher.ThresherBlock;
import org.forsteri.ratatouille.content.thresher.ThresherBlockEntity;
import org.forsteri.ratatouille.entry.CRPartialModels;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

public class ContinuousFryerRenderer extends KineticBlockEntityRenderer<ContinuousFryerBlockEntity> {

    public ContinuousFryerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRenderOffScreen(ContinuousFryerBlockEntity be) {
        return be.isController();
    }


    @Override
    protected SuperByteBuffer getRotatedModel(ContinuousFryerBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacingVertical(RFDPartialModels.FRYER_AXIS, state, state.getValue(ContinuousFryerBlock.HORIZONTAL_FACING));
    }
}
