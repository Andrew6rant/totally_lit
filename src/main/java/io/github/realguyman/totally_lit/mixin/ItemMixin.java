package io.github.realguyman.totally_lit.mixin;

import io.github.Andrew6rant.teenycoal.TeenyCoal;
import io.github.realguyman.totally_lit.TotallyLitModInitializer;
import io.github.realguyman.totally_lit.item.LitTorchItem;
import io.github.realguyman.totally_lit.registry.ItemRegistry;
import io.github.realguyman.totally_lit.registry.TeenyItemRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        PlayerEntity player = null;

        if (entity.isPlayer()) {
            player = (PlayerEntity) entity;
        }

        if (player != null && !world.isClient() && !player.isCreative() && !player.isSpectator() && player.age % 20 == 0) {
            Float chance = null;
            Item item = null;

            if(stack.isOf(Items.JACK_O_LANTERN)) {
                chance = TotallyLitModInitializer.getConfiguration().jackOLanternConfiguration.extinguishInRainChance;
                item = Items.CARVED_PUMPKIN;
            } else if (stack.isOf(Items.LANTERN)) {
                chance = TotallyLitModInitializer.getConfiguration().lanternConfiguration.extinguishInRainChance;
                item = ItemRegistry.UNLIT_LANTERN;
            } else if (stack.isOf(Items.TORCH)) {
                chance = TotallyLitModInitializer.getConfiguration().torchConfiguration.extinguishInRainChance;
                item = ItemRegistry.UNLIT_TORCH;
            } else if (stack.getItem() instanceof LitTorchItem) {
                chance = TotallyLitModInitializer.getConfiguration().torchConfiguration.extinguishInRainChance;
                item = ((LitTorchItem) stack.getItem()).getUnlitItem();
            }
            if (FabricLoader.getInstance().isModLoaded("teenycoal")) {
                if (stack.isOf(TeenyCoal.TEENY_TORCH.asItem())) {
                    chance = TotallyLitModInitializer.getConfiguration().teenytorchConfiguration.extinguishInRainChance;
                    item = TeenyItemRegistry.UNLIT_TEENY_TORCH;
                } else if (stack.getItem() instanceof LitTorchItem) {
                    chance = TotallyLitModInitializer.getConfiguration().teenytorchConfiguration.extinguishInRainChance;
                    item = ((LitTorchItem) stack.getItem()).getUnlitItem();
                }
            }

            if (chance != null && ((player.isSubmergedInWater() || player.isSwimming()) || (player.isTouchingWater() && world.getRandom().nextInt(100) == 0) || (player.age % 940 == 0 && world.hasRain(player.getCameraBlockPos()) && world.getRandom().nextFloat() < chance))) {
                player.getInventory().setStack(slot, new ItemStack(item, stack.getCount()));
            }
        }
    }
}
