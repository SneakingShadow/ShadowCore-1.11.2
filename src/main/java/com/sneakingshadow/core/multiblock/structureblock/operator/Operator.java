package com.sneakingshadow.core.multiblock.structureblock.operator;

import com.sneakingshadow.core.multiblock.MultiBlockRegistry;
import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;

import java.util.ArrayList;

import static com.sneakingshadow.core.multiblock.MultiBlockRegistry.*;

/**
 * Created by SneakingShadow on 23.11.2016.
 */
public abstract class Operator extends StructureBlock {

    /**
     * Takes, possibly unmapped, operands.
     *
     * @return inputList after operands have been taken
     * */
    public abstract ArrayList<Object> takeOperands(ArrayList<Object> inputList, int position);

    /**
     * @return operator is used validly
     *
     * If return false, then operator is deleted from inputList
     * !inputList.isEmpty() is already checked
     * */
    public abstract boolean valid(ArrayList<Object> inputList, int position);

    /**
     * Returns if inputted positions in array holds a valid structure block or not.
     * */
    public static boolean validPositions(ArrayList<Object> inputList, int... positions){
        Boolean bool = true;
        for (int position : positions) {
            bool &= validPosition(inputList, position);
        }
        return bool;
    }

    /**
     * Returns if inputted position in ArrayList holds a valid structure block or not.
     * */
    public static boolean validPosition(ArrayList<Object> inputList, int position){
        return insideRange(inputList, position)
                && !(inputList.get(position) instanceof Character
                        && (
                                NEXT_LINE.equals(inputList.get(position))
                                || NEXT_LEVEL.equals(inputList.get(position))
                                || STRING_KEY.equals(inputList.get(position))
                        )
                        && MultiBlockRegistry.operatorUsed((Character)inputList.get(position))
                );
    }

    /**
     * Returns if inputted position is inside the range of the ArrayList
     * */
    public static boolean insideRange(ArrayList<Object> inputList, int position) {
        return position >= 0 && position < inputList.size();
    }
}
