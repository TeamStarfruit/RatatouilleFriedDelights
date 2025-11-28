package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class FryerGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction dir = state.getValue(HORIZONTAL_FACING);
        return switch (dir) {
            case SOUTH -> 0;
            case WEST  -> 90;
            case NORTH -> 180;
            case EAST  -> 270;
            default -> 0;
        };
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        FryerPart part = state.getValue(ContinuousFryerBlock.PART);
        String path = "block/continuous_fryer/" + part.getSerializedName();
        return prov.models().getExistingFile(prov.modLoc(path));
    }
}
