package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

public class CoatingRecipe extends StandardProcessingRecipe<RecipeInput> {
    public CoatingRecipe(ProcessingRecipeParams params) {
        super(RFDRecipeTypes.COATING, params);
    }

    protected int getMaxInputCount() {
        return 2;
    }

    protected int getMaxOutputCount() {
        return 1;
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
