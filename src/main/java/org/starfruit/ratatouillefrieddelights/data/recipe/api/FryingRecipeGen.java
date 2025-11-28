package org.starfruit.ratatouillefrieddelights.data.recipe.api;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.data.recipe.RataouilleRecipeProvider;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

public abstract class FryingRecipeGen extends ProcessingRecipeGen {
    public FryingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return RFDRecipeTypes.FRYING;
    }
}
