package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.starfruit.ratatouillefrieddelights.ponder.ConcreteNumberFortyTwoScene;

public class RFDPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addStoryBoard(RFDItems.NO42_CONCRETE_MIXING_PASTA, "concretenumber42", ConcreteNumberFortyTwoScene::concrete, new ResourceLocation[0]);
    }
}
