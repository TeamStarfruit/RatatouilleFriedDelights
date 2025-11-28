package org.starfruit.ratatouillefrieddelights.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

public class RFDTreeFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> COLA_TREE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    new ResourceLocation(RatatouilleFriedDelights.MOD_ID, "cola_tree"));
}
