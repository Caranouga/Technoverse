package fr.caranouga.technoverse.screen;

import fr.caranouga.technoverse.Technoverse;
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
    private int invSize;

    public AbstractMachineMenu(MenuType<M> menuType, int pContainerId, Inventory inv, FriendlyByteBuf extraData, B block) {
        this(menuType, pContainerId, inv, (BE) inv.player.level().getBlockEntity(extraData.readBlockPos()), block);
    }

    public AbstractMachineMenu(MenuType<M> menuType, int pContainerId, Inventory inv, BE blockEntity, B block) {
        super(menuType, pContainerId);

        this.blockEntity = blockEntity;
        this.level = inv.player.level();
        this.block = block;
        this.invSize = blockEntity.itemHandler.getSlots();

        addBeforeVanilla();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addAfterVanilla();
    }

    /**
     * Thanks to diesieben07 for this method (<a href="https://github.com/diesieben07/SevenCommons">SevenCommons</a>)
     */
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT; // 27
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT; // 36
    private static final int VANILLA_FIRST_SLOT_IDX = 0;
    private static final int BE_INVENTORY_FIRST_SLOT_IDX = VANILLA_FIRST_SLOT_IDX + VANILLA_SLOT_COUNT; // 36

    private final int BE_INVENTORY_SLOT_COUNT = this.invSize;

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if(!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if(pIndex < BE_INVENTORY_FIRST_SLOT_IDX){
            // The slot clicked is a vanilla slot
            if(!moveItemStackTo(sourceStack, BE_INVENTORY_FIRST_SLOT_IDX, BE_INVENTORY_FIRST_SLOT_IDX + BE_INVENTORY_SLOT_COUNT, false)){
                return ItemStack.EMPTY;
            }
        } else if (pIndex < BE_INVENTORY_FIRST_SLOT_IDX + BE_INVENTORY_SLOT_COUNT) {
            // The slot clicked is a BlockEntity slot
            if(!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_IDX, BE_INVENTORY_FIRST_SLOT_IDX, false)){
                return ItemStack.EMPTY;
            }
        }else{
            Technoverse.LOGGER.warn("Invalid slotIndex: {}", pIndex);
            return ItemStack.EMPTY;
        }

        if(sourceStack.getCount() == 0){
            sourceSlot.set(ItemStack.EMPTY);
        }else{
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(pPlayer, sourceStack);

        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, block);
    }

    protected void addBeforeVanilla(){
    }

    protected void addAfterVanilla(){
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
}