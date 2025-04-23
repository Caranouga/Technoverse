package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Technoverse.MODID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = TABS.register("example_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.EXAMPLE_ITEM.get()))
            .title(Component.translatable("creativetab." + Technoverse.MODID + ".example_tab"))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.EXAMPLE_ITEM.get());
                output.accept(ModBlocks.EXAMPLE_BLOCK.get());
            }).build());
    /*public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = registerTab("example_tab", ModItems.EXAMPLE_ITEM.get());

    private static RegistryObject<CreativeModeTab> registerTab(String id, Item icon) {
        return TABS.register(id, () -> CreativeModeTab.builder()
                .icon(() -> new ItemStack(icon))
                .title(Component.translatable("creativetab." + Technoverse.MODID + "." + id))
                .displayItems(((parameters, output) -> {
                    output.accept(ModItems.EXAMPLE_ITEM.get());
                    output.accept(ModBlocks.EXAMPLE_BLOCK.get());
                }))
                .build());
    }*/

    public static void register(IEventBus eBus) {
        TABS.register(eBus);
    }
}
