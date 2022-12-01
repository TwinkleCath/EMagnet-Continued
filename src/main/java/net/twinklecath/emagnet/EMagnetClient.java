package net.twinklecath.emagnet;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.twinklecath.emagnet.blocks.blockentities.renderers.MagnetJarBlockEntityRenderer;
import net.twinklecath.emagnet.registry.ModBlockEntityTypes;
import net.twinklecath.emagnet.registry.ModBlocks;

public class EMagnetClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MAGNET_JAR, RenderLayer.getTranslucent());

        BlockEntityRendererRegistry.register(ModBlockEntityTypes.MAGNET_JAR, MagnetJarBlockEntityRenderer::new);
    }
}
