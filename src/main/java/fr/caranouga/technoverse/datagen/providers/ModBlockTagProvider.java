package fr.caranouga.technoverse.datagen.providers;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Technoverse.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.EXAMPLE_BLOCK.get())
                .add(ModBlocks.EXAMPLE_BLOCK2.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.EXAMPLE_BLOCK.get())
                .add(ModBlocks.EXAMPLE_BLOCK2.get());

    }
}
