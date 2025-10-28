package org.starfruit.ratatouillefrieddelights.entry;

import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

public class RFDSpriteShifts {
    public static final SpriteShiftEntry FRYER_BELT = get("block/continuous_fryer/ladder_andesite", "block/continuous_fryer/ladder_andesite_scroll");
    public static final SpriteShiftEntry DIP_CUP_SOURCE = get("block/source");

    private static SpriteShiftEntry get(String location) {
        return get(location, location);
    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(RatatouilleFriedDelights.asResource(originalLocation), RatatouilleFriedDelights.asResource(targetLocation));
    }

    public static void init() {
        var _belt = FRYER_BELT.toString();
        var _dipCup = DIP_CUP_SOURCE.toString();
    }
}
