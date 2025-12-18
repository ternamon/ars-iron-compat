package net.fadiese.arsironcompat.util;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.fadiese.arsironcompat.ArsIronConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MagicResistanceUtils {

    private MagicResistanceUtils() {
    }

    public static float getResist(LivingEntity entity, List<SpellSchool> effectSchools) {
        double resist = entity.getAttributeValue(AttributeRegistry.SPELL_RESIST.get());
        Set<Holder<Attribute>> attributeSet = new HashSet<>();
        effectSchools.forEach(elt -> attributeSet.addAll(ArsIronConfig.getISSResistancesForANSchool(elt)));
        for (Holder<Attribute> attributeHolder : attributeSet) {
            resist *= entity.getAttributeValue(attributeHolder);
        }
        return 2 - (float) Utils.softCapFormula(resist);
    }
}
