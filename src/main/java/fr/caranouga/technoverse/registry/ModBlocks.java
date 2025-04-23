package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Technoverse.MODID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK = registerBlock("example_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()
                    .sound(SoundType.ANVIL)));

    // region Utility methods
    private static <T extends Block> RegistryObject<T> registerBlock(String id, Supplier<T> block){
        RegistryObject<T> blockObject = BLOCKS.register(id, block);
        registerBlockItem(id, blockObject);
        return blockObject;
    }

    private static <T extends Block> void registerBlockItem(String id, RegistryObject<T> block){
        ModItems.ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    // endregion

    public static void register(IEventBus eBus) {
        BLOCKS.register(eBus);
    }
}
