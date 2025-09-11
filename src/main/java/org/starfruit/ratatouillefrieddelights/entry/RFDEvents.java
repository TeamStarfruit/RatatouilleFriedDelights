package org.starfruit.ratatouillefrieddelights.entry;

import net.neoforged.fml.common.EventBusSubscriber;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.ContinuousFryerBlockEntity;
import org.starfruit.ratatouillefrieddelights.content.drum_processor.DrumProcessorBlockEntity;

@EventBusSubscriber
public class RFDEvents {
    @net.neoforged.bus.api.SubscribeEvent
    public static void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        DrumProcessorBlockEntity.registerCapabilities(event);
        ContinuousFryerBlockEntity.registerCapabilities(event);
    }
}
