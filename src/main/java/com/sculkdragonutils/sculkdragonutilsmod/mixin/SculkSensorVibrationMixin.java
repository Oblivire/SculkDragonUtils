package com.sculkdragonutils.sculkdragonutilsmod.mixin;

/*import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbilityInstance;
import by.dragonsurvivalteam.dragonsurvival.registry.attachments.MagicData;*/
import com.sculkdragonutils.sculkdragonutilsmod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(targets = "net.minecraft.world.level.block.entity.SculkSensorBlockEntity$VibrationUser")
public abstract class SculkSensorVibrationMixin {

    /*@Inject(method = "isValidVibration", at = @At(value = "RETURN", ordinal = 3, shift = At.Shift.BY, by = -2), cancellable = true)
    public void DisableVibration(Holder<GameEvent> gameEvent, GameEvent.Context context, CallbackInfoReturnable<Boolean> cir){
        Entity mixin_entity = context.sourceEntity();
        if (mixin_entity instanceof Player && VibrationSystem.getGameEventFrequency(gameEvent) <= 3) {
            cir.setReturnValue(false);
        }
    }*/
    @Inject(method = "canReceiveVibration", at = @At(value = "HEAD"), cancellable = true)
    public void DisableVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, @Nullable GameEvent.Context context, CallbackInfoReturnable<Boolean> cir) {
        assert context != null;
        Entity mixin_entity = context.sourceEntity();
        if (mixin_entity instanceof LivingEntity && ((LivingEntity) mixin_entity).hasEffect(ModEffects.SCULK_HARMONY_EFFECT)) {
            int intensity = ((LivingEntity) mixin_entity).getEffect(ModEffects.SCULK_HARMONY_EFFECT).getAmplifier();
            if (VibrationSystem.getGameEventFrequency(gameEvent) <= intensity) {

                /*DragonStateHandler handler = DragonStateProvider.getData((Player) mixin_entity);
                // TODO: Move to helper function since this will be used in other places
                if (handler.isDragon()) {
                    MagicData data = MagicData.getData((Player) mixin_entity);
                    Map<ResourceKey<DragonAbility>, DragonAbilityInstance> actives = data.getAbilities();
                    for (Map.Entry<ResourceKey<DragonAbility>, DragonAbilityInstance> active: actives.entrySet()){
                        String name = active.getKey().toString();
                        DragonAbilityInstance ability = active.getValue();
                        // Check that the ability name against context here, then check if it's active
                        SculkDragonUtils.LOGGER.info("Active ability key: {}", name);
                        SculkDragonUtils.LOGGER.info("Ability enabled? {}", ability.isEnabled());
                    }
                    cir.setReturnValue(false);
                }*/
                cir.setReturnValue(false);
            }
        }
    }
}
