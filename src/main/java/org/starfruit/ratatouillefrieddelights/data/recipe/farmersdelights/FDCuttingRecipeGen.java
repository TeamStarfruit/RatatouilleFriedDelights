package org.starfruit.ratatouillefrieddelights.data.recipe.farmersdelights;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;
import vectorwing.farmersdelight.data.recipe.CuttingRecipes;

@SuppressWarnings("unused")
public class FDCuttingRecipeGen extends CuttingRecipes {
    public static void register(RecipeOutput output) {
        RFDCutting(output);
    }

    private static void RFDCutting(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(RFDItems.BURGER_BUN), Ingredient.of(CommonTags.TOOLS_KNIFE), RFDItems.BOTTOM_BURGER_BUN.get(), 1)
                .addResult(RFDItems.TOP_BURGER_BUN,1)
                .build(output);
    }
}