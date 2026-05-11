package de.hallotheengineer.f5ranger.mixin;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void move(float forwards, float up, float right);
    @Shadow protected abstract float getMaxZoom(float cameraDist);
    @Shadow private boolean detached;
    @Shadow private Entity entity;
    @Shadow @Final private Minecraft minecraft;

    @Unique private float currentVisualDistance = 4.0F;

    /**
     * Redirects the movement call in alignWithEntity that handles third-person distance.
     * In the target version, vanilla calculates cameraScale and cameraDistance (via attributes).
     */
    @Redirect(
            method = "alignWithEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;move(FFF)V")
    )
    private void redirectMove(Camera instance, float forwards, float up, float right) {
        if (this.detached) {
            boolean isMirrored = this.minecraft.options.getCameraType().isMirrored();

            if (isMirrored && F5RangerClient.config.disableSecondPerson) {
                this.move(forwards, up, right);
                return;
            }

            float targetDistance = F5RangerClient.config.cameraDistance;

            if (F5RangerClient.config.smoothCamera) {
                currentVisualDistance = Mth.lerp(F5RangerClient.config.lerpSpeed, currentVisualDistance, targetDistance);
            } else {
                currentVisualDistance = targetDistance;
            }

            float cameraScale = 1.0F;
            float attributeModifier = 1.0F;

            if (this.entity instanceof LivingEntity living) {
                cameraScale = living.getScale();
                float vanillaAttr = (float) living.getAttributeValue(Attributes.CAMERA_DISTANCE);
                attributeModifier = vanillaAttr / 4.0F;
            }

            if (this.entity.isPassenger() && this.entity.getVehicle() instanceof LivingEntity mount) {
                cameraScale = Math.max(cameraScale, mount.getScale());
                float mountAttr = (float) mount.getAttributeValue(Attributes.CAMERA_DISTANCE);
                attributeModifier = Math.max(attributeModifier, mountAttr / 4.0F);
            }

            float finalDistance = currentVisualDistance * cameraScale * attributeModifier;

            float appliedDistance = F5RangerClient.canNoClip() ? finalDistance : this.getMaxZoom(finalDistance);

            this.move(-appliedDistance, 0.0F, 0.0F);
        } else {
            currentVisualDistance = 4.0F;
            this.move(forwards, up, right);
        }
    }
}