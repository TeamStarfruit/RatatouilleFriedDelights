package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.DeployingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.concurrent.CompletableFuture;

public class RFDDeployingRecipeGen extends DeployingRecipeGen {
    GeneratedRecipe
            BOBA_CUP = create("boba_cup", b -> b
            .require(RFDItems.TALL_CUP.get())
            .require(RFDItems.CARDBOARD_STRAW.get())
            .output(RFDItems.BOBA_CUP.get(), 1)
    ),
            BOXED_FRIES = create("boxed_fries", b -> b
                    .require(RFDItems.FRENCH_FRIES.get())
                    .require(AllItems.CARDBOARD.get())
                    .output(RFDBlocks.BOXED_FRIES.get(), 1)
    ),
            BOXED_CHICKEN_NUGGETS = create("boxed_chicken_nuggets", b -> b
                    .require(RFDItems.CHICKEN_NUGGETS.get())
                    .require(AllItems.CARDBOARD.get())
                    .output(RFDBlocks.BOXED_CHICKEN_NUGGETS.get(), 1)
    );

    public RFDDeployingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }
}
