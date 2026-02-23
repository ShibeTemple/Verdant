package is.bradley.verdant;

import is.bradley.verdant.particle.ModParticles;
import is.bradley.verdant.registry.*;
import is.bradley.verdant.registry.ModBlockEntities;
import is.bradley.verdant.registry.ModBlocks;
import is.bradley.verdant.registry.ModEntities;
import is.bradley.verdant.registry.ModItems;
import is.bradley.verdant.registry.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Verdant implements ModInitializer {
    public static final String MOD_ID = "verdant";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Load config (creates defaults if missing)
        VerdantConfig.load();
        // Register sounds first (no dependencies)
        ModSounds.register();
        // Register entity types (depends on sounds, entity classes)
        ModEntities.register();
        // Register items and blocks (spawn egg depends on entity types)
        ModItems.register();
        ModItems.registerSpawnEgg();
        ModBlocks.register();
        ModBlockEntities.register();
        ModParticles.register();

        LOGGER.info("Verdant initialized!");
    }
}