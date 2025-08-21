package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.util.entry.ItemEntry;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import net.minecraft.world.item.Item;

public class RFDItems {
    public static final ItemEntry<Item> FRENCH_FRIES = RatatouilleFriedDelights.REGISTRATE.item("french_fries", Item::new).register();

    public static void register() {}
}
