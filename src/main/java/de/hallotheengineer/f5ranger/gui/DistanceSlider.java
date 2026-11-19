package de.hallotheengineer.f5ranger.gui;

import de.hallotheengineer.f5ranger.F5RangerClient;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class DistanceSlider extends AbstractSliderButton {
    public DistanceSlider(int x, int y, int width, int height) {
        super(x, y, width, height, createMessage(), createValue());
    }

    @Override
    protected void updateMessage() {
        this.setMessage(createMessage());
    }

    @Override
    protected void applyValue() {
        float min = F5RangerClient.config.minDistance;
        float max = F5RangerClient.config.maxDistance;
        F5RangerClient.config.cameraDistance = min + (float) (this.value * (max - min));
    }

    private static Component createMessage() {
        return Component.literal("Camera Distance: " + String.format("%.1f", F5RangerClient.config.cameraDistance));
    }

    private static double createValue() {
        float min = F5RangerClient.config.minDistance;
        float max = F5RangerClient.config.maxDistance;
        return Math.clamp((F5RangerClient.config.cameraDistance - min) / (max - min), 0.0f, 1.0f);
    }
}
