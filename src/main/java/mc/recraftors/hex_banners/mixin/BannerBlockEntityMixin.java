package mc.recraftors.hex_banners.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import mc.recraftors.hex_banners.CursedBannerPatternRegColorPair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityMixin {
    @Redirect(
            method = "getPatternsFromNbt",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/datafixers/util/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;",
                    ordinal = 1
            )
    )
    private static <F, S> Pair<RegistryEntry<BannerPattern>, DyeColor> hb_pairCreator(F first, S second, @Local(ordinal = 1) int j) {
        RegistryEntry<BannerPattern> r = (RegistryEntry<BannerPattern>) first;
        if (second == null) {
            return new CursedBannerPatternRegColorPair(r, j);
        }
        return Pair.of(r, (DyeColor) second);
    }

    @Redirect(
            method = "getPatternsFromNbt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/DyeColor;byId(I)Lnet/minecraft/util/DyeColor;"
            )
    )
    private static DyeColor hb_getColor(int id) {
        if (id < DyeColor.values().length) return DyeColor.byId(id);
        return null;
    }

    @Redirect(
            method = "getPatternsFromNbt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NbtCompound;getInt(Ljava/lang/String;)I"
            )
    )
    private static int hb_intOrHex(NbtCompound instance, String key) {
        if (instance.contains(key, NbtElement.NUMBER_TYPE)) return instance.getInt(key);
        if (instance.contains(key, NbtElement.STRING_TYPE)) {
            String s = instance.getString(key).strip();
            if (s.startsWith("#")) {
                return Integer.parseInt(s.substring(1), 16);
            }
            if (!s.matches("\\d+")) {
                return Integer.parseInt(s, 16);
            }
            return Integer.parseInt(s);
        }
        return -1;
    }
}
