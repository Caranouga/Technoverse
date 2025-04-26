package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.screen.SandingMachineMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Technoverse.MODID);

    //public static final RegistryObject<MenuType<TestMenu>> TEST_MENU = register("test_menu", () -> IForgeMenuType.create(TestMenu::new));
    public static final RegistryObject<MenuType<SandingMachineMenu>> SANDING_MACHINE_MENU = register("sanding_machine_menu",
            () -> IForgeMenuType.create(SandingMachineMenu::new));

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id, Supplier<MenuType<T>> menuTypeSupplier) {
        return MENUS.register(id, menuTypeSupplier);
    }

    public static void register(IEventBus eBus){
        MENUS.register(eBus);
    }
}
