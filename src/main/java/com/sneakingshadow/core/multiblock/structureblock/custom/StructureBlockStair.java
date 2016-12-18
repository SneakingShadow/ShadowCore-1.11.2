package com.sneakingshadow.core.multiblock.structureblock.custom;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.util.ShadowUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;

public class StructureBlockStair extends StructureBlock {

    private ArrayList<Block> blocks = null;
    private Direction[] directions = {Direction.NORTH};

    /**
     * Checks world.getBlock(x,y,z) instanceof BlockStairs
     *
     * Default direction set to north
     * */
    public StructureBlockStair() {}

    /**
     * Checks block.equals(world.getBlock(x,y,z))
     *
     * Default direction set to north
     * */
    public StructureBlockStair(Block block) {
        this(new Block[]{block});
    }

    /**
     * Checks block.equals(world.getBlock(x,y,z)) for every block in blocks.
     *
     * Default direction set to north
     * */
    public StructureBlockStair(Block[] blocks) {
        this.blocks = new ArrayList<Block>();
        Collections.addAll(this.blocks, blocks);
    }

    /*
     * Default metadata values.
     * Opposite of the direction player faces when placing.
     *
     * Down values are for when the stair is placed upside down.
     * */
    private int west = 0;
    private int east = 1;
    private int north = 2;
    private int south = 3;
    private int down_west = 4;
    private int down_east = 5;
    private int down_north = 6;
    private int down_south = 7;

    public StructureBlockStair setMetadataNorth(int metadata) {
        north = metadata;
        return this;
    }
    public StructureBlockStair setMetadataSouth(int metadata) {
        south = metadata;
        return this;
    }
    public StructureBlockStair setMetadataEast(int metadata) {
        east = metadata;
        return this;
    }
    public StructureBlockStair setMetadataWest(int metadata) {
        west = metadata;
        return this;
    }
    public StructureBlockStair setMetadataNorthDown(int metadata) {
        down_north = metadata;
        return this;
    }
    public StructureBlockStair setMetadataSouthDown(int metadata) {
        down_south = metadata;
        return this;
    }
    public StructureBlockStair setMetadataEastDown(int metadata) {
        down_east = metadata;
        return this;
    }
    public StructureBlockStair setMetadataWestDown(int metadata) {
        down_west = metadata;
        return this;
    }

    /**
     * Sets the valid orientation of the stair.
     * */
    public StructureBlockStair setOrientation(Direction direction) {
        directions = new Direction[] {direction};
        return this;
    }

    /**
     * Sets the valid orientations of the stair.
     * */
    public StructureBlockStair setOrientations(Direction... directions) {
        this.directions = directions;
        return this;
    }

    /**
     * Returns the direction with the same metadata.
     * Defaults to north.
     * */
    public Direction getDirectionFromMeta(int metadata) {
        int[] metaTable = new int[]{west,east,north,south,down_north,down_south,down_east,down_west};

        for (int i = 0; i < metaTable.length; i++)
            if (metaTable[i] == metadata)
                for (Direction direction : Direction.values())
                    if (direction.getMetadata() == i)
                        return direction;

        return Direction.NORTH;
    }


    /**
     * The direction the stair is facing.
     *
     * DOWN_NORTH = Upside down stair facing north.
     * */
    public enum Direction {
        WEST(1), EAST(2), NORTH(3), SOUTH(4),
        DOWN_WEST(4), DOWN_EAST(5), DOWN_NORTH(6), DOWN_SOUTH(7);

        int metadata;

        Direction(int metadata) {
            this.metadata = metadata;
        }

        public int getMetadata() {
            return metadata;
        }
    }



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

    @Override
    public boolean blockIsValid(World world, Vec3 pos, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ, int flag) {
        if (flag != ShadowUtil.Y_AXIS_FACES)
            return false;

        Block block = world.getBlock((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);

        if(blocks == null)
        {
            if (block instanceof BlockStairs)
                return validOrientation(world, pos, rotationX, rotationY, rotationZ);
        }
        else
        {
            for (Block stair : blocks)
                if (stair.equals(block))
                    return validOrientation(world, pos, rotationX, rotationY, rotationZ);
        }

        return false;
    }

    private boolean validOrientation(World world, Vec3 pos, int rotationX, int rotationY, int rotationZ) {

        int[] meta = {west, east, north, south, down_west, down_east, down_north, down_south};


        for (Direction direction : directions) {
            Vec3 rotated = ShadowUtil.rotate(Vec3.createVectorHelper(0,1,1), rotationX, rotationY, rotationZ, ShadowUtil.Y_AXIS_FACES);

            int num = (rotated.yCoord == -1 ? 4 : 0)
                    + (rotated.zCoord == 0 ? 0 : 2)
                    + (int)((rotated.zCoord+1)/2)
                    + (int)((rotated.xCoord+1)/2);

            if (direction.getMetadata() == num)
                return meta[num] == world.getBlockMetadata((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);

        }

        return false;
    }
}
