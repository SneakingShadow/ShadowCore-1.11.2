package com.sneakingshadow.core.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by SneakingShadow on 23.11.2016.
 */
public class ArrayListHelper {

    /**
     * Initializes an ArrayList with objects.
     * */
    public static <T> ArrayList<T> createArrayList(T... objects) {
        ArrayList<T> arrayList = new ArrayList<T>();

        Collections.addAll(arrayList, objects);

        return arrayList;
    }

    public static <T> ArrayList<T> fromArray(T[] objects) {
        ArrayList<T> arrayList = new ArrayList<T>();

        Collections.addAll(arrayList,objects);

        return arrayList;
    }

    public static String arrayToString(ArrayList arrayList) {
        String string = "{";

        for (int i = 0; i < arrayList.size(); i++) {
            Object object = arrayList.get(i);

            if (object == null)
                string += "null";
            else if (object instanceof ArrayList)
                string += arrayToString((ArrayList) object);
            else if (object instanceof String)
                string += "\"" + object.toString() + "\"";
            else if (object instanceof Character)
                string += "'" + object.toString() + "'";
            else
                string += object.toString();
            string += i < arrayList.size()-1 ? ", " : "";
        }

        return string+"}";
    }
}
