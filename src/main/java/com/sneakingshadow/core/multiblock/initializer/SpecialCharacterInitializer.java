package com.sneakingshadow.core.multiblock.initializer;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;

public abstract class SpecialCharacterInitializer extends ValueInitializer {

    /**
     * Input what character should be mapped to what structure block.
     * If that value is used, it will return a structure block in getStructureBlock()
     *
     * This uses .equals to compare values
     * */
    public SpecialCharacterInitializer(Character character) {
        super(character);
    }

    /**
     * @return new structure block object
     * */
    public abstract StructureBlock getStructureBlock();

}
