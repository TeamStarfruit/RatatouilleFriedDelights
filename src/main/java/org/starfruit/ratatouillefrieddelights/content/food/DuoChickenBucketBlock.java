package org.starfruit.ratatouillefrieddelights.content.food;

import com.simibubi.create.AllShapes;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

@MethodsReturnNonnullByDefault
public class DuoChickenBucketBlock extends HorizontalDirectionalBlock {
    public static final Property<Integer> REMAINING_BITES = IntegerProperty.create("remaining_bites", 0, 4);
    private static final VoxelShape SHAPE =Shapes.join(
            Shapes.join(
                    Block.box(2.75, 0, 2.75, 13.25, 3, 13.25)
                    , Shapes.join(
                            Block.box(2.25, 3, 2.25, 13.75, 11, 13.75),
                            Block.box(2,11,2,14,12,14)
                            , BooleanOp.OR
                    ), BooleanOp.OR),
            Block.box(3, 6, 3, 13, 12, 13),
            BooleanOp.ONLY_FIRST
    );

    public DuoChickenBucketBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(REMAINING_BITES, 4).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REMAINING_BITES)
                .add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
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
            ItemStack item = ItemStack.EMPTY;
            item = switch (bites) {
                case 4, 2 -> new ItemStack(RFDItems.ORIGINAL_CHICKEN_KEEL.get(), 1);
                case 3, 1 -> new ItemStack(RFDItems.ORIGINAL_CHICKEN_DRUMSTICK.get(), 1);
                default -> item;
            };
            if (!player.addItem(item)) {
                player.drop(item, false);
            }
            level.setBlock(pos, state.setValue(REMAINING_BITES, bites - 1), 3);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6F, 1.0F);
        }

        return InteractionResult.SUCCESS;
    }
}
