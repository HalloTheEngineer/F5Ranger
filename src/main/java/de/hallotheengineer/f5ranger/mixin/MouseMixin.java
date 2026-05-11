package de.hallotheengineer.f5ranger.mixin;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();

        if (F5RangerClient.modifierKey != null && F5RangerClient.modifierKey.isDown()) {
            if (client.options.getCameraType().isFirstPerson()) {
                return;
            }

            F5RangerClient.adjustDistance(yoffset);

            // anti-hotbar-scroll
            ci.cancel();
        }
    }
}
