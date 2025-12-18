package net.fadiese.arsironcompat.mixin.mana;

import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(MagicData.class)
public abstract class MagicDataMixin {

    @Shadow(remap = false)
    private ServerPlayer serverPlayer;

    @WrapMethod(method = "getMana", remap = false)
    public float arsironcompat_getMana(Operation<Float> original) {
        if (serverPlayer == null) {
            return original.call();
        }
        Optional<Double> currentValue = serverPlayer.getCapability(CapabilityRegistry.MANA_CAPABILITY)
                .map(IManaCap::getCurrentMana);
        return currentValue.map(Double::floatValue).orElseGet(original::call);
    }

    @WrapMethod(method = "setMana", remap = false)
    public void arsironcompat_setMana(float mana, Operation<Void> original) {
        if (serverPlayer == null) {
            original.call(mana);
        } else {
            Optional<IManaCap> optional = serverPlayer.getCapability(CapabilityRegistry.MANA_CAPABILITY).resolve();
            optional.ifPresentOrElse(manaCap -> manaCap.setMana(mana), () -> original.call(mana));
        }

    }
}
