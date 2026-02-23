package is.bradley.verdant.block;

import is.bradley.verdant.particle.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class FieryTorchBlock extends TorchBlock implements net.minecraft.block.Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public FieryTorchBlock(Settings settings) {
        super(settings, ParticleTypes.FLAME);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return fluid == Fluids.WATER && !state.get(WATERLOGGED);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (fluidState.getFluid() == Fluids.WATER && !state.get(WATERLOGGED)) {
            world.setBlockState(pos, state.with(WATERLOGGED, true), 3);
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            return true;
        }
        return false;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(WATERLOGGED)) return;
        
        double d = (double) pos.getX() + 0.5;
        double e = (double) pos.getY() + 0.7;
        double f = (double) pos.getZ() + 0.5;
        
        float limit = 0.05f;
        float offset0 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5f));
        float offset1 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5f));
        float offset2 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5f));
        
        world.addParticle(ModParticles.FIERY_SPARK, d + offset0, e + offset1, f + offset2, 0, 0, 0);
        world.addParticle(ModParticles.FIERY_GLOW, d, e - 0.1, f, 0, 0, 0);
    }
}