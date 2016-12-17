package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.multiblock.initializer.OperatorInitializer;
import com.sneakingshadow.core.multiblock.initializer.SpecialCharacterInitializer;
import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.multiblock.structureblock.operator.*;
import com.sneakingshadow.core.multiblock.structureblock.special.*;

public class MultiBlockInit {

    public static void init() {

        //Special characters
        MultiBlockRegistry.register(new SpecialCharacterInitializer(MultiBlock.NULL) {
            @Override
            public StructureBlock getStructureBlock() {
                return new StructureBlockNull();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(MultiBlock.FULL_BLOCK) {
            @Override
            public StructureBlock getStructureBlock() {
                return new StructureBlockFull();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(MultiBlock.AIR) {
            @Override
            public StructureBlock getStructureBlock() {
                return new StructureBlockAir();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(MultiBlock.REPLACEABLE_BLOCK) {
            @Override
            public StructureBlock getStructureBlock() {
                return new StructureBlockReplaceable();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(MultiBlock.LIQUID) {
            @Override
            public StructureBlock getStructureBlock() {
                return new StructureBlockLiquid();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(MultiBlock.OPAQUE_MATERIAL) {
            @Override
            public StructureBlock getStructureBlock() {
                return new StructureBlockOpaqueMaterial();
            }
        });
        MultiBlockRegistry.register(new SpecialCharacterInitializer(MultiBlock.OPAQUE_LIGHT) {
            @Override
            public StructureBlock getStructureBlock() {
                return new StructureBlockOpaque();
            }
        });

        //Operators
        MultiBlockRegistry.register(new OperatorInitializer(MultiBlock.NOT) {
            @Override
            public Operator getOperator() {
                return new OperatorNot();
            }
        });
        MultiBlockRegistry.register(new OperatorInitializer(MultiBlock.AND) {
            @Override public Operator getOperator() {
                return new OperatorAnd();
            }
        });
        MultiBlockRegistry.register(new OperatorInitializer(MultiBlock.OR) {
            @Override public Operator getOperator() {
                return new OperatorOr();
            }
        });

        //Duplicators
        for (Character duplicator : MultiBlock.DUPLICATORS) {
            MultiBlockRegistry.registerDuplicator(new OperatorInitializer(duplicator) {
                @Override public Operator getOperator() {
                    return new OperatorDuplicate();
                }
            });
        }
    }

}
