package com.sneakingshadow.core.multiblock.structureblock;

import com.sneakingshadow.core.util.OreDictionaryHelper;
import net.minecraft.world.World;

public class SBlockOreDictionary extends StructureBlock {

    private String ore_name;

    public SBlockOreDictionary (String string) {
        ore_name = string;
    }

    public boolean blockIsValid(World world, int x, int y, int z){
        return OreDictionaryHelper.isValidItem(ore_name,world,x,y,z);
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return blockIsValid(world, x, y, z);
    }

    public String toString() {
        return "@" + ore_name + "@";
    }

}
