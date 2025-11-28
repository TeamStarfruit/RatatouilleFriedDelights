package org.starfruit.ratatouillefrieddelights.data.recipe.api;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.block.CopperBlockSet;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.common.Tags;
import org.forsteri.ratatouille.data.recipe.RataouilleRecipeProvider;
import org.forsteri.ratatouille.entry.CRItems;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.TumblingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDTags;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class TumblingRecipeGen extends ProcessingRecipeGen {

    public TumblingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
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
}
