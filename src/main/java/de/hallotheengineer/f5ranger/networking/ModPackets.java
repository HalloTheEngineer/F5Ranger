package de.hallotheengineer.f5ranger.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static de.hallotheengineer.f5ranger.F5RangerClient.MOD_ID;

public class ModPackets {
    public record NoClipAllowPayload() implements CustomPayload {
        public static final Id<NoClipAllowPayload> ID = new Id<>(Identifier.of(MOD_ID, "allow_noclip"));
        public static final PacketCodec<PacketByteBuf, NoClipAllowPayload> CODEC = PacketCodec.unit(new NoClipAllowPayload());

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
