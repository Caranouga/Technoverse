package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.items.SandPaperItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Technoverse.MODID);
    public static final ArrayList<RegistryObject<? extends Item>> IN_CREATIVE_TAB = new ArrayList<>();

    public static final RegistryObject<Item> CARANITE = registerItem("caranite");
    public static final RegistryObject<Item> CARANITE_NUGGET = registerItem("caranite_nugget");
    public static final RegistryObject<Item> IMPURE_CARANITE = registerItem("impure_caranite");

    public static final RegistryObject<SandPaperItem> SAND_PAPER = registerItem("sand_paper", () -> new SandPaperItem(), false);

    // region Utility methods
    private static RegistryObject<Item> registerItem(String id){
        return registerItem(id, false);
    }

    private static RegistryObject<Item> registerItem(String id, boolean notInCreativeTab){
        return registerItem(id, () -> new Item(new Item.Properties()), notInCreativeTab);
    }

    private static <T extends Item> RegistryObject<T> registerItem(String id, Supplier<T> item, boolean noInCreativeTab) {
        RegistryObject<T> itemReg = ITEMS.register(id, item);

        if (!noInCreativeTab) IN_CREATIVE_TAB.add(itemReg);

        return itemReg;

    }
    // endregion

    public static void register(IEventBus eBus) {
        ITEMS.register(eBus);
    }
}
