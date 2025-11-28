package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille;

import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.data.recipe.RataouilleRecipeProvider;
import org.forsteri.ratatouille.data.recipe.api.DemoldingRecipeGen;
import org.forsteri.ratatouille.entry.CRRecipeTypes;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

@SuppressWarnings("unused")
public class RFDDemoldingRecipeGen extends DemoldingRecipeGen {
    GeneratedRecipe
            BURGER_BUN = this.create(RFDItems.BURGER_BUN_MOLD_BAKED::get, b -> b
            .output(RFDItems.BURGER_BUN.get())
            .output(RFDItems.BURGER_BUN_MOLD.get())
    ),
            PAN_CAKE = this.create(RFDItems.PANCAKE_MOLD_BAKED::get, b -> b
                    .output(RFDItems.PANCAKE.get())
                    .output(RFDItems.PANCAKE_MOLD.get())
    ),
            CONE = this.create(RFDItems.CONE_MOLD_BAKED::get, b -> b
                    .output(RFDItems.CONE.get())
                    .output(RFDItems.CONE_MOLD.get())
    ),
            BURGER_BUN_SEPARATION = this.create(RFDItems.BURGER_BUN::get, b -> b
                    .output(RFDItems.BOTTOM_BURGER_BUN.get())
                    .output(RFDItems.TOP_BURGER_BUN.get())
    );

    public RFDDemoldingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }
}
