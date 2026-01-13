package de.hallotheengineer.f5ranger.mixin;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void moveBy(float surge, float heave, float sway);
    @Shadow protected abstract float clipToSpace(float f);
    @Shadow private boolean thirdPerson;
    @Shadow private Entity focusedEntity;

    @Unique private float currentVisualDistance = 4.0F;
    @Unique private boolean capturedInverseView;

    @Inject(method = "update", at = @At("HEAD"))
    private void captureUpdateParams(World area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickProgress, CallbackInfo ci) {
        this.capturedInverseView = inverseView;
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V"))
    private void redirectMoveBy(Camera instance, float surge, float heave, float sway) {
        if (this.thirdPerson) {
            if (this.capturedInverseView && F5RangerClient.config.disableSecondPerson) {
                this.moveBy(surge, heave, sway);
                return;
            }

            float targetDistance = F5RangerClient.config.cameraDistance;

            if (F5RangerClient.config.smoothCamera) {
                currentVisualDistance = MathHelper.lerp(F5RangerClient.config.lerpSpeed, currentVisualDistance, targetDistance);
            } else {
                currentVisualDistance = targetDistance;
            }

            float scale = (this.focusedEntity instanceof LivingEntity living) ? living.getScale() : 1.0F;
            float finalDistance = currentVisualDistance * scale;

            float appliedDistance = F5RangerClient.canNoClip() ? finalDistance : this.clipToSpace(finalDistance);

            this.moveBy(-appliedDistance, 0.0F, 0.0F);
        } else {
            currentVisualDistance = 4.0F;
            this.moveBy(surge, heave, sway);
        }
    }
}