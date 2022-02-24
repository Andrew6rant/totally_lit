package io.github.realguyman.totally_lit.mixin;

import io.github.realguyman.totally_lit.Initializer;
import io.github.realguyman.totally_lit.registry.BlockRegistry;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Shadow @Deprecated public abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.isOf(Blocks.TORCH) || state.isOf(Blocks.WALL_TORCH)) {
            if (world.hasRain(pos) && random.nextFloat() < Initializer.configuration.torchConfiguration.extinguishInRainChance) {
                this.scheduledTick(state, world, pos, random);
            } else if (Initializer.configuration.torchConfiguration.extinguishOverTime) {
                WorldTickScheduler<Block> scheduler = world.getBlockTickScheduler();
                Block block = state.getBlock();

                if (!scheduler.isQueued(pos, block) && !scheduler.isTicking(pos, block)) {
                    world.createAndScheduleBlockTick(pos, block, Initializer.configuration.torchConfiguration.burnDuration * 6_000);
                }
            }

            ci.cancel();
        } else if (state.isOf(Blocks.LANTERN)) {
            if ((world.hasRain(pos) && random.nextFloat() < Initializer.configuration.lanternConfiguration.extinguishInRainChance) || state.get(LanternBlock.WATERLOGGED)) {
                this.scheduledTick(state, world, pos, random);
            } else if (Initializer.configuration.lanternConfiguration.extinguishOverTime) {
                WorldTickScheduler<Block> scheduler = world.getBlockTickScheduler();
                Block block = state.getBlock();

                if (!scheduler.isQueued(pos, block) && !scheduler.isTicking(pos, block)) {
                    world.createAndScheduleBlockTick(pos, block, Initializer.configuration.lanternConfiguration.burnDuration * 6_000);
                }
            }

            ci.cancel();
        }
    }

    @Inject(method = "scheduledTick", at = @At("HEAD"))
    private void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        boolean updated = false;

        // TODO: Consider using tags instead of checking each individual block.
        if(state.isOf(Blocks.LANTERN)) {
            updated = world.setBlockState(pos, BlockRegistry.UNLIT_LANTERN.getDefaultState().with(LanternBlock.HANGING, state.get(LanternBlock.HANGING)).with(LanternBlock.WATERLOGGED, state.get(LanternBlock.WATERLOGGED)));
        } else if (state.isOf(Blocks.TORCH)) {
            updated = world.setBlockState(pos, BlockRegistry.UNLIT_TORCH.getDefaultState());
        } else if (state.isOf(Blocks.WALL_TORCH)) {
            updated = world.setBlockState(pos, BlockRegistry.UNLIT_WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING)));
        }

        if (updated) {
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.0625F, random.nextFloat() * 0.5F + 0.125F);
        }
    }
}
