package net.zestyblaze.bettas.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.OrderedTick;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class DriedLeavesBlock extends PlantBlock implements Waterloggable {
    protected static final VoxelShape ONE_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);
    protected static final VoxelShape TWO_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
    protected static final VoxelShape THREE_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 5, 16);
    private static final IntProperty LEAVES = Properties.PICKLES;
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public DriedLeavesBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LEAVES, 1).with(WATERLOGGED, true));
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (state.isOf(this)) {
            return state.with(LEAVES, Math.min(3, state.get(LEAVES) + 1));
        } else {
            FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            boolean flag = fluidState.getFluid() == Fluids.WATER;
            return Objects.requireNonNull(super.getPlacementState(ctx)).with(WATERLOGGED, flag);
        }
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return !floor.getCollisionShape(world, pos).getFace(Direction.UP).isEmpty() || floor.isSideSolidFullSquare(world, pos, Direction.UP);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        return this.canPlantOnTop(world.getBlockState(blockPos), world, blockPos);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            if (state.get(WATERLOGGED)) {
                world.getFluidTickScheduler().scheduleTick(OrderedTick.create(Fluids.WATER, pos));
            }
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().getItem() == this.asItem() && state.get(LEAVES) < 4 || super.canReplace(state, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return switch (state.get(LEAVES)) {
            default -> ONE_SHAPE;
            case 2 -> TWO_SHAPE;
            case 3 -> THREE_SHAPE;
        };
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEAVES, WATERLOGGED);
    }
}
