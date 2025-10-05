package org.starfruit.ratatouillefrieddelights.entry;

import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

public class RFDSpriteShifts {
    public static final SpriteShiftEntry FRYER_BEL = get("block/ladder_andesite", "block/ladder_andesite_scroll");

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(RatatouilleFriedDelights.asResource(originalLocation), RatatouilleFriedDelights.asResource(targetLocation));
    }
}
