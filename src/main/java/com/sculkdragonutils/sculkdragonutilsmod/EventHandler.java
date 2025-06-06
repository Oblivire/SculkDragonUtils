package com.sculkdragonutils.sculkdragonutilsmod;

import com.sculkdragonutils.sculkdragonutilsmod.common.packet.SculkShaderPacket;
import com.sculkdragonutils.sculkdragonutilsmod.common.util.SculkBloomInst;
import com.sculkdragonutils.sculkdragonutilsmod.common.util.ShaderUtil;
import com.sculkdragonutils.sculkdragonutilsmod.effect.ModEffects;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraft.util.Mth.floor;

public class EventHandler {
    private static final List<SculkBloomInst> blooms = new ArrayList<>();
    private static final List<SculkBloomInst> toRemove = new ArrayList<>();

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
        if (event.getEffectInstance().is(ModEffects.SCULK_SIGHT_EFFECT)) {
            if (event.getEntity().hasEffect(ModEffects.SCULK_SIGHT_EFFECT)) {
                return;
            }
            if (level.isClientSide() && event.getEntity() instanceof LocalPlayer) {
                ShaderUtil.loadAndCloseUnsafe("sobel.json", false);
            }
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new SculkShaderPacket(true));
            }
        }
    }
    @SubscribeEvent
    public void onEffectRemoved(MobEffectEvent.Remove event) {
        Level level = event.getEntity().level();
        if (event.getEffect().is(ModEffects.SCULK_SIGHT_EFFECT)) {
            if (level.isClientSide() && event.getEntity() instanceof LocalPlayer) {
                ShaderUtil.loadAndCloseUnsafe("null", true);
            }
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new SculkShaderPacket(false));
            }
        }
    }
    @SubscribeEvent
    public void onEffectExpired(MobEffectEvent.Expired event) {
        Level level = event.getEntity().level();
        assert event.getEffectInstance() != null;
        if (event.getEffectInstance().is(ModEffects.SCULK_SIGHT_EFFECT)) {
            if (level.isClientSide() && event.getEntity() instanceof LocalPlayer) {
                ShaderUtil.loadAndCloseUnsafe("null", true);
            }
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new SculkShaderPacket(false));
            }
        }
    }
    @SubscribeEvent
    public void onExpDrop(LivingExperienceDropEvent event) {
        // Yes I am aware this is too many if conditions, but I don't know if I care enough to fix it
        int exp = event.getDroppedExperience();
        @Nullable Player player = event.getAttackingPlayer();
        if (player != null && player.hasEffect(ModEffects.SCULK_BLOOM_EFFECT) && exp < 50) {
            Entity target = event.getEntity();
            BlockPos target_loc = target.blockPosition();
            Level level = target.level();
            if (SculkBloomInst.canSpreadFrom(level, target_loc)) {
                if (!level.isClientSide()) {  // Only bloom on server
                    float potency = 1.0F + (Objects.requireNonNull(player.getEffect(ModEffects.SCULK_BLOOM_EFFECT)).getAmplifier()  * 0.1F);
                    FoodData player_food = player.getFoodData(); // Putting here for now, I don't want to add another effect
                    player_food.eat(floor(2 * potency), 0.1F * potency);
                    SculkBloomInst newInst = new SculkBloomInst((ServerLevel) level, target_loc.getCenter(), null, false);
                    newInst.bloomParticles(player.blockPosition().getCenter());
                    newInst.addCursors(floor(exp * potency), 1);
                    addBloom(newInst);
                }
                event.setCanceled(true);  // Cancel on both client and server
            }
        }
    }
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        for (SculkBloomInst bloomInst: blooms) {
            bloomInst.update();
            if (bloomInst.isDone()) {
                bloomInst.clear();
                toRemove.add(bloomInst);
            }
        }
        for (SculkBloomInst doneInst: toRemove) {
            blooms.remove(doneInst);
        }
        toRemove.clear();
    }

    public static void addBloom(SculkBloomInst bloomInst) {
        blooms.add(bloomInst);
    }
}
