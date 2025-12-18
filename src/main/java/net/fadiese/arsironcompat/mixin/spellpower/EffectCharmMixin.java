package net.fadiese.arsironcompat.mixin.spellpower;

import alexthw.ars_elemental.common.glyphs.EffectCharm;
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

@Mixin(value = {EffectCharm.class})
public abstract class EffectCharmMixin extends AbstractEffect {

    protected EffectCharmMixin(String tag, String description) {
        super(tag, description);
    }

    @Inject(method = "applyPotion", at = @At(value = "NEW", target = "Lalexthw/ars_elemental/util/EntityCarryMEI;"), remap = false)
    public void arsironcompat_applyPotion(LivingEntity entity, LivingEntity owner, MobEffect potionEffect,
            SpellStats stats, CallbackInfo ci, @Local(name = "ticks") LocalIntRef ticks) {
        int duration = ticks.get();
        duration = (int) (duration * getResist(entity, this.spellSchools));
        ticks.set(duration);
    }
}
