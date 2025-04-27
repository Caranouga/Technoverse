package fr.caranouga.technoverse.registry;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.blocks.SandingMachine;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

import static fr.caranouga.technoverse.registry.ModItems.IN_CREATIVE_TAB;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Technoverse.MODID);
    public static final ArrayList<RegistryObject<? extends Block>> DROP_SELF_BLOCKS = new ArrayList<>();
    public static final ArrayList<RegistryObject<? extends Block>> DEFAULT_BLOCKSTATE = new ArrayList<>();

    // STORAGE BLOCKS
    public static final RegistryObject<Block> CARANITE_BLOCK = registerBlock("caranite_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    // ORES
    public static final RegistryObject<Block> CARANITE_ORE = registerBlock("caranite_ore",
            () -> new DropExperienceBlock(
                    ConstantInt.of(0),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)
            ));

    // MACHINES
    public static final RegistryObject<SandingMachine> SANDING_MACHINE = registerBlock("sanding_machine",
            () -> new SandingMachine(BlockBehaviour.Properties.of().noOcclusion()), false, true);

    // region Utility methods
    private static <T extends Block> RegistryObject<T> registerBlock(String id, Supplier<T> block){
        return registerBlock(id, block, false);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String id, Supplier<T> block, boolean doNotDropSelf, boolean nonDefaultState) {
        RegistryObject<T> blockObject = BLOCKS.register(id, block);
        registerBlockItem(id, blockObject);

        if (!doNotDropSelf) DROP_SELF_BLOCKS.add(blockObject);
        if (!nonDefaultState) DEFAULT_BLOCKSTATE.add(blockObject);

        return blockObject;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String id, Supplier<T> block, boolean doNotDropSelf) {
        return registerBlock(id, block, doNotDropSelf, false);
    }

    private static <T extends Block> void registerBlockItem(String id, RegistryObject<T> block){
        RegistryObject<Item> blockItem = ModItems.ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));

        IN_CREATIVE_TAB.add(blockItem);
    }
    // endregion

    public static void register(IEventBus eBus) {
        BLOCKS.register(eBus);
    }
}
