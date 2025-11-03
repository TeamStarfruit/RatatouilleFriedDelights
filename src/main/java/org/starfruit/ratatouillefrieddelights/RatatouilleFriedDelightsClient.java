package org.starfruit.ratatouillefrieddelights;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerRenderer;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDPonderPlugin;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = RatatouilleFriedDelights.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = RatatouilleFriedDelights.MOD_ID, value = Dist.CLIENT)
public class RatatouilleFriedDelightsClient {
    public RatatouilleFriedDelightsClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
//        RatatouilleFriedDelights.LOGGER.info("HELLO FROM CLIENT SETUP");
//        RatatouilleFriedDelights.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        PonderIndex.addPlugin(new RFDPonderPlugin());
    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(RFDItems.BURGER, BurgerRenderer.DECORATOR);
    }
}
