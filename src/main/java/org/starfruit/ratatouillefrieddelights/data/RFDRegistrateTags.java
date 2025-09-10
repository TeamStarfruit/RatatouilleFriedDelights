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
                        RFDItems.TOP_BURGER_BUN.get()
                );
    }

    private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {}
    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {}
}
