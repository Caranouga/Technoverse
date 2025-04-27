package fr.caranouga.technoverse.screen;

import fr.caranouga.technoverse.blocks.AbstractMachineBlock;
import fr.caranouga.technoverse.blocks.entity.AbstractMachineBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMachineMenu<M extends AbstractMachineMenu<M, BE, B>, BE extends AbstractMachineBlockEntity<M>, B extends AbstractMachineBlock<B, BE>> extends AbstractContainerMenu {
    public final BE blockEntity;
    private final Level level;
    private final B block;

    public AbstractMachineMenu(MenuType<M> menuType, int pContainerId, Inventory inv, BE blockEntity, B block) {
        super(menuType, pContainerId);

        this.blockEntity = blockEntity;
        this.level = inv.player.level();
        this.block = block;

        addBeforeVanilla();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }


    // Function from CofhCore
    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            if (!performMerge(pIndex, stackInSlot)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stackInSlot, stack);

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(pPlayer, stackInSlot);
        }
        return stack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, block);
    }

    protected void addBeforeVanilla(){
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    protected abstract int getNumberOfSlots();

    protected boolean performMerge(int index, ItemStack stack) {
        int invBase = getNumberOfSlots();

        int invFull = slots.size();
        int invHotbar = invFull - 9;
        int invPlayer = invHotbar - 27;

        if (index < invPlayer) {
            return moveItemStackTo(stack, invPlayer, invFull, false);
        } else {
            return moveItemStackTo(stack, 0, invBase, false);
        }
    }
}