package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.TumblingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

import java.util.concurrent.CompletableFuture;

public class TumblingRecipeGen extends StandardProcessingRecipeGen<TumblingRecipe> {

    GeneratedRecipe
            quartz = this.create(
            "quartz_polishing",
            b -> b
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(AllItems.ROSE_QUARTZ.get())
                    .require(Items.SAND)
                    .output(AllItems.POLISHED_ROSE_QUARTZ.get(),8)
                    .duration(500)

            ),
            copper = this.create(
                    "copper_polishing",
                    b -> b
                            .require(Items.EXPOSED_COPPER)
                            .require(Items.SAND)
                            .output(Items.COPPER_BLOCK)
                            .duration(200)

            ),
            PEELED_POTATO = this.create(
                    "peeled_potato",
                    b -> b
                            .require(Items.POTATO)
                            .output(RFDItems.PEELED_POTATO)
                            .duration(100)

            );

    public TumblingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return (RFDRecipeTypes.TUMBLING);
    }
}
