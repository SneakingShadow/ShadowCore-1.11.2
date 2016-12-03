package com.sneakingshadow.core.util;

import java.util.ArrayList;

/**
 * Created by SneakingShadow on 25.11.2016.
 */
public class StringUtil {

    /**
     * Splits the string wherever character is found.
     * If encased is true, then strings that are encased in character will have that character at the start.
     *
     * Example
     *     splitString("String 1@String 2@ String 3", '@', true) -> ArrayList<String> "String 1", "@String 2", " String 3" </String>
     * */
    public static ArrayList<String> splitString(String string, Character character, boolean encased) {
        ArrayList<String> arrayList = new ArrayList<String>();

        string += character;
        int start = 0;
        int end = string.indexOf(character);
        if (end == 0)
            end = string.indexOf(character, 1);

        if (end == -1) {
            arrayList.add(string);
        }

        while (end != -1) {
            String string_object = string.substring(start, end);

            if (!string_object.isEmpty())
                arrayList.add( string_object );

            start = end;
            end = string.indexOf(character, end + 1);

            if (encased && !string_object.isEmpty() && character.equals(string_object.charAt(0))) {
                start++;
            }
        }

        return arrayList;
    }

    public static ArrayList<String> splitString(String string, Character character) {
        return splitString(string, character, false);
    }

    public static ArrayList<String> splitString(String string, Character[] characters) {
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add(string);

        for (Character character : characters) {
            String[] stringArray = stringList.toArray(new String[0]);
            stringList = new ArrayList<String>();

            for (String str : stringArray) {
                ArrayList<String> arrayList = splitString(str, character, false);
                for (String arrayString : arrayList) {
                    stringList.add(arrayString);
                }
            }
        }

        return stringList;
    }

    public static ArrayList<String> splitString(String string, ArrayList<Character> characters) {
        return splitString(string, (Character[]) characters.toArray());
    }

}
