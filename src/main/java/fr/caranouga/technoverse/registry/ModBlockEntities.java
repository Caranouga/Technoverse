package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.blocks.entity.SandingMachineBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Technoverse.MODID);

    /*public static final RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BE = register("test_be", () ->
            BlockEntityType.Builder.of(TestBlockEntity::new, ModBlocks.TEST.get()).build(null));*/
    public static final RegistryObject<BlockEntityType<SandingMachineBlockEntity>> SANDING_MACHINE_BE = register("sanding_machine_be",
            () -> BlockEntityType.Builder.of(SandingMachineBlockEntity::new, ModBlocks.SANDING_MACHINE.get()).build(null));

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> type) {
        return BLOCK_ENTITIES.register(id, type);
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
