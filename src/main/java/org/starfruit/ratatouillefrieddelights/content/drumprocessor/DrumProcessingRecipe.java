package org.starfruit.ratatouillefrieddelights.content.drumprocessor;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class DrumProcessingRecipe extends StandardProcessingRecipe<RecipeInput> {
    public DrumProcessingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeParams params) {
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
    public boolean matches(RecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        int count = 0;
        for (net.minecraft.world.item.crafting.Ingredient ingredient : ingredients) {
            for (int j = 0; j < inv.size(); j++) {
                if (ingredient.test(inv.getItem(j))) {
                    count += 1;
                    break;
                }
            }
        }
        return count == ingredients.size();
    }
}
