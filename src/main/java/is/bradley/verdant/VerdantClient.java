package is.bradley.verdant;

import is.bradley.verdant.client.model.CockroachEntityModel;
import is.bradley.verdant.client.model.SombreroModel;
import is.bradley.verdant.client.render.BoneShieldRenderer;
import is.bradley.verdant.client.render.CockroachEntityRenderer;
import is.bradley.verdant.particle.FieryGlowParticle;
import is.bradley.verdant.particle.FierySparkParticle;
import is.bradley.verdant.particle.ModParticles;
import is.bradley.verdant.registry.ModBlocks;
import is.bradley.verdant.registry.ModEntities;
import is.bradley.verdant.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class VerdantClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register render layers for transparent/cutout blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HEMP_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FIERY_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WALL_FIERY_TORCH, RenderLayer.getCutout());

        // Register bone shield built-in item renderer
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.BONE_SHIELD, new BoneShieldRenderer());

        // Register particle factories
        ParticleFactoryRegistry.getInstance().register(ModParticles.FIERY_GLOW, FieryGlowParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FIERY_SPARK, FierySparkParticle.Factory::new);

        // Register cockroach entity model layers
        EntityModelLayerRegistry.registerModelLayer(CockroachEntityRenderer.COCKROACH_LAYER,
                CockroachEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CockroachEntityRenderer.SOMBRERO_LAYER,
                SombreroModel::getTexturedModelData);

        // Register cockroach entity renderer
        EntityRendererRegistry.register(ModEntities.COCKROACH, CockroachEntityRenderer::new);
    }
}