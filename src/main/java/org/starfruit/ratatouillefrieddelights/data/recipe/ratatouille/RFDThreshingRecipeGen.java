package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.forsteri.ratatouille.data.recipe.api.ThreshingRecipeGen;
import org.forsteri.ratatouille.entry.CRRecipeTypes;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

@SuppressWarnings("unused")
public final class RFDThreshingRecipeGen extends ThreshingRecipeGen {

    GeneratedRecipe
            COLA_NUTS_THRESHING = this.create(
                    RatatouilleFriedDelights.asResource("cola_nuts_threshing"),
            b -> b
                    .require(RFDItems.COLA_FRUITS.get())
                    .output(RFDItems.COLA_NUTS.get())
                    .output(RFDItems.COLA_NUTS.get())
                    .output(0.5F, RFDItems.COLA_NUTS.get(),2).duration(200)
    ),
            SUNFLOWER_SEEDS = this.create(
                    RatatouilleFriedDelights.asResource("sunflower_seeds"),
            b -> b
                    .require(Items.SUNFLOWER)
                    .output(RFDItems.SUNFLOWER_SEEDS.get(),2)
                    .output(0.5F, RFDItems.SUNFLOWER_SEEDS.get(),2)
                    .duration(200)
    );
    /*
        BOIL_STONE = this.create("boil_stone",
                b -> b
                        .require(Tags.Items.STONES)
                        .output(CRItems.BOIL_STONE.get())
                        .duration(200)
        ),

        DRIED_COCOA_BEANS = this.create(
                "dried_cocoa_beans",
                b -> b
                        .require(CRItems.DRIED_COCOA_BEANS.get())
                        .output(CRItems.DRIED_COCOA_NIBS.get(), 2)
                        .output(0.5F, CRItems.DRIED_COCOA_NIBS.get())
                        .duration(200)
        ),

        WHEAT_KERNELS = this.create(
                "wheat_kernels",
                b -> b
                        .require(Items.WHEAT)
                        .output(CRItems.WHEAT_KERNELS.get(), 2)
                        .output(0.5F, CRItems.WHEAT_KERNELS.get())
                        .duration(200)
        );
    */
    public RFDThreshingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }
}