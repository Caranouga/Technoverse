package fr.caranouga.technoverse.datagen.providers;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static fr.caranouga.technoverse.registry.ModBlocks.CARANITE_BLOCK;
import static fr.caranouga.technoverse.registry.ModBlocks.CARANITE_ORE;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Technoverse.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                CARANITE_BLOCK.get(),
                CARANITE_ORE.get()
        );

        tag(BlockTags.NEEDS_IRON_TOOL).add(
                CARANITE_BLOCK.get(),
                CARANITE_ORE.get()
        );
    }
}
