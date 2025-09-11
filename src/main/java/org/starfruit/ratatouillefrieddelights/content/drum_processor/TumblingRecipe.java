package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TumblingRecipe extends DrumProcessingRecipe {
    public TumblingRecipe(ProcessingRecipeParams params) {
        super(RFDRecipeTypes.TUMBLING, params);
    }
}
