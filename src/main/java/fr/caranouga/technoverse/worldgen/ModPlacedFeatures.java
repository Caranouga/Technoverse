package fr.caranouga.technoverse.worldgen;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> CARANITE_ORE_PLACED_KEY = registerKey("caranite_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> ctx) {
        var configuredFeatures = ctx.lookup(Registries.CONFIGURED_FEATURE);

        register(ctx, CARANITE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.CARANITE_ORE_KEY),
                ModOrePlacement.commonOrePlacement(
                        6,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(80))
                ));
    }

    public static ResourceKey<PlacedFeature> registerKey(String id){
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, id));
    }

    private static void register(BootstrapContext<PlacedFeature> ctx, ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        ctx.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
