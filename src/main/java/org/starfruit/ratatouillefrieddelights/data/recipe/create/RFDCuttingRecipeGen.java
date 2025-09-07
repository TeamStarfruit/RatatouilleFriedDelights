package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.api.data.recipe.CuttingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.Ratatouille;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDCuttingRecipeGen extends CuttingRecipeGen {
/*    GeneratedRecipe
            SAUSAGE_CASING = create("sausage_casing", b -> b
            .require(Items.SLIME_BALL)
            .output(CRItems.SAUSAGE_CASING.get(), 2)
    );
*/
    public RFDCuttingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Ratatouille.MOD_ID);
    }
}
