package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.data.recipe.api.SqueezingRecipeGen;
import org.forsteri.ratatouille.entry.CRRecipeTypes;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

@SuppressWarnings("unused")
public class RFDSqueezingRecipeGen extends SqueezingRecipeGen {
/*    GeneratedRecipe
            RAW_SAUSAGE = this.create("raw_sausage", b -> b.require(CRFluids.MINCE_MEAT.get(), 250)
            .require(CRItems.SAUSAGE_CASING.get())
            .output(CRItems.RAW_SAUSAGE.get())),

    RAW_PASTA = this.create("raw_pasta", b -> b.require(CRItems.SALTY_DOUGH.get())
            .output(ModItems.RAW_PASTA.get())),

    COMPOST_1TO1 = this.create("compost_1to1", b -> b
            .require(CRTags.COMPOSTABLE_ITEMS_1to1)
            .output(CRItems.COMPOST_MASS.get(), 1)
            .duration(200)
    ),

    COMPOST_1TO4 = this.create("compost_1to4", b -> b
            .require(CRTags.COMPOSTABLE_ITEMS_1to4)
            .output(CRItems.COMPOST_MASS.get(), 4)
            .duration(200)
    ),

    COMPOST_FROM_MELON = this.create("compost_from_melon", b -> b
            .require(Items.MELON)
            .output(CRItems.COMPOST_MASS.get(), 9)
            .duration(200)
    ),

    COMPOST_2TO1 = this.create("compost_2to1", b -> b
            .require(CRTags.COMPOSTABLE_ITEMS_2to1)
            .output(0.5f, CRItems.COMPOST_MASS.get())
            .duration(200)
    ),

    COMPOST_4TO1 = this.create("compost_4to1", b -> b
            .require(CRTags.COMPOSTABLE_ITEMS_4to1)
            .output(0.25f, CRItems.COMPOST_MASS.get())
            .duration(200)
    );
*/
    public RFDSqueezingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }
}
