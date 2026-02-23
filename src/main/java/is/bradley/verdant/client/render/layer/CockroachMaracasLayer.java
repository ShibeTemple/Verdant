package is.bradley.verdant.client.render.layer;

import is.bradley.verdant.client.model.CockroachEntityModel;
import is.bradley.verdant.client.model.SombreroModel;
import is.bradley.verdant.entity.CockroachEntity;
import is.bradley.verdant.registry.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class CockroachMaracasLayer extends FeatureRenderer<CockroachEntity, CockroachEntityModel> {
    private static final Identifier SOMBRERO_TEX = new Identifier("verdant", "textures/armor/sombrero.png");

    private final SombreroModel sombreroModel;

    public CockroachMaracasLayer(FeatureRendererContext<CockroachEntity, CockroachEntityModel> context,
                                  SombreroModel sombreroModel) {
        super(context);
        this.sombreroModel = sombreroModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, CockroachEntity entity,
                       float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {
        if (!entity.hasMaracas()) return;

        ItemStack maraca = new ItemStack(ModItems.MARACA);

        matrices.push();
        if (entity.isBaby()) {
            matrices.scale(0.65F, 0.65F, 0.65F);
            matrices.translate(0.0, 0.815, 0.125);
        }

        // Left front leg – maraca
        renderMaraca(matrices, vertexConsumers, light, entity, maraca,
                0, -0.25F, 0.0F, 0, -90F, 60F);
        // Right front leg – maraca
        renderMaraca(matrices, vertexConsumers, light, entity, maraca,
                1,  0.25F, 0.0F, 0,  90F, -120F);
        // Left mid leg – maraca
        renderMaraca(matrices, vertexConsumers, light, entity, maraca,
                2, -0.35F, 0.0F, 0, -90F, 60F);
        // Right mid leg – maraca
        renderMaraca(matrices, vertexConsumers, light, entity, maraca,
                3,  0.35F, 0.0F, 0,  90F, -120F);

        // Sombrero on head
        if (!entity.isHeadless()) {
            matrices.push();
            translateToLimb(4, matrices); // go to head transform
            matrices.translate(0F, -0.4F, -0.01F);
            float danceProgress = entity.prevDanceProgress
                    + (entity.danceProgress - entity.prevDanceProgress) * tickDelta;
            matrices.translate(0F, danceProgress * 0.045F, danceProgress * -0.09F);
            matrices.scale(0.8F, 0.8F, 0.8F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(60F * danceProgress * 0.2F));
            VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(SOMBRERO_TEX));
            sombreroModel.render(matrices, vc, light,
                    LivingEntityRenderer.getOverlay(entity, 0.0F));
            matrices.pop();
        }

        matrices.pop();
    }

    private void renderMaraca(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                               int light, CockroachEntity entity, ItemStack stack,
                               int limbIndex, float offsetX, float offsetY, float offsetZ,
                               float rotX, float rotZ) {
        matrices.push();
        translateToLimb(limbIndex, matrices);
        matrices.translate(offsetX, offsetY, offsetZ);
        matrices.scale(1.4F, 1.4F, 1.4F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotX));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotZ));
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                entity, stack, ModelTransformationMode.GROUND, false,
                matrices, vertexConsumers, entity.getWorld(), light,
                OverlayTexture.DEFAULT_UV, entity.getId());
        matrices.pop();
    }

    /** Traverse the model hierarchy to a specific limb/part. */
    private void translateToLimb(int index, MatrixStack matrices) {
        CockroachEntityModel model = getContextModel();
        model.getPart().rotate(matrices);           // root
        model.abdomen.rotate(matrices);             // abdomen
        switch (index) {
            case 0 -> model.leftLegFront.rotate(matrices);
            case 1 -> model.rightLegFront.rotate(matrices);
            case 2 -> model.leftLegMid.rotate(matrices);
            case 3 -> model.rightLegMid.rotate(matrices);
            case 4 -> {
                model.neck.rotate(matrices);
                model.head.rotate(matrices);
            }
        }
    }
}
