package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.config.BettasModConfig;

@SuppressWarnings("ALL")
public class BettaWorldInit {

    public static void registerSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.SWAMP), SpawnGroup.WATER_AMBIENT, BettaEntityInit.BETTA_FISH, BettasModConfig.get().spawnWeight, BettasModConfig.get().minGroupSize, BettasModConfig.get().maxGroupSize);

        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - World Spawns Registered");
        }
    }

    public static void registerFeatures() {

        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - World Gen Registered");
        }
    }
}
