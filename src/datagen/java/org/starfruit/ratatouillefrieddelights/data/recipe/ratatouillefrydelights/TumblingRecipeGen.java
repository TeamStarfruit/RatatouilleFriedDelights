package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.block.CopperBlockSet;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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
            ),
            COPPER_TILES = copperChain(AllBlocks.COPPER_TILES),
            COPPER_SHINGLES = copperChain(AllBlocks.COPPER_SHINGLES),

            COPPER_BLOCK = oxidizationChain(List.of(
                () -> Blocks.COPPER_BLOCK,
                () -> Blocks.EXPOSED_COPPER,
                () -> Blocks.WEATHERED_COPPER,
                () -> Blocks.OXIDIZED_COPPER)),

            CUT_COPPER = oxidizationChain(List.of(
                    () -> Blocks.CUT_COPPER,
                    () -> Blocks.EXPOSED_CUT_COPPER,
                    () -> Blocks.WEATHERED_CUT_COPPER,
                    () -> Blocks.OXIDIZED_CUT_COPPER)),

            CUT_COPPER_STAIRS = oxidizationChain(List.of(
                    () -> Blocks.CUT_COPPER_STAIRS,
                    () -> Blocks.EXPOSED_CUT_COPPER_STAIRS,
                    () -> Blocks.WEATHERED_CUT_COPPER_STAIRS,
                    () -> Blocks.OXIDIZED_CUT_COPPER_STAIRS)),

            CUT_COPPER_SLAB = oxidizationChain(List.of(
                    () -> Blocks.CUT_COPPER_SLAB,
                    () -> Blocks.EXPOSED_CUT_COPPER_SLAB,
                    () -> Blocks.WEATHERED_CUT_COPPER_SLAB,
                    () -> Blocks.OXIDIZED_CUT_COPPER_SLAB)),

            COPPER_DOOR = oxidizationChain(List.of(
                    () -> Blocks.COPPER_DOOR,
                    () -> Blocks.EXPOSED_COPPER_DOOR,
                    () -> Blocks.WEATHERED_COPPER_DOOR,
                    () -> Blocks.OXIDIZED_COPPER_DOOR)),

            COPPER_TRAPDOOR = oxidizationChain(List.of(
                    () -> Blocks.COPPER_TRAPDOOR,
                    () -> Blocks.EXPOSED_COPPER_TRAPDOOR,
                    () -> Blocks.WEATHERED_COPPER_TRAPDOOR,
                    () -> Blocks.OXIDIZED_COPPER_TRAPDOOR)),

            COPPER_GRATE = oxidizationChain(List.of(
                    () -> Blocks.COPPER_GRATE,
                    () -> Blocks.EXPOSED_COPPER_GRATE,
                    () -> Blocks.WEATHERED_COPPER_GRATE,
                    () -> Blocks.OXIDIZED_COPPER_GRATE)),

            COPPER_BULB = oxidizationChain(List.of(
                    () -> Blocks.COPPER_BULB,
                    () -> Blocks.EXPOSED_COPPER_BULB,
                    () -> Blocks.WEATHERED_COPPER_BULB,
                    () -> Blocks.OXIDIZED_COPPER_BULB)),

            CHISELED_COPPER = oxidizationChain(List.of(
                    () -> Blocks.CHISELED_COPPER,
                    () -> Blocks.EXPOSED_CHISELED_COPPER,
                    () -> Blocks.WEATHERED_CHISELED_COPPER,
                    () -> Blocks.OXIDIZED_CHISELED_COPPER))
        ;

    public TumblingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    public GeneratedRecipe oxidizationChain(List<Supplier<ItemLike>> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            Supplier<ItemLike> to = chain.get(i);
            Supplier<ItemLike> from = chain.get(i + 1);
            createWithDeferredId(idWithSuffix(to, "_from_deoxidising"), b -> b.require(from.get())
                    .require(ItemTags.SAND)
                    .duration(200)
                    .output(to.get()));
        }
        return null;
    }

    public GeneratedRecipe copperChain(CopperBlockSet set) {
        for (CopperBlockSet.Variant<?> variant : set.getVariants()) {
            List<Supplier<ItemLike>> chain = new ArrayList<>(4);

            for (WeatheringCopper.WeatherState state : WeatheringCopper.WeatherState.values())
                chain.add(set.get(variant, state, false)::get);

            oxidizationChain(chain);
        }
        return null;
    }


    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return (RFDRecipeTypes.TUMBLING);
    }
}
