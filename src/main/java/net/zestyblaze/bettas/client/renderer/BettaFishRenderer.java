package net.zestyblaze.bettas.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.client.model.BettaFishModel;
import net.zestyblaze.bettas.entity.BettaFishEntity;
import net.zestyblaze.bettas.registry.BettaClientInit;

public class BettaFishRenderer extends MobEntityRenderer<BettaFishEntity, BettaFishModel> {
    private static final Identifier[] TEXTURES = new Identifier[BettaFishEntity.MAX_VARIANTS];

    public BettaFishRenderer(EntityRendererFactory.Context context) {
        super(context, new BettaFishModel(context.getPart(BettaClientInit.BETTA_FISH_MODEL)), 0.2f);
    }

    @Override
    public Identifier getTexture(BettaFishEntity entity) {
        int variant = entity.getVariant();
        if (TEXTURES[variant] == null) {
            Identifier loc = new Identifier(Bettas.MODID, "textures/entity/betta/body_" + variant + ".png");
            if (!MinecraftClient.getInstance().getResourceManager().containsResource(loc)) {
                Bettas.LOGGER.warn("Found unknown variant" + variant + ", using default");
                loc = new Identifier(Bettas.MODID, "textures/entity/betta/body_0.png");
            }
            return TEXTURES[variant] = loc;
        }
        return TEXTURES[variant];
    }

    @Override
    protected void setupTransforms(BettaFishEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        float f = 4.3f * MathHelper.sin(0.6f * animationProgress);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f));
        if (!entity.isSubmergedInWater()) {
            matrices.translate(0.2d, 0.1d, 0);
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0f));
        }
    }
}
