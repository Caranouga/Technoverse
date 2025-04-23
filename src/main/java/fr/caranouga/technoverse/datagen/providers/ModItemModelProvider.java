package fr.caranouga.technoverse.datagen.providers;


import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.registry.ModBlocks;
import fr.caranouga.technoverse.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Technoverse.MODID, exFileHelper);
    }


    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(entry -> {
            basicItem(entry.get());
        });
    }
}
