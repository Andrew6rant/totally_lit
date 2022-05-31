package io.github.realguyman.totally_lit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;

import static io.github.realguyman.totally_lit.registry.BlockRegistry.*;
import static io.github.realguyman.totally_lit.registry.TeenyBlockRegistry.*;

public class TotallyLitClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), UNLIT_LANTERN, UNLIT_TORCH, UNLIT_WALL_TORCH);
        if (FabricLoader.getInstance().isModLoaded("teenycoal")) {
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), UNLIT_TEENY_TORCH, UNLIT_TEENY_WALL_TORCH);
        }
    }
}
