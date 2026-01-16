package org.starfruit.ratatouillefrieddelights.worldgen;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;

import java.util.ArrayList;
import java.util.List;

public class RFDPlacedFeatures {
    public static final ResourceKey<PlacedFeature> COLA_TREE_KEY =
            registerKey("cola_tree");

    public static void bootstrap(BootstrapContext<PlacedFeature> ctx) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = ctx.lookup(Registries.CONFIGURED_FEATURE);

        ctx.register(COLA_TREE_KEY,
                new PlacedFeature(
                        configured.getOrThrow(RFDConfiguredFeatures.COLA),
                        Util.make(new ArrayList<PlacementModifier>(), list -> {
                            // 展开 VegetationPlacements 提供的修饰器
                            list.addAll(VegetationPlacements.treePlacement(
                                    PlacementUtils.countExtra(2, 0.05f, 1),
                                    RFDBlocks.COLA_SAPLING.get()));

                            // 再额外加你需要的检查
                            list.add(BlockPredicateFilter.forPredicate(
                                    BlockPredicate.wouldSurvive(
                                            RFDBlocks.COLA_SAPLING.get().defaultBlockState(),
                                            BlockPos.ZERO)));
                            list.add(HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES));
                        })
                )
        );

    }
    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(RatatouilleFriedDelights.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
