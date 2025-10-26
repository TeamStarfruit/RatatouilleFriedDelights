package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

import java.util.concurrent.CompletableFuture;

public class FryingRecipeGen extends StandardProcessingRecipeGen<FryingRecipe> {
    GeneratedRecipe
            FRIED_APPLE_PIE = this.create("fried_apple_pie",
            b -> b
                    .require(RFDItems.RAW_APPLE_PIE.get())
                    .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.FRIED_APPLE_PIE.get())
                    .duration(200)),

    FILLET_O_FISH = this.create("fillet_o_fish",
                    b -> b
                .require(RFDItems.BREADED_FISH_FILLET.get())
                .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                .requiresHeat(HeatCondition.HEATED)
                .output(RFDItems.FILLET_O_FISH.get())
                .duration(200)),

    FRENCH_FRIES = this.create("french_fries",
            b -> b
                    .require(RFDItems.RAW_POTATO_STICK.get())
                    .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.FRENCH_FRIES.get())
                    .duration(200)),

    CHICKEN_NUGGETS = this.create("chicken_nuggets",
            b -> b
                    .require(RFDItems.RAW_CHICKEN_NUGGETS.get())
                    .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.CHICKEN_NUGGETS.get())
                    .duration(200)),

    ORIGINAL_CHICKEN_DRUMSTICK = this.create("original_chicken_drumstick",
            b -> b
                    .require(RFDItems.BREADED_ORIGINAL_DRUMSTICK.get())
                    .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.ORIGINAL_CHICKEN_DRUMSTICK.get())
                    .duration(200)),

    ORIGINAL_CHICKEN_KEEL = this.create("original_chicken_keel",
            b -> b
                    .require(RFDItems.BREADED_ORIGINAL_KEEL.get())
                    .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.ORIGINAL_CHICKEN_KEEL.get())
                    .duration(200)),

    FRIED_DONUT = this.create("fried_donut",
            b -> b
                    .require(RFDItems.DOUGH_RING.get())
                    .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.FRIED_DONUT.get())
                    .duration(200)),

    FRIED_ONION_RINGS = this.create("fried_onion_rings",
            b -> b
                    .require(RFDItems.BREADED_ONION_RINGS.get())
                    .require(RFDFluids.SUNFLOWER_OIL.get(), 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.ONION_RINGS.get())
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
