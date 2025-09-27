package org.starfruit.ratatouillefrieddelights;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.forsteri.ratatouille.entry.CRPartialModels;
import org.forsteri.ratatouille.entry.CRPonderPlugin;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.starfruit.ratatouillefrieddelights.data.RFDDataGen;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.starfruit.ratatouillefrieddelights.data.loot.RFDLootModifiers;
import org.starfruit.ratatouillefrieddelights.entry.*;
import org.starfruit.ratatouillefrieddelights.worldgen.tree.deco.RFDTreeDecoratorTypes;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RatatouilleFriedDelights.MOD_ID)
public class RatatouilleFriedDelights {
    public static final String MOD_ID = "ratatouille_fried_delights";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(RatatouilleFriedDelights.MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public RatatouilleFriedDelights(IEventBus modEventBus, ModContainer modContainer) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(this::commonSetup);
        RFDCreativeModeTabs.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(RatatouilleFriedDelights::clientInit);

        RFDItems.register();
        RFDFluids.register();
        RFDDataComponents.register(modEventBus);
        RFDRecipeTypes.register(modEventBus);
        RFDBlocks.register();
        RFDBlockEntityTypes.register();
        RFDLootModifiers.register(modEventBus);

        RFDConfigs.register(modLoadingContext, modContainer);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(EventPriority.HIGHEST, RFDDataGen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, RFDDataGen::gatherData);

        RFDTreeDecoratorTypes.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        RFDPartialModels.init();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
    }

    public static LangBuilder lang(){return new LangBuilder(MOD_ID);}

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
