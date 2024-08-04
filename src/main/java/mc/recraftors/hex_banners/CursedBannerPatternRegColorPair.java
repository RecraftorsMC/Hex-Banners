package mc.recraftors.hex_banners;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;

public class CursedBannerPatternRegColorPair extends Pair<RegistryEntry<BannerPattern>, DyeColor> {
    private final int hb_customColor;

    public CursedBannerPatternRegColorPair(RegistryEntry<BannerPattern> first, int hbCustomColor) {
        super(first, null);
        hb_customColor = hbCustomColor;
    }

    public int hb_getCustomColor() {
        return this.hb_customColor;
    }
}
