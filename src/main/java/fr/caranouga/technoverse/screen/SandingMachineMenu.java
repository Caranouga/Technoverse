package fr.caranouga.technoverse.screen;

import fr.caranouga.technoverse.blocks.SandingMachine;
import fr.caranouga.technoverse.blocks.entity.SandingMachineBlockEntity;
import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

public class SandingMachineMenu extends AbstractMachineMenu<SandingMachineMenu, SandingMachineBlockEntity, SandingMachine> {
    public SandingMachineMenu(int pContainerId, Inventory inv, SandingMachineBlockEntity blockEntity) {
        super(ModMenuTypes.SANDING_MACHINE_MENU.get(), pContainerId, inv, blockEntity, ModBlocks.SANDING_MACHINE.get());
    }

    public SandingMachineMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        super(ModMenuTypes.SANDING_MACHINE_MENU.get(), pContainerId, inv, extraData, ModBlocks.SANDING_MACHINE.get());
    }

    @Override
    protected void addBeforeVanilla() {
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 0, 41, 35));
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 1, 121, 35));
    }
}
