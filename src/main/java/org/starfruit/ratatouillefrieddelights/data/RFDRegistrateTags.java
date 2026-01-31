package org.starfruit.ratatouillefrieddelights.data;

import com.pyzpre.createbitterballen.index.FluidRegistry;
import com.pyzpre.createbitterballen.item.FryingOilBottleItem;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.forsteri.ratatouille.entry.CRTags;
import org.starfruit.ratatouillefrieddelights.entry.*;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Objects;

public class RFDRegistrateTags {
    public static void addGenerators() {
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, RFDRegistrateTags::genBlockTags);
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, RFDRegistrateTags::genItemTags);
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, RFDRegistrateTags::genFluidTags);
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, RFDRegistrateTags::genEntityTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {

    }

    @SuppressWarnings("deprecated")
    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        prov.tag(CRTags.MOLD)
                .add(
                        RFDItems.PANCAKE_MOLD.get(),
                        RFDItems.BURGER_BUN_MOLD.get(),
                        RFDItems.TOP_BURGER_BUN.get(),
                        RFDItems.CONE_MOLD.get()
                );

        prov.tag(RFDTags.RFDItemTags.RATATOUILLE_ICE_CREAM.tag)
                .add(
                        RFDItems.ICE_CREAM.get(),
                        RFDItems.VANILLA_CONE.get(),
                        RFDItems.ICE_CUBES.get(),
                        RFDItems.CHOCOLATE_SUNDAE.get()
                );

        prov.tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
                .add(
                        RFDItems.ICE_CREAM.get(),
                        RFDItems.VANILLA_CONE.get(),
                        RFDItems.CHOCOLATE_SUNDAE.get(),
                        RFDItems.COLA.get(),
                        RFDBlocks.BOXED_CHICKEN_NUGGETS.asItem(),
                        RFDBlocks.BOXED_FRIES.asItem(),
                        RFDBlocks.DUO_CHICKEN_BUCKET.asItem()
                );

        prov.tag(RFDTags.RFDItemTags.BURGER_BASE.tag)
                .addTag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag);

        prov.tag(RFDTags.RFDItemTags.BURGER_TOPPINGS.tag) //TODO
                .add(ModItems.BEEF_PATTY.get())
                .add(ModItems.CABBAGE_LEAF.get())
                .addTag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag);

        prov.tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag)
                .add(ModItems.BEEF_PATTY.get())
                .add(ModItems.CABBAGE_LEAF.get());

        prov.tag(RFDTags.RFDItemTags.RATATOUILLE_TAB_INVISIBLE.tag)
                .addTag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENT_RENDERING_HELPER.tag);

        prov.tag(RFDTags.RFDItemTags.CONCRETES.tag)
                .add(
                        Blocks.WHITE_CONCRETE.asItem(),
                        Blocks.ORANGE_CONCRETE.asItem(),
                        Blocks.MAGENTA_CONCRETE.asItem(),
                        Blocks.LIGHT_BLUE_CONCRETE.asItem(),
                        Blocks.YELLOW_CONCRETE.asItem(),
                        Blocks.LIME_CONCRETE.asItem(),
                        Blocks.PINK_CONCRETE.asItem(),
                        Blocks.GRAY_CONCRETE.asItem(),
                        Blocks.LIGHT_GRAY_CONCRETE.asItem(),
                        Blocks.CYAN_CONCRETE.asItem(),
                        Blocks.PURPLE_CONCRETE.asItem(),
                        Blocks.BLUE_CONCRETE.asItem(),
                        Blocks.BROWN_CONCRETE.asItem(),
                        Blocks.GREEN_CONCRETE.asItem(),
                        Blocks.RED_CONCRETE.asItem(),
                        Blocks.BLACK_CONCRETE.asItem()
                );

        for (RFDTags.RFDItemTags tag : RFDTags.RFDItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }

    @SuppressWarnings("deprecated")
    private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {
        TagGen.CreateTagsProvider<Fluid> prov = new TagGen.CreateTagsProvider<>(provIn, Fluid::builtInRegistryHolder);

        prov.tag(RFDTags.RFDFluidTags.OIL.tag)
                .add(RFDFluids.SUNFLOWER_OIL.get().getFlowing(), RFDFluids.SUNFLOWER_OIL.get().getSource())
                .addOptional(FluidRegistry.FRYING_OIL.getId())
                .addOptional(Objects.requireNonNull(ForgeRegistries.FLUID_TYPES.get().getKey(FluidRegistry.FRYING_OIL.get().getFlowing().getFluidType())))
                .addOptional(Objects.requireNonNull(ForgeRegistries.FLUID_TYPES.get().getKey(FluidRegistry.FRYING_OIL.get().getSource().getFluidType())));
    }
    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {}
}
