package io.github.realguyman.totally_lit.block.teenycoal;

import io.github.realguyman.totally_lit.block.UnlitTorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class UnlitTeenyTorchBlock extends UnlitTorchBlock {
    public UnlitTeenyTorchBlock(Settings settings, ParticleEffect particleEffect, Block litBlock) {
        super(settings, particleEffect, litBlock);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.375f, 0f, 0.375f, 0.625f, 0.375f, 0.625f);
    }
}
