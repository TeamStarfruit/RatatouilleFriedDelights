package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

import java.util.function.Supplier;

public class RFDFluids {

    public static final FluidEntry<VirtualFluid> COLA_SYRUP =
            RatatouilleFriedDelights.REGISTRATE
                    .virtualFluid("cola_syrup")
                    .lang("Cola Syrup")
                    .register();

    public static final FluidEntry<VirtualFluid> SUNFLOWER_OIL =
            RatatouilleFriedDelights.REGISTRATE
                    .virtualFluid("sunflower_oil")
                    .lang("Sunflower Oil")
                    .register();

    public static final FluidEntry<VirtualFluid> MAYONNAISE =
            RatatouilleFriedDelights.REGISTRATE
                    .virtualFluid("mayonnaise")
                    .lang("Mayonnaise")
                    .register();

    public static final FluidEntry<VirtualFluid> TARTAR_SAUCE =
            RatatouilleFriedDelights.REGISTRATE
                    .virtualFluid("tartar_sauce")
                    .lang("Tartar Sauce")
                    .register();

    public static final FluidEntry<VirtualFluid> PANCAKE_BATTER =
            RatatouilleFriedDelights.REGISTRATE
                    .virtualFluid("pancake_batter")
                    .lang("Pancake Batter")
                    .register();

    public static final FluidEntry<VirtualFluid> ICE_CREAM_BASE =
            RatatouilleFriedDelights.REGISTRATE
                    .virtualFluid("ice_cream_base")
                    .lang("Ice Cream BASE")
                    .register();

    static {
        RatatouilleFriedDelights.REGISTRATE.setCreativeTab(RFDCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static void register() {
    }

    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                RFDFluids.SolidRenderedPlaceableFluidType fluidType = new RFDFluids.SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }
}
