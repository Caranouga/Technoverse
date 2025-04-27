package fr.caranouga.technoverse.blocks;

import fr.caranouga.technoverse.blocks.entity.SandingMachineBlockEntity;
import fr.caranouga.technoverse.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SandingMachine extends AbstractMachineBlock<SandingMachine, SandingMachineBlockEntity> {
    public SandingMachine(Properties pProperties) {
        super(pProperties, CommonBlockShape.BOX_SHAPE, SandingMachine::new, SandingMachineBlockEntity.class);
    }

    @Override
    protected ItemInteractionResult use(SandingMachineBlockEntity blockEntity, @NotNull ItemStack pStack, @NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        return CommonUses.openScreen(pLevel, pPlayer, blockEntity, pPos, "sanding_machine");
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;

        return createTickerHelper(pBlockEntityType, ModBlockEntities.SANDING_MACHINE_BE.get(),
                (level, blockPos, blockState, blockEntity) -> blockEntity.tick(level, blockPos, blockState));
    }
}
