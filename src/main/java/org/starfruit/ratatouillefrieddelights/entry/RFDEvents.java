package org.starfruit.ratatouillefrieddelights.entry;

import net.neoforged.fml.common.EventBusSubscriber;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.ContinuousFryerBlockEntity;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.DrumProcessorBlockEntity;

@EventBusSubscriber
public class RFDEvents {
    @net.neoforged.bus.api.SubscribeEvent
    public static void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        DrumProcessorBlockEntity.registerCapabilities(event);
        ContinuousFryerBlockEntity.registerCapabilities(event);
    }
}
