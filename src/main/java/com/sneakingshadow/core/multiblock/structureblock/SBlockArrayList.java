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

    public ArrayList<Object> getArrayList() {
        return inputList;
    }

    public SBlockArrayList(ArrayList<Object> inputList) {
        this.inputList = inputList;
    }

    public boolean blockIsValid(World world, Vec3 worldPosition, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ) {
        for (StructureBlock structureBlock : arrayList)
            if (structureBlock.blockIsValid(world, worldPosition, arrayPosition, rotationX, rotationY, rotationZ))
                return true;

        return false;
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        for (StructureBlock structureBlock : arrayList)
            if (structureBlock.startCheckingForStructure(world, x, y, z))
                return true;

        return false;
    }

    public StructureBlock map(HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        for (Object object : inputList) {
            StructureBlock structureBlock = mapObjectNull(object, charMap, stringMap);
            if (structureBlock != null)
                arrayList.add(structureBlock);
        }
        return this;
    }

    public String toString() {
        String string = "{ ";

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
