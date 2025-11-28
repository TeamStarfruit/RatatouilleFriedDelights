package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.content.demolder.DemoldingRecipe;
import org.forsteri.ratatouille.entry.CRRecipeTypes;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class DemoldingRecipeGen extends StandardProcessingRecipeGen<DemoldingRecipe> {
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

    public DemoldingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected CRRecipeTypes getRecipeType() {
        return CRRecipeTypes.DEMOLDING;
    }
}
