package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.config.BettasModConfig;

@SuppressWarnings("ALL")
public class BettaWorldInit {

    public static ConfiguredFeature<?, ?> MOSS_BALL;
    public static PlacedFeature MOSS_BALLS;

    public static void registerSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.SWAMP), SpawnGroup.WATER_AMBIENT, BettaEntityInit.BETTA_FISH, BettasModConfig.get().spawnWeight, BettasModConfig.get().minGroupSize, BettasModConfig.get().maxGroupSize);

        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - World Spawns Registered");
        }
    }

    public static void registerFeatures() {
        MOSS_BALL = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Bettas.MODID, "moss_ball"),
                Feature.ORE.configure(new OreFeatureConfig(
                        new TagMatchRuleTest(BlockTags.UNDERWATER_BONEMEALS), // The blocks this ore can replace.
                        BettaBlocksInit.MOSS_BALL.getDefaultState(), // The ore block to place.
                        35, // The size of the vein. Do not do less than 3 or else it places nothing.
                        0f // % of exposed ore block will not generate if touching air.
                )));

        MOSS_BALLS = Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(Bettas.MODID, "moss_balls"),
                MOSS_BALL.withPlacement(
                        CountPlacementModifier.of(50),
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                YOffset.aboveBottom(40),
                                YOffset.belowTop(110)),
                        BiomePlacementModifier.of())
        );

        BiomeModifications.create(new Identifier(Bettas.MODID, "add_moss_balls")).add(
                ModificationPhase.ADDITIONS,
                (context) -> {
                    Biome.Category category = context.getBiome().getCategory();
                    return category != Biome.Category.NETHER && category != Biome.Category.THEEND && category != Biome.Category.NONE && category == Biome.Category.SWAMP;
                },
                context -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.VEGETAL_DECORATION, MOSS_BALLS));


        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - World Gen Registered");
        }
    }
}
