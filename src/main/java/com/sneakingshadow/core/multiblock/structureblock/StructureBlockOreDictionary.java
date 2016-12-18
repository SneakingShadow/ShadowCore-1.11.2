package com.sneakingshadow.core.multiblock.structureblock;

import com.sneakingshadow.core.util.OreDictionaryHelper;
import net.minecraft.world.World;

public class StructureBlockOreDictionary extends StructureBlock {

    private String ore_name;

    public StructureBlockOreDictionary(String string) {
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

    /**
     * Used for comparing structures, in order to remove duplicates.
     * */
    @Override
    public boolean equalsStructureBlock(StructureBlock structureBlock) {
        return structureBlock instanceof StructureBlockOreDictionary && ore_name.equals(((StructureBlockOreDictionary) structureBlock).getOreName());
    }

    public String getOreName() {
        return ore_name;
    }

    public String toString() {
        return "@" + ore_name + "@";
    }

}
