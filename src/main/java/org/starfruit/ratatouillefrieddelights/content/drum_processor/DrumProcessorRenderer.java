package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

import java.util.Optional;

public class DrumProcessorRenderer extends KineticBlockEntityRenderer<DrumProcessorBlockEntity> {

    public DrumProcessorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(DrumProcessorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        renderItems(be, partialTicks, ms, buffer, light, overlay);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(DrumProcessorBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacingVertical(RFDPartialModels.DRUM,state,state.getValue(DrumProcessorBlock.HORIZONTAL_FACING));
    }

    protected void renderItems(DrumProcessorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
    }
}
