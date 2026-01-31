package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights;

import com.pyzpre.createbitterballen.index.ItemRegistry;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.data.recipe.RataouilleRecipeProvider;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.data.recipe.api.FryingRecipeGen;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDTags;

public class RFDFryingRecipeGen extends FryingRecipeGen {
    GeneratedRecipe
            FRIED_APPLE_PIE = this.create("fried_apple_pie",
            b -> b
                    .require(RFDItems.RAW_APPLE_PIE.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.FRIED_APPLE_PIE.get())
                    .duration(200)),

    FILLET_O_FISH = this.create("fillet_o_fish",
                    b -> b
                .require(RFDItems.BREADED_FISH_FILLET.get())
                            .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                .requiresHeat(HeatCondition.HEATED)
                .output(RFDItems.FILLET_O_FISH.get())
                .duration(200)),

    FRENCH_FRIES = this.create("french_fries",
            b -> b
                    .require(RFDItems.RAW_POTATO_STICKS.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.FRENCH_FRIES.get())
                    .duration(200)),

    CHICKEN_NUGGETS = this.create("chicken_nuggets",
            b -> b
                    .require(RFDItems.RAW_CHICKEN_NUGGETS.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.CHICKEN_NUGGETS.get())
                    .duration(200)),

    ORIGINAL_CHICKEN_DRUMSTICK = this.create("original_chicken_drumstick",
            b -> b
                    .require(RFDItems.BREADED_ORIGINAL_DRUMSTICK.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.ORIGINAL_CHICKEN_DRUMSTICK.get())
                    .duration(200)),

    ORIGINAL_CHICKEN_KEEL = this.create("original_chicken_keel",
            b -> b
                    .require(RFDItems.BREADED_ORIGINAL_KEEL.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.ORIGINAL_CHICKEN_KEEL.get())
                    .duration(200)),

    FRIED_DONUT = this.create("fried_donut",
            b -> b
                    .require(RFDItems.DOUGH_RING.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.FRIED_DONUT.get())
                    .duration(200)),

    FRIED_ONION_RINGS = this.create("fried_onion_rings",
            b -> b
                    .require(RFDItems.BREADED_ONION_RINGS.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(RFDItems.ONION_RINGS.get())
                    .duration(200)),

    CBB_FRIES = create("cbb_fries",
            b -> b.require(ItemRegistry.RAW_FRIES.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(ItemRegistry.FRIES.get())
                    .duration(100)
                    .whenModLoaded("create_bic_bit")),

    CBB_KROKET = create("cbb_kroket",
            b -> b.require(ItemRegistry.RAW_KROKET.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(ItemRegistry.KROKET.get())
                    .duration(100)
                    .whenModLoaded("create_bic_bit")),

    CBB_FRIKANDEL = create("cbb_frikandel",
            b -> b.require(ItemRegistry.RAW_FRIKANDEL.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(ItemRegistry.FRIKANDEL.get())
                    .duration(100)
                    .whenModLoaded("create_bic_bit")),

    CBB_BITTERBALLEN = create("cbb_bitterballen",
            b -> b.require(ItemRegistry.RAW_BITTERBALLEN.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(ItemRegistry.BITTERBALLEN.get())
                    .duration(100)
                    .whenModLoaded("create_bic_bit")),

    CBB_OLIEBOLLEN = create("cbb_oliebollen",
            b -> b.require(com.simibubi.create.AllItems.DOUGH.get())
                    .require(RFDTags.RFDFluidTags.OIL.tag, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(ItemRegistry.OLIEBOLLEN.get())
                    .duration(100)
                    .whenModLoaded("create_bic_bit"));

    public RFDFryingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return RFDRecipeTypes.FRYING;
    }
}
