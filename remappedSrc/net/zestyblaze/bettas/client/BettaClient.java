package net.zestyblaze.bettas.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.zestyblaze.bettas.registry.BettaClientInit;

@Environment(EnvType.CLIENT)
public class BettaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BettaClientInit.registerClientEntityRenders();
        BettaClientInit.registerClientEntityModels();
        BettaClientInit.registerClientRenderOverlays();
    }
}
