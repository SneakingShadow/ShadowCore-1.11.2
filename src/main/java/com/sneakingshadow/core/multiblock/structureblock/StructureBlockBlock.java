package com.sneakingshadow.core.multiblock.structureblock;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class StructureBlockBlock extends StructureBlock {

    private Block block;
    private int metadata;

    public StructureBlockBlock(Block block) {
        this(block, -1);
    }

    public StructureBlockBlock(Block block, int metadata) {
        this.block = block;
        this.metadata = metadata;
    }

    @Override
    public boolean blockIsValid(World world, int x, int y, int z){
        return world.getBlock(x,y,z) == block && (metadata == -1 || metadata == world.getBlockMetadata(x,y,z));
    }

    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        return blockIsValid(world, x, y, z);
    }

    /**
     * Used for comparing structures, in order to remove duplicates.
     * */
    @Override
    public boolean equalsStructureBlock(StructureBlock structureBlock) {
        return structureBlock instanceof StructureBlockBlock && block == ((StructureBlockBlock) structureBlock).getBlock() && metadata == ((StructureBlockBlock) structureBlock).getMetadata();
    }

    public Block getBlock() {
        return block;
    }

    /**
     * Returns -1 if no metadata has been specified
     * */
    public int getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        if (metadata == -1) {
            return block.toString();
        }
        return "["+block.toString()+","+metadata+"]";
    }
}
