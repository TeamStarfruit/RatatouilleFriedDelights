package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.ContinuousFryerBlockEntity;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.ContinuousFryerRenderer;

public class RFDBlockEntityTypes {

    public static final BlockEntityEntry<ContinuousFryerBlockEntity> CONTINUOUS_FRYER = RatatouilleFriedDelights.REGISTRATE
            .<ContinuousFryerBlockEntity>blockEntity(
                    "continuous_fryer",
                    ContinuousFryerBlockEntity::new)
            .validBlocks(RFDBlocks.CONTINUOUS_FRYER)
            .renderer(() -> ContinuousFryerRenderer::new)
            .register();

    public static void register() {
    }
}
