package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.multiblock.initializer.OperatorInitializer;
import com.sneakingshadow.core.multiblock.structureblock.SBlockArrayList;
import com.sneakingshadow.core.multiblock.structureblock.SBlockBlock;
import com.sneakingshadow.core.multiblock.structureblock.SBlockOreDictionary;
import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.multiblock.structureblock.operator.Operator;
import com.sneakingshadow.core.multiblock.structureblock.special.StructureBlockNull;
import com.sneakingshadow.core.util.ArrayListHelper;
import com.sneakingshadow.core.util.StringUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sneakingshadow.core.multiblock.MultiBlock.NEXT_LEVEL;
import static com.sneakingshadow.core.multiblock.MultiBlock.NEXT_LINE;

class InputHandler {

    private ArrayList<Object> sortInput(boolean doMapping) {
        int currentDuplicator = 0;

        duplicator(currentDuplicator);
        inputList();
        brackets();
        duplicator(currentDuplicator);
        arrayListSort();
        arrayListClear();
        oreDictionary();
        specialValues();
        operator();
        duplicator(currentDuplicator);
        mapObjects(doMapping);
        extractStrings();

        return arrayList;
    }

    static StructureArray getStructureArray(Object[] objects) {
        HashMap<Character, StructureBlock> charMap = new HashMap<Character, StructureBlock>();
        HashMap<String, StructureBlock> stringMap = new HashMap<String, StructureBlock>();
        ArrayList<Object> arrayList = (new InputHandler(objects, charMap, stringMap)).sortInput(true);

        if (!charMap.isEmpty() || !stringMap.isEmpty())
            arrayList = map(arrayList, charMap, stringMap);

        StructureArray structureArray = new StructureArray();
        int x = 0, y = 0, z = 0;

        for (Object object : arrayList) {
            if (object instanceof StructureBlock)
                structureArray.set(x++,y,z,(StructureBlock)object);

            if (object instanceof Character) {
                if (NEXT_LINE.equals(object)) {
                    z++;
                    x=0;
                }
                if (NEXT_LEVEL.equals(object)) {
                    y++;
                    x=0;
                    z=0;
                }
            }
        }

        return structureArray;
    }

    private HashMap<Character, StructureBlock> charMap;
    private HashMap<String, StructureBlock> stringMap;
    private ArrayList<Boolean> booleans = new ArrayList<Boolean>();
    private ArrayList<Object> arrayList;

    private InputHandler(Object[] objects, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        this.arrayList = ArrayListHelper.fromArray(objects);
        this.charMap = charMap;
        this.stringMap = stringMap;
        setupBooleans();
    }

    private static final int DUPLICATOR_FOUND = 0;
    private static final int[] DUPLICATORS = {1,2,3};
    private static final int INPUT_LIST = 4;
    private static final int BRACKETS = 5;
    private static final int ARRAY_LIST = 6;
    private static final int ORE_DICTIONARY = 7;
    private static final int OPERATOR = 8;
    private static final int EXTRACT_STRINGS = 9;

    private static final int BOOLEAN_LIST_SIZE = 10;

    /**
     * Modifies the booleans list and sets everything that needs to be done to true, in order to optimize.
     * Also creates ArrayList from object array.
     * */
    private void setupBooleans() {
        for (int i = 0; i < BOOLEAN_LIST_SIZE; i++) {
            booleans.add(false);
        }
        checkArray();
    }

    /**
     * Modifies the booleans list and sets everything that needs to be done to true, in order to optimize.
     * */
    private void checkArray() {
        for (Object object : arrayList) {
            if (object != null) {
                if (object instanceof Character) {
                    checkCharacter(object);
                }
                else if (object instanceof String) {
                    booleans.set(EXTRACT_STRINGS, true);
                    for (int i = 0; i < ((String) object).length(); i++) {
                        checkCharacter(((String) object).charAt(i));
                    }
                }
                else if (object instanceof InputList) {
                    booleans.set(INPUT_LIST, true);
                    checkArray();
                }
                else if (object instanceof ArrayList) {
                    booleans.set(ARRAY_LIST, true);
                }
            }
        }
    }

    //Used by checkArray
    private void checkCharacter(Object object) {
        if (!booleans.get(DUPLICATOR_FOUND))
            for (int i = 0; i < DUPLICATORS.length; i++) {
                if (MultiBlockRegistry.getDuplicatorInitializer(i).isSpecialCharacter(object)) {
                    booleans.set(DUPLICATORS[i], true);
                    booleans.set(DUPLICATOR_FOUND, true);
                }
            }

        if (!booleans.get(ORE_DICTIONARY) && MultiBlock.ORE_DICTIONARY.equals(object)) {
            booleans.set(ORE_DICTIONARY, true);
        }
        else if (!booleans.get(BRACKETS) && (MultiBlock.BRACKET_START.equals(object) || MultiBlock.BRACKET_END.equals(object))) {
            booleans.set(BRACKETS, true);
            booleans.set(ARRAY_LIST,true);
        }
        else if (!booleans.get(OPERATOR) && MultiBlockRegistry.operatorUsed((Character)object))
            booleans.set(OPERATOR, true);
    }

    /**
     * Extracts input list
     * */
    private void inputList() {
        if (booleans.get(INPUT_LIST))
        {
            Object[] objects = arrayList.toArray();
            arrayList = new ArrayList<Object>();
            inputList(objects);
        }
    }
    private void inputList(Object[] objects) {
        for (Object object : objects)
            if (object instanceof InputList)
                inputList(((InputList) object).toArray());
            else
                arrayList.add(object);
    }

    /**
     * Creates arrayLists from what is surrounded in brackets
     * */
    private void brackets() {
        if (booleans.get(BRACKETS)) {
            int bracketsNotClosed = 0;
            ArrayList<Object> bracketList = new ArrayList<Object>();
            ArrayList<Object> inputList = new ArrayList<Object>();

            for (Object object : arrayList) {
                if (object instanceof Character) {
                    boolean bool = true;

                    if (MultiBlock.BRACKET_START.equals(object)) {
                        bracketsNotClosed++;
                        bool = false;
                    } else if (MultiBlock.BRACKET_END.equals(object)) {
                        //Subtract one, unless lower, then set to 0.
                        bracketsNotClosed = bracketsNotClosed > 0 ? bracketsNotClosed - 1 : 0;
                        bool = false;

                        if (bracketsNotClosed == 0) {
                            inputList.add(bracketList);
                            bracketList = new ArrayList<Object>();
                        }
                    }

                    if (bool) {
                        if (bracketsNotClosed > 0)
                            bracketList.add(object);
                        else
                            inputList.add(object);
                    }
                } else if (object instanceof String) {
                    ArrayList<String> stringArray = StringUtil.splitString(
                            (String) object, new Character[]{MultiBlock.BRACKET_START, MultiBlock.BRACKET_END}
                    );

                    for (String string : stringArray) {
                        boolean bool = false;
                        Character character = string.charAt(0);

                        if (MultiBlock.BRACKET_START.equals(character)) {
                            bracketsNotClosed++;
                            bool = true;
                        } else if (MultiBlock.BRACKET_END.equals(character)) {
                            //Subtract one, unless lower, then set to 0.
                            bracketsNotClosed = bracketsNotClosed > 0 ? bracketsNotClosed - 1 : 0;
                            bool = true;

                            if (bracketsNotClosed == 0) {
                                inputList.add(bracketList);
                                bracketList = new ArrayList<Object>();
                            }
                        }

                        string = bool ? string.substring(1, string.length()) : string;
                        if (!string.isEmpty()) {
                            if (bracketsNotClosed > 0) {
                                bracketList.add(string);
                            } else {
                                inputList.add(string);
                            }
                        }
                    }
                } else {
                    if (bracketsNotClosed > 0)
                        bracketList.add(object);
                    else
                        inputList.add(object);
                }
            }

            //In case of not remembering to close
            if (!bracketList.isEmpty())
                inputList.add(bracketList);

            arrayList = inputList;
        }
    }

    /**
     * Sorts all arrayLists and turns them into StructureBlocks
     * */
    private void arrayListSort() {
        if (booleans.get(ARRAY_LIST)) {
            for (int i = 0, arrayListLength = arrayList.size(); i < arrayListLength; i++) {
                Object object = arrayList.get(i);
                if (object instanceof ArrayList) {
                    arrayList.set(i, new SBlockArrayList(
                            (new InputHandler(((ArrayList) object).toArray(), charMap, stringMap)).sortInput(false)
                    ));
                }
            }
        }
    }

    /**
     * Turns OreDictionary strings into StructureBlocks
     * */
    private void oreDictionary() {
        if (booleans.get(ORE_DICTIONARY)) {
            ArrayList<Object> inputList = new ArrayList<Object>();

            for (int i = 0, arrayListSize = arrayList.size(); i < arrayListSize; i++) {
                Object object = arrayList.get(i);

                if (object instanceof Character && MultiBlock.ORE_DICTIONARY.equals(object) && i + 1 < arrayList.size()) {
                    Object object_2 = arrayList.get(++i);
                    if (object_2 instanceof String)
                        inputList.add(new SBlockOreDictionary((String) object_2));
                    else
                        inputList.add(object_2);
                } else if (object instanceof String) {
                    ArrayList<String> stringList = StringUtil.splitString((String) object, MultiBlock.ORE_DICTIONARY, true);

                    for (String string : stringList) {
                        if (MultiBlock.ORE_DICTIONARY.equals(string.charAt(0))) {
                            if (string.length() > 1)
                                inputList.add(new SBlockOreDictionary(string.substring(1, string.length())));
                        } else if (!string.isEmpty())
                            inputList.add(string);
                    }
                } else
                    inputList.add(object);
            }

            this.arrayList = inputList;
        }
    }

    /**
     * Turns special values into StructureBlocks
     * */
    private void specialValues() {
        ArrayList<Object> inputList = new ArrayList<Object>();

        for (Object object : arrayList) {
            if (object instanceof Item)
            {
                Block block = Block.getBlockFromItem((Item)object);
                if (block == Blocks.air)
                    inputList.add(new StructureBlockNull());
                else
                    inputList.add(new SBlockBlock(block));
            }
            else if (object instanceof Block)
            {
                inputList.add(new SBlockBlock((Block) object));
            }
            else if (object instanceof ItemStack)
            {
                Block block = Block.getBlockFromItem(((ItemStack) object).getItem());
                if (block == Blocks.air)
                    inputList.add(new StructureBlockNull());
                else
                    inputList.add(new SBlockBlock(block, ((ItemStack) object).getItemDamage()));
            }
            else if (object == null)
            {
                inputList.add(new StructureBlockNull());
            }
            else if (object instanceof String)
            {
                String string_object = (String)object;
                String string = "";

                for (int i = 0; i < string_object.length(); i++) {
                    if (MultiBlockRegistry.specialCharacterUsed(string_object.charAt(i))) {
                        if (!string.isEmpty())
                            inputList.add(string);
                        string = "";
                        inputList.add(MultiBlockRegistry.getSpecialCharacter(string_object.charAt(i)));
                    } else
                        string += string_object.charAt(i);
                }
                if (!string.isEmpty())
                    inputList.add(string);
            }
            else
            {
                inputList.add(object);
            }
        }

        arrayList = inputList;
    }

    /**
     * Initializes duplicators
     * */
    private void duplicator(int duplicator) {
        if (booleans.get(DUPLICATORS[duplicator]))
            operator(
                ArrayListHelper.createArrayList(
                        MultiBlockRegistry.getDuplicatorInitializer(duplicator)
                )
            );
    }

    /**
     * Initializes operators
     * */
    private void operator() {
        if (booleans.get(OPERATOR))
            operator(MultiBlockRegistry.getOperatorList());
    }

    private void operator(ArrayList<OperatorInitializer> operatorInitializerList) {
        for (OperatorInitializer operatorInitializer : operatorInitializerList){
            boolean foundOperator = true;

            while (foundOperator) {
                foundOperator = false;

                for (int i = 0, arrayListSize = arrayList.size(); i < arrayListSize; i++)
                    if (operatorInitializer.isSpecialCharacter(arrayList.get(i))) {
                        Operator operator = operatorInitializer.getOperator();
                        arrayList.set(i, operator);

                        if (operator.valid(arrayList, i)) {
                            arrayList = operator.takeOperands(arrayList, i);
                        } else
                            arrayList.remove(i);

                        foundOperator = true;
                        break;
                    }

            }
        }
    }

    /**
     * Maps all possible strings.
     * Maps all possible characters.
     * Clears the arraylist of everything that has been mapped, and that is invalid.
     * Note: Special characters have already been taken care of.
     * */
    private void mapObjects(boolean doMapping) {
        ArrayList<Object> inputList = new ArrayList<Object>();

        for (int i = 0; i < arrayList.size(); i++) {
            Object object = arrayList.get(i);

            if (object instanceof Character) {
                Character character = (Character)object;

                if (MultiBlock.STRING_KEY.equals(character))
                {
                    if (arrayList.size() > i+2 && arrayList.get(i+1) instanceof String && arrayList.get(i+2) instanceof StructureBlock) {
                        if (doMapping)
                            stringMap.put((String) arrayList.get(i+1), (StructureBlock) arrayList.get(i+2));
                        i += 2;
                    }
                }
                else if (arrayList.size() > i+1 && arrayList.get(i+1) instanceof StructureBlock)
                {
                    if (doMapping)
                        charMap.put(character, (StructureBlock) arrayList.get(i+1));
                    i++;
                }
            } else {
                inputList.add(object);
            }
        }

        arrayList = inputList;
    }

    /**
     * Extracts strings into characters and string-objects.
     * */
    private void extractStrings() {
        if (booleans.get(EXTRACT_STRINGS)) {
            ArrayList<Object> inputList = new ArrayList<Object>();

            for (Object object : arrayList) {
                if (object instanceof String) {
                    ArrayList<String> strings = StringUtil.splitString((String) object, MultiBlock.STRING_KEY, true);

                    for (String string : strings)
                        if (!string.isEmpty())
                            if (MultiBlock.STRING_KEY.equals(string.charAt(0))) {
                                if (string.length() > 1)
                                    inputList.add(string.substring(1));
                            } else
                                for (int i = 0; i < string.length(); i++)
                                    inputList.add(string.charAt(i));
                } else {
                    inputList.add(object);
                }
            }

            arrayList = inputList;
        }
    }

    /**
     * Extracts strings into characters and string-objects.
     * */
    private static ArrayList<Object> map(ArrayList<Object> arrayList, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        for (int i = 0, inputListSize = arrayList.size(); i < inputListSize; i++) {
            Object object = arrayList.get(i);

            if (object instanceof StructureBlock) {
                arrayList.set(i,((StructureBlock) object).map(charMap, stringMap));
            }
            else if (object instanceof Character && !NEXT_LINE.equals(object) && !NEXT_LEVEL.equals(object))
            {
                StructureBlock structureBlock = charMap.get(object);
                arrayList.set(i,
                        structureBlock != null ?
                                structureBlock.map(charMap, stringMap) : new StructureBlockNull()
                );
            }
            else if (object instanceof String)
            {
                StructureBlock structureBlock = stringMap.get(object);
                arrayList.set(i,
                        structureBlock != null ?
                                structureBlock.map(charMap, stringMap) : new StructureBlockNull()
                );
            }
        }

        return arrayList;
    }

    /**
     * Removes empty arrayLists, and extracts contained object if the arrayList only contains one object.
     * */
    private void arrayListClear() {
        if (booleans.get(ARRAY_LIST)) {
            for (int i = 0, arrayListSize = arrayList.size(); i < arrayListSize; i++) {
                Object object = arrayList.get(i);

                if (object instanceof SBlockArrayList) {
                    ArrayList<Object> arrayInputList = ((SBlockArrayList) object).getInputList();
                    if (!arrayInputList.isEmpty()) {
                        if (arrayInputList.size() == 1)
                            arrayList.set(i, arrayInputList.get(0));
                        else
                            arrayList.set(i, object);
                    } else {
                        arrayList.remove(i--);
                    }
                }
            }
        }
    }

}
