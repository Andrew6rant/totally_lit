package io.github.realguyman.totally_lit.mixin;

import io.github.Andrew6rant.teenycoal.TeenyCoal;
import io.github.realguyman.totally_lit.item.LitTorchItem;
import io.github.realguyman.totally_lit.registry.ItemRegistry;
import io.github.realguyman.totally_lit.registry.TeenyItemRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    protected ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @Shadow public abstract void setStack(ItemStack stack);

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (isSubmergedInWater()) {
            Item unlitVariant = null;
            final ItemStack itemStack = getStack();

            if (itemStack.isOf(Items.TORCH)) {
                unlitVariant = ItemRegistry.UNLIT_TORCH;
            } else if (itemStack.getItem() instanceof LitTorchItem) {
                unlitVariant = ((LitTorchItem) itemStack.getItem()).getUnlitItem();
            } else if (itemStack.isOf(Items.LANTERN)) {
                unlitVariant = ItemRegistry.UNLIT_LANTERN;
            } else if (itemStack.isOf(Items.JACK_O_LANTERN)) {
                unlitVariant = Items.CARVED_PUMPKIN;
            }
            if (FabricLoader.getInstance().isModLoaded("teenycoal")) {
                if (itemStack.isOf(TeenyCoal.TEENY_TORCH.asItem())) {
                    unlitVariant = TeenyItemRegistry.UNLIT_TEENY_TORCH;
                }
            }

            if (unlitVariant != null) {
                setStack(new ItemStack(unlitVariant, itemStack.getCount()));
            }
        }
    }
}
