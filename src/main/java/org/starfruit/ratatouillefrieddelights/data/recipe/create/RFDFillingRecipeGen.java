package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.forsteri.ratatouille.Ratatouille;
import org.forsteri.ratatouille.entry.CRFluids;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDFillingRecipeGen extends FillingRecipeGen {
    GeneratedRecipe

    PANCAKE_MOLD_FILLED = create("pancake_mold_filled", b -> b
            .require(RFDItems.PANCAKE_MOLD.get())
            .require(RFDFluids.PANCAKE_BATTER.get(), 250)
            .output(RFDItems.PANCAKE_MOLD_FILLED.get())
    ),

    COLA_FILLED = create("cola_filled", b -> b
                    .require(Items.GLASS_BOTTLE)
                    .require(RFDFluids.COLA_SYRUP.get(), 100)
                    .output(RFDItems.COLA.get())
    ),

    ICE_CREAM_FILLED = create("ice_cream_filled", b -> b
            .require(Items.GLASS_BOTTLE)
            .require(RFDFluids.ICE_CREAM_BASE.get(), 100)
            .output(RFDItems.ICE_CREAM.get())
    ),

    CHOCOLATE_SUNDAE_FILLED = create("chocolate_sundae_filled", b -> b
            .require(RFDItems.ICE_CREAM)
            .require(AllFluids.CHOCOLATE.get(), 100)
            .output(RFDItems.CHOCOLATE_SUNDAE.get())
    ),

    VANILLA_CONE_FILLED = create("vanilla_cone_filled", b -> b
            .require(RFDItems.CONE.get())
            .require(RFDFluids.ICE_CREAM_BASE.get(), 100)
            .output(RFDItems.VANILLA_CONE.get())
    );

    public RFDFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }
}
