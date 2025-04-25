package fr.caranouga.technoverse.compat;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.recipes.sanding.SandingRecipe;
import fr.caranouga.technoverse.registry.ModItems;
import fr.caranouga.technoverse.registry.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JeiTechnoverseModPlugin implements IModPlugin {
    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<SandingRecipe> sandingRecipes = recipeManager.getAllRecipesFor(ModRecipes.SANDING_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(SandingRecipeCategory.RECIPE_TYPE, sandingRecipes);
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        // Register JEI GUI handlers here
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.SAND_PAPER.get().asItem()), SandingRecipeCategory.RECIPE_TYPE);
    }
}
