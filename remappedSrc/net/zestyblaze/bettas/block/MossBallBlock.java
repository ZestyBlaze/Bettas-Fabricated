package net.zestyblaze.bettas.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.zestyblaze.bettas.registry.BettaBlocksInit;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

@SuppressWarnings("deprecation")
public class MossBallBlock extends PlantBlock implements Waterloggable, Fertilizable {
    private static final IntProperty BALLS = Properties.PICKLES;
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static final VoxelShape ONE_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 7, 16);
    protected static final VoxelShape TWO_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 7, 16);
    protected static final VoxelShape THREE_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 7, 16);
    protected static final VoxelShape FOUR_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

    public MossBallBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(BALLS, 1).with(WATERLOGGED, true));
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if(blockState.isOf(this)) {
            return blockState.with(BALLS, Math.min(4, blockState.get(BALLS) + 1));
        } else {
            FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            boolean flag = fluidState.getFluid() == Fluids.WATER;
            return Objects.requireNonNull(super.getPlacementState(ctx)).with(WATERLOGGED, flag);
        }
    }

    private static boolean isInBadEnvironment(BlockState state) {
        return !state.get(WATERLOGGED);
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
        if(!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            if(state.get(WATERLOGGED)) {
                world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().getItem() == this.asItem() && state.get(BALLS) < 4 || super.canReplace(state, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return switch (state.get(BALLS)) {
            default -> ONE_SHAPE;
            case 2 -> TWO_SHAPE;
            case 3 -> THREE_SHAPE;
            case 4 -> FOUR_SHAPE;
        };
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BALLS, WATERLOGGED);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if(!isInBadEnvironment(state) && world.getBlockState(pos.down()).getBlock() == BettaBlocksInit.MOSS_BALL_BLOCK) {
            int j = 1;
            int i1 = pos.getX() - 2;
            int j1 = 0;

            for(int k1 = 0; k1 < 5; ++k1) {
                for(int l1 = 0; l1 < j; ++l1) {
                    int i2 = 2 + pos.getY() - 1;

                    for(int j2 = i2 - 2; j2 < i2; ++j2) {
                        BlockPos blockPos = new BlockPos(i1 + k1, j2, pos.getZ() - j1 + l1);
                        if(state.getBlock() == BettaBlocksInit.MOSS_BALL_BLOCK) {
                            world.setBlockState(blockPos, BettaBlocksInit.MOSS_BALL_BLOCK.getDefaultState().with(BALLS, random.nextInt(4) + 1), 3);
                        }
                    }
                }
            }

            ++j1;
        }
        world.setBlockState(pos, state.with(BALLS, 4), 2);
    }
}
