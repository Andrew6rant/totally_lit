package io.github.realguyman.totally_lit.mixin;

import io.github.Andrew6rant.teenycoal.TeenyCoal;
import io.github.realguyman.totally_lit.TotallyLitModInitializer;
import io.github.realguyman.totally_lit.access.CampfireBlockEntityAccess;
import io.github.realguyman.totally_lit.block.LitTorchBlock;
import io.github.realguyman.totally_lit.block.LitWallTorchBlock;
import io.github.realguyman.totally_lit.registry.BlockRegistry;
import io.github.realguyman.totally_lit.registry.TagRegistry;
import io.github.realguyman.totally_lit.registry.TeenyBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
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
        if (state.isIn(BlockTags.CAMPFIRES)) {
            final BlockEntity blockEntity = world.getBlockEntity(pos);

            if (world.hasRain(pos.up()) && CampfireBlock.isLitCampfire(state) && blockEntity instanceof CampfireBlockEntity && random.nextFloat() < TotallyLitModInitializer.getConfiguration().campfireConfiguration.extinguishInRainChance && world.setBlockState(pos, state.with(CampfireBlock.LIT, false))) {
                ((CampfireBlockEntityAccess) blockEntity).setBurningTicks(0);
                CampfireBlock.extinguish(null, world, pos, state);
                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            ci.cancel();
        } else if (state.isIn(BlockTags.CANDLES) || state.isIn(BlockTags.CANDLE_CAKES)) {
            if (AbstractCandleBlock.isLitCandle(state) && ((world.hasRain(pos.up()) && random.nextFloat() < TotallyLitModInitializer.getConfiguration().candleConfiguration.extinguishInRainChance) || (state.contains(CandleBlock.WATERLOGGED) && state.get(CandleBlock.WATERLOGGED)))) {
                this.scheduledTick(state, world, pos, random);
            } else if (!AbstractCandleBlock.isLitCandle(state) && TotallyLitModInitializer.getConfiguration().candleConfiguration.extinguishOverTime) {
                WorldTickScheduler<Block> scheduler = world.getBlockTickScheduler();
                Block block = state.getBlock();

                if (!scheduler.isQueued(pos, block) && !scheduler.isTicking(pos, block)) {
                    world.createAndScheduleBlockTick(pos, block, TotallyLitModInitializer.getConfiguration().candleConfiguration.burnDuration * 6_000);
                }
            }

            ci.cancel();
        } else if (state.isOf(Blocks.JACK_O_LANTERN)) {
            if ((world.hasRain(pos) && random.nextFloat() < TotallyLitModInitializer.getConfiguration().jackOLanternConfiguration.extinguishInRainChance)) {
                this.scheduledTick(state, world, pos, random);
            } else if (TotallyLitModInitializer.getConfiguration().jackOLanternConfiguration.extinguishOverTime) {
                WorldTickScheduler<Block> scheduler = world.getBlockTickScheduler();
                Block block = state.getBlock();

                if (!scheduler.isQueued(pos, block) && !scheduler.isTicking(pos, block)) {
                    world.createAndScheduleBlockTick(pos, block, TotallyLitModInitializer.getConfiguration().jackOLanternConfiguration.burnDuration * 6_000);
                }
            }

            ci.cancel();
        } else if (state.isOf(Blocks.LANTERN)) {
            if ((world.hasRain(pos) && random.nextFloat() < TotallyLitModInitializer.getConfiguration().lanternConfiguration.extinguishInRainChance) || Boolean.TRUE.equals(state.get(LanternBlock.WATERLOGGED))) {
                this.scheduledTick(state, world, pos, random);
            } else if (TotallyLitModInitializer.getConfiguration().lanternConfiguration.extinguishOverTime) {
                WorldTickScheduler<Block> scheduler = world.getBlockTickScheduler();
                Block block = state.getBlock();

                if (!scheduler.isQueued(pos, block) && !scheduler.isTicking(pos, block)) {
                    world.createAndScheduleBlockTick(pos, block, TotallyLitModInitializer.getConfiguration().lanternConfiguration.burnDuration * 6_000);
                }
            }

            ci.cancel();
        } else if (state.isIn(TagRegistry.EXTINGUISHABLE_TORCH_BLOCKS) || state.isOf(Blocks.TORCH) || state.isOf(Blocks.WALL_TORCH)) {
            if (world.hasRain(pos) && random.nextFloat() < TotallyLitModInitializer.getConfiguration().torchConfiguration.extinguishInRainChance) {
                this.scheduledTick(state, world, pos, random);
            } else if (TotallyLitModInitializer.getConfiguration().torchConfiguration.extinguishOverTime) {
                WorldTickScheduler<Block> scheduler = world.getBlockTickScheduler();
                Block block = state.getBlock();

                if (!scheduler.isQueued(pos, block) && !scheduler.isTicking(pos, block)) {
                    world.createAndScheduleBlockTick(pos, block, TotallyLitModInitializer.getConfiguration().torchConfiguration.burnDuration * 6_000);
                }
            }

            ci.cancel();
        }
        if (FabricLoader.getInstance().isModLoaded("teenycoal")) {
            if (state.isIn(TagRegistry.EXTINGUISHABLE_TORCH_BLOCKS) || state.isOf(TeenyCoal.TEENY_TORCH) || state.isOf(TeenyCoal.TEENY_WALL_TORCH)) {
                if (world.hasRain(pos) && random.nextFloat() < TotallyLitModInitializer.getConfiguration().teenytorchConfiguration.extinguishInRainChance) {
                    this.scheduledTick(state, world, pos, random);
                } else if (TotallyLitModInitializer.getConfiguration().teenytorchConfiguration.extinguishOverTime) {
                    WorldTickScheduler<Block> scheduler = world.getBlockTickScheduler();
                    Block block = state.getBlock();

                    if (!scheduler.isQueued(pos, block) && !scheduler.isTicking(pos, block)) {
                        world.createAndScheduleBlockTick(pos, block, TotallyLitModInitializer.getConfiguration().teenytorchConfiguration.burnDuration * 6_000);
                    }
                }

                ci.cancel();
            }
        }
    }

    @Inject(method = "scheduledTick", at = @At("HEAD"))
    private void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        boolean updated = false;

        // TODO: Consider using tags instead of checking each individual block.
        if (AbstractCandleBlock.isLitCandle(state)) {
            AbstractCandleBlock.extinguish(null, state, world, pos);
        } else if (state.isOf(Blocks.JACK_O_LANTERN)) {
            updated = world.setBlockState(pos, Blocks.CARVED_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, state.get(CarvedPumpkinBlock.FACING)));
        } else if(state.isOf(Blocks.LANTERN)) {
            updated = world.setBlockState(pos, BlockRegistry.UNLIT_LANTERN.getDefaultState().with(LanternBlock.HANGING, state.get(LanternBlock.HANGING)).with(LanternBlock.WATERLOGGED, state.get(LanternBlock.WATERLOGGED)));
        } else if (state.isOf(Blocks.TORCH)) {
            updated = world.setBlockState(pos, BlockRegistry.UNLIT_TORCH.getDefaultState());
        } else if (state.isOf(Blocks.WALL_TORCH)) {
            updated = world.setBlockState(pos, BlockRegistry.UNLIT_WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING)));
        } else if (state.isIn(TagRegistry.EXTINGUISHABLE_TORCH_BLOCKS)) {
            if (state.getBlock() instanceof LitTorchBlock litTorch) {
                updated = world.setBlockState(pos, litTorch.getDefaultState());
            } else if (state.getBlock() instanceof LitWallTorchBlock litWallTorchBlock) {
                updated = world.setBlockState(pos, litWallTorchBlock.getUnlitBlock().getDefaultState().with(Properties.FACING, state.get(Properties.FACING)));
            }
        }
        if (FabricLoader.getInstance().isModLoaded("teenycoal")) {
            if (state.isOf(TeenyCoal.TEENY_TORCH)) {
                updated = world.setBlockState(pos, TeenyBlockRegistry.UNLIT_TEENY_TORCH.getDefaultState());
            } else if (state.isOf(TeenyCoal.TEENY_WALL_TORCH)) {
                updated = world.setBlockState(pos, TeenyBlockRegistry.UNLIT_TEENY_WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING)));
            }
        }

        if (updated) {
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.0625F, random.nextFloat() * 0.5F + 0.125F);
        }
    }
}
