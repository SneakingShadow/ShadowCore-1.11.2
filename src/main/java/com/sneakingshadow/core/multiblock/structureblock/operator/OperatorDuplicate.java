package com.sneakingshadow.core.multiblock.structureblock.operator;

import net.minecraft.world.World;

import java.util.ArrayList;

public class OperatorDuplicate extends Operator {
    /**
     * Takes, possibly unmapped, operands
     *
     * @param inputList
     * @param position
     * @return positions to be removed in inputList
     */
    @Override
    public ArrayList<Object> takeOperands(ArrayList<Object> inputList, int position) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        //Adds the start of the array
        for (int i = 0; i < position-1; i++) {
            arrayList.add(inputList.get(i));
        }

        //Adds original and duplicates
        for (int i = 0; i < (Integer)inputList.get(position+1); i++)
            arrayList.add(inputList.get(position-1));

        //Adds rest of array, if there is more left
        for (int i = position+2; i < inputList.size(); i++) {
            arrayList.add(inputList.get(i));
        }

        return arrayList;
    }

    /**
     * @param inputList
     * @param position
     * @return operator is used validly
     * <p>
     * If return false, then operator is deleted from inputList
     * !inputList.isEmpty() is already checked.
     */
    @Override
    public boolean valid(ArrayList<Object> inputList, int position) {
        return insideRange(inputList, position-1)
                && insideRange(inputList, position+1)
                && inputList.get(position+1) instanceof Integer
                && ((Integer)inputList.get(position+1)) > 0;
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return false;
    }
}
