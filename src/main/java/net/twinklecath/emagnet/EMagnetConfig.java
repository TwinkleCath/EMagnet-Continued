package net.twinklecath.emagnet;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = EMagnet.MOD_ID)
public class EMagnetConfig implements ConfigData{

    @ConfigEntry.Category("magnets")
    @ConfigEntry.Gui.TransitiveObject
    @Comment("Magnets configuration options.")
    public Magnets magnets = new Magnets();

    public static class Magnets {
        @Comment("This value determines how big the capacity of the basic magnet is [DEFAULT: 10000]")
        public int capacity_basic_magnet = 5000;
        @Comment("This value determines how big the capacity of the advanced magnet is [DEFAULT: 30000]")
        public int capacity_advanced_magnet = 30000;
        @Comment("This value determines how big the range of the basic magnet is [DEFAULT: 8]")
        public int range_basic_magnet = 8;
        @Comment("This value determines how big the range of the advanced magnet is [DEFAULT: 16]")
        public int range_advanced_magnet = 16;
    }

    @ConfigEntry.Category("magnets")
    @ConfigEntry.Gui.TransitiveObject
    @Comment("Magnets configuration options.")
    public Blocks blocks = new Blocks();

    public static class Blocks {
        @Comment("If false the magnet jar is not disabled else if true the magnet jar is disabled [DEFAULT: false]")
        public boolean disable_magnet_jar = false;

        @Comment("If false the magnet jar can not be disabled with redstone else if true the magnet jar can be disabled with redstone (when the magnet jar is disabled it can't attract items anymore but it can still charge)  [DEFAULT: true]")
        public boolean disable_magnet_jar_with_redstone = true;
    }
}
