package is.bradley.verdant.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import is.bradley.verdant.particle.ModParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.world.BlockView;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import net.minecraft.util.math.random.Random;

import java.util.Map;

public class WallFieryTorchBlock extends FieryTorchBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.createCuboidShape(5.5, 3, 11, 10.5, 13, 16),
            Direction.SOUTH, Block.createCuboidShape(5.5, 3, 0, 10.5, 13, 5),
            Direction.WEST, Block.createCuboidShape(11, 3, 5.5, 16, 13, 10.5),
            Direction.EAST, Block.createCuboidShape(0, 3, 5.5, 5, 13, 10.5)
    ));

    public WallFieryTorchBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] directions = ctx.getPlacementDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.with(FACING, direction2);
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        FluidState fluidState = world.getFluidState(pos);
        if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)) {
            return fluidState.getFluid() == Fluids.WATER ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(WATERLOGGED)) return;
        
        Direction direction = state.get(FACING);
        double d = (double) pos.getX() + 0.5;
        double e = (double) pos.getY() + 0.65;
        double f = (double) pos.getZ() + 0.5;
        double g = 0.22;
        double h = 0.27;
        Direction direction2 = direction.getOpposite();
        
        float limit = 0.05f;
        float offset0 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5f));
        float offset1 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5f));
        float offset2 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5f));
        
        world.addParticle(ModParticles.FIERY_SPARK, 
                d + offset0 + h * (double) direction2.getOffsetX(), 
                e + offset1 + g, 
                f + offset2 + h * (double) direction2.getOffsetZ(), 
                0, 0, 0);
        world.addParticle(ModParticles.FIERY_GLOW, 
                d + h * (double) direction2.getOffsetX(), 
                e + g - 0.1, 
                f + h * (double) direction2.getOffsetZ(), 
                0, 0, 0);
    }
}