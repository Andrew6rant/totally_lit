package io.github.realguyman.totally_lit.block;

import io.github.realguyman.totally_lit.registry.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class UnlitTorchBlock extends TorchBlock {
    private final Block litBlock;

    public UnlitTorchBlock(Settings settings, ParticleEffect particleEffect, Block litBlock) {
        super(settings, particleEffect);
        this.litBlock = litBlock;
    }

    public Block getLitBlock() {
        return litBlock;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        if (player.getStackInHand(hand).isIn(TagRegistry.TORCH_IGNITER_ITEMS) && world.setBlockState(pos, litBlock.getDefaultState())) {
            // TODO: Add a quiet flame sound.
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, result);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {}
}
