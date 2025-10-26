package org.starfruit.ratatouillefrieddelights.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.minecraft.client.gui.GuiGraphics;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

public class AnimatedContinuousFryer extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        int scale = 22;
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0);
        AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);

        matrixStack.pushPose();
        matrixStack.translate(3, 11, 0);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-22.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5F));
        blockElement(RFDPartialModels.FRYER_AXIS)
                .rotateBlock(getCurrentAngle() * 2, 0, 0)
                .scale(scale)
                .render(graphics);
        matrixStack.popPose();

        matrixStack.translate(-2, 18, 0);
        blockElement(RFDPartialModels.FRYER_BELT)
                .rotateBlock(22.5, 22.5, 0)
                .scale(scale)
                .render(graphics);
        matrixStack.popPose();

        matrixStack.translate(-2, 18, 0);
        blockElement(RFDBlocks.CONTINUOUS_FRYER.getDefaultState())
                .rotateBlock(22.5, 22.5, 0)
                .scale(scale)
                .render(graphics);
        matrixStack.popPose();
    }
}
