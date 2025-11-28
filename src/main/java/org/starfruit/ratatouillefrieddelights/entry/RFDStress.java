package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleSupplier;

public class RFDStress extends ConfigBase {
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_IMPACTS = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_CAPACITIES = new Object2DoubleOpenHashMap<>();
    protected final Map<ResourceLocation, ConfigValue<Double>> capacities = new HashMap<>();
    protected final Map<ResourceLocation, ConfigValue<Double>> impacts = new HashMap<>();

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
        return setImpact((double) 0.0F);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double value) {
        return (builder) -> {
            assertFromMod(builder);
            ResourceLocation id = RatatouilleFriedDelights.asResource(builder.getName());
            DEFAULT_IMPACTS.put(id, value);
            return builder;
        };
    }

    private static void assertFromMod(BlockBuilder<?, ?> builder) {
        if (!builder.getOwner().getModid().equals(RatatouilleFriedDelights.MOD_ID)) {
            throw new IllegalStateException("BlockBuilder must be from Ratatouille: Fried Delights mod, but was from " + builder.getOwner().getModid());
        }
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double value) {
        return (builder) -> {
            assertFromMod(builder);
            ResourceLocation id = RatatouilleFriedDelights.asResource(builder.getName());
            DEFAULT_CAPACITIES.put(id, value);
            return builder;
        };
    }

    public void registerAll(Builder builder) {
        builder.comment(new String[]{".", RFDStress.Comments.su, RFDStress.Comments.impact}).push("impact");
        DEFAULT_IMPACTS.forEach((id, value) -> this.impacts.put(id, builder.define(id.getPath(), value)));
        builder.pop();
        builder.comment(new String[]{".", RFDStress.Comments.su, RFDStress.Comments.capacity}).push("capacity");
        DEFAULT_CAPACITIES.forEach((id, value) -> this.capacities.put(id, builder.define(id.getPath(), value)));
        builder.pop();
    }

    public @NotNull String getName() {
        return "stressValues.ratatouille";
    }

    public @Nullable DoubleSupplier getImpact(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        ConfigValue<Double> value = this.impacts.get(id);
        DoubleSupplier var10000;
        if (value == null) {
            var10000 = null;
        } else {
            Objects.requireNonNull(value);
            var10000 = value::get;
        }

        return var10000;
    }

    public @Nullable DoubleSupplier getCapacity(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        ConfigValue<Double> value = this.capacities.get(id);
        DoubleSupplier var10000;
        if (value == null) {
            var10000 = null;
        } else {
            Objects.requireNonNull(value);
            var10000 = value::get;
        }

        return var10000;
    }

    private static class Comments {
        static String su = "[in Stress Units]";
        static String impact = "Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives.";
        static String capacity = "Configure how much stress a source can accommodate for.";
    }
}
