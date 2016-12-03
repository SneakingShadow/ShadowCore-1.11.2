package com.sneakingshadow.core.multiblock.initializer;

import com.sneakingshadow.core.multiblock.structureblock.operator.Operator;

public abstract class OperatorInitializer extends ValueInitializer {

    /**
     * Input what character should be mapped to what operator.
     *
     * If that value is used, it will return an operator in getOperator()
     * This uses .equals to compare values
     * */
    public OperatorInitializer(Character character) {
        super(character);
    }

    /**
     * @return new operator object
     * */
    public abstract Operator getOperator();

}
