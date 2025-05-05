package com.sculkdragonutils.sculkdragonutilsmod.mixin;

import com.sculkdragonutils.sculkdragonutilsmod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SculkSensorBlock.class)
public abstract class SculkSensorMixin {

    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    public void disableStep(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(ModEffects.SCULK_HARMONY_EFFECT)) {
            ci.cancel();
        }
    }
}
