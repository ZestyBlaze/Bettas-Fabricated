package net.zestyblaze.bettas.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.zestyblaze.bettas.entity.BettaFishEntity;

public class BettaFishModel extends EntityModel<BettaFishEntity> {
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart finTop;
    private final ModelPart finBottom;
    private final ModelPart finLeftBottom;
    private final ModelPart finRightBottom;
    private final ModelPart finLeft;
    private final ModelPart finRight;

    public BettaFishModel(ModelPart root) {
        this.body = root.getChild("body");
        this.finLeftBottom = this.body.getChild("finLeftBottom");
        this.tail = this.body.getChild("tail");
        this.finRight = this.body.getChild("finRight");
        this.finTop = this.body.getChild("finTop");
        this.finRightBottom = this.body.getChild("finRightBottom");
        this.finBottom = this.body.getChild("finBottom");
        this.finLeft = this.body.getChild("finLeft");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData1 = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 7.0F), ModelTransform.pivot(0.0F, 22.5F, -0.5F));
        modelPartData1.addChild("finLeftBottom", ModelPartBuilder.create().uv(11, 9).cuboid(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 2.0F), ModelTransform.pivot(0.5F, 1.5F, -0.5F));
        modelPartData1.addChild("tail", ModelPartBuilder.create().uv(0, 6).cuboid(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 4.0F), ModelTransform.pivot(0.0F, 0.0F, 3.5F));
        modelPartData1.addChild("finRight", ModelPartBuilder.create().uv(-1, 1).cuboid(-2.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F), ModelTransform.pivot(-1.0F, 0.5F, -0.5F));
        modelPartData1.addChild("finTop", ModelPartBuilder.create().uv(0, 1).cuboid(0.0F, -3.0F, -1.5F, 0.0F, 3.0F, 3.0F), ModelTransform.pivot(0.0F, -1.5F, 2.0F));
        modelPartData1.addChild("finRightBottom", ModelPartBuilder.create().uv(11, 9).cuboid(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 2.0F), ModelTransform.pivot(-0.5F, 1.5F, -0.5F));
        modelPartData1.addChild("finBottom", ModelPartBuilder.create().uv(11, -6).cuboid(0.0F, 0.0F, -3.0F, 0.0F, 2.0F, 6.0F), ModelTransform.pivot(0.0F, 1.5F, 0.5F));
        modelPartData1.addChild("finLeft", ModelPartBuilder.create().uv(-1, 3).cuboid(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, true), ModelTransform.pivot(1.0F, 0.5F, -0.5F));
        return TexturedModelData.of(modelData, 32, 16);
    }

    @Override
    public void setAngles(BettaFishEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.finLeftBottom.roll = -0.34906584f;
        this.finRight.roll = -0.7853982f;
        this.finRightBottom.roll = 0.34906584f;
        this.finBottom.yaw = 3.1415927f;
        this.finLeft.roll = 0.7853982f;

        float f = 1.0f;
        if (!entity.isSubmergedInWater()) {
            f = 1.5f;
        }
        this.tail.yaw = -f * 0.45f * MathHelper.sin(0.6f * animationProgress);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.body).forEach((modelPart -> modelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha)));
    }
}
