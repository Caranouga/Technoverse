package fr.caranouga.technoverse.screen;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.blocks.entity.TestBlockEntity;
import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class TestMenu extends AbstractContainerMenu {
    public final TestBlockEntity blockEntity;
    private final Level level;

    public TestMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public TestMenu(int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.TEST_MENU.get(), pContainerId);

        this.blockEntity = (TestBlockEntity) blockEntity;
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 80, 35));
    }

    /**
     * Thanks to diesieben07 for this method (<a href="https://github.com/diesieben07/SevenCommons">SevenCommons</a>)
     */
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_IDX = 0;
    private static final int BE_INVENTORY_FIRST_SLOT_IDX = VANILLA_FIRST_SLOT_IDX + VANILLA_SLOT_COUNT;

    private static final int BE_INVENTORY_SLOT_COUNT = 1;

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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.TEST.get());
    }

    private void addPlayerInventory(Inventory pInv) {
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                this.addSlot(new Slot(pInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory pInv){
        for(int i = 0; i < 9; ++i){
            this.addSlot(new Slot(pInv, i, 8 + i * 18, 142));
        }
    }
}
