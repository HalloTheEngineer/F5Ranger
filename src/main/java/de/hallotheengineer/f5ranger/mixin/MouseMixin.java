package de.hallotheengineer.f5ranger.mixin;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (F5RangerClient.modifierKey != null && F5RangerClient.modifierKey.isPressed()) {
            if (client.options.getPerspective().isFirstPerson()) {
                return;
            }

            F5RangerClient.adjustDistance(vertical);

            // anti-hotbar-scroll
            ci.cancel();
        }
    }
}
