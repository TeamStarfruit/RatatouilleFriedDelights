package org.starfruit.ratatouillefrieddelights;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerRenderer;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDPonderPlugin;

@Mod.EventBusSubscriber(modid = RatatouilleFriedDelights.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RatatouilleFriedDelightsClient {
    private RatatouilleFriedDelightsClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new RFDPonderPlugin());
    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(RFDItems.BURGER.get(), BurgerRenderer.DECORATOR);
    }
}
