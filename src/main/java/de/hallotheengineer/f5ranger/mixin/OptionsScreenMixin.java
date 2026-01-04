package de.hallotheengineer.f5ranger.mixin;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void addCameraSlider(CallbackInfo ci) {
        if (!F5RangerClient.config.showUISlider) return;

        int buttonWidth = 150;
        int x = this.width / 2 - (buttonWidth / 2);

        int y = this.height / 6 + 144;

        SliderWidget slider = new SliderWidget(
                x, y,
                buttonWidth, 20,
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

        this.addDrawableChild(slider);
    }

    @Unique
    private Text getSliderMessage() {
        return new LiteralText("Camera Distance: " + String.format("%.1f", F5RangerClient.config.cameraDistance));
    }

    @Unique
    private double getSliderValue() {
        float min = F5RangerClient.config.minDistance;
        float max = F5RangerClient.config.maxDistance;
        return MathHelper.clamp((F5RangerClient.config.cameraDistance - min) / (max - min), 0.0, 1.0);
    }
}