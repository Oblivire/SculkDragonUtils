package com.sculkdragonutils.sculkdragonutilsmod;

import com.sculkdragonutils.sculkdragonutilsmod.commands.SculkBloomCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class SculkDragonUtilsCommands {
    @SubscribeEvent
    public static void serverRegisterCommandsEvent(RegisterCommandsEvent event) {
        SculkBloomCommand.register(event);
    }
}
