package net.fadiese.arsironcompat.event;

import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHarm;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.fadiese.arsironcompat.ArsIronCompat;
import net.fadiese.arsironcompat.ArsIronConfig;
import net.fadiese.arsironcompat.util.MagicResistanceUtils;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ArsIronCompat.MODID)
public class SpellEvents {

    @SubscribeEvent
    public static void onISSSpellDamageEvent(SpellDamageEvent event) {
        if (event.getSpellDamageSource().getEntity() instanceof ServerPlayer player && player.getAttributes()
                .hasAttribute(PerkAttributes.SPELL_DAMAGE_BONUS.get())) {
            float newAmount =
                    event.getAmount() + (float) player.getAttributeValue(PerkAttributes.SPELL_DAMAGE_BONUS.get());
            event.setAmount(newAmount);
        }
    }

    @SubscribeEvent
    public static void onArsSpellModifierEvent(SpellModifierEvent event) {
        if (event.caster instanceof ServerPlayer player && event.spellPart != null) {
            double additionalConvertedDamage = 0d;
            double additionalDuration = 0d;
            if (player.getAttributes().hasAttribute(AttributeRegistry.SPELL_POWER.get())) {
                double spellPower = player.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
                additionalConvertedDamage = (spellPower - 1.0d) * 10;
                additionalDuration = (spellPower - 1.0d) * 2.0d;
            }

            for (SpellSchool school : event.spellPart.spellSchools) {
                for (Holder<Attribute> powerAttribute : ArsIronConfig.getISSPowersForANSchool(school)) {
                    if (player.getAttributes().hasAttribute(powerAttribute)) {
                        double spellPower = player.getAttributeValue(powerAttribute);
                        additionalConvertedDamage += (spellPower - 1.0d) * 10;
                        additionalDuration += (spellPower - 1.0d) * 2.0d;
                    }
                }
            }
            event.builder.addDamageModifier(additionalConvertedDamage);
            if (!(event.spellPart instanceof EffectHarm) || event.builder.build().getDurationMultiplier() > 0) {
                event.builder.addDurationModifier(additionalDuration);
            }
        }
    }

    @SubscribeEvent
    public static void onSpellDamageEventPre(com.hollingsworth.arsnouveau.api.event.SpellDamageEvent.Pre event) {
        if (event.target instanceof LivingEntity livingEntity) {
            List<SpellSchool> currentEffectSchools = event.context.getSpell().recipe.get(
                    event.context.getCurrentIndex() - 1).spellSchools;
            event.damage *= MagicResistanceUtils.getResist(livingEntity, currentEffectSchools);
        }
    }
}
