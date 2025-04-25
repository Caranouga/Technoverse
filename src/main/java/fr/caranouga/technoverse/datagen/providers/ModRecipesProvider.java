package fr.caranouga.technoverse.datagen.providers;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.datagen.builders.SandingRecipeBuilder;
import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
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

        SandingRecipeBuilder.sanding(RecipeCategory.MISC, ModItems.IMPURE_CARANITE.get(), ModItems.CARANITE.get(), 1)
                .unlockedBy(getHasName(ModItems.IMPURE_CARANITE.get()), has(ModItems.IMPURE_CARANITE.get()))
                .save(pRecipeOutput, Technoverse.MODID + ":sanding_" + getItemName(ModItems.CARANITE.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SAND_PAPER.get(), 1)
                .requires(ItemTags.SAND)
                .requires(Items.PAPER)
                .unlockedBy("has_sand", has(ItemTags.SAND))
                .save(pRecipeOutput);
    }

    private void storageBlock(Block block, Item item, RecipeOutput pRecipeOutput) {
        nineByNine(item, block)
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(block) + "_from_" + getItemName(item));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, 9)
                .requires(block)
                .unlockedBy(getHasName(block), has(block))
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(item) + "_from_" + getItemName(block));
    }

    private void nuggetsToIngot(Item nugget, Item ingot, RecipeOutput pRecipeOutput) {
        nineByNine(nugget, ingot)
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(ingot) + "_from_" + getItemName(nugget));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
                .requires(ingot)
                .unlockedBy(getHasName(ingot), has(ingot))
                .save(pRecipeOutput, Technoverse.MODID + ":" + getItemName(nugget) + "_from_" + getItemName(ingot));
    }

    private ShapedRecipeBuilder nineByNine(ItemLike nine, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', nine)
                .unlockedBy(getHasName(nine), has(nine));
    }
}
