package org.starfruit.ratatouillefrieddelights.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.mutable.MutableInt;
import org.forsteri.ratatouille.entry.CRBlocks;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;

import java.util.ArrayList;
import java.util.List;

public class FryingCategory extends CreateRecipeCategory<FryingRecipe> {
    public FryingCategory(Info<FryingRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, FryingRecipe recipe, IFocusGroup focuses) {
        List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(recipe.getIngredients());

        int size = condensedIngredients.size();
        int xOffset = size < 3 ? (3 - size) * 19 / 2 : 0;
        int i = 0;

        for (Pair<Ingredient, MutableInt> pair : condensedIngredients) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack itemStack : pair.getFirst().getItems()) {
                ItemStack copy = itemStack.copy();
                copy.setCount(pair.getSecond().getValue());
                stacks.add(copy);
            }

            builder
                    .addSlot(RecipeIngredientRole.INPUT, 0 + xOffset + (i % 3) * 19, 2 - (i / 3) * 19)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(stacks);
            i++;
        }
        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        int y = 0;
        for (ProcessingOutput output : results) {
            int xOffset2 = y % 2 == 0 ? 0 : 19;
            int yOffset2 = (y / 2) * -19;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 133 + xOffset2, 27 + yOffset2)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addRichTooltipCallback(addStochasticTooltip(output));

            y++;
        }
    }

    @Override
    protected void draw(FryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY) {
        int blockX = 30;
        int blocky = 38;
        AllGuiTextures.JEI_ARROW.render(gui, 85, 30);
        AllGuiTextures.JEI_SHADOW.render(gui, blockX, blocky);
        AllGuiTextures.JEI_DOWN_ARROW.render(gui, blockX + 12, blocky - 32);
        PoseStack stack = gui.pose();
        stack.pushPose();
        stack.translate(blockX -5, blocky - 5, 0);
        stack.mulPose(Axis.XP.rotationDegrees(-12.5f));
        stack.mulPose(Axis.YP.rotationDegrees(22.5f));
        GuiGameElement.of(RFDBlocks.CONTINUOUS_FRYER.getDefaultState())
                .scale(22)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(gui);
        stack.popPose();
    }
}
