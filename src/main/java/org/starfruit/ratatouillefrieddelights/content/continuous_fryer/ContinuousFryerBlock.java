package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;

public class ContinuousFryerBlock extends Block implements IWrenchable, IBE<ContinuousFryerBlockEntity> {
    public ContinuousFryerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<ContinuousFryerBlockEntity> getBlockEntityClass() {
        return ContinuousFryerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ContinuousFryerBlockEntity> getBlockEntityType() {
        return RFDBlockEntityTypes.CONTINUOUS_FRYER.get();
    }
}