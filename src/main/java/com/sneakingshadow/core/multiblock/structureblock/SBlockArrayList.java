package com.sneakingshadow.core.multiblock.structureblock;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SneakingShadow on 22.11.2016.
 */
public class SBlockArrayList extends StructureBlock {

    private ArrayList<StructureBlock> arrayList = new ArrayList<StructureBlock>();
    private ArrayList<Object> inputList = new ArrayList<Object>();
    private ArrayList<Boolean> boolList = new ArrayList<Boolean>();

    public ArrayList<Object> getInputList() {
        return inputList;
    }

    public ArrayList<StructureBlock> getArrayList() {
        return arrayList;
    }

    public SBlockArrayList(ArrayList<Object> inputList) {
        this.inputList = inputList;
    }

    public boolean blockIsValid(World world, Vec3 worldPosition, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ) {
        //adds missing booleans
        for (int i = boolList.size(), size = arrayList.size(); i < size; i++)
            boolList.add(true);

        for (int i = 0, size = arrayList.size(); i < size; i++) {
            boolList.set(i,
                    boolList.get(i) && arrayList.get(i).blockIsValid(world, worldPosition, arrayPosition, rotationX, rotationY, rotationZ)
            );
        }

        for (Boolean bool : boolList)
            if (bool)
                return true;

        return false;
    }

    /**
     * Gets called at end of every search for valid structure.
     * Used by ArrayList to reset information of valid calls.
     * */
    public void reset() {
        boolList = new ArrayList<Boolean>();
        for (StructureBlock structureBlock : arrayList) {
            structureBlock.reset();
            boolList.add(true);
        }
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        for (StructureBlock anArrayList : arrayList)
            if (anArrayList.startCheckingForStructure(world, x, y, z))
                return true;

        return false;
    }

    public StructureBlock map(HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        for (Object object : inputList) {
            StructureBlock structureBlock = mapObjectNull(object, charMap, stringMap);
            if (structureBlock != null)
                arrayList.add(structureBlock);
        }
        reset();
        return this;
    }

    /**
     * Used for comparing structures, in order to remove duplicates.
     * */
    @Override
    public boolean equalsStructureBlock(StructureBlock structureBlock) {
        return this == structureBlock;
    }

    public String toString() {
        String string = "ArrayList: @" + Integer.toHexString(hashCode());

        string += "{ ";

        if (!arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                string += arrayList.get(i) + (i < arrayList.size() - 1 ? " , " : "");
            }
        } else {
            for (int i = 0; i < inputList.size(); i++) {
                string += inputList.get(i) + (i < inputList.size() - 1 ? " , " : "");
            }
        }

        return string + " }";
    }

}
