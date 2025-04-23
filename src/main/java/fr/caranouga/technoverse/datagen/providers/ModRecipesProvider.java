package fr.caranouga.technoverse.datagen.providers;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NonNull RecipeOutput pRecipeOutput) {
        storageBlock(ModBlocks.EXAMPLE_BLOCK.get(), ModItems.EXAMPLE_ITEM.get(), pRecipeOutput);
        storageBlock(ModBlocks.EXAMPLE_BLOCK2.get(), Items.DIRT , pRecipeOutput);
    }

    private void storageBlock(Block block, Item item, RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', item)
                .unlockedBy(getHasName(item), has(item))
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(block) + "_from_" + getItemName(item));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, 9)
                .requires(block)
                .unlockedBy(getHasName(block), has(block))
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(item) + "_from_" + getItemName(block));
    }
}
