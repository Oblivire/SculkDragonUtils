package com.sculkdragonutils.sculkdragonutilsmod.common.util;

import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
//import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.RelativeMovement;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class SculkBloomInst {
    private final ServerLevel level;
    private final SculkSpreader sculkSpreader;
    private final RandomSource randomsource;
    private final BlockPos blockpos;
    private final Vec3 vec3;
    private final Set<RelativeMovement> set;

    public SculkBloomInst(ServerLevel level, Vec3 vec3, Set<RelativeMovement> set) {
        this.level = level;
        this.vec3 = vec3;
        this.set = set;
        this.blockpos = BlockPos.containing(vec3);
        this.sculkSpreader = SculkSpreader.createLevelSpreader();
        this.randomsource = level.getRandom();
    }

    public void addCursors(int charge, int cursorNum) {
        level.sendParticles(ParticleTypes.SCULK_SOUL, vec3.x + (double)0.5F, vec3.y + 1.15, vec3.z + (double)0.5F, 2, 0.2, 0.0F, 0.2, 0.0F);
        level.playSound(null, blockpos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + randomsource.nextFloat() * 0.4F);
        for (int k = 0; k < cursorNum; k++) {
            sculkSpreader.addCursors(blockpos, charge);
        }
    }

    public boolean isDone() {
        int totalCharge = 0;
        for (SculkSpreader.ChargeCursor cursor: sculkSpreader.getCursors()) {
            totalCharge += cursor.getCharge();
        }
        return totalCharge <= 0;
    }

    public void clear() {
        sculkSpreader.clear();
    }

    public void update(){
        // Below spreader code from net.minecraft.world.level.levelgen.feature.SculkPatchFeature

        sculkSpreader.updateCursors(level, blockpos, randomsource, true);

        /*int i1 = UniformInt.of(0, 1).sample(randomsource);

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
        }*/
    }
}
