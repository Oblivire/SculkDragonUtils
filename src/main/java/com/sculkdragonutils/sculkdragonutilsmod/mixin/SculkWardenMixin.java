package com.sculkdragonutils.sculkdragonutilsmod.mixin;

import com.sculkdragonutils.sculkdragonutilsmod.effect.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public abstract class SculkWardenMixin {

    @Inject(method = "canTargetEntity", at = @At("HEAD"), cancellable = true)
    public void DisableAttack(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(ModEffects.WARDEN_HARMONY_EFFECT)) {
            cir.setReturnValue(false);
        }
    }

}
