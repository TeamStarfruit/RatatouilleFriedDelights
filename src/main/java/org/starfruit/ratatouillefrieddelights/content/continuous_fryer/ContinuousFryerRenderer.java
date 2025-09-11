package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;

import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.platform.NeoForgeCatnipServices;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.fluids.FluidStack;

/**
 * 连续油炸器渲染器（雏形）
 * - 渲染内部油槽液面（be.oilTank）
 * - 渲染传送/处理中物品
 * - 处理中时渲染“油面扰动”盒
 */
public class ContinuousFryerRenderer extends SmartBlockEntityRenderer<ContinuousFryerBlockEntity> {

    public ContinuousFryerRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    protected void renderSafe(ContinuousFryerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        renderOil(be, partialTicks, ms, buffer, light);
        renderItem(be, partialTicks, ms, buffer, light, overlay);
    }

    /* ----------------------- 物品渲染：与 Drain 基本一致 ----------------------- */
    protected void renderItem(ContinuousFryerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        TransportedItemStack transported = be.heldItem;
        if (transported == null)
            return;

        var msr = TransformStack.of(ms);
        Vec3 itemPosition = VecHelper.getCenterOf(be.getBlockPos());
        Direction insertedFrom = transported.insertedFrom;
        if (!insertedFrom.getAxis().isHorizontal())
            return;

        ms.pushPose();
        ms.translate(.5f, 15 / 16f, .5f);
        msr.nudge(0);

        float offset = Mth.lerp(partialTicks, transported.prevBeltPosition, transported.beltPosition);
        float sideOffset = Mth.lerp(partialTicks, transported.prevSideOffset, transported.sideOffset);

        Vec3 offsetVec = Vec3.atLowerCornerOf(insertedFrom.getOpposite().getNormal()).scale(.5f - offset);
        ms.translate(offsetVec.x, offsetVec.y, offsetVec.z);

        boolean alongX = insertedFrom.getClockWise().getAxis() == Direction.Axis.X;
        if (!alongX) sideOffset *= -1;
        ms.translate(alongX ? sideOffset : 0, 0, alongX ? 0 : sideOffset);

        ItemStack stack = transported.stack;
        Random r = new Random(0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int count = (int) (Mth.log2((int) (stack.getCount()))) / 2;
        boolean renderUpright = BeltHelper.isItemUpright(stack);
        BakedModel baked = itemRenderer.getModel(stack, null, null, 0);
        boolean blockItem = baked.isGui3d();

        if (renderUpright)
            ms.translate(0, 3 / 32d, 0);

        int positive = insertedFrom.getAxisDirection().getStep();
        float verticalAngle = positive * offset * 360;
        if (insertedFrom.getAxis() != Direction.Axis.X)
            msr.rotateXDegrees(verticalAngle);
        if (insertedFrom.getAxis() != Direction.Axis.Z)
            msr.rotateZDegrees(-verticalAngle);

        if (renderUpright) {
            Entity camera = Minecraft.getInstance().cameraEntity;
            if (camera != null) {
                Vec3 camPos = camera.position();
                Vec3 vectorForOffset = itemPosition.add(offsetVec);
                Vec3 diff = vectorForOffset.subtract(camPos);

                if (insertedFrom.getAxis() != Direction.Axis.X)
                    diff = VecHelper.rotate(diff, verticalAngle, Direction.Axis.X);
                if (insertedFrom.getAxis() != Direction.Axis.Z)
                    diff = VecHelper.rotate(diff, -verticalAngle, Direction.Axis.Z);

                float yRot = (float) Mth.atan2(diff.z, -diff.x);
                ms.mulPose(Axis.YP.rotation((float) (yRot - Math.PI / 2)));
            }
            ms.translate(0, 0, -1 / 16f);
        }

        for (int i = 0; i <= count; i++) {
            ms.pushPose();
            if (blockItem)
                ms.translate(r.nextFloat() * .0625f * i, 0, r.nextFloat() * .0625f * i);
            ms.scale(.5f, .5f, .5f);
            if (!blockItem && !renderUpright)
                msr.rotateXDegrees(90);
            itemRenderer.render(stack, ItemDisplayContext.FIXED, false, ms, buffer, light, overlay, baked);
            ms.popPose();

            if (!renderUpright) {
                if (!blockItem) msr.rotateYDegrees(10);
                ms.translate(0, blockItem ? 1 / 64d : 1 / 16d, 0);
            } else {
                ms.translate(0, 0, -1 / 16f);
            }
        }

        ms.popPose();
    }

    /* ----------------------- 油液渲染：槽液面 + 处理中扰动 ----------------------- */
    protected void renderOil(ContinuousFryerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        SmartFluidTankBehaviour tank = be.internalTank;
        if (tank == null)
            return;

        TankSegment seg = tank.getPrimaryTank();
        FluidStack fluid = seg.getRenderedFluid();
        float level = seg.getFluidLevel().getValue(partialTicks);

        // 1) 静态液面（随油量升降）
        if (!fluid.isEmpty() && level > 0f) {
            float yMin = 5f / 16f;     // 液面基准下界
            float min  = 2f / 16f;
            float max  = min + (12f / 16f);
            float yOffset = (7f / 16f) * level;

            ms.pushPose();
            ms.translate(0, yOffset, 0);
            NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(
                    fluid,              // 流体
                    min, yMin - yOffset, min,
                    max, yMin,          max,
                    buffer, ms, light,
                    false,  // 不切角（与 Drain 一致）
                    false
            );
            ms.popPose();
        }

        // 2) 处理中扰动：以油体本身做一个小盒子的“翻滚/气泡”渲染
        if (be.heldItem == null || be.processingTicks <= 0)
            return;

        // 用当前油体来做扰动渲染；如果没有油体则直接跳过（没有油就不应该在炸）
        if (fluid.isEmpty())
            return;

        int processingTicks = be.processingTicks;
        float processPT = be.processingTicks - partialTicks;

        // 让 15→5 tick 区间作为扰动“峰值”，0~1 的进度 [0,1]
        float progress = 1 - (processPT - 5f) / 10f;
        progress = Mth.clamp(progress, 0, 1);

        // 构造一个随进度“鼓起/收缩”的半径（与 Drain 的思路相近）
        float radius = (float) (Math.pow(((2 * progress) - 1), 2) - 1); // [-1,0] 之间
        // 拿一个以锅中心为轴的小盒子，inflate 一点
        AABB bb = new AABB(0.5, 1.0, 0.5, 0.5, 0.25, 0.5).inflate(radius / 32f);

        // 渲染这个小盒子（看起来像油面在中点翻滚）
        NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(
                fluid,
                (float) bb.minX, (float) bb.minY, (float) bb.minZ,
                (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ,
                buffer, ms, light,
                true,   // cutout 顶面（让它更像一个“泡”）
                false
        );
    }
}
