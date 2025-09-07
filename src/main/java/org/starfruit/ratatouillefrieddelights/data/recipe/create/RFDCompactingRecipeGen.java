package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.Ratatouille;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDCompactingRecipeGen extends CompactingRecipeGen {
/*    GeneratedRecipe
            CAKE_MOLD = create("cake_mold", b -> b
            .require(AllItems.IRON_SHEET.get())
            .require(AllItems.IRON_SHEET.get())
            .require(AllItems.IRON_SHEET.get())
            .output(CRItems.CAKE_MOLD.get(), 1)
    ),
            CHOCOLATE_MOLD = create("chocolate_mold", b -> b
                    .require(AllItems.IRON_SHEET.get())
                    .require(AllItems.IRON_SHEET.get())
                    .output(CRItems.CHOCOLATE_MOLD.get(), 1)
            ),

            POPSICLE_MOLD = create("popsicle_mold", b -> b
                    .require(AllItems.IRON_SHEET.get())
                    .output(CRItems.POPSICLE_MOLD.get(), 1)
            );
*/
    public RFDCompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Ratatouille.MOD_ID);
    }
}
