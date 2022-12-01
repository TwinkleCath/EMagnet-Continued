package net.twinklecath.emagnet.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.twinklecath.emagnet.EMagnet;
import net.twinklecath.emagnet.blocks.blockentities.MagnetJarBlockEntity;

public class ModBlockEntityTypes {

    public static BlockEntityType<MagnetJarBlockEntity> MAGNET_JAR = FabricBlockEntityTypeBuilder.create(MagnetJarBlockEntity::new, ModBlocks.MAGNET_JAR).build(null);

    public static void registerBlockEntityTypes() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(EMagnet.MOD_ID, "magnet_jar"), MAGNET_JAR);
    }
}
