package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.forsteri.ratatouille.entry.CRFluids;
import org.forsteri.ratatouille.entry.CRItems;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
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
            .requiresHeat(HeatCondition.HEATED)
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

    PUFF_PASTRY = create("puff_pastry", b -> b
            .require(AllItems.WHEAT_FLOUR.get())
            .require(Items.SUGAR)
            .require(RFDItems.BUTTER)
            .output(RFDItems.PUFF_PASTRY.get())
    ),

    CHICKEN_SO_BEAUTIFUL = create("chicken_so_beautiful", b -> b
            .require(Items.CHICKEN)
            .require(RFDItems.SECRET_SEASONING_POWDER)
            .output(RFDItems.BATTERED_ORIGINAL_DRUMSTICK.get())
            .output(RFDItems.BATTERED_ORIGINAL_KEEL.get())
    ),

    BUTTER = create("butter", b -> b
            .require(Tags.Fluids.MILK, 500)
            .output(RFDItems.BUTTER.get())
            .requiresHeat(HeatCondition.HEATED)
    ),

//    PANCAKE_BATTER = create("pancake_batter", b -> b
//            .require(AllItems.WHEAT_FLOUR.get())
//            .require(Items.SUGAR)
//            .require(Items.EGG)
//            .require(RFDItems.BUTTER)
//            .require(Tags.Fluids.MILK, 250)
//            .output(RFDFluids.PANCAKE_BATTER.get(), 1000)
//    ),

    MAYONNAISE = create("mayonnaise", b -> b
            .require(RFDFluids.SUNFLOWER_OIL.get(), 500)
            .require(CRFluids.EGG_YOLK.get(), 500)
            .output(RFDFluids.MAYONNAISE.get(), 1000)
    ),

    TARTAR_SAUCE = create("tartar_sauce", b -> b
            .require(RFDFluids.MAYONNAISE.get(), 1000)
            .require(Items.SEA_PICKLE)
            .output(RFDFluids.TARTAR_SAUCE.get(), 1000)
    ),

    KETCHUP = create("ketchup", b -> b
            .require(ModItems.TOMATO.get())
            .require(ModItems.TOMATO.get())
            .require(Items.SUGAR)
            .output(RFDFluids.KETCHUP.get(), 1000)
    ),

    ICE_CREAM_BASE = create("ice_cream_base", b -> b
            .require(RFDItems.ICE_CUBES.get())
            .require(RFDItems.ICE_CUBES.get())
            .require(Items.SUGAR)
            .require(Tags.Fluids.MILK, 500)
            .output(RFDFluids.ICE_CREAM_BASE.get(), 1000)
    ),

    COLA_SYRUP = create("cola_syrup", b -> b
            .require(RFDItems.COLA_NUTS.get())
            .require(RFDItems.COLA_NUTS.get())
            .require(Items.SUGAR)
            .require(Items.SUGAR)
            .require(Tags.Fluids.WATER, 1000)
            .output(RFDFluids.COLA_SYRUP.get(), 1000)
    ),

    CONE_MOLD = create("cone_mold", b -> b
            .require(AllItems.IRON_SHEET.get())
            .output(RFDItems.CONE_MOLD.get())
    );

    public RFDMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }
}
