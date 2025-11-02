package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights;

import com.simibubi.create.AllItems;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.IItemHandler;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.forsteri.ratatouille.entry.CRItems;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.TumblingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModItems;

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
            COPPER_BLOCK = this.create(
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
                            .require(Items.SAND)
                            .output(RFDItems.PEELED_POTATO)
                            .duration(100)

            ),
            DOUGH_RING = create("dough_ring", b -> b
                    .require(CRItems.SALTY_DOUGH.get())
                    .require(RFDItems.BUTTER.get())
                    .output(RFDItems.DOUGH_RING.get())
            ),
            PASTA42 = this.create(
                    "pasta_42",
                    b -> b
                            .require(ModItems.RAW_PASTA.get())
                            .require(Tags.Items.CONCRETES)
                            .output(RFDItems.NO42_CONCRETE_MIXING_PASTA.get())
                            .duration(200)
            );

    public TumblingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return (RFDRecipeTypes.TUMBLING);
    }
}
