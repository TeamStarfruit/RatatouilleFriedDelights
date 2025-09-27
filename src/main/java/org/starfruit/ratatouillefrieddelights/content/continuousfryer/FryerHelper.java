package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class FryerHelper {
    public static BlockPos getPositionForOffset(ContinuousFryerBlockEntity controller, int offset) {
        BlockPos pos = controller.getBlockPos();
        Vec3i vec = controller.getFryerFacing()
                .getNormal();
        BeltSlope slope = controller.getBlockState()
                .getValue(BeltBlock.SLOPE);
        int verticality = slope == BeltSlope.DOWNWARD ? -1 : slope == BeltSlope.UPWARD ? 1 : 0;

        return pos.offset(offset * vec.getX(), Mth.clamp(offset, 0, controller.fryerLength - 1) * verticality,
                offset * vec.getZ());
    }

    public static Vec3 getVectorForOffset(ContinuousFryerBlockEntity controller, float offset) {
        BeltSlope slope = controller.getBlockState()
                .getValue(BeltBlock.SLOPE);
        int verticality = slope == BeltSlope.DOWNWARD ? -1 : slope == BeltSlope.UPWARD ? 1 : 0;
        float verticalMovement = verticality;
        if (offset < .5)
            verticalMovement = 0;
        verticalMovement = verticalMovement * (Math.min(offset, controller.fryerLength - .5f) - .5f);
        Vec3 vec = VecHelper.getCenterOf(controller.getBlockPos());
        Vec3 horizontalMovement = Vec3.atLowerCornerOf(controller.getFryerFacing()
                        .getNormal())
                .scale(offset - .5f);

        if (slope == BeltSlope.VERTICAL)
            horizontalMovement = Vec3.ZERO;

        vec = vec.add(horizontalMovement)
                .add(0, verticalMovement, 0);
        return vec;
    }
}
