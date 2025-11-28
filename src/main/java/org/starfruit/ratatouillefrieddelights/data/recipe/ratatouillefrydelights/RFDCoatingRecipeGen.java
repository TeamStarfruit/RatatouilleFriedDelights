package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.data.recipe.api.CoatingRecipeGen;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModItems;

public class RFDCoatingRecipeGen extends CoatingRecipeGen {

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
            ),
            BREADED_ONION_RINGS = this.create(
                    "onion_coating",
                    b -> b
                            .require(ModItems.ONION.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_ONION_RINGS.get())
                            .duration(200)
            );

    public RFDCoatingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }
}
