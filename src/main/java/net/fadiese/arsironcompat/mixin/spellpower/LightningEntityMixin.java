package net.fadiese.arsironcompat.mixin.spellpower;

import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.entity.LightningEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

import static net.fadiese.arsironcompat.util.MagicResistanceUtils.getResist;


@Mixin(LightningEntity.class)
public class LightningEntityMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/hollingsworth/arsnouveau/common/entity/LightningEntity;getDamage(Lnet/minecraft/world/entity/Entity;)F"))
    private float arsironcompat_getDamage(LightningEntity instance, Entity entity, Operation<Float> original,
            @Share("entityResistance") LocalFloatRef resistanceRef) {
        float resistance = 1.0f;
        if (entity instanceof LivingEntity livingEntity) {
            resistance = getResist(livingEntity, List.of(SpellSchools.ELEMENTAL_AIR));
        }
        resistanceRef.set(resistance);
        return original.call(instance, entity) * resistance;
    }

    @WrapOperation(method = "tick", at = @At(value = "NEW", target = "Lnet/minecraft/world/effect/MobEffectInstance;"))
    private MobEffectInstance arsironcompat_modifyEffectDuration(MobEffect pEffect, int pDuration, int pAmplifier,
            Operation<MobEffectInstance> original, @Share("entityResistance") LocalFloatRef resistanceRef) {
        return original.call(pEffect, Mth.floor(pDuration * resistanceRef.get()), pAmplifier);
    }
}
