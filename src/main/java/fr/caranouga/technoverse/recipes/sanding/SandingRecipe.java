package fr.caranouga.technoverse.recipes.sanding;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.caranouga.technoverse.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

// TODO: Add abstraction
public record SandingRecipe(Ingredient input, ItemStack output) implements Recipe<SandingRecipeInput> {
    @Override
    @NotNull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);

        return ingredients;
    }

    @Override
    public boolean matches(@NotNull SandingRecipeInput pInput, @NotNull Level pLevel) {
        if(pLevel.isClientSide()) return false;

        return input.test(pInput.getItem(0));
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull SandingRecipeInput pInput, @NotNull HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    @NotNull
    public ItemStack getResultItem(@NotNull HolderLookup.Provider pRegistries) {
        return output;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SANDING_SERIALIZER.get();
    }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return ModRecipes.SANDING_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SandingRecipe> {
        public static final MapCodec<SandingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SandingRecipe::input),
                ItemStack.CODEC.fieldOf("result").forGetter(SandingRecipe::output)
        ).apply(instance, SandingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SandingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, SandingRecipe::input,
                ItemStack.STREAM_CODEC, SandingRecipe::output,
                SandingRecipe::new
        );

        @Override
        @NotNull
        public MapCodec<SandingRecipe> codec() {
            return CODEC;
        }

        @Override
        @NotNull
        public StreamCodec<RegistryFriendlyByteBuf, SandingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
