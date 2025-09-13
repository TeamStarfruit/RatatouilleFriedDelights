package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import org.forsteri.ratatouille.content.thresher.ThresherVisual;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.*;
import org.starfruit.ratatouillefrieddelights.content.drum_processor.*;

public class RFDBlockEntityTypes {

    public static final BlockEntityEntry<ContinuousFryerBlockEntity> CONTINUOUS_FRYER = RatatouilleFriedDelights.REGISTRATE
            .blockEntity("continuous_fryer", ContinuousFryerBlockEntity::new)
            .visual(() -> ContinuousFryerVisual::new)
            .validBlocks(RFDBlocks.CONTINUOUS_FRYER)
            .renderer(() -> ContinuousFryerRenderer::new)
            .register();

    public static final BlockEntityEntry<DrumProcessorBlockEntity> DRUM_PROCESSOR = RatatouilleFriedDelights.REGISTRATE
            .blockEntity("drum_processor", DrumProcessorBlockEntity::new)
            .visual(() -> DrumProcessorVisual::new)
            .validBlocks(RFDBlocks.DRUM_PROCESSOR)
            .renderer(() -> DrumProcessorRenderer::new)
            .register();

    public static void register() {
    }
}
