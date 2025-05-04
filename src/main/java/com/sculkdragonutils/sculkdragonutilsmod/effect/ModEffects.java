package com.sculkdragonutils.sculkdragonutilsmod.effect;

import com.sculkdragonutils.sculkdragonutilsmod.SculkDragonUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, SculkDragonUtils.MODID);

    public static final Holder<MobEffect> SCULK_HARMONY_EFFECT = MOB_EFFECTS.register("sculk_harmony",
            () -> new SculkHarmonyEffect(MobEffectCategory.NEUTRAL, 0x000000));

    public static final Holder<MobEffect> WARDEN_HARMONY_EFFECT = MOB_EFFECTS.register("warden_harmony",
            () -> new WardenHarmonyEffect(MobEffectCategory.NEUTRAL, 0x000000));

    public static final Holder<MobEffect> REVERBERATING_EFFECT = MOB_EFFECTS.register("reverberating",
            () -> new ReverberatingEffect(MobEffectCategory.NEUTRAL, 0xFFFFFF));

    public static final Holder<MobEffect> SCULK_SIGHT_EFFECT = MOB_EFFECTS.register("sculk_sight",
            () -> new SculkSightEffect(MobEffectCategory.NEUTRAL, 0x000000));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
