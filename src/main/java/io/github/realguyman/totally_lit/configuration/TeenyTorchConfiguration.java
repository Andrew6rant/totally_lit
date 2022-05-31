package io.github.realguyman.totally_lit.configuration;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "teeny_torches")
public class TeenyTorchConfiguration implements ConfigData {
    @ConfigEntry.BoundedDiscrete(min = 1, max = 4 * 7)
    @ConfigEntry.Gui.Tooltip
    public int burnDuration = 2;

    public float extinguishInRainChance = 0.35F;

    public boolean extinguishOverTime = true;
}
