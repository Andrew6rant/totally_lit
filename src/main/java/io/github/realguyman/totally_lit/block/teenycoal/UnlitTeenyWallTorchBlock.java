package io.github.realguyman.totally_lit.block.teenycoal;

import io.github.realguyman.totally_lit.block.UnlitWallTorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class UnlitTeenyWallTorchBlock extends UnlitWallTorchBlock {
    public UnlitTeenyWallTorchBlock(Settings settings, ParticleEffect particleEffect, Block litBlock) {
        super(settings, particleEffect, litBlock);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case NORTH -> VoxelShapes.cuboid(0.34375f, 0.1875f, 0.75f, 0.65625f, 0.5625f, 1f);
            case SOUTH -> VoxelShapes.cuboid(0.34375f, 0.1875f, 0f, 0.65625f, 0.5625f, 0.25f);
            case EAST -> VoxelShapes.cuboid(0f, 0.1875f, 0.34375f, 0.25f, 0.5625f, 0.65625f);
            case WEST -> VoxelShapes.cuboid(0.75f, 0.1875f, 0.34375f, 1f, 0.5625f, 0.65625f);
            case DOWN, UP -> VoxelShapes.cuboid(0.375f, 0f, 0.375f, 0.625f, 0.375f, 0.625f); // This should never be called, but just in case, it defaults to the default Teeny Torch cuboid
        };
    }
}
