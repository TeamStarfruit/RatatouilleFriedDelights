package org.starfruit.ratatouillefrieddelights.content.food;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

@MethodsReturnNonnullByDefault
public class BoxedFriesBlock extends HorizontalDirectionalBlock {
    public static final Property<Integer> REMAINING_BITES = IntegerProperty.create("remaining_bites", 0, 3);
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            // SOUTH
            Block.box(5, 0, 6, 11, 10, 10),
            // WEST
            Block.box(6, 0, 5, 10, 10, 11),
            // NORTH
            Block.box(5, 0, 6, 11, 10, 10),
            // EAST
            Block.box(6, 0, 5, 10, 10, 11)
    };
    private static final VoxelShape[] SHELLS = new VoxelShape[]{
            // SOUTH
            Block.box(5, 0, 6, 11, 6, 10),
            // WEST
            Block.box(6, 0, 5, 10, 6, 11),
            // NORTH
            Block.box(5, 0, 6, 11, 6, 10),
            // EAST
            Block.box(6, 0, 5, 10, 6, 11)
    };

    public BoxedFriesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(REMAINING_BITES, 3).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return state.getValue(REMAINING_BITES) == 0 ? SHELLS[state.getValue(FACING).get2DDataValue()]: SHAPES[state.getValue(FACING).get2DDataValue()];
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REMAINING_BITES)
                .add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(REMAINING_BITES);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!player.getItemInHand(hand).isEmpty())
            return InteractionResult.PASS;
        return useWithoutItem(state, level, pos, player);
    }

    private InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        int bites = state.getValue(REMAINING_BITES);

        if (bites == 0) {
            level.destroyBlock(pos, true, player);
        } else {
            ItemStack fry = new ItemStack(RFDItems.A_FRY.get());
            if (!player.addItem(fry)) {
                player.drop(fry, false);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6F, 1.0F);

            level.setBlock(pos, state.setValue(REMAINING_BITES, bites - 1), 3);
        }

        return InteractionResult.SUCCESS;
    }
}
