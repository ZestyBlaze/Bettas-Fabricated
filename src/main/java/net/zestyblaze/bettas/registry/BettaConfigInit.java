package net.zestyblaze.bettas.registry;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.config.BettasModConfig;

public class BettaConfigInit {
    public static void registerConfig() {
        AutoConfig.register(BettasModConfig.class, GsonConfigSerializer::new);

        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - Config Registered");
        }
    }
}
