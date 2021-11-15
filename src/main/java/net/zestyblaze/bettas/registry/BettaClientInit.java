package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.client.model.BettaFishModel;
import net.zestyblaze.bettas.client.renderer.BettaFishRenderer;

public class BettaClientInit {
    public static final EntityModelLayer BETTA_FISH_MODEL = new EntityModelLayer(new Identifier(Bettas.MODID, "betta_fish"), "betta_fish_model");

    public static void registerClient() {
        EntityRendererRegistry.register(BettaEntityInit.BETTA_FISH, BettaFishRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(BETTA_FISH_MODEL, BettaFishModel::getTexturedModelData);
    }
}
