package io.github.realguyman.totally_lit.registry;

import io.github.Andrew6rant.teenycoal.TeenyCoal;
import io.github.realguyman.totally_lit.TotallyLitModInitializer;
import io.github.realguyman.totally_lit.item.UnlitTorchItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TeenyItemRegistry {
    public static final Item UNLIT_TEENY_TORCH = add("unlit_teeny_torch", new UnlitTorchItem(TeenyBlockRegistry.UNLIT_TEENY_TORCH, TeenyBlockRegistry.UNLIT_TEENY_WALL_TORCH, new Item.Settings().group(TeenyCoal.ITEM_GROUP), Items.TORCH));

    private static Item add(String path, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(TotallyLitModInitializer.IDENTIFIER, path), item);
    }
}
