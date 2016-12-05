package com.sneakingshadow.core.multiblock.structureblock.special;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import net.minecraft.world.World;

import static com.sneakingshadow.core.multiblock.MultiBlockRegistry.NULL;

/**
 * Created by SneakingShadow on 23.11.2016.
 */
public class SBlockNull extends StructureBlock {

    public boolean blockIsValid(World world, int x, int y, int z){
        return true;
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return true;
    }

    public String toString() {
        return "'" + NULL + "'";
    }
}
