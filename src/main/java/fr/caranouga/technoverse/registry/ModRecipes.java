package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.recipes.sanding.SandingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Technoverse.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Technoverse.MODID);

    public static final RegistryObject<RecipeSerializer<SandingRecipe>> SANDING_SERIALIZER =
            SERIALIZERS.register("sanding", SandingRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<SandingRecipe>> SANDING_TYPE =
            TYPES.register("sanding", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "sanding";
                }
            });

    public static void register(IEventBus eBus) {
        SERIALIZERS.register(eBus);
        TYPES.register(eBus);
    }
}
