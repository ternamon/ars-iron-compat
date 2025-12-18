package net.fadiese.arsironcompat.mixin.spellpower;

import alexthw.ars_elemental.common.glyphs.EffectEnvenom;
import alexthw.ars_elemental.common.glyphs.EffectSpark;
import alexthw.ars_elemental.common.glyphs.EffectWaterGrave;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

import static net.fadiese.arsironcompat.util.MagicResistanceUtils.getResist;

@Mixin(value = {EffectEnvenom.class, EffectFreeze.class, EffectGravity.class, EffectHarm.class, EffectHex.class,
        EffectSnare.class, EffectSpark.class, EffectWaterGrave.class, EffectWither.class})
public abstract class GenericHarmfulPotionEffectMixin extends AbstractEffect implements IPotionEffect {

    protected GenericHarmfulPotionEffectMixin(String tag, String description) {
        super(tag, description);
    }

    @Override
    public void applyPotion(LivingEntity entity, MobEffect potionEffect, SpellStats stats, int baseDurationSeconds,
            int durationBuffSeconds, boolean showParticles) {
        if (entity == null) {
            return;
        }
        int ticks = (int) ((baseDurationSeconds * 20 + durationBuffSeconds * stats.getDurationInTicks()) * getResist(
                entity, this.spellSchools));
        int amp = (int) stats.getAmpMultiplier();
        entity.addEffect(new MobEffectInstance(potionEffect, ticks, amp, false, showParticles, true));
    }
}
