package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Technoverse.MODID);

    public static final RegistryObject<Item> EXAMPLE_ITEM = registerItem("example_item");

    private static RegistryObject<Item> registerItem(String id){
        return ITEMS.register(id, () -> new Item(new Item.Properties()));
    }

    public static void register(IEventBus eBus) {
        ITEMS.register(eBus);
    }
}
