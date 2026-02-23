package is.bradley.verdant.particle;

import is.bradley.verdant.Verdant;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final DefaultParticleType FIERY_GLOW = FabricParticleTypes.simple();
    public static final DefaultParticleType FIERY_SPARK = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Verdant.MOD_ID, "fiery_glow"), FIERY_GLOW);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Verdant.MOD_ID, "fiery_spark"), FIERY_SPARK);
    }

    public static void registerClient() {
        // Particle factory registration is handled via client registration
    }
}