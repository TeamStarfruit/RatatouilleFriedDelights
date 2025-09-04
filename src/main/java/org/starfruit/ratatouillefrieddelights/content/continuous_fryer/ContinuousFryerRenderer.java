package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ContinuousFryerRenderer extends SafeBlockEntityRenderer<ContinuousFryerBlockEntity> {
    public ContinuousFryerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void renderSafe(ContinuousFryerBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                           MultiBufferSource buffer, int packedLight, int packedOverlay) {
        // No special rendering yet
    }
}