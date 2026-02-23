package is.bradley.verdant.client.render;

import is.bradley.verdant.client.model.CockroachEntityModel;
import is.bradley.verdant.client.model.SombreroModel;
import is.bradley.verdant.client.render.layer.CockroachMaracasLayer;
import is.bradley.verdant.entity.CockroachEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CockroachEntityRenderer extends MobEntityRenderer<CockroachEntity, CockroachEntityModel> {
    private static final Identifier TEXTURE = new Identifier("verdant", "textures/entity/cockroach.png");

    public static final EntityModelLayer COCKROACH_LAYER =
            new EntityModelLayer(new Identifier("verdant", "cockroach"), "main");
    public static final EntityModelLayer SOMBRERO_LAYER =
            new EntityModelLayer(new Identifier("verdant", "sombrero"), "main");

    public CockroachEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CockroachEntityModel(context.getPart(COCKROACH_LAYER)), 0.3F);
        SombreroModel sombrero = new SombreroModel(context.getPart(SOMBRERO_LAYER));
        this.addFeature(new CockroachMaracasLayer(this, sombrero));
    }

    @Override
    public Identifier getTexture(CockroachEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(CockroachEntity entity, MatrixStack matrices, float tickDelta) {
        matrices.scale(0.85F, 0.85F, 0.85F);
    }
}
