package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.forsteri.ratatouille.Ratatouille;
import org.forsteri.ratatouille.entry.CRFluids;
import org.forsteri.ratatouille.entry.CRItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDMixingRecipeGen extends MixingRecipeGen {

    GeneratedRecipe
            BURGER_BUN_MOLD_UNBAKED = create("burger_bun_mold_unbaked", b -> b
            .require(AllItems.WHEAT_FLOUR.get())
            .require(AllItems.WHEAT_FLOUR.get())
            .require(RFDItems.BUTTER)
            .require(CRFluids.EGG_YOLK.get(), 250)
            .require(RFDItems.BURGER_BUN_MOLD)
            .output(RFDItems.BURGER_BUN_MOLD_UNBAKED.get(), 1)
    ),

    RAW_CHEESE = create("raw_cheese", b -> b
            .require(Tags.Fluids.MILK, 1000)
            .require(CRItems.SALT)
            .output(RFDItems.RAW_CHEESE.get())
    ),

    RAW_FISH_FILLET = create("raw_fish_fillet", b -> b
            .require(CRFluids.EGG_YOLK.get(), 250)
            .require(CRItems.SALT)
            .require(Tags.Items.FOODS_RAW_FISH)
            .output(RFDItems.RAW_FISH_FILLET.get())
    ),

    RAW_CHICKEN_NUGGETS = create("raw_chicken_nuggets", b -> b
            .require(AllItems.WHEAT_FLOUR.get())
            .require(CRFluids.EGG_YOLK.get(), 250)
            .require(CRItems.SALT)
            .require(ModItems.CHICKEN_CUTS.get())
            .output(RFDItems.RAW_CHICKEN_NUGGETS.get())
            ),

    SECRET_SEASONING_POWDER = create("secret_seasoning_powder", b -> b
            .require(AllItems.WHEAT_FLOUR.get())
            .require(Items.BEETROOT)
            .require(Items.DRIED_KELP)
            .require(CRItems.SALT)
            .require(ModItems.ONION.get())
            .output(RFDItems.SECRET_SEASONING_POWDER.get())
    ),

    PUFF_PASTRY = create("puff_pastry", b -> b
            .require(AllItems.WHEAT_FLOUR.get())
            .require(Items.SUGAR)
            .require(RFDItems.BUTTER)
            .output(RFDItems.PUFF_PASTRY.get())
    ),

    CHICKEN_SO_BEAUTIFUL = create("chicken_so_beautiful", b -> b
            .require(Items.CHICKEN)
            .require(RFDItems.SECRET_SEASONING_POWDER)
            .require(Items.EGG)
            .output(RFDItems.RAW_ORIGINAL_RECIPE_CHICKEN_LEG.get())
            .output(RFDItems.RAW_ORIGINAL_RECIPE_CHICKEN_BREAST.get())
    ),

    BUTTER = create("butter", b -> b
            .require(Tags.Fluids.MILK, 500)
            .output(RFDItems.BUTTER.get())
            .requiresHeat(HeatCondition.HEATED)
    );

    public RFDMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Ratatouille.MOD_ID);
    }
}
