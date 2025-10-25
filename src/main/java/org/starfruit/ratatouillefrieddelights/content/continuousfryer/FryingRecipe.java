package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

public class FryingRecipe extends StandardProcessingRecipe<RecipeInput> {
    public FryingRecipe(ProcessingRecipeParams params) {
        super(RFDRecipeTypes.FRYING, params);
    }

    protected int getMaxInputCount() {
        return 1;
    }

    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 0;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(RecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.getFirst()
                .test(inv.getItem(0));
    }
}
