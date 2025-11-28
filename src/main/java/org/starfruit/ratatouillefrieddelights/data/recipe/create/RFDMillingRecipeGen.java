package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.api.data.recipe.MillingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDMillingRecipeGen extends MillingRecipeGen {

    GeneratedRecipe
            BREADCRUMB = create("breadcrumb", b -> b
            .require(Items.BREAD)
            .output(RFDItems.BREADCRUMB.get(),2)
            .output(0.5f, RFDItems.BREADCRUMB.get(), 2)
            .duration(200)
    );

    public RFDMillingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }
}
