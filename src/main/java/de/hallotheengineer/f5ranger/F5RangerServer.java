package de.hallotheengineer.f5ranger;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class F5RangerServer implements DedicatedServerModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("[F5Ranger]");
    @Override
    public void onInitializeServer() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayNetworking.send(
                    handler.player,
                    F5RangerClient.ALLOW_NOCLIP_ID,
                    PacketByteBufs.empty()
            );
        });

        LOGGER.info("Clients are now allowed to use NoClip via F5Ranger.");
    }
}
