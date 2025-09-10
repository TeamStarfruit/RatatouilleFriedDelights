package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.api.data.recipe.CuttingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.forsteri.ratatouille.entry.CRItems;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDCuttingRecipeGen extends CuttingRecipeGen {
    GeneratedRecipe
            RAW_POTATO_STICK_CUT = create("raw_potato_stick_cut", b -> b
            .require(Items.POTATO)
            .output(RFDItems.RAW_POTATO_STICK.get(), 2)
    ),

    CHEESE_SLICE_CUT = create("cheese_slice_cut", b -> b
            .require(RFDItems.CHEESE)
            .output(RFDItems.CHEESE_SLICE.get(), 4)
    ),

    TOMATO_SLICES_CUT = create("tomato_slices_cut", b -> b
            .require(ModItems.TOMATO.get())
            .output(RFDItems.TOMATO_SLICES.get(), 2)
    ),

    APPLE_SLICES_CUT = create("apple_slices_cut", b -> b
            .require(Items.APPLE)
            .output(RFDItems.APPLE_SLICES.get(), 2)
    ),

    ICE_CUBES_CUT = create("ice_cubes_cut", b -> b
            .require(Items.ICE)
            .output(RFDItems.ICE_CUBES.get(), 2)
    ),

    BURGER_BUN_MOLD_CUT = create("burger_bun_mold_cut", b -> b
            .require(CRItems.CAKE_MOLD.get())
            .output(RFDItems.BURGER_BUN_MOLD.get(), 2)
    ),

    PAN_CAKE_MOLD_CUT = create("pancake_mold_cut", b -> b
            .require(RFDItems.BURGER_BUN_MOLD.get())
            .output(RFDItems.PANCAKE_MOLD.get(), 1)
    );

    public RFDCuttingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }
}
