package org.starfruit.ratatouillefrieddelights.entry;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

@EventBusSubscriber(modid = RatatouilleFriedDelights.MOD_ID)
public class RFDFuels {
    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.is(RFDItems.FRIED_RESIDUE.get())) {
            event.setBurnTime(200);
        }
    }
}
