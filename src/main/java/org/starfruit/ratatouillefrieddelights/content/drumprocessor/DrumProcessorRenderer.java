package org.starfruit.ratatouillefrieddelights.content.drumprocessor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

public class DrumProcessorRenderer extends KineticBlockEntityRenderer<DrumProcessorBlockEntity> {

    public DrumProcessorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(DrumProcessorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        renderItems(be, partialTicks, ms, buffer, light, overlay);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(DrumProcessorBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacingVertical(RFDPartialModels.DRUM,state,state.getValue(DrumProcessorBlock.HORIZONTAL_FACING));
    }

    protected void renderItems(DrumProcessorBlockEntity be, float partialTicks,
                               PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.getLevel() == null) return;

        var item1 = be.inputInv.getStackInSlot(0);
        var item2 = be.inputInv.getStackInSlot(1);
        var itemRenderer = Minecraft.getInstance().getItemRenderer();

        // 当前旋转角度，跟随速度
        float angleRad = getAngleForBe(be, be.getBlockPos(), getRotationAxisOf(be));
        float angleDeg = angleRad * (180f / (float)Math.PI);
        float scale = 0.4f;
        float xOffset = 0f;
        float zOffset = 0f;

        Direction dir = be.getEjectDirection();
        switch (dir) {
            case NORTH -> zOffset = 7f/16f - 0.5f;
            case SOUTH -> zOffset = -7f/16f + 0.5f;
            case WEST  -> xOffset = 7f/16f - 0.5f;
            case EAST  -> xOffset = -7f/16f + 0.5f;
        }
        com.mojang.math.Axis axis;
        switch (dir.getAxis()) {
            case X -> axis = Axis.XP;
            case Z -> axis = Axis.ZP;
            default -> axis = Axis.YP;
        }

        if (!item1.isEmpty()) {
            ms.pushPose();
            ms.translate(0.5, 0.5, 0.5);
            ms.mulPose(axis.rotationDegrees(angleDeg));
            ms.translate(xOffset, 0.2, zOffset);
            ms.scale(scale, scale, scale);

            if (!(item1.getItem() instanceof net.minecraft.world.item.BlockItem)) {
                ms.mulPose(Axis.XP.rotationDegrees(90));
            }

            itemRenderer.renderStatic(item1, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.getLevel(), 0);
            ms.popPose();
        }

        if (!item2.isEmpty()) {
            ms.pushPose();
            ms.translate(0.5, 0.5, 0.5);
            ms.mulPose(axis.rotationDegrees(angleDeg));
            ms.translate(xOffset, -0.2, zOffset);
            ms.scale(scale, scale, scale);

            if (!(item2.getItem() instanceof net.minecraft.world.item.BlockItem)) {
                ms.mulPose(Axis.XP.rotationDegrees(90));
            }

            itemRenderer.renderStatic(item2, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.getLevel(), 0);
            ms.popPose();
        }
    }


}
