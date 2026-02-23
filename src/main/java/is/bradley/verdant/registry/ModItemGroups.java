package is.bradley.verdant.registry;

import is.bradley.verdant.Verdant;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup VERDANT = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.FIERY_TORCH))
            .displayName(Text.translatable("itemGroup.verdant"))
            .entries((displayContext, entries) -> {
                // Bone items
                entries.add(ModItems.BONE_SWORD);
                entries.add(ModItems.BONE_SHOVEL);
                entries.add(ModItems.BONE_PICKAXE);
                entries.add(ModItems.BONE_AXE);
                entries.add(ModItems.BONE_HOE);
                entries.add(ModItems.BONE_SICKLE);
                entries.add(ModItems.BONE_HELMET);
                entries.add(ModItems.BONE_CHESTPLATE);
                entries.add(ModItems.BONE_LEGGINGS);
                entries.add(ModItems.BONE_BOOTS);
                entries.add(ModItems.BONE_SHIELD);
                
                // Hemp items
                entries.add(ModItems.HEMP);
                entries.add(ModItems.HEMP_SEEDS);
                entries.add(ModItems.ROPE);
                
                // Fiery items
                entries.add(ModItems.FIERY_GLASS);
                entries.add(ModItems.FIERY_GLASS_BLOCK);
                entries.add(ModItems.FIERY_TORCH);
                
                // Beams - regular
                entries.add(ModItems.OAK_BEAM);
                entries.add(ModItems.SPRUCE_BEAM);
                entries.add(ModItems.BIRCH_BEAM);
                entries.add(ModItems.JUNGLE_BEAM);
                entries.add(ModItems.ACACIA_BEAM);
                entries.add(ModItems.DARK_OAK_BEAM);
                entries.add(ModItems.MANGROVE_BEAM);
                entries.add(ModItems.CRIMSON_BEAM);
                entries.add(ModItems.WARPED_BEAM);
                
                // Beams - small
                entries.add(ModItems.OAK_SMALL_BEAM);
                entries.add(ModItems.SPRUCE_SMALL_BEAM);
                entries.add(ModItems.BIRCH_SMALL_BEAM);
                entries.add(ModItems.JUNGLE_SMALL_BEAM);
                entries.add(ModItems.ACACIA_SMALL_BEAM);
                entries.add(ModItems.DARK_OAK_SMALL_BEAM);
                entries.add(ModItems.MANGROVE_SMALL_BEAM);
                entries.add(ModItems.CRIMSON_SMALL_BEAM);
                entries.add(ModItems.WARPED_SMALL_BEAM);
                
                // Panels
                entries.add(ModItems.OAK_PANELS);
                entries.add(ModItems.SPRUCE_PANELS);
                entries.add(ModItems.BIRCH_PANELS);
                entries.add(ModItems.JUNGLE_PANELS);
                entries.add(ModItems.ACACIA_PANELS);
                entries.add(ModItems.DARK_OAK_PANELS);
                entries.add(ModItems.MANGROVE_PANELS);
                entries.add(ModItems.CRIMSON_PANELS);
                entries.add(ModItems.WARPED_PANELS);
            })
            .build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(Verdant.MOD_ID, "verdant"), VERDANT);
    }
}