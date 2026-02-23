package is.bradley.verdant.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BoneShieldRenderer implements BuiltinItemRenderer {
    private static final Identifier TEXTURE =
            new Identifier("verdant", "textures/entity/shields/bone.png");

    private ShieldEntityModel model;

    @Override
    public void render(ItemStack stack, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (model == null) {
            model = new ShieldEntityModel(
                    MinecraftClient.getInstance().getEntityModelLoader()
                            .getModelPart(EntityModelLayers.SHIELD));
        }
        matrices.push();
        matrices.scale(1.0f, -1.0f, -1.0f);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(TEXTURE));
        model.render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        matrices.pop();
    }
}