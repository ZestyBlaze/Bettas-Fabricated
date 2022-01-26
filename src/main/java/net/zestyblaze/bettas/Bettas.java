package net.zestyblaze.bettas;

import net.fabricmc.api.ModInitializer;
import net.zestyblaze.bettas.config.BettasModConfig;
import net.zestyblaze.bettas.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bettas implements ModInitializer {
    public static final String MODID = "bettas";
    public static final String MODNAME = "Bettas";

    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    @Override
    public void onInitialize() {
        LOGGER.info(MODNAME + " is installed correctly, loading now! Thanks for installing! <3");
        BettaConfigInit.registerConfig();
        BettaBlocksInit.registerBlocks();
        BettaItemsInit.registerItems();
        BettaEntityInit.registerEntities();
        BettaWorldInit.registerSpawns();
        BettaWorldInit.registerFeatures();

        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - Mod Fully Loaded");
        }
    }
}
