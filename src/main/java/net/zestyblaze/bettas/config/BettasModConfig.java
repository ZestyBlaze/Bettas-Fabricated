package net.zestyblaze.bettas.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.zestyblaze.bettas.Bettas;

@Config(name = Bettas.MODID)
public class BettasModConfig implements ConfigData {

    @Comment("Enables debug mode. NOTE: SPAMS THE CONSOLE")
    public boolean debugMode = false;

    public static BettasModConfig get() {
        return AutoConfig.getConfigHolder(BettasModConfig.class).getConfig();
    }
}
