package fr.caranouga.technoverse.blocks.entity;

import fr.caranouga.technoverse.recipes.sanding.SandingRecipe;
import fr.caranouga.technoverse.recipes.sanding.SandingRecipeInput;
import fr.caranouga.technoverse.registry.ModBlockEntities;
import fr.caranouga.technoverse.registry.ModRecipes;
import fr.caranouga.technoverse.screen.SandingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SandingMachineBlockEntity extends AbstractMachineBlockEntity<SandingMachineMenu> {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public SandingMachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SANDING_MACHINE_BE.get(), "sanding_machine", pPos, pBlockState, 2, SandingMachineMenu.class, SandingMachineBlockEntity.class);

        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SandingMachineBlockEntity.this.progress;
                    case 1 -> SandingMachineBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex){
                    case 0: SandingMachineBlockEntity.this.progress = pValue;
                    case 1: SandingMachineBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected SandingMachineBlockEntity getBlockEntityInstance() {
        return this;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("sanding_machine.progress", progress);
        pTag.putInt("sanding_machine.max_progress", maxProgress);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        progress = pTag.getInt("sanding_machine.progress");
        maxProgress = pTag.getInt("sanding_machine.max_progress");
    }

    // TODO: Debug this
    public void tick(Level level, BlockPos pos, BlockState state){
        if(hasRecipe()){
            setPoweredState(pos, state, true);
            increaseCraftingProgress();
            setChanged(level, pos, state);

            if(hasCraftingFinished()){
                craftItem();
                resetProgress(pos, state);
            }
        }else{
            resetProgress(pos, state);
        }
    }

    // TODO: Debug this
    private void setPoweredState(BlockPos pos, BlockState state, boolean stateValue) {
        level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 3);
        /*state.setValue(BlockStateProperties.LIT, stateValue);
        level.setBlockAndUpdate(worldPosition, state);*/
    }

    private void resetProgress(BlockPos pos, BlockState state) {
        this.progress = 0;
        this.maxProgress = 72;

        setPoweredState(pos, state, false);
    }

    private void craftItem() {
        this.itemHandler.insertItem(OUTPUT_SLOT, getRecipe().get().value().output().copy(), false);
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<SandingRecipe>> recipe = getRecipe();
        return recipe.isPresent() && canInsertItemIntoOutputSlot(recipe.get().value().output());
    }

    private Optional<RecipeHolder<SandingRecipe>> getRecipe(){
        return this.level.getRecipeManager().getRecipeFor(
                ModRecipes.SANDING_TYPE.get(), new SandingRecipeInput(itemHandler.getStackInSlot(INPUT_SLOT)), level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        ItemStack outputSlotItem = this.itemHandler.getStackInSlot(OUTPUT_SLOT);

        boolean isEmpty = outputSlotItem.isEmpty();
        int maxCount = isEmpty ? this.itemHandler.getSlotLimit(OUTPUT_SLOT) : outputSlotItem.getMaxStackSize();
        int currentCount = outputSlotItem.getCount();

        return isEmpty || (outputSlotItem.getItem() == output.getItem() && maxCount >= currentCount + output.getCount());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new SandingMachineMenu(pContainerId, pPlayerInventory, getBlockEntityInstance(), this.data);
    }
}
