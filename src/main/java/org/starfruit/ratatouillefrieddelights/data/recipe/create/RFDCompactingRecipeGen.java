package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDCompactingRecipeGen extends CompactingRecipeGen {
    GeneratedRecipe
            SUNFLOWER_OIL = create("sunflower_oil", b -> b
            .require(RFDItems.SUNFLOWER_SEEDS.get())
            .output(RFDFluids.SUNFLOWER_OIL.get(), 100)
    );

    public RFDCompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }
}
