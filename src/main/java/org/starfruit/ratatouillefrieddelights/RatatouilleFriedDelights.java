package org.starfruit.ratatouillefrieddelights;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.starfruit.ratatouillefrieddelights.config.RFDConfigs;
import org.starfruit.ratatouillefrieddelights.data.RFDDataGen;
import org.starfruit.ratatouillefrieddelights.data.loot.RFDLootModifiers;
import org.starfruit.ratatouillefrieddelights.entry.*;
import org.starfruit.ratatouillefrieddelights.worldgen.tree.deco.RFDTreeDecoratorTypes;

// The value here should match an entry in the META-INF/mods.toml file
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

    public RatatouilleFriedDelights() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);
        modEventBus.addListener(this::commonSetup);
        RFDCreativeModeTabs.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(RatatouilleFriedDelights::clientInit);

        RFDItems.register();
        RFDFluids.register();
        RFDRecipeTypes.register(modEventBus);
        RFDBlocks.register();
        RFDBlockEntityTypes.register();

        RFDSpriteShifts.init();

//        RFDDataComponents.register(modEventBus);
        RFDLootModifiers.register(modEventBus);

        RFDConfigs.register(modLoadingContext);

//        modContainer.addConfig(ModConfig.Type.COMMON, Config.SPEC);

        RFDTreeDecoratorTypes.register(modEventBus);

        modEventBus.addListener(EventPriority.LOWEST, RFDDataGen::gatherData);
        modEventBus.addListener(EventPriority.HIGH, RFDDataGen::gatherDataHighPriority);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
//        if (Config.LOG_DIRT_BLOCK.get()) {
//            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
//        }

//        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.get());

//        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
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
        return new ResourceLocation(MOD_ID, path);
    }
}
