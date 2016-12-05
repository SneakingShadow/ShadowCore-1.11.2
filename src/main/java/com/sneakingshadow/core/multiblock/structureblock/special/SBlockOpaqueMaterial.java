package com.sneakingshadow.core.multiblock.structureblock.special;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import net.minecraft.world.World;

import static com.sneakingshadow.core.multiblock.MultiBlockRegistry.OPAQUE_MATERIAL;

public class SBlockOpaqueMaterial extends StructureBlock {

    public boolean blockIsValid(World world, int x, int y, int z){
        return world.getBlock(x,y,z).getMaterial().isOpaque();
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return blockIsValid(world, x, y, z);
    }

    public String toString() {
        return "'" + OPAQUE_MATERIAL + "'";
    }

}
