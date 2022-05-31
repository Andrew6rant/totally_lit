package io.github.realguyman.totally_lit.registry;

import io.github.Andrew6rant.teenycoal.TeenyCoal;
import io.github.realguyman.totally_lit.TotallyLitModInitializer;
import io.github.realguyman.totally_lit.block.teenycoal.UnlitTeenyTorchBlock;
import io.github.realguyman.totally_lit.block.teenycoal.UnlitTeenyWallTorchBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TeenyBlockRegistry {
    public static final Block UNLIT_TEENY_TORCH = add("unlit_teeny_torch", new UnlitTeenyTorchBlock(AbstractBlock.Settings.copy(TeenyCoal.TEENY_TORCH).luminance(state -> 0), null, TeenyCoal.TEENY_TORCH));
    public static final Block UNLIT_TEENY_WALL_TORCH = add("unlit_teeny_wall_torch", new UnlitTeenyWallTorchBlock(AbstractBlock.Settings.copy(TeenyCoal.TEENY_WALL_TORCH).luminance(state -> 0), null, TeenyCoal.TEENY_WALL_TORCH));

    private static Block add(String path, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(TotallyLitModInitializer.IDENTIFIER, path), block);
    }
}
