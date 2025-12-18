package net.fadiese.arsironcompat.mixin.spellpower;

import alexthw.ars_elemental.common.glyphs.EffectSpores;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static net.fadiese.arsironcompat.util.MagicResistanceUtils.getResist;

@Mixin(value = {EffectSpores.class})
public abstract class EffectSporesMixin extends AbstractEffect {

    protected EffectSporesMixin(String tag, String description) {
        super(tag, description);
    }

    @ModifyArg(method = "onResolveEntity", at = @At(value = "INVOKE", target = "Lalexthw/ars_elemental/common/glyphs/EffectSpores;damage(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lcom/hollingsworth/arsnouveau/api/spell/SpellStats;FILnet/minecraft/world/entity/LivingEntity;Lcom/hollingsworth/arsnouveau/api/spell/SpellContext;Lcom/hollingsworth/arsnouveau/api/spell/SpellResolver;)V"), index = 5, remap = false)
    public int arsironcompat_applyResistanceToSpellDuration(Vec3 vec, ServerLevel world, LivingEntity shooter,
            SpellStats stats, float damage, int snareTime, LivingEntity livingEntity, SpellContext spellContext,
            SpellResolver resolver) {
        return (int) (snareTime * getResist(livingEntity, this.spellSchools));
    }
}
