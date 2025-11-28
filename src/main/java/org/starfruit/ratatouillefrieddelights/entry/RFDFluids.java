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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.forsteri.ratatouille.content.NoPlaceBucketItem;
import org.joml.Vector3f;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

import java.util.function.Supplier;

public class RFDFluids {

    public static final FluidEntry<ForgeFlowingFluid.Flowing> COLA_SYRUP =
            RatatouilleFriedDelights.REGISTRATE
                    .standardFluid("cola_syrup")
                    .properties(p -> p
                            .density(1450))
                    .lang("Cola Syrup")
                    .source(ForgeFlowingFluid.Source::new).block().build()
                    .bucket().build().register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> SUNFLOWER_OIL =
            RatatouilleFriedDelights.REGISTRATE
                    .standardFluid("sunflower_oil")
                    .properties(p -> p
                            .density(1450))
                    .lang("Sunflower Oil")
                    .source(ForgeFlowingFluid.Source::new).block().build()
                    .bucket().build().register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MAYONNAISE =
            RatatouilleFriedDelights.REGISTRATE
                    .standardFluid("mayonnaise")
                    .properties(p -> p
                            .density(1050))
                    .lang("Mayonnaise")
                    .source(ForgeFlowingFluid.Source::new).block().build()
                    .bucket().build().register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> TARTAR_SAUCE =
            RatatouilleFriedDelights.REGISTRATE
                    .standardFluid("tartar_sauce")
                    .properties(p -> p
                            .density(1050))
                    .lang("Tartar Sauce")
                    .source(ForgeFlowingFluid.Source::new).block().build()
                    .bucket().build().register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> KETCHUP =
            RatatouilleFriedDelights.REGISTRATE
                    .standardFluid("ketchup")
                    .properties(p -> p
                            .density(1050))
                    .lang("Ketchup")
                    .source(ForgeFlowingFluid.Source::new).block().build()
                    .bucket().build().register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> ICE_CREAM_BASE =
            RatatouilleFriedDelights.REGISTRATE
                    .standardFluid("ice_cream_base")
                    .properties(p -> p
                            .density(1050))
                    .lang("Ice Cream Base")
                    .source(ForgeFlowingFluid.Source::new).block().build()
                    .bucket().build().register();

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
