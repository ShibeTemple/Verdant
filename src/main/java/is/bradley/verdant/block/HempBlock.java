package is.bradley.verdant.block;

import is.bradley.verdant.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.block.ShapeContext;

public class HempBlock extends CropBlock {
    public static final IntProperty AGE = IntProperty.of("age", 0, 3);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.createCuboidShape(4, 0, 4, 12, 4, 12),
            Block.createCuboidShape(4, 0, 4, 12, 8, 12),
            Block.createCuboidShape(4, 0, 4, 12, 12, 12),
            Block.createCuboidShape(4, 0, 4, 12, 16, 12)
    };

    public HempBlock(Settings settings) {
        super(settings);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.HEMP_SEEDS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(AGE)];
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}