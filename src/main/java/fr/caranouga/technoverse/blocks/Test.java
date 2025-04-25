package fr.caranouga.technoverse.blocks;

import com.mojang.serialization.MapCodec;
import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.blocks.entity.TestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Test extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);
    public static final MapCodec<Test> CODEC = simpleCodec(Test::new);

    public Test(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    protected VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    protected RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new TestBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()){
            if(pLevel.getBlockEntity(pPos) instanceof TestBlockEntity blockEntity){
                blockEntity.drops();
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    @NotNull
    protected ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, Level pLevel,
                                              @NotNull BlockPos pPos, @NotNull Player pPlayer,
                                              @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof TestBlockEntity blockEntity){
            if(pPlayer.isCrouching()){
                if(!pLevel.isClientSide()) {
                    ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(blockEntity, Component.translatable("container." + Technoverse.MODID + ".test")), pPos);
                }

                return ItemInteractionResult.SUCCESS;
            }

            if(blockEntity.inventory.getStackInSlot(0).isEmpty() && !pStack.isEmpty()){
                blockEntity.inventory.insertItem(0, pStack.copy(), false);
                pStack.shrink(1);
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1F, 2F);
                return ItemInteractionResult.SUCCESS;
            } else if(pStack.isEmpty()){
                ItemStack onTest = blockEntity.inventory.extractItem(0, 1, false);
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, onTest);
                blockEntity.clearContents();
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1F, 1F);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }
}
