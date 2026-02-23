package is.bradley.verdant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class VerdantConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("verdant.json");

    private static VerdantConfig instance = new VerdantConfig();

    // ── Config fields ──────────────────────────────────────────────────────────

    /**
     * Controls how cockroaches obtain maracas.
     * <ul>
     *   <li>{@code false} (default) — cockroaches can only be given a maraca by a player
     *       right-clicking them; they will not pick up dropped maracas from the ground.</li>
     *   <li>{@code true} — cockroaches will autonomously pick up dropped maracas
     *       from the ground as well.</li>
     * </ul>
     */
    public boolean cockroachMaracaPickup = false;

    // ── Load / save ────────────────────────────────────────────────────────────

    public static VerdantConfig get() {
        return instance;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                instance = GSON.fromJson(reader, VerdantConfig.class);
                if (instance == null) {
                    instance = new VerdantConfig();
                }
            } catch (IOException e) {
                Verdant.LOGGER.error("Failed to read verdant.json config, using defaults", e);
                instance = new VerdantConfig();
            }
        } else {
            instance = new VerdantConfig();
        }
        save(); // always write back so missing keys get added
    }

    private static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            Verdant.LOGGER.error("Failed to write verdant.json config", e);
        }
    }
}
