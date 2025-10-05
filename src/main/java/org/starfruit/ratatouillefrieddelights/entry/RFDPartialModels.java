package org.starfruit.ratatouillefrieddelights.entry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

public class RFDPartialModels {
    public static final PartialModel DRUM = block("block/drum_processor/partial");
    public static final PartialModel FRYER_AXIS = block("block/continuous_fryer/axis");
    public static final PartialModel FRYER_BELT = block("block/continuous_fryer/belt");

    public RFDPartialModels(){}

    private static PartialModel block(String path) {
        return PartialModel.of(RatatouilleFriedDelights.asResource(path));
    }

    public static void init() {}
}

