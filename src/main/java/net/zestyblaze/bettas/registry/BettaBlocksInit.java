package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.block.MossBallBlock;
import net.zestyblaze.bettas.config.BettasModConfig;

public class BettaBlocksInit {

    public static final Block MOSS_BALL = new MossBallBlock(FabricBlockSettings.of(Material.DECORATION, MapColor.GREEN).sounds(BlockSoundGroup.WET_GRASS).strength(0).noCollision());
    public static final Block MOSS_BALL_BLOCK = new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC, MapColor.GREEN).sounds(BlockSoundGroup.WET_GRASS).strength(0.5f));

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(Bettas.MODID, "moss_ball"), MOSS_BALL);
        Registry.register(Registry.BLOCK, new Identifier(Bettas.MODID, "moss_ball_block"), MOSS_BALL_BLOCK);

        if(BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - Blocks Registered");
        }
    }

}
