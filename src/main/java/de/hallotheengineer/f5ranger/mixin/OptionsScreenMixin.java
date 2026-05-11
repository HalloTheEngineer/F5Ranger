package de.hallotheengineer.f5ranger.mixin;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    @Final
    private HeaderAndFooterLayout layout;

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addToFooter(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"
            )
    )
    private void addCameraSlider(CallbackInfo ci) {
        if (!F5RangerClient.config.showUISlider) return;

        AbstractSliderButton slider = new AbstractSliderButton(
                0, 0,
                150, 20,
                getSliderMessage(),
                getSliderValue()) {

            @Override
            protected void updateMessage() {
                this.setMessage(getSliderMessage());
            }

            @Override
            protected void applyValue() {
                float min = F5RangerClient.config.minDistance;
                float max = F5RangerClient.config.maxDistance;
                F5RangerClient.config.cameraDistance = min + (float) (this.value * (max - min));
            }
        };

        layout.addToFooter(slider);
    }

    @Unique
    private Component getSliderMessage() {
        return Component.literal("Camera Distance: " + String.format("%.1f", F5RangerClient.config.cameraDistance));
    }

    @Unique
    private double getSliderValue() {
        float min = F5RangerClient.config.minDistance;
        float max = F5RangerClient.config.maxDistance;
        return Math.clamp((F5RangerClient.config.cameraDistance - min) / (max - min), 0.0f, 1.0f);
    }
}