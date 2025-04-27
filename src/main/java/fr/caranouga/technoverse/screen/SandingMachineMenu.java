package fr.caranouga.technoverse.screen;

import fr.caranouga.technoverse.blocks.SandingMachine;
import fr.caranouga.technoverse.blocks.entity.SandingMachineBlockEntity;
import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.items.SlotItemHandler;

import static fr.caranouga.technoverse.screen.AbstractMachineScreen.ARROW_WIDTH;

public class SandingMachineMenu extends AbstractMachineMenu<SandingMachineMenu, SandingMachineBlockEntity, SandingMachine> {
    private final ContainerData data;

    public SandingMachineMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, (SandingMachineBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public SandingMachineMenu(int pContainerId, Inventory inv, SandingMachineBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.SANDING_MACHINE_MENU.get(), pContainerId, inv, blockEntity, ModBlocks.SANDING_MACHINE.get());

        this.data = data;
        addDataSlots(data);
    }

    @Override
    protected void addBeforeVanilla() {
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 0, 41, 35));
        this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, 1, 121, 35));
    }

    @Override
    protected int getNumberOfSlots() {
        return 2;
    }

    protected boolean isCrafting(){
        return this.data.get(0) > 0;
    }

    protected int getScaledArrowProgress(){
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);

        return maxProgress != 0 && progress != 0 ? progress * ARROW_WIDTH / maxProgress : 0;
    }
}