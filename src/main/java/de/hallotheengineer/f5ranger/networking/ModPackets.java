package de.hallotheengineer.f5ranger.networking;


import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class ModPackets {
    public record NoClipAllowPayload() implements CustomPacketPayload {
        public static final Type<NoClipAllowPayload> TYPE = new Type<>(
                Identifier.fromNamespaceAndPath(F5RangerClient.MOD_ID, "allow_noclip")
        );

        public static final StreamCodec<FriendlyByteBuf, NoClipAllowPayload> CODEC =
                StreamCodec.unit(new NoClipAllowPayload());

        @Override
        public @NonNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
