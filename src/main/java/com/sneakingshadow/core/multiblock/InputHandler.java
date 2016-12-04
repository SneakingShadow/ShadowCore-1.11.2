package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.multiblock.initializer.OperatorInitializer;
import com.sneakingshadow.core.multiblock.structureblock.SBlockArrayList;
import com.sneakingshadow.core.multiblock.structureblock.SBlockBlock;
import com.sneakingshadow.core.multiblock.structureblock.SBlockOreDictionary;
import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.multiblock.structureblock.operator.Operator;
import com.sneakingshadow.core.multiblock.structureblock.special.SBlockNull;
import com.sneakingshadow.core.util.ArrayListHelper;
import com.sneakingshadow.core.util.StringUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sneakingshadow.core.multiblock.MultiBlockInit.NEXT_LEVEL;
import static com.sneakingshadow.core.multiblock.MultiBlockInit.NEXT_LINE;

class InputHandler {

    static StructureArray getStructureArray(Object[] objects) {
        HashMap<Character, StructureBlock> charMap = new HashMap<Character, StructureBlock>();
        HashMap<String, StructureBlock> stringMap = new HashMap<String, StructureBlock>();

        ArrayList<Object> arrayList = sortInput(objects, charMap, stringMap, true);

        if (!(charMap.isEmpty() && stringMap.isEmpty()))
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


    /**
     * Sorts all the inputs, and maps objects to the maps.
     * */
    private static ArrayList<Object> sortInput(Object[] objects, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap, boolean doMapping) {
        ArrayList<Boolean> booleans = neededFunctions(objects);
        ArrayList<Object> arrayList = ArrayListHelper.fromArray(objects);

        if (booleans.get(DUPLICATORS[0]))
            arrayList = duplicator(arrayList, MultiBlockInit.getDuplicatorInitializer(0));
        if (booleans.get(INPUT_LIST))
            arrayList = inputList(arrayList, new ArrayList<Object>());
        if (booleans.get(BRACKETS))
            arrayList = brackets(arrayList);
        if (booleans.get(ARRAY_LIST)) {
            arrayList = arrayListSort(arrayList, charMap, stringMap);
            arrayList = arrayListClear(arrayList);
        }
        if (booleans.get(ORE_DICTIONARY))
            arrayList = oreDictionary(arrayList);
        arrayList = specialValues(arrayList);
        if (booleans.get(DUPLICATORS[1]))
            arrayList = duplicator(arrayList, MultiBlockInit.getDuplicatorInitializer(2));
        if (booleans.get(OPERATOR))
            arrayList = operator(arrayList, MultiBlockInit.getOperatorList());
        if (booleans.get(DUPLICATORS[2]))
            arrayList = duplicator(arrayList, MultiBlockInit.getDuplicatorInitializer(3));

        //Allows arrayListSort to sort its content in this manner without conflicts.
        if (doMapping) {
            mapObjects(arrayList, charMap, stringMap);
        }
        arrayList = clearMappedAndInvalid(arrayList);

        if (booleans.get(EXTRACT_STRINGS))
            arrayList = extractStrings(arrayList);

        return arrayList;
    }


    private static final int[] DUPLICATORS = {0,1,2};
    private static final int INPUT_LIST = 3;
    private static final int BRACKETS = 4;
    private static final int ARRAY_LIST = 5;
    private static final int ORE_DICTIONARY = 6;
    private static final int OPERATOR = 7;
    private static final int EXTRACT_STRINGS = 8;

    private static final int BOOLEAN_LIST_SIZE = 10;

    /**
     * Modifies the booleans list and sets everything that needs to be done to true, in order to optimize.
     * Also creates ArrayList from object array.
     * */
    private static ArrayList<Boolean> neededFunctions(Object[] inputs) {
        ArrayList<Boolean> booleans = new ArrayList<Boolean>();
        for (int i = 0; i < BOOLEAN_LIST_SIZE; i++) {
            booleans.add(false);
        }
        booleans = checkArray(inputs, booleans);

        return booleans;
    }

    /**
     * Modifies the booleans list and sets everything that needs to be done to true, in order to optimize.
     * */
    private static ArrayList<Boolean> checkArray(Object[] objects, ArrayList<Boolean> booleans) {
        for (Object object : objects) {
            if (object == null) {

            }
            else if (object instanceof Character) {
                booleans = checkCharacter(object, booleans);
            }
            else if (object instanceof String) {
                booleans.set(EXTRACT_STRINGS, true);
                for (int i = 0; i < ((String) object).length(); i++) {
                    booleans = checkCharacter(((String) object).charAt(i), booleans);
                }
            }
            else if (object instanceof InputList) {
                booleans.set(INPUT_LIST, true);
                booleans = checkArray(((InputList) object).toArray(), booleans);
            }
            else if (object instanceof ArrayList) {
                booleans.set(ARRAY_LIST, true);
            }
        }

        return booleans;
    }

    //Used by checkArray
    private static ArrayList<Boolean> checkCharacter(Object object, ArrayList<Boolean> booleans) {
        if (!(booleans.get(DUPLICATORS[0])
                        && booleans.get(DUPLICATORS[1])
                        && booleans.get(DUPLICATORS[2])
                        && booleans.get(DUPLICATORS[3])))
            for (int i = 0; i < DUPLICATORS.length; i++)
                if (MultiBlockInit.getDuplicatorInitializer(i).isSpecialCharacter(object))
                    booleans.set(DUPLICATORS[i], true);

        if (!booleans.get(ORE_DICTIONARY) && MultiBlockInit.ORE_DICTIONARY.equals(object))
            booleans.set(ORE_DICTIONARY, true);

        else if (!booleans.get(BRACKETS) &&
                (MultiBlockInit.BRACKET_START.equals(object)
                || MultiBlockInit.BRACKET_END.equals(object))) {
            booleans.set(BRACKETS, true);
            booleans.set(ARRAY_LIST,true);
        }
        else if (!booleans.get(OPERATOR) && MultiBlockInit.operatorUsed((Character)object))
            booleans.set(OPERATOR, true);

        return booleans;
    }

    /**
     * Extracts input list
     * */
    private static ArrayList<Object> inputList(ArrayList inputList, ArrayList<Object> arrayList) {
        for (Object object : inputList)
            if (object instanceof InputList)
                inputList(
                        duplicator(((ArrayList<Object>) object), MultiBlockInit.getDuplicatorInitializer(0)),
                        arrayList
                );
            else
                arrayList.add(object);

        return arrayList;
    }

    /**
     * Creates arrayLists from what is surrounded in brackets
     * */
    private static ArrayList<Object> brackets(ArrayList<Object> objects) {
        int bracketsNotClosed = 0;
        ArrayList<Object> bracketList = new ArrayList<Object>();
        ArrayList<Object> inputList = new ArrayList<Object>();

        for (Object object : objects) {

            if (object instanceof Character) {
                boolean bool = true;

                if (MultiBlockInit.BRACKET_START.equals(object)) {
                    bracketsNotClosed++;
                    bool = false;
                }
                else if (MultiBlockInit.BRACKET_END.equals(object))
                {
                    //Subtract one, unless lower, then set to 0.
                    bracketsNotClosed = bracketsNotClosed > 0 ? bracketsNotClosed-1 : 0;
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
            }
            else if (object instanceof String) {
                ArrayList<String> stringArray = StringUtil.splitString(
                        (String) object, new Character[] {MultiBlockInit.BRACKET_START, MultiBlockInit.BRACKET_END}
                );

                for (String string : stringArray) {
                    Character character = string.charAt(0);

                    boolean bool = false;

                    if (MultiBlockInit.BRACKET_START.equals(character)) {
                        bracketsNotClosed++;
                        bool = true;
                    }
                    else if (MultiBlockInit.BRACKET_END.equals(character))
                    {
                        //Subtract one, unless lower, then set to 0.
                        bracketsNotClosed = bracketsNotClosed > 0 ? bracketsNotClosed-1 : 0;
                        bool = true;

                        if (bracketsNotClosed == 0) {
                            inputList.add(bracketList);
                            bracketList = new ArrayList<Object>();
                        }
                    }

                    string = bool ? string.substring(1,string.length()) : string;
                    if (!string.isEmpty()) {
                        if (bracketsNotClosed > 0) {
                            bracketList.add(string);
                        } else {
                            inputList.add(string);
                        }
                    }
                }
            } else
                if (bracketsNotClosed > 0)
                    bracketList.add(object);
                else
                    inputList.add(object);
        }

        //In case of not remembering to close
        if (!bracketList.isEmpty())
            inputList.add(bracketList);

        return inputList;
    }

    /**
     * Sorts all arrayLists and turns them into StructureBlocks
     * */
    private static ArrayList<Object> arrayListSort(ArrayList<Object> objects, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        for (Object object : objects)
            if (object instanceof ArrayList) {
                arrayList.add(
                        new SBlockArrayList(
                                sortInput( ((ArrayList) object).toArray(), charMap, stringMap, false)
                        )
                );
            } else
                arrayList.add(object);

        return arrayList;
    }

    /**
     * Turns OreDictionary strings into StructureBlocks
     * */
    private static ArrayList<Object> oreDictionary(ArrayList<Object> objects) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        for (int i = 0; i < objects.size(); i++) {
            Object object = objects.get(i);

            if (object instanceof Character && MultiBlockInit.ORE_DICTIONARY.equals(object) && i+1 < objects.size()) {
                i++;
                if (objects.get(i) instanceof String)
                    arrayList.add(new SBlockOreDictionary((String) objects.get(i)));
                else
                    arrayList.add(objects.get(i));
            } else if (object instanceof String) {
                ArrayList<String> stringList = StringUtil.splitString((String)object, MultiBlockInit.ORE_DICTIONARY,true);

                for (String string : stringList) {
                    if (MultiBlockInit.ORE_DICTIONARY.equals(string.charAt(0))) {
                        if (string.length() > 1)
                            arrayList.add(new SBlockOreDictionary(string.substring(1, string.length())));
                    } else if (!string.isEmpty())
                        arrayList.add(string);
                }
            } else
                arrayList.add(object);
        }

        return arrayList;
    }

    /**
     * Turns special values into StructureBlocks
     * */
    private static ArrayList<Object> specialValues(ArrayList<Object> objects) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        for (Object object : objects) {
            if (object instanceof Item) {
                Block block = Block.getBlockFromItem((Item)object);
                if (block == Blocks.air)
                    arrayList.add(new SBlockNull());
                else
                    arrayList.add(new SBlockBlock(block));
            }
            else if (object instanceof Block)
                arrayList.add(new SBlockBlock((Block)object));

            else if (object instanceof ItemStack) {
                Block block = Block.getBlockFromItem(((ItemStack) object).getItem());
                if (block == Blocks.air)
                    object = null;  //This will be handled by another if statement.
                else
                    arrayList.add(new SBlockBlock(block, ((ItemStack) object).getItemDamage()));
            }
            else if (object == null)
                arrayList.add(new SBlockNull());

            else if (object instanceof String)
            {
                String string_object = (String)object;
                String string = "";

                for (int i = 0; i < string_object.length(); i++) {
                    if (MultiBlockInit.specialCharacterUsed(string_object.charAt(i))) {
                        arrayList.add(string);
                        string = "";
                        arrayList.add(MultiBlockInit.getSpecialCharacter(string_object.charAt(i)));
                    } else
                        string += string_object.charAt(i);
                }
                if (!string.isEmpty())
                    arrayList.add(string);
            }
            else
                arrayList.add(object);
        }

        return arrayList;
    }

    /**
     * Initializes duplicators
     * */
    private static ArrayList<Object> duplicator(ArrayList<Object> inputList, OperatorInitializer operatorInitializer) {
        return operator(inputList, ArrayListHelper.createArrayList(operatorInitializer));
    }

    /**
     * Turns operator characters into Operators
     * */
    private static ArrayList<Object> operator(ArrayList<Object> inputList, ArrayList<OperatorInitializer> operatorInitializerList) {
        for (OperatorInitializer operatorInitializer : operatorInitializerList){
            boolean foundOperator = true;

            while (foundOperator) {
                foundOperator = false;

                for (int i = 0; i < inputList.size(); i++) {
                    if (operatorInitializer.isSpecialCharacter(inputList.get(i))) {
                        Operator operator = operatorInitializer.getOperator();
                        inputList.set(i,operator);

                        if (operator.valid(inputList, i)) {
                            inputList = operator.takeOperands(inputList, i);
                        } else
                            inputList.remove(i);

                        foundOperator = true;
                        break;
                    }
                }
            }
        }

        return inputList;
    }

    /**
     * Maps all possible strings.
     * Maps all possible characters.
     * Note: Special characters have already been taken care of.
     * */
    private static void mapObjects(ArrayList<Object> inputList, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        for (int i = 0; i < inputList.size(); i++) {
            Object object = inputList.get(i);

            if (object instanceof Character) {
                Character character = (Character)object;

                if (MultiBlockInit.STRING_KEY.equals(character)) {
                    if (inputList.size() > i+2 && inputList.get(i+1) instanceof String && inputList.get(i+2) instanceof StructureBlock)
                        stringMap.put(MultiBlockInit.STRING_KEY + (String)inputList.get(++i), (StructureBlock) inputList.get(++i));

                } else if (inputList.size() > i+1 && inputList.get(i+1) instanceof StructureBlock)
                    charMap.put(character, (StructureBlock) inputList.get(++i));

            }
        }
    }

    /**
     * Clears the arraylist of everything that has been/would be mapped, and that is generally invalid.
     * */
    private static ArrayList<Object> clearMappedAndInvalid(ArrayList<Object> inputList) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        for (int i = 0; i < inputList.size(); i++) {
            Object object = inputList.get(i);

            if (object instanceof Character && !NEXT_LINE.equals(object) && !NEXT_LEVEL.equals(object)) {
                Character character = (Character)object;

                if (MultiBlockInit.STRING_KEY.equals(character)) {
                    if (inputList.size() > i+2 && inputList.get(i+1) instanceof String && inputList.get(i+2) instanceof StructureBlock)
                        i+=2;
                } else if (inputList.size() > i+1 && inputList.get(i+1) instanceof StructureBlock)
                    ++i;
            } else
                arrayList.add(object);
        }

        return arrayList;
    }

    /**
     * Extracts strings into characters and string-objects.
     * */
    private static ArrayList<Object> extractStrings(ArrayList<Object> inputList) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        for (Object object : inputList)
            if (object instanceof String) {
                ArrayList<String> strings = StringUtil.splitString((String)object, MultiBlockInit.STRING_KEY, true);

                for (String string : strings)
                    if(!string.isEmpty())
                        if(MultiBlockInit.STRING_KEY.equals(string.charAt(0))) {
                            if (string.length() > 1)
                                arrayList.add(string);
                        } else
                            for (int i = 0; i < string.length(); i++)
                                arrayList.add(string.charAt(i));
            } else
                arrayList.add(object);

        return arrayList;
    }

    /**
     * Extracts strings into characters and string-objects.
     * */
    private static ArrayList<Object> map(ArrayList<Object> inputList, HashMap<Character, StructureBlock> charMap, HashMap<String, StructureBlock> stringMap) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        for (Object object : inputList) {
            if (object instanceof StructureBlock) {
                arrayList.add(((StructureBlock) object).map(charMap, stringMap));
            } else if (object instanceof Character && !NEXT_LINE.equals(object) && !NEXT_LEVEL.equals(object)) {
                StructureBlock structureBlock = charMap.get(object);
                arrayList.add(
                        structureBlock != null ?
                                structureBlock.map(charMap, stringMap) : new SBlockNull()
                );
            } else if (object instanceof String) {
                StructureBlock structureBlock = stringMap.get(object);
                arrayList.add(
                        structureBlock != null ?
                                structureBlock.map(charMap, stringMap) : new SBlockNull()
                );
            } else
                arrayList.add(object);
        }

        return arrayList;
    }

    /**
     * Extracts strings into characters and string-objects.
     * */
    private static ArrayList<Object> arrayListClear(ArrayList<Object> inputList) {
        ArrayList<Object> arrayList = new ArrayList<Object>();

        for (Object object : inputList) {
            if (object instanceof SBlockArrayList) {
                ArrayList<Object> arrayInputList = ((SBlockArrayList) object).getArrayList();
                if (!arrayInputList.isEmpty()) {
                    if (arrayInputList.size() == 1)
                        arrayList.add(arrayInputList.get(0));
                    else
                        arrayList.add(object);
                }else
                    arrayList.add(new SBlockNull());
            } else
                arrayList.add(object);
        }

        return arrayList;
    }

}
