package de.hallotheengineer.f5ranger;

import de.hallotheengineer.f5ranger.config.ModConfig;
import de.hallotheengineer.f5ranger.networking.ModPackets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class F5RangerClient implements ClientModInitializer {
    public static ModConfig config;
    public static final String MOD_ID = "f5ranger";

    private static final float DEFAULT_VANILLA_DISTANCE = 4.0f;

    public static KeyBinding modifierKey;
    public static KeyBinding.Category keyCategory = KeyBinding.Category.create(Identifier.of(MOD_ID));
    public static boolean serverAllowsNoClip = false;

    @Override
    public void onInitializeClient() {
        modifierKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.f5ranger.modifier",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                keyCategory
        ));

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        PayloadTypeRegistry.playS2C().register(ModPackets.NoClipAllowPayload.ID, ModPackets.NoClipAllowPayload.CODEC);

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            AutoConfig.getConfigHolder(ModConfig.class).save();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> serverAllowsNoClip = false);
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.NoClipAllowPayload.ID, (payload, context) ->
                context.client().execute(() -> serverAllowsNoClip = true));
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
    public static boolean canNoClip() {
        return config.noClip && (MinecraftClient.getInstance().isInSingleplayer() || serverAllowsNoClip);
    }
}
