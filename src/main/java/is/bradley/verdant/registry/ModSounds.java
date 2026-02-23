package is.bradley.verdant.registry;

import is.bradley.verdant.Verdant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent COCKROACH_HURT = register("cockroach_hurt");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(Verdant.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        // triggers static init
    }
}
