package is.bradley.verdant.registry;

import is.bradley.verdant.Verdant;
import is.bradley.verdant.entity.CockroachEggEntity;
import is.bradley.verdant.entity.CockroachEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<CockroachEntity> COCKROACH = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Verdant.MOD_ID, "cockroach"),
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, CockroachEntity::new)
                    .dimensions(EntityDimensions.fixed(0.7f, 0.3f))
                    .trackRangeBlocks(5)
                    .build()
    );

    public static final EntityType<CockroachEggEntity> COCKROACH_EGG = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Verdant.MOD_ID, "cockroach_egg"),
            FabricEntityTypeBuilder.<CockroachEggEntity>create(SpawnGroup.MISC, CockroachEggEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .trackRangeBlocks(4)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(COCKROACH, CockroachEntity.createCockroachAttributes());
        SpawnRestriction.register(COCKROACH,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                CockroachEntity::canSpawn);
    }
}
