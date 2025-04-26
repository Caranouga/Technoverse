package fr.caranouga.technoverse.blocks.entity;

import fr.caranouga.technoverse.registry.ModBlockEntities;
import fr.caranouga.technoverse.screen.SandingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class SandingMachineBlockEntity extends AbstractMachineBlockEntity<SandingMachineMenu> {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    public SandingMachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SANDING_MACHINE_BE.get(), "sanding_machine", pPos, pBlockState, 2, SandingMachineMenu.class, SandingMachineBlockEntity.class);
    }

    @Override
    protected SandingMachineBlockEntity getBlockEntityInstance() {
        return this;
    }
}
