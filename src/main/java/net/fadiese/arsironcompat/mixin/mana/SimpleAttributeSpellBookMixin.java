package net.fadiese.arsironcompat.mixin.mana;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.client.IDisplayMana;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import net.fadiese.arsironcompat.ArsIronConfig;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Collection;
import java.util.UUID;

@Mixin(SimpleAttributeSpellBook.class)
public abstract class SimpleAttributeSpellBookMixin implements IDisplayMana {

    @Shadow(remap = false)
    @Final
    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    @WrapMethod(method = "getAttributeModifiers", remap = false)
    public Multimap<Attribute, AttributeModifier> arsironcompat_getAttributeModifiers(SlotContext slotContext,
            UUID uuid, ItemStack stack, Operation<Multimap<Attribute, AttributeModifier>> original) {
        System.out.println("Mixin SimpleAttributeSpellBook");
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = new ImmutableMultimap.Builder<>();
        for (Attribute attribute : defaultModifiers.keySet()) {
            Collection<AttributeModifier> modifiers = defaultModifiers.get(attribute);
            if (AttributeRegistry.MAX_MANA.get().equals(attribute)) {
                for (AttributeModifier attributeModifier : modifiers) {
                    attributeBuilder.put(PerkAttributes.MAX_MANA.get(),
                            new AttributeModifier(uuid, attributeModifier.getName(), Math.floor(
                                    attributeModifier.getAmount() * ArsIronConfig.MAX_MANA_CONVERSION_RATIO.get()),
                                    attributeModifier.getOperation()));
                }
            } else if (AttributeRegistry.MANA_REGEN.get().equals(attribute)) {
                for (AttributeModifier attributeModifier : modifiers) {
                    attributeBuilder.put(PerkAttributes.MANA_REGEN_BONUS.get(),
                            new AttributeModifier(uuid, attributeModifier.getName(), attributeModifier.getAmount(),
                                    attributeModifier.getOperation()));
                }
            } else {
                for (AttributeModifier attributeModifier : modifiers) {
                    attributeBuilder.put(attribute,
                            new AttributeModifier(uuid, attributeModifier.getName(), attributeModifier.getAmount(),
                                    attributeModifier.getOperation()));
                }
            }

        }
        return attributeBuilder.build();
    }
}
