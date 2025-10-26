package org.starfruit.ratatouillefrieddelights.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.starfruit.ratatouillefrieddelights.compat.jei.category.animations.AnimatedContinuousFryer;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

import java.util.ArrayList;
import java.util.List;

public class FryingCategory extends CreateRecipeCategory<FryingRecipe> {
    private final AnimatedContinuousFryer fryer = new AnimatedContinuousFryer();
    private static final int Y_OFFSET = -15;
    private static final int INPUT_Y_ADJUST = -10;

    public FryingCategory(Info<FryingRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, FryingRecipe recipe, IFocusGroup focuses) {
        List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(recipe.getIngredients());
        if (!condensedIngredients.isEmpty()) {
            Pair<Ingredient, MutableInt> pair = condensedIngredients.get(0);
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack itemStack : pair.getFirst().getItems()) {
                ItemStack copy = itemStack.copy();
                copy.setCount(pair.getSecond().getValue());
                stacks.add(copy);
            }
            builder.addSlot(RecipeIngredientRole.INPUT, 20, 35 + Y_OFFSET + INPUT_Y_ADJUST)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(stacks);
        }

        if (!recipe.getFluidIngredients().isEmpty()) {
            FluidIngredient fluidIn = recipe.getFluidIngredients().get(0);
            addFluidSlot(builder, 20, 55 + Y_OFFSET + INPUT_Y_ADJUST, fluidIn);
        }

        List<ProcessingOutput> results = recipe.getRollableResults();
        if (!results.isEmpty()) {
            ProcessingOutput output = results.get(0);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 35 + Y_OFFSET)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addRichTooltipCallback(addStochasticTooltip(output));
        }

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (!requiredHeat.testBlazeBurner(HeatLevel.NONE)) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 100, 52 + Y_OFFSET)
                    .addItemStack(com.simibubi.create.AllBlocks.BLAZE_BURNER.asStack());
        }
        if (!requiredHeat.testBlazeBurner(HeatLevel.KINDLED)) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 120, 52 + Y_OFFSET)
                    .addItemStack(com.simibubi.create.AllItems.BLAZE_CAKE.asStack());
        }
    }

    @Override
    protected void draw(FryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY) {
        int blockX = 40;
        int blockY = 50 + Y_OFFSET;

        AllGuiTextures.JEI_ARROW.render(gui, 85, 35 + Y_OFFSET);
        AllGuiTextures.JEI_SHADOW.render(gui, blockX, blockY);
        AllGuiTextures.JEI_DOWN_ARROW.render(gui, blockX + 10, blockY - 32);

        PoseStack stack = gui.pose();
        stack.pushPose();
        stack.translate(blockX - 8, blockY - 5, 0);
        stack.mulPose(Axis.XP.rotationDegrees(-12.5f));
        stack.mulPose(Axis.YP.rotationDegrees(22.5f));
        GuiGameElement.of(RFDBlocks.CONTINUOUS_FRYER.getDefaultState())
                .scale(24)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(gui);
        stack.popPose();

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        boolean noHeat = requiredHeat == HeatCondition.NONE;

        if (noHeat)
            return;
        gui.drawString(Minecraft.getInstance().font,
                CreateLang.translateDirect(requiredHeat.getTranslationKey()),
                90, 35 + Y_OFFSET + INPUT_Y_ADJUST, requiredHeat.getColor(), false);
    }
}
