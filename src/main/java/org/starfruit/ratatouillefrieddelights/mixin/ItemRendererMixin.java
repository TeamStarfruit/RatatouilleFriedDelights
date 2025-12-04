package org.starfruit.ratatouillefrieddelights.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.starfruit.ratatouillefrieddelights.content.dipcup.DipableItem;

import java.util.Iterator;
import java.util.List;

@Mixin(value = net.minecraft.client.renderer.entity.ItemRenderer.class)
public class ItemRendererMixin {

    @Redirect(
        method = "renderQuadList",
        at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFFIIZ)V"
        ))
    private void redirectPutBulkData(
            VertexConsumer vertexConsumer,
            PoseStack.Pose pose,
            BakedQuad quad,
            float red,
            float green,
            float blue,
            float alpha,
            int light,
            int overlay,
            boolean recalculateNormals,
            @Local(argsOnly = true) ItemStack pItemStack,
            @Local(ordinal = 2) int i
    ) {
        if (!(pItemStack.getItem() instanceof DipableItem)) {
            vertexConsumer.putBulkData(pose, quad, red, green, blue, alpha, light, overlay, recalculateNormals);
            return;
        }

        float extractedAlpha = (float)((i >> 24) & 255) / 255.0F;
        vertexConsumer.putBulkData(pose, quad, red, green, blue, extractedAlpha, light, overlay, recalculateNormals);
    }
}