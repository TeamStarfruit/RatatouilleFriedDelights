package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.util.entry.ItemEntry;
import org.forsteri.ratatouille.Ratatouille;
import org.forsteri.ratatouille.entry.CRCreativeModeTabs;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import net.minecraft.world.item.Item;

public class RFDItems {
    static {
        Ratatouille.REGISTRATE.setCreativeTab(RFDCreativeModeTabs.BASE_CREATIVE_TAB);
    }
    public static final ItemEntry<Item> FRENCH_FRIES = RatatouilleFriedDelights.REGISTRATE.item("french_fries", Item::new).register();

    public static void register() {}
}
