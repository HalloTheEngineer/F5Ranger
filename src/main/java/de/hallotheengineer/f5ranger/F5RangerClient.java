package de.hallotheengineer.f5ranger;

import de.hallotheengineer.f5ranger.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class F5RangerClient implements ClientModInitializer {
    public static ModConfig config;
    public static final String MOD_ID = "f5ranger";

    private static final float DEFAULT_VANILLA_DISTANCE = 4.0f;

    public static KeyBinding modifierKey;

    @Override
    public void onInitializeClient() {
        modifierKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.f5ranger.modifier",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "key.category.minecraft." + MOD_ID
        ));

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            AutoConfig.getConfigHolder(ModConfig.class).save();
        });
    }

    public static void adjustDistance(double verticalAmount) {
        float currentDist = config.cameraDistance;

        // Formula: base_step * (current_distance / 4.0)
        // Causes the cam distance to change exponentially
        float speedMultiplier = Math.max(0.5f, currentDist / DEFAULT_VANILLA_DISTANCE);

        float delta = (float) (verticalAmount * config.scrollStep * speedMultiplier);

        config.cameraDistance = Math.clamp(
                config.cameraDistance - delta,
                config.minDistance,
                config.maxDistance
        );
    }
}
