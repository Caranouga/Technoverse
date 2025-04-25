package fr.caranouga.technoverse.datagen.builders;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.recipes.sanding.SandingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SandingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final Ingredient ingredient;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SandingRecipeBuilder(RecipeCategory pCategory, Ingredient pIngredient, ItemLike pResult, int pCount) {
        this.category = pCategory;
        this.ingredient = pIngredient;
        this.result = pResult.asItem();
        this.count = pCount;
    }

    public static SandingRecipeBuilder sanding(RecipeCategory pCategory, ItemLike pIngredient, ItemLike pResult, int pCount) {
        return new SandingRecipeBuilder(pCategory, Ingredient.of(pIngredient), pResult, pCount);
    }

    @Override
    @NotNull
    public RecipeBuilder unlockedBy(@NotNull String pName, @NotNull Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);

        return this;
    }

    @Override
    @NotNull
    public RecipeBuilder group(@Nullable String pGroupName) {
        Technoverse.LOGGER.warn("SandingRecipeBuilder does not support groups");
        return this;
    }

    @Override
    @NotNull
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, @NotNull ResourceLocation pId) {
        this.ensureValid(pId);
        Advancement.Builder advBuilder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advBuilder::addCriterion);

        SandingRecipe sandingRecipe = new SandingRecipe(
                this.ingredient,
                new ItemStack(this.result, this.count)
        );
        pRecipeOutput.accept(pId, sandingRecipe, advBuilder.build(pId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }
}
