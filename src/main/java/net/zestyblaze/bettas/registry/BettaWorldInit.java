package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.HeightmapDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.config.BettasModConfig;
import net.zestyblaze.bettas.feature.MossBallFeature;

public class BettaWorldInit {
    public static void registerSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.SWAMP), SpawnGroup.WATER_AMBIENT, BettaEntityInit.BETTA_FISH, BettasModConfig.get().spawnWeight, BettasModConfig.get().minGroupSize, BettasModConfig.get().maxGroupSize);

        if(BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - World Spawns Registered");
        }
    }

    private static final Feature<CountConfig> MOSS_BALL = new MossBallFeature(CountConfig.CODEC);

    private static final ConfiguredFeature<?, ?> MOSS_BALLS = MOSS_BALL.configure(new CountConfig(ConstantIntProvider.create(5)))
            .decorate(Decorator.HEIGHTMAP.configure(new HeightmapDecoratorConfig(Heightmap.Type.OCEAN_FLOOR)))
            .spreadHorizontally()
            .applyChance(1);

    public static void registerFeatures() {
        RegistryKey<ConfiguredFeature<?, ?>> mossBalls = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(Bettas.MODID, "moss_balls"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, mossBalls.getValue(), MOSS_BALLS);

        Registry.register(Registry.FEATURE, new Identifier(Bettas.MODID, "moss_balls"), MOSS_BALL);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.SWAMP), GenerationStep.Feature.SURFACE_STRUCTURES, mossBalls);

        if(BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - World Features Registered");
        }
    }
}
