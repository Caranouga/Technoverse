package fr.caranouga.technoverse.worldgen;

import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeature {
    public static final ResourceKey<ConfiguredFeature<?, ?>> CARANITE_ORE_KEY = registerKey("caranite_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceable = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endstoneReplaceable = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> caraniteOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.CARANITE_ORE.get().defaultBlockState())
        );

        register(ctx, CARANITE_ORE_KEY, Feature.ORE, new OreConfiguration(caraniteOres, 6));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String id){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, id));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> ctx, ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature, FC featureConfiguration
    ) {
        ctx.register(key, new ConfiguredFeature<>(feature, featureConfiguration));
    }
}
