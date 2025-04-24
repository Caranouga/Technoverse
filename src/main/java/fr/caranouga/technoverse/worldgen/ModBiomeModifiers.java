package fr.caranouga.technoverse.worldgen;

import fr.caranouga.technoverse.Technoverse;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_CARANITE_ORE = registerKey("add_caranite_ore");

    public static void bootstrap(BootstrapContext<BiomeModifier> ctx){
        var placedFeature = ctx.lookup(Registries.PLACED_FEATURE);
        var biomes = ctx.lookup(Registries.BIOME);

        ctx.register(ADD_CARANITE_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.CARANITE_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES)
        );
    }

    private static ResourceKey<BiomeModifier> registerKey(String id) {;
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, id));
    }
}
