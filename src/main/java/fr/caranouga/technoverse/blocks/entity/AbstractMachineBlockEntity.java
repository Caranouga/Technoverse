package fr.caranouga.technoverse.blocks.entity;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.screen.AbstractMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractMachineBlockEntity<M extends AbstractMachineMenu<M, ?, ?>> extends BlockEntity implements MenuProvider {
    public ItemStackHandler itemHandler;
    private LazyOptional<IItemHandler> lanzyItemHandler = LazyOptional.empty();

    private final String id;
    private final Class<M> menuClass;
    private final Class<? extends AbstractMachineBlockEntity<M>> beClass;

    public AbstractMachineBlockEntity(BlockEntityType<?> beType, String id, BlockPos pPos, BlockState pBlockState, int inventorySize, Class<M> menuClass, Class<? extends AbstractMachineBlockEntity<M>> beClass) {
        super(beType, pPos, pBlockState);

        this.itemHandler = new ItemStackHandler(inventorySize){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        };
        this.id = id;
        this.menuClass = menuClass;
        this.beClass = beClass;
    }

    public void clearContents(){
        for(int i = 0; i < this.itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void drops(){
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    // region Save and load
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, @NotNull HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, @NotNull HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }
    // endregion

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lanzyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lanzyItemHandler.invalidate();
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return Component.translatable("container." + Technoverse.MODID + "." + id);
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        try {
            Constructor<?> constructor = this.menuClass.getConstructor(int.class, Inventory.class, beClass);

            return (AbstractContainerMenu) constructor.newInstance(pContainerId, pPlayerInventory, getBlockEntityInstance());
        }catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException err){
            Technoverse.LOGGER.error("Error during reflection to create menu:", err);
        }

        return null;
    }

    protected abstract AbstractMachineBlockEntity<M> getBlockEntityInstance();
}
