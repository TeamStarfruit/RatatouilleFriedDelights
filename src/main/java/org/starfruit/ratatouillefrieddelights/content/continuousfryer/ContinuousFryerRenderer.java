package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.foundation.render.ShadowRenderHelper;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.levelWrappers.WrappedLevel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;
import org.starfruit.ratatouillefrieddelights.entry.RFDSpriteShifts;

import java.util.Random;

public class ContinuousFryerRenderer extends KineticBlockEntityRenderer<ContinuousFryerBlockEntity> {

    public ContinuousFryerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRenderOffScreen(ContinuousFryerBlockEntity be) {
        return be.isController();
    }


    @Override
    protected SuperByteBuffer getRotatedModel(ContinuousFryerBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacingVertical(RFDPartialModels.FRYER_AXIS, state, state.getValue(ContinuousFryerBlock.HORIZONTAL_FACING));
    }

    @Override
    protected void renderSafe(ContinuousFryerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                              int overlay) {

        if (!VisualizationManager.supportsVisualization(be.getLevel())) {

            BlockState blockState = be.getBlockState();

            Direction facing = blockState.getValue(ContinuousFryerBlock.HORIZONTAL_FACING);
            Direction.AxisDirection axisDirection = facing.getAxisDirection();

            boolean sideways = false;
            boolean alongX = facing.getAxis() == Direction.Axis.X;

            PoseStack localTransforms = new PoseStack();
            var msr = TransformStack.of(localTransforms);
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            float renderTick = AnimationTickHolder.getRenderTime(be.getLevel());

            msr.center()
                    .rotateYDegrees(AngleHelper.horizontalAngle(facing) + 0 + 0)
                    .rotateZDegrees(0)
                    .rotateXDegrees(0)
                    .uncenter();

            for (boolean bottom : Iterate.trueAndFalse) {

                PartialModel beltPartial = RFDPartialModels.FRYER_BELT;

                SuperByteBuffer beltBuffer = CachedBuffers.partial(beltPartial, blockState)
                        .light(light);

                SpriteShiftEntry spriteShift = RFDSpriteShifts.FRYER_BELT;

                // UV shift
                float speed = be.getSpeed();
                if (speed != 0) {
                    float time = renderTick * axisDirection.getStep();
                    if (!sideways && alongX)
                        speed = -speed;

                    float scrollMult = 0.5f;

                    float spriteSize = spriteShift.getTarget()
                            .getV1()
                            - spriteShift.getTarget()
                            .getV0();

                    double scroll = speed * time / (31.5 * 16) + (bottom ? 0.5 : 0.0);
                    scroll = scroll - Math.floor(scroll);
                    scroll = scroll * spriteSize * scrollMult;

                    beltBuffer.shiftUVScrolling(spriteShift, (float) scroll);
                }

                beltBuffer
                        .transform(localTransforms)
                        .renderInto(ms, vb);

            }

        }

        renderItems(be, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderItems(ContinuousFryerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                               int light, int overlay) {
        if (!be.isController())
            return;
        if (be.fryerLength == 0)
            return;

        ms.pushPose();

        Direction beltFacing = be.getFryerFacing();
        Vec3i directionVec = beltFacing.getNormal();
        Vec3 beltStartOffset = Vec3.atLowerCornerOf(directionVec)
                .scale(-.5)
                .add(.5, 13 / 16f, .5);
        ms.translate(beltStartOffset.x, beltStartOffset.y, beltStartOffset.z);

        int verticality = 0;
        boolean slopeAlongX = beltFacing.getAxis() == Direction.Axis.X;
        boolean onContraption = be.getLevel() instanceof WrappedLevel;

        FryerInventory inventory = be.getItemInventory();
        for (var transported : inventory.getTransportedItems())
            renderItem(be, partialTicks, ms, buffer, light, overlay, beltFacing, directionVec, verticality,
                    slopeAlongX, onContraption, transported, beltStartOffset);
        if (inventory.getLazyClientItem() != null)
            renderItem(be, partialTicks, ms, buffer, light, overlay, beltFacing, directionVec, verticality,
                    slopeAlongX, onContraption, inventory.getLazyClientItem(), beltStartOffset);

        ms.popPose();
    }

    private void renderItem(ContinuousFryerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                            int overlay, Direction beltFacing, Vec3i directionVec, int verticality, boolean slopeAlongX,
                            boolean onContraption, FryingItemStack transported, Vec3 beltStartOffset) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        float offset = Mth.lerp(partialTicks, transported.prevFryerPosition, transported.fryerPosition);
        float sideOffset = Mth.lerp(partialTicks, transported.prevSideOffset, transported.sideOffset);
        float verticalMovement = verticality;

        if (be.getSpeed() == 0) {
            offset = transported.fryerPosition;
            sideOffset = transported.sideOffset;
        }

        if (offset < .5)
            verticalMovement = 0;
        else
            verticalMovement = verticality * (Math.min(offset, be.fryerLength - .5f) - .5f);
        Vec3 offsetVec = Vec3.atLowerCornerOf(directionVec)
                .scale(offset);
        if (verticalMovement != 0)
            offsetVec = offsetVec.add(0, verticalMovement, 0);
        float slopeAngle = 0;
        Vec3 itemPos = beltStartOffset.add(
                        be.getBlockPos().getX(),
                        be.getBlockPos().getY(),
                        be.getBlockPos().getZ())
                .add(offsetVec);

        if (this.shouldCullItem(itemPos, be.getLevel())) {
            return;
        }

        ms.pushPose();
        TransformStack.of(ms).nudge(transported.angle);
        ms.translate(offsetVec.x, offsetVec.y, offsetVec.z);

        boolean alongX = beltFacing.getClockWise()
                .getAxis() == Direction.Axis.X;
        if (!alongX)
            sideOffset *= -1;
        ms.translate(alongX ? sideOffset : 0, 0, alongX ? 0 : sideOffset);

        int stackLight;
        if (onContraption) {
            stackLight = light;
        } else {
            int segment = (int) Math.floor(offset);
            mutablePos.set(be.getBlockPos()).move(directionVec.getX() * segment, verticality * segment, directionVec.getZ() * segment);
            stackLight = LevelRenderer.getLightColor(be.getLevel(), mutablePos);
        }

        boolean renderUpright = false;
        BakedModel bakedModel = itemRenderer.getModel(transported.stack, be.getLevel(), null, 0);
        boolean blockItem = bakedModel.isGui3d();

        int count = 0;
        if (be.getLevel() instanceof PonderLevel || mc.player.getEyePosition(1.0F).distanceTo(itemPos) < 16)
            count = (int) (Mth.log2((int) (transported.stack.getCount()))) / 2;

        Random r = new Random(transported.angle);

        ms.mulPose((slopeAlongX ? Axis.ZP : Axis.XP).rotationDegrees(slopeAngle));
        ms.pushPose();
        ms.translate(0, -1 / 8f + 0.005f, 0);
        ShadowRenderHelper.renderShadow(ms, buffer, .75f, .2f);
        ms.popPose();

        for (int i = 0; i <= count; i++) {
            ms.pushPose();

            boolean box = PackageItem.isPackage(transported.stack);
            ms.mulPose(Axis.YP.rotationDegrees(transported.angle));
            if (!blockItem && !renderUpright) {
                ms.translate(0, -.09375, 0);
                ms.mulPose(Axis.XP.rotationDegrees(90));
            }

            if (blockItem && !box)
                ms.translate(r.nextFloat() * .0625f * i, 0, r.nextFloat() * .0625f * i);

            if (box) {
                ms.translate(0, 4 / 16f, 0);
                ms.scale(1.5f, 1.5f, 1.5f);
            } else {
                ms.scale(.5f, .5f, .5f);
            }

            itemRenderer.render(transported.stack, ItemDisplayContext.FIXED, false, ms, buffer, stackLight, overlay, bakedModel);
            ms.popPose();

            if (!renderUpright) {
                if (!blockItem)
                    ms.mulPose(Axis.YP.rotationDegrees(10));
                ms.translate(0, blockItem ? 1 / 64d : 1 / 16d, 0);
            } else
                ms.translate(0, 0, -1 / 16f);

        }

        ms.popPose();
    }
}
