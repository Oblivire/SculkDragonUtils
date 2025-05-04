package com.sculkdragonutils.sculkdragonutilsmod;

import com.sculkdragonutils.sculkdragonutilsmod.common.packet.PayloadHandler;
import com.sculkdragonutils.sculkdragonutilsmod.common.packet.SculkShaderPacket;
import com.sculkdragonutils.sculkdragonutilsmod.common.util.ShaderUtil;
import com.sculkdragonutils.sculkdragonutilsmod.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Objects;

public class EventHandler {
    @SubscribeEvent
    public void onShriek(VanillaGameEvent event) {
        Holder<GameEvent> vanilla_event = event.getVanillaEvent();
        /*if (vanilla_event == GameEvent.RESONATE_1) {

        }*/
        // TODO: Replace Mixins with GameEvent.Resonate<#>?  Neoforge doesn't appear to find them
        if (vanilla_event == GameEvent.SHRIEK) {
            MobEffectInstance effectinstance = new MobEffectInstance(ModEffects.REVERBERATING_EFFECT, 80, 0, false, false, true);
            ServerLevel level = (ServerLevel) event.getLevel();
            MobEffectUtil.addEffectToPlayersAround(level, event.getCause(), event.getEventPosition(), 16, effectinstance, 80);
        }
    }
    @SubscribeEvent
    public void onEffectAdded(MobEffectEvent.Added event) {
        Level level = event.getEntity().level();
        if (event.getEntity() instanceof Player && event.getEffectInstance().is(ModEffects.SCULK_SIGHT_EFFECT)) {
            if (level.isClientSide()) {
                ShaderUtil.loadAndCloseUnsafe("sobel.json", false);
            }
            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new SculkShaderPacket(true));
        }
    }
    @SubscribeEvent
    public void onEffectRemoved(MobEffectEvent.Remove event) {
        Level level = event.getEntity().level();
        if (event.getEntity() instanceof Player && event.getEffect().is(ModEffects.SCULK_SIGHT_EFFECT)) {
            if (level.isClientSide()) {
                ShaderUtil.loadAndCloseUnsafe("null", true);
            }
            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new SculkShaderPacket(false));
        }
    }
    @SubscribeEvent
    public void onEffectExpired(MobEffectEvent.Expired event) {
        Level level = event.getEntity().level();
        if (event.getEntity() instanceof Player) {
            assert event.getEffectInstance() != null;
            if (event.getEffectInstance().is(ModEffects.SCULK_SIGHT_EFFECT)) {
                if (level.isClientSide()) {
                    ShaderUtil.loadAndCloseUnsafe("null", true);
                }
                PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new SculkShaderPacket(false));
            }
        }
    }
}
