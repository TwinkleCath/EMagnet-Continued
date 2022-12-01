package net.twinklecath.emagnet.registry;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.twinklecath.emagnet.EMagnet;
import net.twinklecath.emagnet.EMagnetConfig;
import net.twinklecath.emagnet.blocks.MagnetJarBlock;


public class ModBlocks {
    private static EMagnetConfig config = AutoConfig.getConfigHolder(EMagnetConfig.class).getConfig();

    public static final Block MAGNET_JAR = new MagnetJarBlock(FabricBlockSettings.of(Material.GLASS).strength(0.5f, 500.0f)
            .sounds(BlockSoundGroup.GLASS).luminance(10).nonOpaque());

    public static void registerBlocks() {
        if (!config.blocks.disable_magnet_jar){
            Registry.register(Registry.BLOCK, new Identifier(EMagnet.MOD_ID, "magnet_jar"), MAGNET_JAR);
        }
    }
}
