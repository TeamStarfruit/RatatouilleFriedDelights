package org.starfruit.ratatouillefrieddelights.content.drumprocessor;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CoatingRecipe extends DrumProcessingRecipe {
    public CoatingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(RFDRecipeTypes.COATING, params);
    }
}
