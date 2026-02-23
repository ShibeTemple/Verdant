package is.bradley.verdant.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Simple sombrero hat model for the dancing cockroach.
 * Crown: uv(0,64) box(-4,-11,-4, 8,6,8)
 * Brim:  uv(22,73) box(-11,-8,-11, 22,3,22)
 * Texture size: 128x128 (matches alexsmobs sombrero.png)
 */
public class SombreroModel {
    private final net.minecraft.client.model.ModelPart root;
    private final net.minecraft.client.model.ModelPart crown;
    private final net.minecraft.client.model.ModelPart brim;

    public SombreroModel(net.minecraft.client.model.ModelPart root) {
        this.root  = root.getChild("sombrero_root");
        this.crown = this.root.getChild("crown");
        this.brim  = this.root.getChild("brim");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData partData = modelData.getRoot();

        ModelPartData sombreroRoot = partData.addChild("sombrero_root",
                ModelPartBuilder.create(),
                ModelTransform.NONE);

        sombreroRoot.addChild("crown",
                ModelPartBuilder.create().uv(0, 64).cuboid(-4.0F, -11.0F, -4.0F, 8, 6, 8),
                ModelTransform.NONE);

        sombreroRoot.addChild("brim",
                ModelPartBuilder.create().uv(22, 73).cuboid(-11.0F, -8.0F, -11.0F, 22, 3, 22),
                ModelTransform.NONE);

        return TexturedModelData.of(modelData, 128, 128);
    }

    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        root.render(matrices, vertices, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
