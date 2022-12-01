package net.twinklecath.emagnet.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.twinklecath.emagnet.EMagnet;

public class ModItemGroup {
    public static final ItemGroup EMAGNET = FabricItemGroupBuilder.build(
            new Identifier(EMagnet.MOD_ID, "emagnet_tab"), () -> new ItemStack(ModItems.MAGNET_JAR));
}
