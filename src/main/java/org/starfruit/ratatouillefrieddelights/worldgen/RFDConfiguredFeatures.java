package org.starfruit.ratatouillefrieddelights.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;
import org.starfruit.ratatouillefrieddelights.worldgen.tree.deco.ColaFruitDecorator;

public class RFDConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> COLA_TREE_KEY = registerKey("cola_tree");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        var config = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(RFDBlocks.COLA_LOG.get()),
                new ForkingTrunkPlacer(2, 3, 2),

                BlockStateProvider.simple(RFDBlocks.COLA_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 3),

                new TwoLayersFeatureSize(1, 0, 2)
        )
                .ignoreVines()
                // ★ 在这里启用果实装饰器
                .decorators(java.util.List.of(new ColaFruitDecorator(0.1F, 3)))
                .build();

        register(context, COLA_TREE_KEY, Feature.TREE, config);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(RatatouilleFriedDelights.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
