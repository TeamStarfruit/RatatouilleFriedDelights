package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille_fry_delights;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.drum_processor.CoatingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class CoatingRecipeGen extends StandardProcessingRecipeGen<CoatingRecipe> {

    GeneratedRecipe
            RICE = this.create(
            "rice",
            b -> b
                    .require(ModItems.RICE_PANICLE.get())
                    .require(Items.SAND)
                    .output(ModItems.RICE.get())
                    .duration(200)

    );

    public CoatingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return (RFDRecipeTypes.COATING);
    }
}
