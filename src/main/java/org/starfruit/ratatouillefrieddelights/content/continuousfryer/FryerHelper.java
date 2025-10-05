package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class FryerHelper {
    public static BlockPos getPositionForOffset(ContinuousFryerBlockEntity controller, int offset) {
        BlockPos pos = controller.getBlockPos();
        Vec3i vec = controller.getFryerFacing()
                .getNormal();
        int verticality = 0;

        return pos.offset(offset * vec.getX(), Mth.clamp(offset, 0, controller.fryerLength - 1) * verticality,
                offset * vec.getZ());
    }

    public static Vec3 getVectorForOffset(ContinuousFryerBlockEntity controller, float offset) {
        Direction facing = controller.getFryerFacing();

        Vec3 origin = VecHelper.getCenterOf(controller.getBlockPos());

        Vec3 horizontalMovement = Vec3.atLowerCornerOf(facing.getNormal())
                .scale(offset - 0.5f);
        return origin.add(horizontalMovement);
    }


    public static ContinuousFryerBlockEntity getSegmentBE(LevelAccessor world, BlockPos pos) {
        if (world instanceof Level l && !l.isLoaded(pos))
            return null;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ContinuousFryerBlockEntity))
            return null;
        return (ContinuousFryerBlockEntity) blockEntity;
    }

    public static ContinuousFryerBlockEntity getControllerBE(LevelAccessor world, BlockPos pos) {
        ContinuousFryerBlockEntity segment = getSegmentBE(world, pos);
        if (segment == null)
            return null;
        BlockPos controllerPos = segment.controller;
        if (controllerPos == null)
            return null;
        return getSegmentBE(world, controllerPos);
    }
}
