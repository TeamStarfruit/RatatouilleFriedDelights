package org.starfruit.ratatouillefrieddelights.content.burger;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;

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

        BurgerContents burgerContents = stack.getOrDefault(RFDDataComponents.BURGER_CONTENTS, new BurgerContents(List.of(
                new ItemStack(RFDItems.BOTTOM_BURGER_BUN.get()),
                new ItemStack(ModItems.BEEF_PATTY.get()),
                new ItemStack(ModItems.BEEF_PATTY.get()),
                new ItemStack(RFDItems.CHEESE_SLICE.get()),
                new ItemStack(RFDItems.SHREDDED_LETTUCE.get()),
                new ItemStack(RFDItems.TOMATO_INGREDIENT.get()),
                new ItemStack(RFDItems.TOP_BURGER_BUN.get())
        )));

        int totalHeight = burgerContents.items
                .stream()
                .map(ingredient -> BurgerRenderingProperties.BURGER_RENDERING_PROPERTIES_MAP
                        .getOrDefault(ingredient.getItem(), SimpleBurgerRenderingProperties.of(4, 3, () -> ingredient)).renderingHeight())
                .mapToInt(Integer::intValue).sum();
        totalHeight += 5;
        totalHeight = Math.max(totalHeight, 18);
        totalHeight = 18;

        poseStack.translate(-0.25, 0, 0);
        poseStack.scale(16f/totalHeight, 16f/totalHeight, 16f/totalHeight);
        poseStack.translate((1 - 16f/totalHeight)/2 * totalHeight, 1, 0);

        poseStack.pushPose();
        for (ItemStack burgerContent : burgerContents.items()) {
            BurgerRenderingProperties renderingProperties = BurgerRenderingProperties.BURGER_RENDERING_PROPERTIES_MAP
                    .getOrDefault(burgerContent.getItem(), SimpleBurgerRenderingProperties.of(4, 3, () -> burgerContent)); // Height of 3 to provide maximum error

            poseStack.pushPose();
            poseStack.translate(0, (double)1 * renderingProperties.renderingPivot(), 0);
            guiGraphics.renderItem(renderingProperties.renderingItem().get(), 0, 0);
            poseStack.popPose();
            poseStack.translate(0, (double)-1 * renderingProperties.renderingHeight(), 0);
            poseStack.scale(1F + 1/256F, 1F, 1F + 1/356F);
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

        BurgerContents burgerContents = stack.getOrDefault(RFDDataComponents.BURGER_CONTENTS, new BurgerContents(List.of(
                new ItemStack(RFDItems.BOTTOM_BURGER_BUN.get()),
                new ItemStack(ModItems.BEEF_PATTY.get()),
                new ItemStack(ModItems.BEEF_PATTY.get()),
                new ItemStack(RFDItems.CHEESE_SLICE.get()),
                new ItemStack(RFDItems.SHREDDED_LETTUCE.get()),
                new ItemStack(RFDItems.TOMATO_INGREDIENT.get()),
                new ItemStack(RFDItems.TOP_BURGER_BUN.get())
        )));

        ms.pushPose();
        ms.translate(0, 1/16f, 0);

        ms.translate(0, 3/16f, 0);
        if (transformType != ItemDisplayContext.FIXED)
            ms.scale(0.5F, 0.5F, 0.5F);
        for (ItemStack burgerContent : burgerContents.items()) {
            BurgerRenderingProperties renderingProperties = BurgerRenderingProperties.BURGER_RENDERING_PROPERTIES_MAP
                    .getOrDefault(burgerContent.getItem(), SimpleBurgerRenderingProperties.of(4, 3, () -> burgerContent)); // Height of 3 to provide maximum error

            ms.pushPose();
            ms.translate(0, (double)-1/16 * renderingProperties.renderingPivot(), 0);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(renderingProperties.renderingItem().get(), ItemDisplayContext.FIXED, light, overlay, ms, buffer, null, 0);
            ms.popPose();
            ms.translate(0, (double)1/16 * renderingProperties.renderingHeight(), 0);
            ms.scale(1 + 1/256F, 1F, 1 + 1/256F);
        }
        ms.popPose();
    }
}
