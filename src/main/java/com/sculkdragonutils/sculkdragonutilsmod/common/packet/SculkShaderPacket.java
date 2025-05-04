package com.sculkdragonutils.sculkdragonutilsmod.common.packet;

import com.sculkdragonutils.sculkdragonutilsmod.SculkDragonUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SculkShaderPacket(boolean toUse) implements CustomPacketPayload {

    public static final Type<SculkShaderPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SculkDragonUtils.MODID, "trackpacket"));

    public static final StreamCodec<ByteBuf, SculkShaderPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SculkShaderPacket::toUse,
            SculkShaderPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
