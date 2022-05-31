package io.github.realguyman.totally_lit.block.teenycoal;

import io.github.realguyman.totally_lit.block.LitWallTorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class LitTeenyWallTorchBlock extends LitWallTorchBlock {
    public LitTeenyWallTorchBlock(Settings settings, ParticleEffect particleEffect, Block unlitBlock) {
        super(settings, particleEffect, unlitBlock);
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random){
        Direction direction = state.get(FACING).getOpposite();
        double d = (double)pos.getX() + 0.5D;
        double e = (double)pos.getY() + 0.4D;
        double f = (double)pos.getZ() + 0.5D;
        world.addParticle(ParticleTypes.SMOKE, d + 0.37D * (double)direction.getOffsetX(), e + 0.22D, f + 0.37D * (double)direction.getOffsetZ(), 0.0D, 0.0D, 0.0D);
        world.addParticle(this.particle, d + 0.37D * (double)direction.getOffsetX(), e + 0.22D, f + 0.37D * (double)direction.getOffsetZ(), 0.0D, 0.0D, 0.0D);
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
