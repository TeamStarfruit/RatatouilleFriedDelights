package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille_fry_delights;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class FryingRecipeGen extends StandardProcessingRecipeGen<FryingRecipe> {
    GeneratedRecipe
            FRIED_APPLE_PIE = this.create("fried_apple_pie",
            b -> b
                    .require(RFDItems.RAW_APPLE_PIE.get())
                    .output(RFDItems.FRIED_APPLE_PIE.get())
                    .duration(200));


    public FryingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return RFDRecipeTypes.FRYING;
    }
}
