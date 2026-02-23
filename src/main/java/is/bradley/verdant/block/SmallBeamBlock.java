package is.bradley.verdant.block;

import is.bradley.verdant.block.entity.SmallBeamBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class SmallBeamBlock extends Block implements BlockEntityProvider, Waterloggable {
    public static final BooleanProperty X_AXIS = BooleanProperty.of("x_axis");
    public static final BooleanProperty Y_AXIS = BooleanProperty.of("y_axis");
    public static final BooleanProperty Z_AXIS = BooleanProperty.of("z_axis");
    public static final IntProperty CONNECTIONS = IntProperty.of("connections", 0, 3);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final EnumProperty<Direction.Axis> DEFAULT_AXIS = Properties.AXIS;

    public SmallBeamBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(X_AXIS, false)
                .with(Y_AXIS, false)
                .with(Z_AXIS, false)
                .with(CONNECTIONS, 0)
                .with(WATERLOGGED, false)
                .with(DEFAULT_AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SmallBeamBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape voxelshape = VoxelShapes.empty();

        if (state.get(X_AXIS)) {
            voxelshape = VoxelShapes.union(voxelshape, Block.createCuboidShape(0.0, 5.0, 5.0, 16.0, 11.0, 11.0));
        }
        if (state.get(Y_AXIS)) {
            voxelshape = VoxelShapes.union(voxelshape, Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0));
        }
        if (state.get(Z_AXIS)) {
            voxelshape = VoxelShapes.union(voxelshape, Block.createCuboidShape(5.0, 5.0, 0.0, 11.0, 11.0, 16.0));
        }
        if (!state.get(X_AXIS) && !state.get(Y_AXIS) && !state.get(Z_AXIS)) {
            voxelshape = VoxelShapes.fullCube();
        }

        return voxelshape;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(X_AXIS, Y_AXIS, Z_AXIS, CONNECTIONS, WATERLOGGED, DEFAULT_AXIS);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return calculateState(getDefaultState(), ctx.getWorld(), ctx.getBlockPos(), ctx.getSide().getAxis())
                .with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    private BlockState calculateState(BlockState currentState, WorldAccess world, BlockPos pos, Direction.Axis defaultAxis) {
        boolean xBool = defaultAxis == Direction.Axis.X;
        boolean yBool = defaultAxis == Direction.Axis.Y;
        boolean zBool = defaultAxis == Direction.Axis.Z;

        BlockState northState = world.getBlockState(pos.offset(Direction.NORTH));
        BlockState eastState = world.getBlockState(pos.offset(Direction.EAST));
        BlockState southState = world.getBlockState(pos.offset(Direction.SOUTH));
        BlockState westState = world.getBlockState(pos.offset(Direction.WEST));
        BlockState upState = world.getBlockState(pos.offset(Direction.UP));
        BlockState downState = world.getBlockState(pos.offset(Direction.DOWN));

        if (eastState.getBlock() == this && eastState.get(X_AXIS)) {
            xBool = true;
        } else if (westState.getBlock() == this && westState.get(X_AXIS)) {
            xBool = true;
        }

        if (upState.getBlock() == this && upState.get(Y_AXIS)) {
            yBool = true;
        } else if (downState.getBlock() == this && downState.get(Y_AXIS)) {
            yBool = true;
        }

        if (northState.getBlock() == this && northState.get(Z_AXIS)) {
            zBool = true;
        } else if (southState.getBlock() == this && southState.get(Z_AXIS)) {
            zBool = true;
        }

        int count = 0;
        if (xBool) count++;
        if (yBool) count++;
        if (zBool) count++;

        return currentState
                .with(X_AXIS, xBool)
                .with(Y_AXIS, yBool)
                .with(Z_AXIS, zBool)
                .with(CONNECTIONS, count)
                .with(DEFAULT_AXIS, defaultAxis);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SmallBeamBlockEntity) {
            ((SmallBeamBlockEntity) blockEntity).ropeConnectionCalculations(world, state, pos);
        }
        
        return calculateState(state, world, pos, state.get(DEFAULT_AXIS));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}