package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.client.model.BettaFishModel;
import net.zestyblaze.bettas.client.renderer.BettaFishRenderer;
import net.zestyblaze.bettas.config.BettasModConfig;

public class BettaClientInit {
    public static final EntityModelLayer BETTA_FISH_MODEL = new EntityModelLayer(new Identifier(Bettas.MODID, "betta_fish"), "betta_fish_model");

    public static void registerClientEntityRenders() {
        EntityRendererRegistry.register(BettaEntityInit.BETTA_FISH, BettaFishRenderer::new);

        if(BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: ClientRegistry - Renders Registered");
        }
    }

    public static void registerClientEntityModels() {
        EntityModelLayerRegistry.registerModelLayer(BETTA_FISH_MODEL, BettaFishModel::getTexturedModelData);

        if(BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: ClientRegistry - Models Registered");
        }
    }

    public static void registerClientRenderOverlays() {
        BlockRenderLayerMap.INSTANCE.putBlock(BettaBlocksInit.DRIED_LEAVES, RenderLayer.getCutout());

        if(BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: ClientRegistry - Overlays Registered");
        }
    }
}
