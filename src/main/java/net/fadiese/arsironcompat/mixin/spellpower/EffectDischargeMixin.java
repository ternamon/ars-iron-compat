package net.fadiese.arsironcompat.mixin.spellpower;

import alexthw.ars_elemental.common.glyphs.EffectDischarge;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.fadiese.arsironcompat.util.MagicResistanceUtils.getResist;

@Mixin(value = {EffectDischarge.class})
public abstract class EffectDischargeMixin extends AbstractEffect {

    protected EffectDischargeMixin(String tag, String description) {
        super(tag, description);
    }

    @Inject(method = "applyPotion", at = @At(value = "NEW", target = "Lnet/minecraft/world/effect/MobEffectInstance;"), remap = false)
    public void arsironcompat_applyPotion(LivingEntity entity, MobEffect potionEffect, SpellStats stats,
            int baseDurationSeconds, int durationBuffSeconds, boolean showParticles, CallbackInfo ci,
            @Local(name = "ticks") LocalIntRef ticks) {
        int duration = ticks.get();
        duration = (int) (duration * getResist(entity, this.spellSchools));
        ticks.set(duration);
    }
}
