package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.CoatingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class CoatingRecipeGen extends StandardProcessingRecipeGen<CoatingRecipe> {

    GeneratedRecipe
            RAW_FILLET_O_FISH = this.create(
                    "fillet_fish_coating",
                    b -> b
                            .require(RFDItems.RAW_FISH_FILLET.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_FISH_FILLET.get())
                            .duration(200)
            ),
            BREADED_ORIGINAL_DRUMSTICK = this.create(
                    "drumstick_coating",
                    b -> b
                            .require(RFDItems.BATTERED_ORIGINAL_DRUMSTICK.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_ORIGINAL_DRUMSTICK.get())
                            .duration(200)
            ),
            BREADED_ORIGINAL_KEEL = this.create(
                    "keel_coating",
                    b -> b
                            .require(RFDItems.BATTERED_ORIGINAL_KEEL.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_ORIGINAL_KEEL.get())
                            .duration(200)
            ),
            BREADED_ONION_RINGS = this.create(
                    "onion_coating",
                    b -> b
                            .require(ModItems.ONION.get())
                            .require(RFDItems.BREADCRUMB)
                            .output(RFDItems.BREADED_ONION_RINGS.get())
                            .duration(200)
            ),
            PASTA42 = this.create(
                    "pasta_42",
                    b -> b
                            .require(ModItems.RAW_PASTA.get())
                            .require(Tags.Items.CONCRETES)
                            .output(RFDItems.NUMBER42_CONCRETE_MIXING_PASTA.get())
                            .duration(200)
            );

    public CoatingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return (RFDRecipeTypes.COATING);
    }
}
