package net.fadiese.arsironcompat.event;

import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.fadiese.arsironcompat.ArsIronCompat;
import net.fadiese.arsironcompat.ArsIronConfig;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = ArsIronCompat.MODID)
public class ItemAttributeModifierEvents {

    @SubscribeEvent
    public static void onAttributeModify(ItemAttributeModifierEvent event) {
        if (event.getModifiers().containsKey(AttributeRegistry.MAX_MANA.get())) {
            Collection<AttributeModifier> maxManaModifiers = event.getModifiers().get(AttributeRegistry.MAX_MANA.get());
            maxManaModifiers.forEach(elt -> {
                event.addModifier(PerkAttributes.MAX_MANA.get(), new AttributeModifier(elt.getId(), elt.getName(),
                        Math.floor(elt.getAmount() * ArsIronConfig.MAX_MANA_CONVERSION_RATIO.get()),
                        elt.getOperation()));
            });
            event.removeAttribute(AttributeRegistry.MAX_MANA.get());
        }
        if (event.getModifiers().containsKey(AttributeRegistry.MANA_REGEN.get())) {
            Collection<AttributeModifier> manaRegenModifiers = event.getModifiers()
                    .get(AttributeRegistry.MANA_REGEN.get());
            manaRegenModifiers.forEach(elt -> {
                event.addModifier(PerkAttributes.MANA_REGEN_BONUS.get(), elt);
            });
            event.removeAttribute(AttributeRegistry.MANA_REGEN.get());
        }
    }
}
