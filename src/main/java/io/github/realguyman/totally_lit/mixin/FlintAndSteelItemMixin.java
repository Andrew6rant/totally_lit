package io.github.realguyman.totally_lit.mixin;

import io.github.Andrew6rant.teenycoal.TeenyCoal;
import io.github.Andrew6rant.teenycoal.block.TeenyWallTorch;
import io.github.realguyman.totally_lit.block.LitTorchBlock;
import io.github.realguyman.totally_lit.block.LitWallTorchBlock;
import io.github.realguyman.totally_lit.registry.BlockRegistry;
import io.github.realguyman.totally_lit.registry.TagRegistry;
import io.github.realguyman.totally_lit.registry.TeenyBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void lightUnlitTorchBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Hand hand = context.getHand();
        boolean updated = false;

        if(state.isOf(BlockRegistry.UNLIT_LANTERN) && Boolean.TRUE.equals(!state.get(LanternBlock.WATERLOGGED))) {
            updated = world.setBlockState(pos, Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, state.get(LanternBlock.HANGING)));
        } else if (state.isOf(BlockRegistry.UNLIT_TORCH)) {
            updated = world.setBlockState(pos, Blocks.TORCH.getDefaultState());
        } else if (state.isOf(BlockRegistry.UNLIT_WALL_TORCH)) {
            updated = world.setBlockState(pos, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING)));
        } else if (state.isIn(TagRegistry.EXTINGUISHABLE_TORCH_BLOCKS)) {
            if (state.getBlock() instanceof LitTorchBlock litTorch) {
                updated = world.setBlockState(pos, litTorch.getDefaultState());
            } else if (state.getBlock() instanceof LitWallTorchBlock litWallTorchBlock) {
                updated = world.setBlockState(pos, litWallTorchBlock.getUnlitBlock().getDefaultState().with(Properties.FACING, state.get(Properties.FACING)));
            }
        }
        if (FabricLoader.getInstance().isModLoaded("teenycoal")) {
            if (state.isOf(TeenyBlockRegistry.UNLIT_TEENY_TORCH)) {
                updated = world.setBlockState(pos, TeenyCoal.TEENY_TORCH.getDefaultState());
            } else if (state.isOf(TeenyBlockRegistry.UNLIT_TEENY_WALL_TORCH)) {
                updated = world.setBlockState(pos, TeenyCoal.TEENY_WALL_TORCH.getDefaultState().with(TeenyWallTorch.FACING, state.get(TeenyWallTorch.FACING)));
            }
        }

        if (updated) {
            PlayerEntity player = context.getPlayer();

            if (player != null) {
                ItemStack itemInHand = player.getStackInHand(hand);

                if (itemInHand != null) {
                    itemInHand.damage(1, player, playerInScope -> playerInScope.sendToolBreakStatus(hand));
                    world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}
