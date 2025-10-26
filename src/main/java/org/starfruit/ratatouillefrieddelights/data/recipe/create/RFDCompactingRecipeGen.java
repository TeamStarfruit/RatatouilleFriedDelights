package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.forsteri.ratatouille.entry.CRItems;
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
    ),
            DOUGH_RING = create("dough_ring", b -> b
                    .require(CRItems.SALTY_DOUGH.get())
                    .require(RFDItems.BUTTER.get())
                    .output(RFDItems.DOUGH_RING.get())
    ),
            CHARCOAL_COMPACT = create("charcoal_compact", b -> b
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .require(RFDItems.FRIED_RESIDUE.get())
                    .output(Items.CHARCOAL)
    );

    public RFDCompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }
}
