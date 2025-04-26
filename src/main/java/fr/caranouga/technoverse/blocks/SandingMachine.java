package fr.caranouga.technoverse.blocks;

import fr.caranouga.technoverse.blocks.entity.SandingMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class SandingMachine extends AbstractMachineBlock<SandingMachine, SandingMachineBlockEntity> {
    public SandingMachine(Properties pProperties) {
        super(pProperties, CommonBlockShape.BOX_SHAPE, SandingMachine::new, SandingMachineBlockEntity.class);
    }

    @Override
    protected ItemInteractionResult use(SandingMachineBlockEntity blockEntity, @NotNull ItemStack pStack, @NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        return CommonUses.openScreen(pLevel, pPlayer, blockEntity, pPos, "sanding_machine");
    }
}
