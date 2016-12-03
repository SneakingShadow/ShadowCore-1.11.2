package com.sneakingshadow.core.multiblock.structureblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;

public class StructureBlockStair extends StructureBlockRotation {

    private ArrayList<Block> blocks = null;

    /**
     * Checks world.getBlock(x,y,z) instanceof BlockStairs
     *
     * Default direction set to north
     * */
    public StructureBlockStair() {}

    /**
     * Checks world.getBlock(x,y,z).equals(block)
     *
     * Default direction set to north
     * */
    public StructureBlockStair(Block block) {
        this(new Block[]{block});
    }

    /**
     * Checks world.getBlock(x,y,z).equals(block) for every block in blocks.
     *
     * Default direction set to north
     * */
    public StructureBlockStair(Block[] blocks) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        Collections.addAll(arrayList,blocks);
        this.blocks = arrayList;
    }



    /**
     * A small un-official check to determine if it should continue checking in world.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    @Override
    public boolean startCheckingForStructure(World world, int x, int y, int z) {
        if (blocks == null)
            return world.getBlock(x,y,z) instanceof BlockStairs;
        else
            for (Block block : blocks)
                if (world.getBlock(x,y,z).equals(block))
                    return true;

        return false;
    }

    /**
     * @param world
     * @param worldPosition position of block in world
     * @param arrayPosition position of block in world
     * @param rotationX rotation around xAxis
     * @param rotationY rotation around yAxis
     * @param rotationZ rotation around zAxis
     *
     * Note:
     *     Rotations go from 0 to 3, and are measured in rotations of 90°, which is equal to 1/2 pi radians.
     * */
    public boolean blockIsValid(World world, Vec3 worldPosition, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ) {
        if(blocks == null) {
            boolean bool = world.getBlock((int)worldPosition.xCoord, (int)worldPosition.yCoord, (int)worldPosition.zCoord) instanceof BlockStairs;
            for (Direction direction : getDirections())
                if (bool && direction.rotate(rotationY).correctOrientation(world.getBlockMetadata((int)worldPosition.xCoord, (int)worldPosition.yCoord, (int)worldPosition.zCoord)))
                    return true;

        } else {
            for (Block block : blocks) {
                boolean bool = world.getBlock((int)worldPosition.xCoord, (int)worldPosition.yCoord, (int)worldPosition.zCoord).equals(block);
                for (Direction direction : getDirections())
                    if (bool && direction.rotate(rotationY).correctOrientation(world.getBlockMetadata((int)worldPosition.xCoord, (int)worldPosition.yCoord, (int)worldPosition.zCoord)))
                        return true;
            }
        }

        return false;
    }
}
