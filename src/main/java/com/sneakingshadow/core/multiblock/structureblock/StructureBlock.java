package com.sneakingshadow.core.multiblock.structureblock;

import com.sneakingshadow.core.multiblock.structureblock.special.SBlockFalse;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.HashMap;

import static com.sneakingshadow.core.multiblock.MultiBlockInit.STRING_KEY;

/**
 * Created by SneakingShadow on 22.11.2016.
 */
public abstract class StructureBlock {

    /*
     * Needed functions to implement:
     *     startCheckingForStructure
     *     one of the blockIsValid functions
     * */

    /**
     * A small un-official check to determine if it should continue checking in world.
     * */
    public abstract boolean startCheckingForStructure(World world, int x, int y, int z);

    public boolean blockIsValid(World world, int x, int y, int z){
        return false;
    }

    /**
     * @param world
     * @param worldPosition position of block in world
     * @param arrayPosition position of block in world
     * @param rotationX rotation around xAxis
     * @param rotationY rotation around yAxis
     * @param rotationZ rotation around zAxis
     *
     * Note:
     *     Rotations go from 0 to 3, and are measured in rotations of 90°, which is equal to 1/2 pi radians.
     * */
    public boolean blockIsValid(World world, Vec3 worldPosition, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ) {
        return blockIsValid(world, (int)worldPosition.xCoord, (int)worldPosition.yCoord, (int)worldPosition.zCoord);
    }

    /**
     * Gets called at end of every search for valid structure.
     * Used by OperatorOr to reset information of valid calls.
     * */
    public void reset() {}

    /**
     * Gets called at the end of structure initialization, in order to let operators and arrayList sort its contained structure blocks out.
     * @return itself
     *
     * Note:
     *     All characters at this point is characters that are either mapped to something, not mapped, NEXT_LINE, or NEXT_LEVEL
     *     Recommended to use mapObjectNull(object, charMap, stringMap) for simplicity.
     * */
    public StructureBlock map(HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        return this;
    }

    /**
     * Returns the correct StructureBlock, mapped or not.
     * If a structure block is inputted, it gets called .map on
     *
     * @return Structure block, or null if invalid
     * */
    public StructureBlock mapObjectNull(Object object, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        if (object instanceof StructureBlock)
            return ((StructureBlock) object).map(charMap, stringMap);

        else if (object instanceof Character) {
            StructureBlock structureBlock = charMap.get(object);
            if (structureBlock != null)
                return structureBlock.map(charMap, stringMap);

        } else if (object instanceof String && !((String) object).isEmpty() && STRING_KEY.equals(((String) object).charAt(0))) {
            StructureBlock structureBlock = stringMap.get(object);
            if (structureBlock != null)
                return structureBlock.map(charMap, stringMap);

        }
        return null;
    }

    /**
     * Calls mapObjectNull, but returns a structure block that only returns false if mapObjectNull returns null
     *
     * @return Structure Block
     * */
    public StructureBlock mapObject(Object object, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        StructureBlock structureBlock = mapObjectNull(object, charMap, stringMap);

        return structureBlock != null ? structureBlock : new SBlockFalse();
    }

}
