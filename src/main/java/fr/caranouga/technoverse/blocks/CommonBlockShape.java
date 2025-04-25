package fr.caranouga.technoverse.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CommonBlockShape {
    public static final VoxelShape BOX_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
}
