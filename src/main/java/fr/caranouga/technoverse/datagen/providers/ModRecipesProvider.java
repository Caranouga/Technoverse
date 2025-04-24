package fr.caranouga.technoverse.datagen.providers;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
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
        storageBlock(ModBlocks.CARANITE_BLOCK.get(), ModItems.CARANITE.get(), pRecipeOutput);
        nuggetsToIngot(ModItems.CARANITE_NUGGET.get(), ModItems.CARANITE.get(), pRecipeOutput);
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

    private void nuggetsToIngot(Item nugget, Item ingot, RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', nugget)
                .unlockedBy(getHasName(nugget), has(nugget))
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(ingot) + "_from_" + getItemName(nugget));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
                .requires(ingot)
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(nugget) + "_from_" + getItemName(ingot));
    }
}
