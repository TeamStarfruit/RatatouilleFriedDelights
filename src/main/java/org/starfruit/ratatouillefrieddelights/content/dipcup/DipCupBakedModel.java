package org.starfruit.ratatouillefrieddelights.content.dipcup;

import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.starfruit.ratatouillefrieddelights.entry.RFDSpriteShifts;

import java.util.ArrayList;
import java.util.List;

public class DipCupBakedModel extends BakedModelWrapperWithData {
    public DipCupBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData) {
        boolean opened = state.getValue(DipCupBlock.OPENED);
        int remaining = state.getValue(DipCupBlock.REMAINING_DIP);
        builder.with(DipCupModelData.OPENED, opened);
        builder.with(DipCupModelData.SEAL_SPRITE, ((DipCupBlock) state.getBlock()).sealSprite);
        builder.with(DipCupModelData.REMAINING_DIP, remaining);
        return builder;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource rand,
            @NotNull ModelData data,
            @Nullable RenderType renderType) {

        List<BakedQuad> quads = new ArrayList<>(super.getQuads(state, side, rand, data, renderType));
        if (state == null || side != null) return quads;

        boolean opened = Boolean.TRUE.equals(data.get(DipCupModelData.OPENED));
        SpriteShiftEntry seal = data.get(DipCupModelData.SEAL_SPRITE);
        int remaining = data.get(DipCupModelData.REMAINING_DIP) == null ? 0 : data.get(DipCupModelData.REMAINING_DIP);

        if (!opened && seal != null) {
            quads.add(renderSeal(state, seal));
            return quads;
        }

        if (remaining > 0 && opened) {
            quads.add(renderDip(state, remaining));
        }

        return quads;
    }

    private BakedQuad renderSeal(BlockState state, SpriteShiftEntry seal) {
        Direction facing = state.hasProperty(DipCupBlock.FACING)
                ? state.getValue(DipCupBlock.FACING)
                : Direction.NORTH;

        TextureAtlasSprite sprite = seal.getTarget();
        int[] vertexData = new int[BakedQuadHelper.VERTEX_STRIDE * 4];
        float y = 3f / 16f + 0.001f;

        float x0 = 4f / 16f;
        float x1 = 12f / 16f;
        float z0 = 4f / 16f;
        float z1 = 12f / 16f;

        float[][] positions = {
                {x0, z0},
                {x0, z1},
                {x1, z1},
                {x1, z0}
        };

        for (int i = 0; i < 4; i++) {
            float[] p = rotate(positions[i][0], positions[i][1], facing);
            float u = (i == 0 || i == 1) ? sprite.getU0() : sprite.getU1();
            float v = (i == 0 || i == 3) ? sprite.getV0() : sprite.getV1();
            setVertex(vertexData, i, p[0], y, p[1], u, v,  0xFFFFFFFF);
        }

        return new BakedQuad(vertexData, -1, Direction.UP, sprite, true);
    }

    private BakedQuad renderDip(BlockState state, int remaining) {
        Direction facing = state.hasProperty(DipCupBlock.FACING)
                ? state.getValue(DipCupBlock.FACING)
                : Direction.NORTH;

        int color = ((DipCupBlock) state.getBlock()).dipColor;
        TextureAtlasSprite sprite = RFDSpriteShifts.DIP_CUP_SOURCE.getTarget();
        int[] vertexData = new int[BakedQuadHelper.VERTEX_STRIDE * 4];

        float baseY = 0.5f / 16f;
        float height = switch (remaining) {
            case 3 -> 2.5f / 16f;
            case 2 -> 1.7f / 16f;
            case 1 -> 1.0f / 16f;
            default -> 0f;
        };
        float y = baseY + height;

        float x0 = 4.5f / 16f;
        float x1 = 11.5f / 16f;
        float z0 = 4.3f / 16f;
        float z1 = 11.7f / 16f;

        float[][] positions = {
                {x0, z0},
                {x0, z1},
                {x1, z1},
                {x1, z0}
        };

        for (int i = 0; i < 4; i++) {
            float[] p = rotate(positions[i][0], positions[i][1], facing);
            float u = (i == 0 || i == 1) ? sprite.getU0() : sprite.getU1();
            float v = (i == 0 || i == 3) ? sprite.getV0() : sprite.getV1();
            setVertex(vertexData, i, p[0], y, p[1], u, v, color);
        }

        return new BakedQuad(vertexData, -1, Direction.UP, sprite, true);
    }

    private float[] rotate(float x, float z, Direction facing) {
        return switch (facing) {
            case NORTH -> new float[]{x, z};
            case SOUTH -> new float[]{1f - x, 1f - z};
            case WEST -> new float[]{z, 1f - x};
            case EAST -> new float[]{1f - z, x};
            default -> new float[]{x, z};
        };
    }

    private void setVertex(int[] vertexData, int vertex, float x, float y, float z, float u, float v, int color) {
        int offset = vertex * BakedQuadHelper.VERTEX_STRIDE;
        vertexData[offset + BakedQuadHelper.X_OFFSET] = Float.floatToRawIntBits(x);
        vertexData[offset + BakedQuadHelper.Y_OFFSET] = Float.floatToRawIntBits(y);
        vertexData[offset + BakedQuadHelper.Z_OFFSET] = Float.floatToRawIntBits(z);
        vertexData[offset + BakedQuadHelper.COLOR_OFFSET] = color;
        vertexData[offset + BakedQuadHelper.U_OFFSET] = Float.floatToRawIntBits(u);
        vertexData[offset + BakedQuadHelper.V_OFFSET] = Float.floatToRawIntBits(v);
        vertexData[offset + BakedQuadHelper.LIGHT_OFFSET] = 0;
        vertexData[offset + BakedQuadHelper.NORMAL_OFFSET] = (127 << 24) | (127 << 16) | (0 << 8);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.cutout());
    }

    private static class DipCupModelData {
        private static final ModelProperty<Boolean> OPENED = new ModelProperty<>();
        private static final ModelProperty<SpriteShiftEntry> SEAL_SPRITE = new ModelProperty<>();
        private static final ModelProperty<Integer> REMAINING_DIP = new ModelProperty<>();
    }
}
