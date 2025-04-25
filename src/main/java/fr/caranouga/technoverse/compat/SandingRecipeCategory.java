package fr.caranouga.technoverse.compat;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.recipes.sanding.SandingRecipe;
import fr.caranouga.technoverse.registry.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SandingRecipeCategory implements IRecipeCategory<SandingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, "sanding");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, "textures/gui/jei/sanding.png");

    public static final RecipeType<SandingRecipe> RECIPE_TYPE = new RecipeType<SandingRecipe>(UID, SandingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public SandingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 76, 23);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.SAND_PAPER.get()));
    }

    @Override
    @NotNull
    public RecipeType<SandingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    @NotNull
    public Component getTitle() {
        return Component.translatable("jei." + Technoverse.MODID + ".sanding");
    }

    @Override
    @NotNull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SandingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 6).addIngredients(recipe.getIngredients().getFirst());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 6).addItemStack(recipe.getResultItem(null));
    }
}
