package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.multiblock.initializer.OperatorInitializer;
import com.sneakingshadow.core.multiblock.initializer.SpecialCharacterInitializer;
import com.sneakingshadow.core.multiblock.initializer.ValueInitializer;
import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.util.ArrayListHelper;

import java.util.ArrayList;

public class MultiBlockRegistry {

    //Special values
    public static final Character NULL = ' ';
    public static final Character FULL_BLOCK = '+';
    public static final Character AIR = '_';
    public static final Character REPLACEABLE_BLOCK = '-';
    public static final Character LIQUID = '~';
    public static final Character OPAQUE_MATERIAL = '*';
    public static final Character OPAQUE_LIGHT = '#';

    //Operators
    public static final Character BRACKET_START = '(';
    public static final Character BRACKET_END = ')';
    public static final Character NOT = '!';
    public static final Character AND = '&';
    public static final Character OR = '|';

    //Duplicators
    public static final Character DUPLICATE_LEVEL_0 = '<';
    public static final Character DUPLICATE_LEVEL_1 = '>';
    public static final Character DUPLICATE_LEVEL_2 = '=';

    //Modifiers
    public static final Character ORE_DICTIONARY = '@';

    //Mapping
    public static final Character STRING_KEY = '^';

    //Structure modifiers
    public static final Character NEXT_LINE = '/';
    public static final Character NEXT_LEVEL = '\\';



    private static ArrayList<Character> specialCharactersUsed = new ArrayList<Character>();
    private static ArrayList<Character> operatorsUsed = new ArrayList<Character>();

    private static ArrayList<Character> otherCharactersUsed = ArrayListHelper.createArrayList(
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            STRING_KEY, ORE_DICTIONARY, BRACKET_START, BRACKET_END, NEXT_LINE, NEXT_LEVEL
    );

    private static ArrayList<SpecialCharacterInitializer> specialCharacterInitializerList = new ArrayList<SpecialCharacterInitializer>();
    private static ArrayList<OperatorInitializer> operatorInitializerList = new ArrayList<OperatorInitializer>();
    private static ArrayList<OperatorInitializer> duplicatorInitializerList = new ArrayList<OperatorInitializer>();

    public static boolean specialCharacterUsed(Character value) {
        return specialCharactersUsed.contains(value);
    }

    public static boolean operatorUsed(Character value) {
        return operatorsUsed.contains(value);
    }

    public static boolean characterUsed(Character value) {
        return otherCharactersUsed.contains(value) || specialCharacterUsed(value) || operatorUsed(value);
    }


    //-------------Special Values-------------//
    /**
     * @return structure block from special value, or null if invalid value.
     * */
    static StructureBlock getSpecialCharacter(Character character) {
        for (SpecialCharacterInitializer initializer : specialCharacterInitializerList)
            if (initializer.isSpecialCharacter(character))
                return initializer.getStructureBlock();

        return null;
    }

    /**
     * Special values are registered here.
     * @return successful
     * */
    public static boolean register(SpecialCharacterInitializer specialCharacterInitializer) {
        return register(specialCharacterInitializer, specialCharacterInitializerList, specialCharactersUsed);
    }

    //-------------Operators-------------//
    /**
     * @return returns list of operator
     * */
    static ArrayList<OperatorInitializer> getOperatorList() {
        return operatorInitializerList;
    }

    /**
     * Operators are registered here.
     * @return successful
     * */
    public static boolean register(OperatorInitializer operatorInitializer) {
        return register(operatorInitializer, operatorInitializerList, operatorsUsed);
    }

    //------------Duplicators------------//
    /**
     * @return returns operator, and null if invalid value
     * */
    static OperatorInitializer getDuplicatorInitializer(int number) {
        return duplicatorInitializerList.get(number);
    }

    /**
     * Duplicators are registered here.
     * @return successful
     * */
    static boolean registerDuplicator(OperatorInitializer duplicatorInitializer) {
        return register(duplicatorInitializer, duplicatorInitializerList, operatorsUsed);
    }

    //------------Private stuff------------//
    private static <T extends ValueInitializer> boolean register(T initializer, ArrayList<T> arrayList, ArrayList<Character> charactersUsedList) {
        if (!characterUsed(initializer.getCharacter())) {
            arrayList.add(initializer);
            charactersUsedList.add(initializer.getCharacter());
            return true;
        }
        return false;
    }

}
