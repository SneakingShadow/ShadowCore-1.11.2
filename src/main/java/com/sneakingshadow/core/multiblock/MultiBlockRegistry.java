package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.multiblock.initializer.OperatorInitializer;
import com.sneakingshadow.core.multiblock.initializer.SpecialCharacterInitializer;
import com.sneakingshadow.core.multiblock.initializer.ValueInitializer;
import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.util.ArrayListHelper;

import java.util.ArrayList;

public class MultiBlockRegistry {

    private static ArrayList<Character> specialCharactersUsed = new ArrayList<Character>();
    private static ArrayList<Character> operatorsUsed = new ArrayList<Character>();

    private static ArrayList<Character> otherCharactersUsed = ArrayListHelper.createArrayList(
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            MultiBlock.STRING_KEY, MultiBlock.ORE_DICTIONARY, MultiBlock.BRACKET_START, MultiBlock.BRACKET_END, MultiBlock.NEXT_LINE, MultiBlock.NEXT_LEVEL
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


    public void shout() {
        System.out.println("shout");
    }
}
