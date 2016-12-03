package com.sneakingshadow.core.multiblock;

import com.sun.istack.internal.NotNull;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;

import static com.sneakingshadow.core.multiblock.MultiBlockUtil.rotate;

public class MultiBlock {

    private StructureArray structureArray;
    private boolean rotatesAroundX = false;
    private boolean rotatesAroundY = true;
    private boolean rotatesAroundZ = false;

    public MultiBlock(Object... objects) {
        structureArray = InputHandler.getStructureArray(objects);
    }


    /*
    * Set rotation around axis
    * */
    public MultiBlock setRotationXAxis(boolean bool) {
        rotatesAroundX = bool;
        return this;
    }
    public MultiBlock setRotationYAxis(boolean bool) {
        rotatesAroundY = bool;
        return this;
    }
    public MultiBlock setRotationZAxis(boolean bool) {
        rotatesAroundZ = bool;
        return this;
    }


    /*
    * Get rotation around axis
    * */
    public boolean rotatesAroundXAxis() {
        return rotatesAroundX;
    }
    public boolean rotatesAroundYAxis() {
        return rotatesAroundY;
    }
    public boolean rotatesAroundZAxis() {
        return rotatesAroundZ;
    }


    /**
     * Checks for a structure in world, in any possible position that overlaps x,y,z.
     * Returns null if it didn't find a structure.
     * */
    public Structure findStructure(World world, int x, int y, int z) {
        ArrayList<Structure> structure = findStructures(world, x, y, z, false);
        return structure.isEmpty() ? null : structure.get(0);
    }

    /**
     * Finds all possible structures in world. Useful if you want to guarantee there aren't structures overlapping.
     * Returns and empty array if none are found
     * */
    public ArrayList<Structure> findStructures(World world, int x, int y, int z) {
        return findStructures(world, x, y, z, true);
    }

    /**
     * Returns an empty ArrayList if no structures are found.
     * */
    @NotNull
    private ArrayList<Structure> findStructures(World world, int x, int y, int z, boolean checkAllStructures) {
        ArrayList<Structure> structureList = new ArrayList<Structure>();

        if (y < 0 || y > 255)
            return structureList;

        //Loop through array
        for (int ix = 0; ix < structureArray.sizeX(); ix++) {
            for (int iy = 0; iy < structureArray.sizeY(); iy++) {
                for (int iz = 0; iz < structureArray.sizeZ(); iz++) {
                    //Check if program should continue checking for structure at these array coordinates
                    if (structureArray.get(ix, iy, iz).startCheckingForStructure(world, x, y, z)) {
                        /*
                        * Looks at array as a cube with 6 sides and 4 possible rotations per side.
                        * */

                        //Top side, and if rotatesAroundX or rotatesAroundZ then the bottom side as well
                        for (int rotationX = 0; rotationX < (rotatesAroundX || rotatesAroundZ ? 4 : 1); rotationX += 2) {
                            for (int rotationY = 0; rotationY < (rotatesAroundY ? 4 : 1); rotationY++) {
                                Structure structure = validate(world, x, y, z, ix, iy, iz, rotationX, 0, 0);
                                if (structure != null) {
                                    structureList.add(structure);
                                    if (!checkAllStructures)
                                        return structureList;
                                }
                            }
                        }

                        //Two of the four sides that are not top nor bottom, that are inaccessible by rotationZ
                        if (rotatesAroundX) {
                            for (int rotationX = 1; rotationX < 4; rotationX += 2) {
                                for (int rotationZ = 0; rotationZ < (rotatesAroundZ ? 4 : 1); rotationZ++) {
                                    Structure structure = validate(world, x, y, z, ix, iy, iz, rotationX, 0, rotationZ);
                                    if (structure != null) {
                                        structureList.add(structure);
                                        if (!checkAllStructures)
                                            return structureList;
                                    }
                                }
                            }
                        }

                        //Two of the four sides that are not top nor bottom, that are inaccessible by rotationX
                        if (rotatesAroundZ) {
                            for (int rotationZ = 1; rotationZ < 4; rotationZ += 2) {
                                for (int rotationX = 0; rotationX < (rotatesAroundX ? 4 : 1); rotationX++) {
                                    Structure structure = validate(world, x, y, z, ix, iy, iz, rotationX, 0, rotationZ);
                                    if (structure != null) {
                                        structureList.add(structure);
                                        if (!checkAllStructures)
                                            return structureList;
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }

        return structureList;
    }


    /**
     * validates the side accessed by rotationX and rotationZ, in any rotationY.
     * */
    private Structure validate(World world, int x, int y, int z, int ix, int iy, int iz, int rotationX, int rotationY, int rotationZ) {
        Vec3 arrayPosition = rotate(Vec3.createVectorHelper(ix, iy, iz), rotationX, rotationY, rotationZ);
        Vec3 startCorner = arrayPosition.subtract(Vec3.createVectorHelper(x, y, z));

        if (validate(world, startCorner, rotationX, rotationY, rotationZ)) {
            return new Structure(this, world, startCorner, rotationX, rotationY, rotationZ);
        }

        return null;
    }

    /**
     * Validates a structure based on:
     * @param world
     * @param cornerPosition the location of the corner of the structure.
     * @param rotationX the rotation of the structure around the x-axis.
     * @param rotationY the rotation of the structure around the y-axis.
     * @param rotationZ the rotation of the structure around the z-axis.
     *
     * Rotations are measured in quarter-full rotations, meaning from 0 to 3.
     * */
    public boolean validate(World world, Vec3 cornerPosition, int rotationX, int rotationY, int rotationZ) {
        for (int x = 0; x < structureArray.sizeX(); x++) {
            for (int y = 0; y < structureArray.sizeY(); y++) {
                for (int z = 0; z < structureArray.sizeZ(); z++) {
                    Vec3 currentArrayPosition = Vec3.createVectorHelper(x,y,z);
                    Vec3 currentWorldPosition = rotate(
                            Vec3.createVectorHelper(x,y,z), rotationX, rotationY, rotationZ //can't rotate currentArrayPosition, and therefore have to make a new one
                    ).addVector(cornerPosition.xCoord, cornerPosition.yCoord, cornerPosition.zCoord);

                    if (!structureArray.blockIsValid(world, currentWorldPosition, currentArrayPosition, rotationX, rotationY, rotationZ)) {
                        structureArray.reset();
                        return false;
                    }
                }
            }
        }
        structureArray.reset();

        return true;
    }


    public int sizeX() {
        return structureArray.sizeX();
    }
    public int sizeY() {
        return structureArray.sizeY();
    }
    public int sizeZ() {
        return structureArray.sizeZ();
    }


    /**
     * Outputs the structure in string form.
     * Will try to optimize readability, by putting the axis with the lowest size first.
     * */
    public String toString() {
        return super.toString() + "\n\n" + structureArray.toString();
    }

    public void debugStructureArray() {
        structureArray.debug();
    }
}

