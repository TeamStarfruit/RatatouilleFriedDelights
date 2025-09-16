package org.starfruit.ratatouillefrieddelights.data;

import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.forsteri.ratatouille.entry.CRTags;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDTags;
import vectorwing.farmersdelight.common.registry.ModItems;

public class RFDRegistrateTags {
    public static void addGenerators() {
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, RFDRegistrateTags::genBlockTags);
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, RFDRegistrateTags::genItemTags);
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, RFDRegistrateTags::genFluidTags);
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, RFDRegistrateTags::genEntityTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {

    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        prov.tag(CRTags.MOLD)
                .add(
                        RFDItems.PANCAKE_MOLD.get(),
                        RFDItems.BURGER_BUN_MOLD.get(),
                        RFDItems.TOP_BURGER_BUN.get(),
                        RFDItems.CONE_MOLD.get()
                );

        prov.tag(RFDTags.AllItemTags.RATATOUILLE_ICE_CREAM.tag)
                .add(
                        RFDItems.ICE_CREAM.get(),
                        RFDItems.VANILLA_CONE.get()
                );

        prov.tag(RFDTags.AllItemTags.BURGER_BASE.tag)
                .addTag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag);

        prov.tag(RFDTags.AllItemTags.BURGER_TOPPINGS.tag) //TODO
                .add(ModItems.BEEF_PATTY.get())
                .add(ModItems.CABBAGE_LEAF.get())
                .addTag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag);

        prov.tag(RFDTags.AllItemTags.RATATOUILLE_TAB_INVISIBLE.tag)
                .addTag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENT_RENDERING_HELPER.tag);

        for (RFDTags.AllItemTags tag : RFDTags.AllItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }

    private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {}
    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {}
}
