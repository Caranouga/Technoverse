package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Technoverse.MODID);

    public static final RegistryObject<DataComponentType<ItemStack>> POLISHING = register("polishing", builder -> builder.persistent(ItemStack.CODEC));

    // region Utility methods
    private static <T> RegistryObject<DataComponentType<T>> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return DATA_COMPONENTS.register(id, () -> builderOperator.apply(DataComponentType.builder()).build());
    }
    // endregion

    public static void register(IEventBus eBus) {
        DATA_COMPONENTS.register(eBus);
    }
}
