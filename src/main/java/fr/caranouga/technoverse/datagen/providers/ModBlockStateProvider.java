package fr.caranouga.technoverse.datagen.providers;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Technoverse.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.DEFAULT_BLOCKSTATE.forEach(block -> blockWithItem(block.get()));

        createMachine(ModBlocks.SANDING_MACHINE);
    }

    private void createMachine(RegistryObject<? extends Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();
        String blockName = blockRegistryObject.getId().getPath();

        this.getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    boolean isActive = state.getValue(BlockStateProperties.LIT);
                    int yRot = (int) facing.toYRot();

                    String frontTexture = blockName + (isActive ? "_front_on" : "_front");
                    String sideTexture = blockName + (isActive ? "_side_on" : "_side");

                    return ConfiguredModel.builder()
                            .modelFile(models().withExistingParent(blockName + "_" + (isActive ? "on" : "off"), modLoc("block/machine_template"))
                                    .texture("front", modLoc("block/" + frontTexture))
                                    .texture("side", modLoc("block/" + sideTexture))
                            )
                            .rotationY(yRot)
                            .build();
                });
    }

    private void blockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    private static PropertyDispatch createHorizontalFacingDispatch() {
        return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                .select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.NORTH, Variant.variant());
    }
}
