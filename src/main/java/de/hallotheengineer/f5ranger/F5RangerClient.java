package de.hallotheengineer.f5ranger;

import com.mojang.blaze3d.platform.InputConstants;
import de.hallotheengineer.f5ranger.config.ModConfig;
import de.hallotheengineer.f5ranger.networking.ModPackets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class F5RangerClient implements ClientModInitializer {
    public static ModConfig config;
    public static final String MOD_ID = "f5ranger";

    private static final float DEFAULT_VANILLA_DISTANCE = 4.0f;

    public static KeyMapping modifierKey;
    public static KeyMapping.Category keyCategory = KeyMapping.Category.register(Identifier.parse(MOD_ID));
    public static boolean serverAllowsNoClip = false;

    @Override
    public void onInitializeClient() {
        modifierKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.f5ranger.modifier",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                keyCategory
        ));

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        PayloadTypeRegistry.clientboundPlay().register(ModPackets.NoClipAllowPayload.TYPE, ModPackets.NoClipAllowPayload.CODEC);

        ClientLifecycleEvents.CLIENT_STOPPING.register(_ -> AutoConfig.getConfigHolder(ModConfig.class).save());
        ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> serverAllowsNoClip = false);

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.NoClipAllowPayload.TYPE, (_, context) ->
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
        return config.noClip && (
                (Minecraft.getInstance().getSingleplayerServer() != null && Minecraft.getInstance().getSingleplayerServer().isSingleplayer())
                        || serverAllowsNoClip
        );
    }
}
