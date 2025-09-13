package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille_fry_delights;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.drum_processor.CoatingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

import java.util.concurrent.CompletableFuture;

public class CoatingRecipeGen extends StandardProcessingRecipeGen<CoatingRecipe> {

    GeneratedRecipe
            RAW_FILLET_O_FISH = this.create(
                    "fillet_fish_coating",
                    b -> b
                            .require(RFDItems.RAW_FISH_FILLET.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_FISH_FILLET.get())
                            .duration(200)
            ),
            BREADED_ORIGINAL_DRUMSTICK = this.create(
                    "drumstick_coating",
                    b -> b
                            .require(RFDItems.BATTERED_ORIGINAL_DRUMSTICK.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_ORIGINAL_DRUMSTICK.get())
                            .duration(200)
            ),
            BREADED_ORIGINAL_KEEL = this.create(
                    "keel_coating",
                    b -> b
                            .require(RFDItems.BATTERED_ORIGINAL_KEEL.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_ORIGINAL_KEEL.get())
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
