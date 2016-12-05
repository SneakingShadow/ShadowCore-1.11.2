package com.sneakingshadow.core.multiblock.structureblock.operator;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.multiblock.structureblock.special.SBlockFalse;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sneakingshadow.core.multiblock.MultiBlockRegistry.OR;

/**
 * Created by SneakingShadow on 23.11.2016.
 */
public class OperatorOr extends Operator {

    private StructureBlock operand_1 = new SBlockFalse();
    private StructureBlock operand_2 = new SBlockFalse();

    private Object operand_1_input;
    private Object operand_2_input;

    private boolean operand_1_bool = true;
    private boolean operand_2_bool = true;

    public boolean blockIsValid(World world, Vec3 worldPosition, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ) {
        operand_1_bool &= operand_1.blockIsValid(world, (int)worldPosition.xCoord, (int)worldPosition.yCoord, (int)worldPosition.zCoord);
        operand_2_bool &= operand_2.blockIsValid(world, (int)worldPosition.xCoord, (int)worldPosition.yCoord, (int)worldPosition.zCoord);
        return operand_1_bool || operand_2_bool;
    }

    /**
     * A small un-official check to determine if it should continue checking in world.
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return operand_1.startCheckingForStructure(world, x, y, z) || operand_2.startCheckingForStructure(world, x, y, z);
    }

    /**
     * @param inputList
     * @param position
     * @return positions to be removed in inputList.
     */
    @Override
    public ArrayList<Object> takeOperands(ArrayList<Object> inputList, int position) {
        operand_2_input = inputList.remove(position+1);
        operand_1_input = inputList.remove(position-1);
        return inputList;
    }

    /**
     * @param inputList
     * @param position
     * @return operator is used validly.
     * <p>
     * If false operator is deleted from inputList.
     * !inputList.isEmpty() is allready checked.
     */
    @Override
    public boolean valid(ArrayList<Object> inputList, int position) {
        return validPositions(inputList,position-1,position+1);
    }

    public void reset() {
        operand_1_bool = true;
        operand_2_bool = true;
    }

    /**
     * Gets called at the end of structure initialization, in order to let operators and arrayList sort its contained structure blocks out.
     *
     * Note:
     *     Mapped string in stringMap start with MultiBlockInit.STRING_KEY
     * */
    public StructureBlock map(HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        operand_1 = mapObject(operand_1_input, charMap, stringMap);
        operand_2 = mapObject(operand_2_input, charMap, stringMap);

        return this;
    }

    @Override
    public String toString() {
        String string =  "OperatorOr@" + Integer.toHexString(hashCode());
        return string + ": (" + operand_1.toString() + " " + OR + " " + operand_2.toString() + ")";
    }
}
