package de.hallotheengineer.f5ranger;

import de.hallotheengineer.f5ranger.networking.ModPackets;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class F5RangerServer implements DedicatedServerModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("[F5Ranger]");
    @Override
    public void onInitializeServer() {
        PayloadTypeRegistry.playS2C().register(
                ModPackets.NoClipAllowPayload.ID,
                ModPackets.NoClipAllowPayload.CODEC
        );

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                ServerPlayNetworking.send(handler.getPlayer(), new ModPackets.NoClipAllowPayload()));

        LOGGER.info("Clients are now allowed to use NoClip via F5Ranger.");
    }
}
