package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.forsteri.ratatouille.entry.CRFluids;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDFillingRecipeGen extends FillingRecipeGen {
    GeneratedRecipe

    PANCAKE_MOLD_FILLED = create("pancake_mold_filled", b -> b
            .require(RFDItems.PANCAKE_MOLD.get())
            .require(CRFluids.CAKE_BATTER.get(), 250)
            .output(RFDItems.PANCAKE_MOLD_FILLED.get())
    ),

    CONE_MOLD_FILLED = create("cone_mold_filled", b -> b
            .require(RFDItems.CONE_MOLD.get())
            .require(CRFluids.CAKE_BATTER.get(), 50)
            .output(RFDItems.CONE_MOLD_FILLED.get())
    ),

    ICE_CREAM_FILLED = create("ice_cream_filled", b -> b
            .require(RFDItems.SHORT_CUP)
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
    ),

    SUNFLOWER_SEED_OIL_BOTTLE = create("sunflower_seed_oil_bottle", b -> b
            .require(Items.GLASS_BOTTLE)
            .require(RFDFluids.SUNFLOWER_OIL.get(), 125)
            .output(RFDItems.SUNFLOWER_SEED_OIL_BOTTLE.get())
    ),

    CREAMY_DONUT = create("creamy_donut", b -> b
            .require(RFDItems.FRIED_DONUT.get())
            .require(Tags.Fluids.MILK, 250)
            .output(RFDItems.CREAMY_DONUT.get())
    ),

    CHOCOLATE_DONUT = create("chocolate_donut", b -> b
            .require(RFDItems.FRIED_DONUT.get())
            .require(AllFluids.CHOCOLATE.get(), 250)
            .output(RFDItems.CHOCOLATE_DONUT.get())
    ),

    KETCHUP_DIP_CUP = create("ketchup_dip_cup", b -> b
            .require(RFDFluids.KETCHUP.get(), 250)
            .require(AllItems.CARDBOARD.get())
            .output(RFDBlocks.KETCHUP_DIP_CUP.get())
    ),

    TARTAR_DIP_CUP = create("tartar_dip_cup", b -> b
            .require(RFDFluids.TARTAR_SAUCE.get(), 250)
            .require(AllItems.CARDBOARD.get())
            .output(RFDBlocks.TARTAR_DIP_CUP.get())
    ),

    HONEY_DIP_CUP = create("honey_dip_cup", b -> b
            .require(AllFluids.HONEY.get(), 250)
            .require(AllItems.CARDBOARD.get())
            .output(RFDBlocks.HONEY_DIP_CUP.get())
    );

    public RFDFillingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }
}
