package com.sneakingshadow.core.multiblock;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by SneakingShadow on 22.11.2016.
 */
public class InputList extends ArrayList<Object> {

    /**
     * Used by MultiBlock to differentiate between ArrayLists and input.
     * */
    public InputList(Object... objects) {
        Collections.addAll(this, objects);
    }

}
