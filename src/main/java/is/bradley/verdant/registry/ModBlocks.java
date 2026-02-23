package is.bradley.verdant.registry;

import is.bradley.verdant.Verdant;
import is.bradley.verdant.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    // Hemp Crop
    public static final Block HEMP_CROP = register("hemp_crop", 
            new HempBlock(FabricBlockSettings.create().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)));
    
    // Rope
    public static final Block ROPE = register("rope", 
            new RopeBlock(FabricBlockSettings.create().noCollision().strength(0.0f).sounds(BlockSoundGroup.WOOL)));
    
    // Fiery Glass Ore and Block
    public static final Block FIERY_GLASS_ORE = register("fiery_glass_ore", 
            new OreXPBlock(FabricBlockSettings.create().strength(3.0f).requiresTool().sounds(BlockSoundGroup.STONE), 
                    UniformIntProvider.create(2, 5)));
    public static final Block FIERY_GLASS_BLOCK = register("fiery_glass_block", 
            new Block(FabricBlockSettings.create().strength(4.0f).requiresTool().sounds(BlockSoundGroup.STONE).luminance(15)));
    
    // Fiery Torches
    public static final Block FIERY_TORCH = register("fiery_torch", 
            new FieryTorchBlock(FabricBlockSettings.create().noCollision().breakInstantly().luminance(15).sounds(BlockSoundGroup.BAMBOO)));
    public static final Block WALL_FIERY_TORCH = register("wall_fiery_torch", 
            new WallFieryTorchBlock(FabricBlockSettings.create().noCollision().breakInstantly().luminance(15).sounds(BlockSoundGroup.BAMBOO).dropsLike(FIERY_TORCH)));
    
    // Regular Beams (pillar blocks)
    public static final Block OAK_BEAM = register("oak_beam", createBeamBlock(MapColor.OAK_TAN));
    public static final Block SPRUCE_BEAM = register("spruce_beam", createBeamBlock(MapColor.SPRUCE_BROWN));
    public static final Block BIRCH_BEAM = register("birch_beam", createBeamBlock(MapColor.PALE_YELLOW));
    public static final Block JUNGLE_BEAM = register("jungle_beam", createBeamBlock(MapColor.DIRT_BROWN));
    public static final Block ACACIA_BEAM = register("acacia_beam", createBeamBlock(MapColor.ORANGE));
    public static final Block DARK_OAK_BEAM = register("dark_oak_beam", createBeamBlock(MapColor.BROWN));
    public static final Block MANGROVE_BEAM = register("mangrove_beam", createBeamBlock(MapColor.RED));
    public static final Block CRIMSON_BEAM = register("crimson_beam", createNetherBeamBlock(MapColor.DARK_CRIMSON));
    public static final Block WARPED_BEAM = register("warped_beam", createNetherBeamBlock(MapColor.TEAL));

    // Small Beams (with BlockEntity)
    public static final Block OAK_SMALL_BEAM = register("oak_small_beam", createSmallBeamBlock(MapColor.OAK_TAN));
    public static final Block SPRUCE_SMALL_BEAM = register("spruce_small_beam", createSmallBeamBlock(MapColor.SPRUCE_BROWN));
    public static final Block BIRCH_SMALL_BEAM = register("birch_small_beam", createSmallBeamBlock(MapColor.PALE_YELLOW));
    public static final Block JUNGLE_SMALL_BEAM = register("jungle_small_beam", createSmallBeamBlock(MapColor.DIRT_BROWN));
    public static final Block ACACIA_SMALL_BEAM = register("acacia_small_beam", createSmallBeamBlock(MapColor.ORANGE));
    public static final Block DARK_OAK_SMALL_BEAM = register("dark_oak_small_beam", createSmallBeamBlock(MapColor.BROWN));
    public static final Block MANGROVE_SMALL_BEAM = register("mangrove_small_beam", createSmallBeamBlock(MapColor.RED));
    public static final Block CRIMSON_SMALL_BEAM = register("crimson_small_beam", createNetherSmallBeamBlock(MapColor.DARK_CRIMSON));
    public static final Block WARPED_SMALL_BEAM = register("warped_small_beam", createNetherSmallBeamBlock(MapColor.TEAL));

    // Panels
    public static final Block OAK_PANELS = register("oak_panels", createPanelBlock(MapColor.OAK_TAN));
    public static final Block SPRUCE_PANELS = register("spruce_panels", createPanelBlock(MapColor.SPRUCE_BROWN));
    public static final Block BIRCH_PANELS = register("birch_panels", createPanelBlock(MapColor.PALE_YELLOW));
    public static final Block JUNGLE_PANELS = register("jungle_panels", createPanelBlock(MapColor.DIRT_BROWN));
    public static final Block ACACIA_PANELS = register("acacia_panels", createPanelBlock(MapColor.ORANGE));
    public static final Block DARK_OAK_PANELS = register("dark_oak_panels", createPanelBlock(MapColor.BROWN));
    public static final Block MANGROVE_PANELS = register("mangrove_panels", createPanelBlock(MapColor.RED));
    public static final Block CRIMSON_PANELS = register("crimson_panels", createNetherPanelBlock(MapColor.DARK_CRIMSON));
    public static final Block WARPED_PANELS = register("warped_panels", createNetherPanelBlock(MapColor.TEAL));

    private static Block createBeamBlock(MapColor color) {
        return new PillarBlock(FabricBlockSettings.create().mapColor(color).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    }
    
    private static Block createNetherBeamBlock(MapColor color) {
        return new PillarBlock(FabricBlockSettings.create().mapColor(color).strength(2.0f).sounds(BlockSoundGroup.NETHER_STEM));
    }
    
    private static Block createSmallBeamBlock(MapColor color) {
        return new SmallBeamBlock(FabricBlockSettings.create().mapColor(color).strength(2.0f).sounds(BlockSoundGroup.WOOD));
    }
    
    private static Block createNetherSmallBeamBlock(MapColor color) {
        return new SmallBeamBlock(FabricBlockSettings.create().mapColor(color).strength(2.0f).sounds(BlockSoundGroup.NETHER_STEM));
    }
    
    private static Block createPanelBlock(MapColor color) {
        return new Block(FabricBlockSettings.create().mapColor(color).strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD));
    }
    
    private static Block createNetherPanelBlock(MapColor color) {
        return new Block(FabricBlockSettings.create().mapColor(color).strength(2.0f, 3.0f).sounds(BlockSoundGroup.NETHER_STEM));
    }

    private static Block register(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(Verdant.MOD_ID, name), block);
    }

    public static void register() {
        // Blocks are registered via static initializers above
    }
}