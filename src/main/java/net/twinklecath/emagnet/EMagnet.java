package net.twinklecath.emagnet;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.twinklecath.emagnet.registry.ModBlockEntityTypes;
import net.twinklecath.emagnet.registry.ModBlocks;
import net.twinklecath.emagnet.registry.ModItems;

public class EMagnet implements ModInitializer {

    public static final String MOD_ID ="emagnet";

    @Override
    public void onInitialize() {
        AutoConfig.register(EMagnetConfig.class, JanksonConfigSerializer::new);

        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModBlockEntityTypes.registerBlockEntityTypes();
    }
}
