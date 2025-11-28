package org.starfruit.ratatouillefrieddelights.content.drumprocessor;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class DrumProcessingRecipe extends ProcessingRecipe<RecipeWrapper> {
    public DrumProcessingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(typeInfo, params);
    }

    protected int getMaxInputCount() {
        return 16;
    }

    protected int getMaxOutputCount() {
        return 16;
    }

    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        int count = 0;
        for (net.minecraft.world.item.crafting.Ingredient ingredient : ingredients) {
            for (int j = 0; j < inv.getContainerSize(); j++) {
                if (ingredient.test(inv.getItem(j))) {
                    count += 1;
                    break;
                }
            }
        }
        return count == ingredients.size();
    }
}
