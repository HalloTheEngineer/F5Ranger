package de.hallotheengineer.f5ranger.mixin;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void addCameraSlider(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder) {
        if (!F5RangerClient.config.showUISlider) return;

        SliderWidget slider = new SliderWidget(
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

        adder.add(slider, 2, adder.copyPositioner().marginTop(4).alignHorizontalCenter());
    }

    @Unique
    private Text getSliderMessage() {
        return Text.literal("Camera Distance: " + String.format("%.1f", F5RangerClient.config.cameraDistance));
    }

    @Unique
    private double getSliderValue() {
        float min = F5RangerClient.config.minDistance;
        float max = F5RangerClient.config.maxDistance;
        return MathHelper.clamp((F5RangerClient.config.cameraDistance - min) / (max - min), 0.0, 1.0);
    }
}