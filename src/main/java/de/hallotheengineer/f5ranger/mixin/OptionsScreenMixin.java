package de.hallotheengineer.f5ranger.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.hallotheengineer.f5ranger.F5RangerClient;
import de.hallotheengineer.f5ranger.gui.DistanceSlider;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addToContents(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"
            )
    )
    private void addCameraSlider(CallbackInfo ci, @Local GridLayout.RowHelper helper) {
        if (!F5RangerClient.config.showUISlider) return;

        helper.addChild(new DistanceSlider(0, 0, 150, 20), 2);
    }
}
