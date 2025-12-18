package net.fadiese.arsironcompat.mixin.spellpower;

import static net.fadiese.arsironcompat.util.MagicResistanceUtils.getResist;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectColdSnap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EffectColdSnap.class)
public abstract class EffectColdSnapMixin extends AbstractEffect {

    protected EffectColdSnapMixin(String tag, String description) {
        super(tag, description);
    }

    @WrapOperation(method = "damage", at = @At(value = "NEW", target = "Lnet/minecraft/world/effect/MobEffectInstance;"), remap = false)
    private MobEffectInstance arsironcompat_modifyDuration(MobEffect pEffect, int pDuration,
            Operation<MobEffectInstance> original, Vec3 vec, ServerLevel world, LivingEntity shooter,
            LivingEntity livingEntity, SpellStats stats, SpellContext context, SpellResolver resolver, int snareTime,
            float damage) {
        return original.call(pEffect, Mth.floor(pDuration * getResist(livingEntity, this.spellSchools)));
    }
}
