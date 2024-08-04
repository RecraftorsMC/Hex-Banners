package mc.recraftors.hex_banners.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import mc.recraftors.hex_banners.CursedBannerPatternRegColorPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(BannerBlockEntityRenderer.class)
public abstract class BannerBlockEntityRendererMixin {
    @Redirect(
            method = "renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DyeColor;getColorComponents()[F")
    )
    private static float[] hb_resolveColorFloat(DyeColor instance, @Local Pair<RegistryEntry<BannerPattern>, DyeColor> pair) {
        if (instance == null) {
            if (!(((Object)pair) instanceof CursedBannerPatternRegColorPair p)) throw new UnsupportedOperationException();
            return new float[]{
                    ((p.hb_getCustomColor() & 0xFF0000) >> 16) / 255f,
                    ((p.hb_getCustomColor() & 0x00FF00) >> 8) / 255f,
                    (p.hb_getCustomColor() & 0x0000FF) / 255f
            };
        }
        return instance.getColorComponents();
    }
}
