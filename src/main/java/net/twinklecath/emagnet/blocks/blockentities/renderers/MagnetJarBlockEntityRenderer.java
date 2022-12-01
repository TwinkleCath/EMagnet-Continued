package net.twinklecath.emagnet.blocks.blockentities.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;
import net.twinklecath.emagnet.blocks.blockentities.MagnetJarBlockEntity;

public class MagnetJarBlockEntityRenderer implements BlockEntityRenderer<MagnetJarBlockEntity> {

    public MagnetJarBlockEntityRenderer(BlockEntityRendererFactory.Context dispatcher) {}

    @Override
    public void render(MagnetJarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getStack(0)!=ItemStack.EMPTY){
            matrices.push();

            matrices.translate(0.5, 0.25, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime() + tickDelta) * 4));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);

            matrices.pop();
        }
    }
}