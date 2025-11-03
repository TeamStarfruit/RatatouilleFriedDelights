package org.starfruit.ratatouillefrieddelights.entry;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

public class RFDPonderPlugin implements PonderPlugin {
    public String getModId() {
        return RatatouilleFriedDelights.MOD_ID;
    }

    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        RFDPonderScenes.register(helper);
    }

}
