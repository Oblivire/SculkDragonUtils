package com.sculkdragonutils.sculkdragonutilsmod.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sculkdragonutils.sculkdragonutilsmod.SculkDragonUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SculkSpreader;
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
                        (command) -> bloom(
                                command.getSource(),
                                Vec3Argument.getCoordinates(command, "at"),
                                1
                                )
                        ).then(
                            Commands.argument("amount", IntegerArgumentType.integer(1)
                        ).executes(
                                    (command) -> bloom(
                                            command.getSource(),
                                            Vec3Argument.getCoordinates(command, "at"),
                                            IntegerArgumentType.getInteger(command, "amount")
                                    )
                            )
                    )
                )
        );
    }

    private static int bloom(CommandSourceStack source, Coordinates at, int amount) throws CommandSyntaxException {
        // Code extrapolated from net.minecraft.server.commands.TeleportCommand.teleportToPos
        Vec3 vec3 = at.getPosition(source);
        ServerLevel level = source.getLevel();
        Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
        RandomSource randomsource = level.getRandom();
        SculkSpreader sculkSpreader = SculkSpreader.createLevelSpreader();

        if (at.isXRelative()) {
            set.add(RelativeMovement.X);
        }

        if (at.isYRelative()) {
            set.add(RelativeMovement.Y);
        }

        if (at.isZRelative()) {
            set.add(RelativeMovement.Z);
        }

        BlockPos blockpos = BlockPos.containing(vec3);
        level.sendParticles(ParticleTypes.SCULK_SOUL, vec3.x + (double)0.5F, vec3.y + 1.15, vec3.z + (double)0.5F, 2, 0.2, (double)0.0F, 0.2, (double)0.0F);
        level.playSound((Player)null, blockpos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + randomsource.nextFloat() * 0.4F);

        // Below spreader code from net.minecraft.world.level.levelgen.feature.SculkPatchFeature

        int cursorNum = UniformInt.of(5, 10).sample(randomsource);
        int updateAmount = UniformInt.of(32, 64).sample(randomsource);
        //int i = 1;

        //for (int j = 0; j < i; j++) {
        for (int k = 0; k < cursorNum; k++) {
            sculkSpreader.addCursors(blockpos, amount);
        }

        //boolean flag = j < 1;

        for (int l = 0; l < updateAmount; l++) {
            sculkSpreader.updateCursors(level, blockpos, randomsource, true);
        }

        sculkSpreader.clear();
        //}

        int i1 = UniformInt.of(0, 1).sample(randomsource);

        for (int j1 = 0; j1 < i1; j1++) {
            BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(5) - 2, 0, randomsource.nextInt(5) - 2);
            BlockPos blockpos2 = blockpos1.below();
            if (level.getBlockState(blockpos1).isAir()
                    && level.getBlockState(blockpos2).isCollisionShapeFullBlock(level, blockpos2)) {
                level.setBlock(blockpos2, Blocks.SCULK_CATALYST.defaultBlockState(), 3);
            }
        }

        i1 = UniformInt.of(0, 1).sample(randomsource);

        for (int j1 = 0; j1 < i1; j1++) {
            BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(5) - 2, 0, randomsource.nextInt(5) - 2);
            if (level.getBlockState(blockpos1).isAir()
                    && level.getBlockState(blockpos1.below()).isFaceSturdy(level, blockpos1.below(), Direction.UP)) {
                level.setBlock(
                        blockpos1, Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, Boolean.valueOf(false)), 3
                );
            }
        }

        i1 = UniformInt.of(0, 1).sample(randomsource);

        for (int j1 = 0; j1 < i1; j1++) {
            BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(5) - 2, 0, randomsource.nextInt(5) - 2);
            if (level.getBlockState(blockpos1).isAir()
                    && level.getBlockState(blockpos1.below()).isFaceSturdy(level, blockpos1.below(), Direction.UP)) {
                level.setBlock(
                        blockpos1, Blocks.SCULK_SENSOR.defaultBlockState(), 3
                );
            }
        }

        return 0;
    }
}
