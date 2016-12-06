package com.sneakingshadow.core.multiblock.structureblock.special;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import net.minecraft.world.World;

public class SBlockFalse extends StructureBlock {

    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return false;
    }

    @Override
    public String toString() {
        return "invalid";
    }

    /**
     * Used for comparing structures, in order to remove duplicates.
     * */
    @Override
    public boolean equalsStructureBlock(StructureBlock structureBlock) {
        return structureBlock instanceof SBlockFalse;
    }
}