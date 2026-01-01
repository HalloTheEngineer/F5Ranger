package de.hallotheengineer.f5ranger.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "f5ranger")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public float cameraDistance = 4.0f;

    @ConfigEntry.Gui.Tooltip
    public float minDistance = 1.0f;

    @ConfigEntry.Gui.Tooltip
    public float maxDistance = 50.0f;

    @ConfigEntry.Gui.Tooltip
    public float scrollStep = 0.75f;

    @ConfigEntry.Gui.Tooltip
    public float lerpSpeed = 0.05f;

    @ConfigEntry.Gui.Tooltip
    public boolean smoothCamera = true;

    @ConfigEntry.Gui.Tooltip
    public boolean noClip = false;

    @ConfigEntry.Gui.Tooltip
    public boolean disableSecondPerson = false;
}
