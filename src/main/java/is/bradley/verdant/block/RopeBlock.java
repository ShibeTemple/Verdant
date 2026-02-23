package is.bradley.verdant.block;

import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public class RopeBlock extends Block implements Waterloggable {
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final BooleanProperty KNOTTED = BooleanProperty.of("knotted");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    
    public static final Map<Direction, BooleanProperty> DIR_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
        map.put(Direction.UP, UP);
        map.put(Direction.DOWN, DOWN);
    });

    public RopeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false)
                .with(DOWN, false)
                .with(KNOTTED, false)
                .with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = Block.createCuboidShape(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);
        if (state.get(NORTH)) {
            shape = VoxelShapes.union(shape, Block.createCuboidShape(6.0, 6.0, 0.0, 10.0, 10.0, 6.0));
        }
        if (state.get(SOUTH)) {
            shape = VoxelShapes.union(shape, Block.createCuboidShape(6.0, 6.0, 10.0, 10.0, 10.0, 16.0));
        }
        if (state.get(WEST)) {
            shape = VoxelShapes.union(shape, Block.createCuboidShape(0.0, 6.0, 6.0, 6.0, 10.0, 10.0));
        }
        if (state.get(EAST)) {
            shape = VoxelShapes.union(shape, Block.createCuboidShape(10.0, 6.0, 6.0, 16.0, 10.0, 10.0));
        }
        if (state.get(DOWN)) {
            shape = VoxelShapes.union(shape, Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 6.0, 10.0));
        }
        if (state.get(UP)) {
            shape = VoxelShapes.union(shape, Block.createCuboidShape(6.0, 10.0, 6.0, 10.0, 16.0, 10.0));
        }
        return shape;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, KNOTTED, WATERLOGGED);
    }

    public boolean isLadder(BlockState state, WorldView world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return calculateState(getDefaultState(), ctx.getWorld(), ctx.getBlockPos())
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    private BlockState calculateKnot(BlockState currentState) {
        int count = 0;

        if (currentState.get(NORTH)) count++;
        if (currentState.get(SOUTH)) count++;
        if (currentState.get(EAST)) count++;
        if (currentState.get(WEST)) count++;
        if (currentState.get(UP)) count++;
        if (currentState.get(DOWN)) count++;

        boolean doKnot = count > 2 || count == 0;
        return currentState.with(KNOTTED, doKnot);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return calculateState(state, world, pos);
    }

    private BlockState calculateState(BlockState currentState, WorldAccess world, BlockPos pos) {
        boolean northType = shouldConnect(world, pos, Direction.NORTH);
        boolean eastType = shouldConnect(world, pos, Direction.EAST);
        boolean southType = shouldConnect(world, pos, Direction.SOUTH);
        boolean westType = shouldConnect(world, pos, Direction.WEST);
        boolean upType = shouldConnect(world, pos, Direction.UP);
        boolean downType = shouldConnectDown(world, pos);

        return calculateKnot(currentState
                .with(NORTH, northType)
                .with(EAST, eastType)
                .with(SOUTH, southType)
                .with(WEST, westType)
                .with(UP, upType)
                .with(DOWN, downType));
    }

    private boolean shouldConnect(WorldAccess world, BlockPos pos, Direction direction) {
        BlockPos offsetPos = pos.offset(direction);
        BlockState state = world.getBlockState(offsetPos);
        return state.isSideSolidFullSquare(world, offsetPos, direction.getOpposite()) || 
               state.getBlock() instanceof RopeBlock || 
               state.getBlock() instanceof SmallBeamBlock;
    }

    private boolean shouldConnectDown(WorldAccess world, BlockPos pos) {
        BlockPos offsetPos = pos.down();
        BlockState state = world.getBlockState(offsetPos);
        return state.isSideSolidFullSquare(world, offsetPos, Direction.UP) || 
               state.getBlock() instanceof RopeBlock || 
               state.getBlock() instanceof SmallBeamBlock;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}