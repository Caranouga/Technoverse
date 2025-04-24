package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Technoverse.MODID);

    public static final RegistryObject<CreativeModeTab> TECHNOVERSE_TAB = register("technoverse_tab",
            () -> new ItemStack(ModItems.CARANITE.get()),
            (parameters, output) -> {
                ModItems.IN_CREATIVE_TAB.forEach(item -> output.accept(item.get()));
            });
    /*public static final RegistryObject<CreativeModeTab> TECHNOVERSE_TAB = TABS.register("technoverse_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.CARANITE.get()))
            .title(Component.translatable("creativetab." + Technoverse.MODID + ".technoverse_tab"))
            .displayItems((parameters, output) -> {
                ModItems.IN_CREATIVE_TAB.forEach(item -> output.accept(item.get()));
            }).build());*/

    // region Utility methods
    private static RegistryObject<CreativeModeTab> register(String id, Supplier<ItemStack> icon,  CreativeModeTab.DisplayItemsGenerator generator) {
        return TABS.register(id, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("creativetab." + Technoverse.MODID + "." + id))
                .displayItems(generator)
                .build());
    }
    // endregion

    public static void register(IEventBus eBus) {
        TABS.register(eBus);
    }
}
