package org.starfruit.ratatouillefrieddelights.content.dipcup;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllItems;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.starfruit.ratatouillefrieddelights.entry.RFDSpriteShifts;

public class DipCupBlock extends HorizontalDirectionalBlock {
    public static final Property<Integer> REMAINING_DIP = IntegerProperty.create("remaining_dip", 1, 3);
    public static final Property<Boolean> OPENED = BooleanProperty.create("opened");
    public static final MapCodec<HorizontalDirectionalBlock> CODEC = simpleCodec(DipCupBlock::new);
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            // SOUTH
            Block.box(6, 0, 5, 10, 3, 11),
            // WEST
            Block.box(5, 0, 6, 11, 3, 10),
            // NORTH
            Block.box(6, 0, 5, 10, 3, 11),
            // EAST
            Block.box(5, 0, 6, 11, 3, 10)
    };
    // RGBA
    public final int dipColor;
    public final SpriteShiftEntry sealSprite;

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPES[state.getValue(FACING).get2DDataValue()];
    }

    public DipCupBlock(BlockBehaviour.Properties props) {
        this(props, 0xFFFFFF, RFDSpriteShifts.DIP_CUP_KETCHUP);
    }

    public static int rgbaToAbgr(int rgba) {
        int r = (rgba >> 24) & 0xFF;
        int g = (rgba >> 16) & 0xFF;
        int b = (rgba >> 8) & 0xFF;
        int a = rgba & 0xFF;
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    public DipCupBlock(BlockBehaviour.Properties props, int dipColor, SpriteShiftEntry sealSprite) {
        super(props);
        this.dipColor = rgbaToAbgr(dipColor);
        this.sealSprite = sealSprite;
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(REMAINING_DIP, 3)
                        .setValue(FACING, Direction.NORTH)
                        .setValue(OPENED, false)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(REMAINING_DIP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REMAINING_DIP)
                .add(OPENED)
                .add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        boolean opened = state.getValue(OPENED);
        if (opened) {
            return InteractionResult.SUCCESS;
        }

        level.setBlock(pos, state.setValue(OPENED, true), 3);
        level.playSound(null, pos,
                SoundEvents.WOODEN_TRAPDOOR_OPEN,
                SoundSource.BLOCKS, 0.6F, 1.0F);

        return InteractionResult.SUCCESS;
    }
}
