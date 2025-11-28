package org.starfruit.ratatouillefrieddelights.entry;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

@Mod.EventBusSubscriber(modid = RatatouilleFriedDelights.MOD_ID)
public class RFDFuels {
    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.is(RFDItems.FRIED_RESIDUE.get())) {
            event.setBurnTime(200);
        }
    }
}
