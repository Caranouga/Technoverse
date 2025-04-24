package fr.caranouga.technoverse.recipes.sanding;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record SandingRecipeInput(ItemStack input) implements RecipeInput {

    @Override
    @NotNull
    public ItemStack getItem(int pIndex) {
        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}
