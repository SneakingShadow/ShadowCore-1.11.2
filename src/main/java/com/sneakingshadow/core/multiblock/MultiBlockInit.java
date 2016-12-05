package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.multiblock.initializer.OperatorInitializer;
import com.sneakingshadow.core.multiblock.initializer.SpecialCharacterInitializer;
import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.multiblock.structureblock.operator.*;
import com.sneakingshadow.core.multiblock.structureblock.special.*;

import static com.sneakingshadow.core.multiblock.MultiBlockRegistry.*;

public class MultiBlockInit {

    public static void init() {
        //Special characters
        MultiBlockRegistry.register(new SpecialCharacterInitializer(NULL) {
            @Override
            public StructureBlock getStructureBlock() {
                return new SBlockNull();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(FULL_BLOCK) {
            @Override
            public StructureBlock getStructureBlock() {
                return new SBlockFull();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(AIR) {
            @Override
            public StructureBlock getStructureBlock() {
                return new SBlockAir();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(REPLACEABLE_BLOCK) {
            @Override
            public StructureBlock getStructureBlock() {
                return new SBlockReplaceable();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(LIQUID) {
            @Override
            public StructureBlock getStructureBlock() {
                return new SBlockLiquid();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(OPAQUE_MATERIAL) {
            @Override
            public StructureBlock getStructureBlock() {
                return new SBlockOpaqueMaterial();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(OPAQUE_LIGHT) {
            @Override
            public StructureBlock getStructureBlock() {
                return new SBlockLightOpaque();
            }
        });

        //Operators
        MultiBlockRegistry.register(new OperatorInitializer(NOT) {
            @Override
            public Operator getOperator() {
                return new OperatorNot();
            }
        });
        MultiBlockRegistry.register(new OperatorInitializer(AND) {
            @Override public Operator getOperator() {
                return new OperatorAnd();
            }
        });
        MultiBlockRegistry.register(new OperatorInitializer(OR) {
            @Override public Operator getOperator() {
                return new OperatorOr();
            }
        });

        //Duplicators
        MultiBlockRegistry.registerDuplicator(new OperatorInitializer(DUPLICATE_LEVEL_0) {
            @Override public Operator getOperator() {
                return new OperatorDuplicate();
            }
        });
        MultiBlockRegistry.registerDuplicator(new OperatorInitializer(DUPLICATE_LEVEL_1) {
            @Override public Operator getOperator() {
                return new OperatorDuplicate();
            }
        });
        MultiBlockRegistry.registerDuplicator(new OperatorInitializer(DUPLICATE_LEVEL_2) {
            @Override public Operator getOperator() {
                return new OperatorDuplicate();
            }
        });
    }

}
