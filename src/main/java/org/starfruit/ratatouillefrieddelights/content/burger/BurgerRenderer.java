package org.starfruit.ratatouillefrieddelights.content.burger;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;

public class BurgerRenderer extends CustomRenderedItemModelRenderer {
    public static final IItemDecorator DECORATOR = (guiGraphics, font, stack, xOffset, yOffset) -> {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, 100);


        if (!stack.has(RFDDataComponents.BURGER_CONTENTS))
            return false;

        BurgerContents burgerContents = stack.get(RFDDataComponents.BURGER_CONTENTS);

        int totalHeight = burgerContents.items
                .stream()
                .map(ingredient -> BurgerRenderingProperties.BURGER_RENDERING_PROPERTIES
                        .getOrDefault(ingredient.getItem(), Couple.create(4, 3)).getSecond())
                .mapToInt(Integer::intValue).sum();
        totalHeight += 5;
        totalHeight = Math.max(totalHeight, 18);
        totalHeight = 18;

        poseStack.translate(-0.25, 0, 0);
        poseStack.scale(16f/totalHeight, 16f/totalHeight, 16f/totalHeight);
        poseStack.translate((1 - 16f/totalHeight)/2 * totalHeight, 1, 0);

        poseStack.pushPose();
        for (ItemStack burgerContent : burgerContents.items()) {
            Couple<Integer> renderingProperties = BurgerRenderingProperties.BURGER_RENDERING_PROPERTIES
                    .getOrDefault(burgerContent.getItem(), Couple.create(4, 3)); // Height of 3 to provide maximum error

            poseStack.pushPose();
            poseStack.translate(0, (double)1 * renderingProperties.getFirst(), 0);
            guiGraphics.renderItem(burgerContent, 0, 0);
            poseStack.popPose();
            poseStack.translate(0, (double)-1 * renderingProperties.getSecond(), 0);
            poseStack.scale(1.005F, 1F, 1.005F);
        }
        poseStack.popPose();
        poseStack.popPose();

        return false;
    };

    protected static final PartialModel COG = PartialModel.of(Create.asResource("item/potato_cannon/cog"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
                          ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (transformType == ItemDisplayContext.GUI)
            return;

        if (!stack.has(RFDDataComponents.BURGER_CONTENTS))
            return;

        BurgerContents burgerContents = stack.get(RFDDataComponents.BURGER_CONTENTS);

        ms.pushPose();
        ms.translate(0, 1/16f, 0);
        if (transformType != ItemDisplayContext.FIXED)
            ms.translate(0, 3/16f, 0);
        if (transformType != ItemDisplayContext.FIXED)
            ms.scale(0.5F, 0.5F, 0.5F);
        for (ItemStack burgerContent : burgerContents.items()) {
            Couple<Integer> renderingProperties = BurgerRenderingProperties.BURGER_RENDERING_PROPERTIES
                    .getOrDefault(burgerContent.getItem(), Couple.create(4, 3)); // Height of 3 to provide maximum error

            ms.pushPose();
            ms.translate(0, (double)-1/16 * renderingProperties.getFirst(), 0);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(burgerContent, ItemDisplayContext.FIXED, light, overlay, ms, buffer, null, 0);
            ms.popPose();
            ms.translate(0, (double)1/16 * renderingProperties.getSecond(), 0);
            ms.scale(1.005F, 1F, 1.005F);
        }
        ms.popPose();
    }
}
