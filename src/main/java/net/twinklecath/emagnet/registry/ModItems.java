package net.twinklecath.emagnet.registry;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.twinklecath.emagnet.EMagnet;
import net.twinklecath.emagnet.EMagnetConfig;
import net.twinklecath.emagnet.items.AdvancedMagnet;
import net.twinklecath.emagnet.items.BasicMagnet;

public class ModItems {
    private static EMagnetConfig config = AutoConfig.getConfigHolder(EMagnetConfig.class).getConfig();

    // Items
    public static final Item BASIC_MAGNET = new BasicMagnet(new FabricItemSettings().group(ModItemGroup.EMAGNET).maxCount(1),
            config.magnets.range_basic_magnet, config.magnets.capacity_basic_magnet, 32);
    public static final Item ADVANCED_MAGNET = new AdvancedMagnet(
            new FabricItemSettings().group(ModItemGroup.EMAGNET).fireproof().maxCount(1), config.magnets.range_advanced_magnet,
            config.magnets.capacity_advanced_magnet, 128);

    // Block Items
    public static final BlockItem MAGNET_JAR = new BlockItem(ModBlocks.MAGNET_JAR,
            new FabricItemSettings().group(ModItemGroup.EMAGNET));

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(EMagnet.MOD_ID, "basic_magnet"), BASIC_MAGNET);
        Registry.register(Registry.ITEM, new Identifier(EMagnet.MOD_ID, "advanced_magnet"), ADVANCED_MAGNET);
        Registry.register(Registry.ITEM, new Identifier(EMagnet.MOD_ID, "magnet_jar"), MAGNET_JAR);
    }
}
