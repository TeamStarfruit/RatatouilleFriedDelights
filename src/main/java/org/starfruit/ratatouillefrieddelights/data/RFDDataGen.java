package org.starfruit.ratatouillefrieddelights.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDTags;

import java.util.Map;
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
//        RatatouilleRegistrateTags.addGenerators();
        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("en_us", langConsumer);
            providePonderLang(langConsumer);
        });

        RatatouilleFriedDelights.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, RFDDataGen::genItemTags);
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        prov.tag(RFDTags.AllItemTags.BURGER_BASE.tag)
                .addTag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag);

        prov.tag(RFDTags.AllItemTags.BURGER_TOPPINGS.tag)
                .addTag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag);

        for (RFDTags.AllItemTags tag : RFDTags.AllItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }

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

//        generator.addProvider(event.includeServer(), new RatatouilleStandardRecipeGen(output, lookupProvider));
//        generator.addProvider(event.includeServer(), new RatatouilleSequencedAssemblyRecipeGen(output, lookupProvider));


        if (event.includeServer()) {
//            RatatouilleRecipeProvider.registerAllProcessing(generator, output, lookupProvider);
        }
    }
}
