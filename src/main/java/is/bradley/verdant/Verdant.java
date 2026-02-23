package is.bradley.verdant;

import is.bradley.verdant.particle.ModParticles;
import is.bradley.verdant.registry.*;
import is.bradley.verdant.registry.ModBlockEntities;
import is.bradley.verdant.registry.ModBlocks;
import is.bradley.verdant.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Verdant implements ModInitializer {
    public static final String MOD_ID = "verdant";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Register items and blocks
        ModItems.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModParticles.register();
        
        LOGGER.info("Verdant initialized!");
    }
}