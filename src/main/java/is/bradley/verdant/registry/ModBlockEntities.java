package is.bradley.verdant.registry;

import is.bradley.verdant.Verdant;
import is.bradley.verdant.block.entity.SmallBeamBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<SmallBeamBlockEntity> SMALL_BEAM = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(Verdant.MOD_ID, "small_beam"),
            BlockEntityType.Builder.create(SmallBeamBlockEntity::new, 
                    ModBlocks.OAK_SMALL_BEAM,
                    ModBlocks.SPRUCE_SMALL_BEAM,
                    ModBlocks.BIRCH_SMALL_BEAM,
                    ModBlocks.JUNGLE_SMALL_BEAM,
                    ModBlocks.ACACIA_SMALL_BEAM,
                    ModBlocks.DARK_OAK_SMALL_BEAM,
                    ModBlocks.MANGROVE_SMALL_BEAM,
                    ModBlocks.CRIMSON_SMALL_BEAM,
                    ModBlocks.WARPED_SMALL_BEAM
            ).build(null));

    public static void register() {
        // Block entities are registered via static initializers above
    }
}