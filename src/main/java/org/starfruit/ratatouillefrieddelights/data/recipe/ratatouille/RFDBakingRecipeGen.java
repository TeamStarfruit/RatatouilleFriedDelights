package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille;

import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.data.recipe.api.BakingRecipeGen;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

public class RFDBakingRecipeGen extends BakingRecipeGen {

//    GeneratedRecipe
//            RICE = this.create(
//            CRItems.BOIL_STONE::get,
//            b -> b.output(CRItems.MATURE_MATTER.get()).duration(200)
//    );

    public RFDBakingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }

}