package com.sculkdragonutils.sculkdragonutilsmod.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sculkdragonutils.sculkdragonutilsmod.EventHandler;
import com.sculkdragonutils.sculkdragonutilsmod.common.util.SculkBloomInst;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.EnumSet;
import java.util.Set;

public class SculkBloomCommand {

    public static void register(final RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("sculk-bloom").requires(
            sourceStack -> sourceStack.hasPermission(Commands.LEVEL_GAMEMASTERS)
                ).then(Commands.argument("at", Vec3Argument.vec3()).executes(
                    (command) -> bloomCommand(
                        command.getSource(),
                        Vec3Argument.getCoordinates(command, "at"),
                        1,
                        1
                        )
                    ).then(
                        Commands.argument("charge", IntegerArgumentType.integer(1)
                    ).executes(
                        (command) -> bloomCommand(
                                command.getSource(),
                                Vec3Argument.getCoordinates(command, "at"),
                                IntegerArgumentType.getInteger(command, "charge"),
                                1
                        )
                        ).then(
                            Commands.argument("cursors", IntegerArgumentType.integer(1, 10)
                        ).executes(
                            (command) -> bloomCommand(
                                command.getSource(),
                                Vec3Argument.getCoordinates(command, "at"),
                                IntegerArgumentType.getInteger(command, "charge"),
                                IntegerArgumentType.getInteger(command, "cursors")
                                )
                            )
                        )
                )
            )
        );
    }

    private static int bloomCommand(CommandSourceStack source, Coordinates at, int charge, int cursors) throws CommandSyntaxException {
        // Code extrapolated from net.minecraft.server.commands.TeleportCommand.teleportToPos
        Vec3 vec3 = at.getPosition(source);
        ServerLevel level = source.getLevel();
        Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);

        if (at.isXRelative()) {
            set.add(RelativeMovement.X);
        }

        if (at.isYRelative()) {
            set.add(RelativeMovement.Y);
        }

        if (at.isZRelative()) {
            set.add(RelativeMovement.Z);
        }

        SculkBloomInst bloomInst = new SculkBloomInst(level, vec3, set);
        bloomInst.bloomParticles(null);
        bloomInst.addCursors(charge, cursors);
        EventHandler.addBloom(bloomInst);

        return 0;
    }
}
