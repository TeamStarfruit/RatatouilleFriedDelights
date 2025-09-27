package org.starfruit.ratatouillefrieddelights.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.data.recipe.RFDRecipeProvider;
import org.starfruit.ratatouillefrieddelights.data.recipe.RFDStandardRecipeGen;
import org.starfruit.ratatouillefrieddelights.data.recipe.create.RFDSequencedAssemblyRecipeGen;
import org.starfruit.ratatouillefrieddelights.entry.RFDTags;
import org.starfruit.ratatouillefrieddelights.worldgen.*;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class RFDDataGen {
    public RFDDataGen() {
    }

    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(RatatouilleFriedDelights.MOD_ID))
            addExtraRegistrateData();
    }

    private static void addExtraRegistrateData() {
        RFDRegistrateTags.addGenerators();
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("en_us", langConsumer);
            providePonderLang(langConsumer);
        });

//        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, RFDDataGen::genItemTags);
    }

/*    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

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
*/
    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/" + RatatouilleFriedDelights.MOD_ID + "/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
//        PonderIndex.addPlugin(new CRPonderPlugin());
//        PonderIndex.getLangAccess().provideLang(Ratatouille.MOD_ID, consumer);
    }

    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(RatatouilleFriedDelights.MOD_ID))
            return;

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // === 新增：世界生成（Configured/Placed Feature） ===
        RegistrySetBuilder worldgenBuilder = new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, RFDConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE,     RFDPlacedFeatures::bootstrap)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS,     RFDBiomeModifiers::bootstrap);

        generator.addProvider(
                event.includeServer(),
                new DatapackBuiltinEntriesProvider(
                        output, lookupProvider, worldgenBuilder,
                        Set.of(RatatouilleFriedDelights.MOD_ID)
                )
        );

        generator.addProvider(event.includeServer(), new RFDStandardRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new RFDSequencedAssemblyRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new RFDGlobalLootModifierProvider(output, lookupProvider));


        if (event.includeServer()) {
            RFDRecipeProvider.registerAllProcessing(generator, output, lookupProvider);
        }
    }
}
