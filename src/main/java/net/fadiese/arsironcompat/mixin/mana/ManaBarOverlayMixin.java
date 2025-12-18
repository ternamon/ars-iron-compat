package net.fadiese.arsironcompat.mixin.mana;

import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ManaBarOverlay.class)
public class ManaBarOverlayMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true, remap = false)
    public void arsironcompat_render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth,
            int screenHeight, CallbackInfo ci) {
        ci.cancel();
    }
}
