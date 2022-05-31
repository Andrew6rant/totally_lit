package io.github.realguyman.totally_lit.registry;

import io.github.realguyman.totally_lit.TotallyLitModInitializer;
import io.github.realguyman.totally_lit.item.UnlitTorchItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static final Item UNLIT_LANTERN = add("unlit_lantern", new BlockItem(BlockRegistry.UNLIT_LANTERN, new Item.Settings().group(ItemGroup.DECORATIONS)));
    public static final Item UNLIT_TORCH = add("unlit_torch", new UnlitTorchItem(BlockRegistry.UNLIT_TORCH, BlockRegistry.UNLIT_WALL_TORCH, new Item.Settings().group(ItemGroup.DECORATIONS), Items.TORCH));

    private static Item add(String path, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(TotallyLitModInitializer.IDENTIFIER, path), item);
    }
}
