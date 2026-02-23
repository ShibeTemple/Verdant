package is.bradley.verdant.registry;

import is.bradley.verdant.Verdant;
import is.bradley.verdant.item.BasicShieldItem;
import is.bradley.verdant.item.CockroachOothecaItem;
import is.bradley.verdant.item.SickleItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // Cockroach Items
    public static final Item COCKROACH_WING_FRAGMENT = register("cockroach_wing_fragment",
            new Item(new FabricItemSettings()));
    public static final Item COCKROACH_WING = register("cockroach_wing",
            new Item(new FabricItemSettings()));
    public static final Item COCKROACH_OOTHECA = register("cockroach_ootheca",
            new CockroachOothecaItem(new FabricItemSettings().maxCount(16)));
    public static final Item MARACA = register("maraca",
            new Item(new FabricItemSettings()));
    public static final Item SOMBRERO = register("sombrero",
            new Item(new FabricItemSettings()));
    // Spawn egg registered after entity types; see ModEntities.register()
    // Bone Tools
    public static final Item BONE_SWORD = register("bone_sword", 
            new SwordItem(ModToolMaterials.BONE, 3, -2.4f, new FabricItemSettings()));
    public static final Item BONE_SHOVEL = register("bone_shovel", 
            new ShovelItem(ModToolMaterials.BONE, 1.5f, -3.0f, new FabricItemSettings()));
    public static final Item BONE_PICKAXE = register("bone_pickaxe", 
            new PickaxeItem(ModToolMaterials.BONE, 1, -2.8f, new FabricItemSettings()));
    public static final Item BONE_AXE = register("bone_axe", 
            new AxeItem(ModToolMaterials.BONE, 7.0f, -3.2f, new FabricItemSettings()));
    public static final Item BONE_HOE = register("bone_hoe", 
            new HoeItem(ModToolMaterials.BONE, 1, -2.0f, new FabricItemSettings()));
    public static final Item BONE_SICKLE = register("bone_sickle", 
            new SickleItem(ModToolMaterials.BONE, 2, new FabricItemSettings()));
    
    // Bone Armor
    public static final Item BONE_HELMET = register("bone_helmet", 
            new ArmorItem(ModArmorMaterials.BONE, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item BONE_CHESTPLATE = register("bone_chestplate", 
            new ArmorItem(ModArmorMaterials.BONE, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item BONE_LEGGINGS = register("bone_leggings", 
            new ArmorItem(ModArmorMaterials.BONE, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item BONE_BOOTS = register("bone_boots", 
            new ArmorItem(ModArmorMaterials.BONE, ArmorItem.Type.BOOTS, new FabricItemSettings()));
    public static final Item BONE_SHIELD = register("bone_shield", 
            new BasicShieldItem(new FabricItemSettings().maxDamage(400), Items.BONE));
    
    // Hemp Items
    public static final Item HEMP = register("hemp", 
            new Item(new FabricItemSettings()));
    public static final Item HEMP_SEEDS = register("hemp_seeds", 
            new AliasedBlockItem(ModBlocks.HEMP_CROP, new FabricItemSettings()));
    public static final Item ROPE = register("rope", 
            new BlockItem(ModBlocks.ROPE, new FabricItemSettings()));
    
    // Fiery Items
    public static final Item FIERY_GLASS = register("fiery_glass", 
            new Item(new FabricItemSettings()));
    public static final Item FIERY_GLASS_ORE = register("fiery_glass_ore", 
            new BlockItem(ModBlocks.FIERY_GLASS_ORE, new FabricItemSettings()));
    public static final Item FIERY_GLASS_BLOCK = register("fiery_glass_block", 
            new BlockItem(ModBlocks.FIERY_GLASS_BLOCK, new FabricItemSettings()));
    public static final Item FIERY_TORCH = register("fiery_torch", 
            new BlockItem(ModBlocks.FIERY_TORCH, new FabricItemSettings()));
    
    // Regular Beams
    public static final Item OAK_BEAM = register("oak_beam", 
            new BlockItem(ModBlocks.OAK_BEAM, new FabricItemSettings()));
    public static final Item SPRUCE_BEAM = register("spruce_beam", 
            new BlockItem(ModBlocks.SPRUCE_BEAM, new FabricItemSettings()));
    public static final Item BIRCH_BEAM = register("birch_beam", 
            new BlockItem(ModBlocks.BIRCH_BEAM, new FabricItemSettings()));
    public static final Item JUNGLE_BEAM = register("jungle_beam", 
            new BlockItem(ModBlocks.JUNGLE_BEAM, new FabricItemSettings()));
    public static final Item ACACIA_BEAM = register("acacia_beam", 
            new BlockItem(ModBlocks.ACACIA_BEAM, new FabricItemSettings()));
    public static final Item DARK_OAK_BEAM = register("dark_oak_beam", 
            new BlockItem(ModBlocks.DARK_OAK_BEAM, new FabricItemSettings()));
    public static final Item MANGROVE_BEAM = register("mangrove_beam", 
            new BlockItem(ModBlocks.MANGROVE_BEAM, new FabricItemSettings()));
    public static final Item CRIMSON_BEAM = register("crimson_beam", 
            new BlockItem(ModBlocks.CRIMSON_BEAM, new FabricItemSettings()));
    public static final Item WARPED_BEAM = register("warped_beam", 
            new BlockItem(ModBlocks.WARPED_BEAM, new FabricItemSettings()));
    
    // Small Beams
    public static final Item OAK_SMALL_BEAM = register("oak_small_beam", 
            new BlockItem(ModBlocks.OAK_SMALL_BEAM, new FabricItemSettings()));
    public static final Item SPRUCE_SMALL_BEAM = register("spruce_small_beam", 
            new BlockItem(ModBlocks.SPRUCE_SMALL_BEAM, new FabricItemSettings()));
    public static final Item BIRCH_SMALL_BEAM = register("birch_small_beam", 
            new BlockItem(ModBlocks.BIRCH_SMALL_BEAM, new FabricItemSettings()));
    public static final Item JUNGLE_SMALL_BEAM = register("jungle_small_beam", 
            new BlockItem(ModBlocks.JUNGLE_SMALL_BEAM, new FabricItemSettings()));
    public static final Item ACACIA_SMALL_BEAM = register("acacia_small_beam", 
            new BlockItem(ModBlocks.ACACIA_SMALL_BEAM, new FabricItemSettings()));
    public static final Item DARK_OAK_SMALL_BEAM = register("dark_oak_small_beam", 
            new BlockItem(ModBlocks.DARK_OAK_SMALL_BEAM, new FabricItemSettings()));
    public static final Item MANGROVE_SMALL_BEAM = register("mangrove_small_beam", 
            new BlockItem(ModBlocks.MANGROVE_SMALL_BEAM, new FabricItemSettings()));
    public static final Item CRIMSON_SMALL_BEAM = register("crimson_small_beam", 
            new BlockItem(ModBlocks.CRIMSON_SMALL_BEAM, new FabricItemSettings()));
    public static final Item WARPED_SMALL_BEAM = register("warped_small_beam", 
            new BlockItem(ModBlocks.WARPED_SMALL_BEAM, new FabricItemSettings()));
    
    // Panels
    public static final Item OAK_PANELS = register("oak_panels", 
            new BlockItem(ModBlocks.OAK_PANELS, new FabricItemSettings()));
    public static final Item SPRUCE_PANELS = register("spruce_panels", 
            new BlockItem(ModBlocks.SPRUCE_PANELS, new FabricItemSettings()));
    public static final Item BIRCH_PANELS = register("birch_panels", 
            new BlockItem(ModBlocks.BIRCH_PANELS, new FabricItemSettings()));
    public static final Item JUNGLE_PANELS = register("jungle_panels", 
            new BlockItem(ModBlocks.JUNGLE_PANELS, new FabricItemSettings()));
    public static final Item ACACIA_PANELS = register("acacia_panels", 
            new BlockItem(ModBlocks.ACACIA_PANELS, new FabricItemSettings()));
    public static final Item DARK_OAK_PANELS = register("dark_oak_panels", 
            new BlockItem(ModBlocks.DARK_OAK_PANELS, new FabricItemSettings()));
    public static final Item MANGROVE_PANELS = register("mangrove_panels", 
            new BlockItem(ModBlocks.MANGROVE_PANELS, new FabricItemSettings()));
    public static final Item CRIMSON_PANELS = register("crimson_panels", 
            new BlockItem(ModBlocks.CRIMSON_PANELS, new FabricItemSettings()));
    public static final Item WARPED_PANELS = register("warped_panels", 
            new BlockItem(ModBlocks.WARPED_PANELS, new FabricItemSettings()));

    // Spawn egg – must be registered AFTER entity types
    public static Item COCKROACH_SPAWN_EGG;

    public static void registerSpawnEgg() {
        COCKROACH_SPAWN_EGG = register("cockroach_spawn_egg",
                new SpawnEggItem(ModEntities.COCKROACH, 0x0D0909, 0x42241E, new FabricItemSettings()));
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Verdant.MOD_ID, name), item);
    }

    public static void register() {
        ModItemGroups.register();
    }
}