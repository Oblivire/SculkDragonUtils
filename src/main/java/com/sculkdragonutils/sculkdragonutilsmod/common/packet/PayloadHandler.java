package com.sculkdragonutils.sculkdragonutilsmod.common.packet;

import com.sculkdragonutils.sculkdragonutilsmod.common.util.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PayloadHandler {
    public static class ClientPayloadHandler {

        public static void handleDataOnNetwork(final SculkShaderPacket data, final IPayloadContext context) {
            context.enqueueWork(() -> {
                        String shader = "";
                        if (!data.toUse()) {
                            Minecraft.getInstance().gameRenderer.shutdownEffect();
                            shader = "null";
                        } else {
                            shader = "sobel.json";
                        }

                        ShaderUtil.load(shader, !data.toUse());
                    })
                    .exceptionally(e -> {
                        context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
                        return null;
                    });
        }
    }
    public static class ServerPayloadHandler {
        public static void handleDataOnNetwork(final SculkShaderPacket data, final IPayloadContext context) {
            context.enqueueWork(() -> {
                    })
                    .exceptionally(e -> {
                        context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
                        return null;
                    });
        }
    }
}
