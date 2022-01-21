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

    @Comment("The spawn weight for the Betta fish to spawn")
    public int spawnWeight = 1;

    @Comment("The min group size that the Betta Fish to spawn in")
    public int minGroupSize = 1;

    @Comment("The max group size that the Betta Fish spawn in. Higher values mean more lag")
    public int maxGroupSize = 2;

    public static BettasModConfig get() {
        return AutoConfig.getConfigHolder(BettasModConfig.class).getConfig();
    }
}
