package fr.caranouga.technoverse.datagen.providers;

import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        ModBlocks.DROP_SELF_BLOCKS.forEach(block -> {
            dropSelf(block.get());
        });

        this.add(ModBlocks.CARANITE_ORE.get(), block -> createOreDrop(block, ModItems.IMPURE_CARANITE.get()));
    }

    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
