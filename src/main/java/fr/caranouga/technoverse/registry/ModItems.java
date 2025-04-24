package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Technoverse.MODID);
    public static final ArrayList<RegistryObject<? extends Item>> IN_CREATIVE_TAB = new ArrayList<>();

    public static final RegistryObject<Item> CARANITE = registerItem("caranite");
    public static final RegistryObject<Item> CARANITE_NUGGET = registerItem("caranite_nugget");

    // region Utility methods
    private static RegistryObject<Item> registerItem(String id){
        return registerItem(id, false);
    }

    private static RegistryObject<Item> registerItem(String id, boolean notInCreativeTab){
        RegistryObject<Item> item = ITEMS.register(id, () -> new Item(new Item.Properties()));

        if (!notInCreativeTab) IN_CREATIVE_TAB.add(item);

        return item;
    }
    // endregion

    public static void register(IEventBus eBus) {
        ITEMS.register(eBus);
    }
}
