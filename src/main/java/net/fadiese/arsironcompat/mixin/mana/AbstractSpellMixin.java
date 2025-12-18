package net.fadiese.arsironcompat.mixin.mana;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.fadiese.arsironcompat.ArsIronConfig;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractSpell.class)
public class AbstractSpellMixin {

    @WrapMethod(method = "getManaCost", remap = false)
    public int arsironcompat_getManaCost(int level, Operation<Integer> original) {
        return Mth.floor(original.call(level) * ArsIronConfig.MANA_COST_CONVERSION_RATIO.get());
    }
}
