package com.sneakingshadow.core.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import static com.sneakingshadow.core.multiblock.MultiBlockUtil.rotate;

/**
 * Created by SneakingShadow on 23.11.2016.
 */
public class Structure {

    private MultiBlock multiBlock;
    private World world;
    private Vec3 startCorner;
    private Vec3 endCorner;
    private int rotationX;
    private int rotationY;
    private int rotationZ;

    /**
     * Represents the structure in world.
     *
     * @param world
     * @param startCorner coordinate of the start corner of the structure.
     * @param rotationX rotation of the structure around the x-axis.
     * @param rotationY rotation of the structure around the y-axis.
     * @param rotationZ rotation of the structure around the z-axis.
     *
     * */
    public Structure(MultiBlock multiBlock, World world, Vec3 startCorner, int rotationX, int rotationY, int rotationZ) {
        this.multiBlock = multiBlock;
        this.world = world;
        this.startCorner = startCorner;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        setEndCorner();
    }

    public boolean stillValid() {
        return multiBlock.validate(world, startCorner, rotationX, rotationY, rotationZ);
    }

    public MultiBlock getMultiBlock() {
        return multiBlock;
    }

    /**
     * Reads structure from nbt
     * */
    public Structure(MultiBlock multiBlock, World world, NBTTagCompound nbtTagCompound) {
        this.multiBlock = multiBlock;
        this.world = world;
        this.readFromNBT(nbtTagCompound);
    }

    /**
     * Useful for saving multiblock structure, not needing to do 'expensive' checks unless needed to.
     *
     * Tags made in nbtTagCompound:
     *     xCoord
     *     yCoord
     *     zCoord
     *     rotationX
     *     rotationY
     *     rotationZ
     * */
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("xCoord", (int) startCorner.xCoord);
        nbtTagCompound.setInteger("yCoord", (int) startCorner.yCoord);
        nbtTagCompound.setInteger("zCoord", (int) startCorner.zCoord);
        nbtTagCompound.setInteger("rotationX", rotationX);
        nbtTagCompound.setInteger("rotationY", rotationY);
        nbtTagCompound.setInteger("rotationZ", rotationZ);
        return nbtTagCompound;
    }

    /**
     * Useful for updating structure
     * */
    public Structure readFromNBT(NBTTagCompound nbtTagCompound) {
        startCorner = Vec3.createVectorHelper(
                nbtTagCompound.getInteger("xCoord"),
                nbtTagCompound.getInteger("yCoord"),
                nbtTagCompound.getInteger("zCoord")
        );
        rotationX = nbtTagCompound.getInteger("rotationX");
        rotationY = nbtTagCompound.getInteger("rotationY");
        rotationZ = nbtTagCompound.getInteger("rotationZ");
        setEndCorner();

        return this;
    }

    public int getStartXCoord() {
        return (int)startCorner.xCoord;
    }
    public int getStartYCoord() {
        return (int)startCorner.yCoord;
    }
    public int getStartZCoord() {
        return (int)startCorner.zCoord;
    }

    public int getEndXCoord() {
        return (int)endCorner.xCoord;
    }
    public int getEndYCoord() {
        return (int)endCorner.yCoord;
    }
    public int getEndZCoord() {
        return (int)endCorner.zCoord;
    }

    private void setEndCorner() {
        Vec3 arraySize = rotate(Vec3.createVectorHelper(multiBlock.sizeX()-1, multiBlock.sizeY()-1, multiBlock.sizeZ()-1), rotationX,rotationY,rotationZ);
        endCorner = startCorner.addVector(arraySize.xCoord,arraySize.yCoord,arraySize.zCoord);
    }

    @Override
    public String toString() {
        return super.toString() + "\n"
                + "MultiBlock: @" + Integer.toHexString(multiBlock.hashCode()) + "\n"
                + "Start corner: (" + startCorner.xCoord + "," + startCorner.yCoord + "," + startCorner.zCoord + ")" + "\n"
                + "End corner: (" + endCorner.xCoord + "," + endCorner.yCoord + "," + endCorner.zCoord + ")";
    };
}
