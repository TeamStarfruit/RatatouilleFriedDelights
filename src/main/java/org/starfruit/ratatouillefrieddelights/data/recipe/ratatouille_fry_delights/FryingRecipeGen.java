package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille_fry_delights;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

import java.util.concurrent.CompletableFuture;

public class FryingRecipeGen extends StandardProcessingRecipeGen<FryingRecipe> {
    GeneratedRecipe
            FRIED_APPLE_PIE = this.create("fried_apple_pie",
            b -> b
                    .require(RFDItems.RAW_APPLE_PIE.get())
                    .output(RFDItems.FRIED_APPLE_PIE.get())
                    .duration(200)),

    FILLET_O_FISH = this.create("fillet_o_fish",
                    b -> b
                .require(RFDItems.BREADED_FISH_FILLET.get())
                .output(RFDItems.FILLET_O_FISH.get())
                .duration(200)),

    FRENCH_FRIES = this.create("french_fries",
            b -> b
                    .require(RFDItems.RAW_POTATO_STICK.get())
                    .output(RFDItems.FRENCH_FRIES.get())
                    .duration(200)),

    CHICKEN_NUGGETS = this.create("chicken_nuggets",
            b -> b
                    .require(RFDItems.RAW_CHICKEN_NUGGETS.get())
                    .output(RFDItems.CHICKEN_NUGGETS.get())
                    .duration(200)),

    ORIGINAL_CHICKEN_DRUMSTICK = this.create("original_chicken_drumstick",
            b -> b
                    .require(RFDItems.BREADED_ORIGINAL_DRUMSTICK.get())
                    .output(RFDItems.ORIGINAL_CHICKEN_DRUMSTICK.get())
                    .duration(200)),

    ORIGINAL_CHICKEN_KEEL = this.create("original_chicken_keel",
            b -> b
                    .require(RFDItems.BREADED_ORIGINAL_KEEL.get())
                    .output(RFDItems.ORIGINAL_CHICKEN_KEEL.get())
                    .duration(200))
    ;


    public FryingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return RFDRecipeTypes.FRYING;
    }
}
