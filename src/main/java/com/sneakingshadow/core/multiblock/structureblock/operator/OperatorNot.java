package com.sneakingshadow.core.multiblock.structureblock.operator;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.multiblock.structureblock.special.SBlockNull;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sneakingshadow.core.multiblock.MultiBlockRegistry.NOT;

/**
 * Created by SneakingShadow on 23.11.2016.
 */
public class OperatorNot extends Operator {

    private StructureBlock operand = new SBlockNull(); //Will be inverted to always false

    private Object operand_input;

    public boolean blockIsValid(World world, Vec3 worldPosition, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ) {
        return !operand.blockIsValid(world, worldPosition, arrayPosition, rotationX, rotationY, rotationZ);
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return !operand.startCheckingForStructure(world, x, y, z);
    }

    /**
     * @param inputList
     * @param position
     * @return positions to be removed in inputList.
     */
    @Override
    public ArrayList<Object> takeOperands(ArrayList<Object> inputList, int position) {
        operand_input = inputList.remove(position+1);
        return inputList;
    }

    /**
     * @param inputList
     * @param position
     * @return operator is used validly.
     *
     * If false operator is deleted from inputList.
     * !inputList.isEmpty() is allready checked.
     */
    @Override
    public boolean valid(ArrayList<Object> inputList, int position) {
        return validPosition(inputList, position+1);
    }

    /**
     * Gets called at the end of structure initialization, in order to let operators and arrayList sort its contained structure blocks out.
     *
     * Note:
     *     Mapped string in stringMap start with MultiBlockInit.STRING_KEY
     * */
    public StructureBlock map(HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        operand = mapObjectNull(operand_input, charMap, stringMap);
        operand = operand != null ? operand : new SBlockNull();

        return this;
    }

    public StructureBlock getOperand() {
        return operand;
    }

    /**
     * Used for comparing structures, in order to remove duplicates.
     * */
    @Override
    public boolean equalsStructureBlock(StructureBlock structureBlock) {
        return structureBlock instanceof OperatorNot
                && operand.equalsStructureBlock(((OperatorNot) structureBlock).getOperand());
    }

    public String toString() {
        return NOT + operand.toString();
    }
}
