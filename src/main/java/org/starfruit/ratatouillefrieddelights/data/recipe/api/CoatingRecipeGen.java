package org.starfruit.ratatouillefrieddelights.data.recipe.api;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModItems;

public abstract class CoatingRecipeGen extends ProcessingRecipeGen {

    public CoatingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return (RFDRecipeTypes.COATING);
    }
}
