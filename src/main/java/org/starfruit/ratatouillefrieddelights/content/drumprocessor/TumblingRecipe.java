package org.starfruit.ratatouillefrieddelights.content.drumprocessor;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TumblingRecipe extends DrumProcessingRecipe {
    public TumblingRecipe(ProcessingRecipeParams params) {
        super(RFDRecipeTypes.TUMBLING, params);
    }
}
