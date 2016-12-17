package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.util.ShadowUtil;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;

import static com.sneakingshadow.core.util.ShadowUtil.rotate;

public class MultiBlock {

    //Special values
    public static final Character NULL = ' ';
    public static final Character FULL_BLOCK = '+';
    public static final Character AIR = '_';
    public static final Character REPLACEABLE_BLOCK = '-';
    public static final Character LIQUID = '~';
    public static final Character OPAQUE_MATERIAL = '*';
    public static final Character OPAQUE_LIGHT = '#';
    //Operators
    public static final Character BRACKET_START = '(';
    public static final Character BRACKET_END = ')';
    public static final Character NOT = '!';
    public static final Character AND = '&';
    public static final Character OR = '|';
    //Duplicators
    public static final Character DUPLICATE_LEVEL_0 = '<';
    public static final Character DUPLICATE_LEVEL_1 = '>';
    public static final Character DUPLICATE_LEVEL_2 = '=';
    public static final Character[] DUPLICATORS = {DUPLICATE_LEVEL_0, DUPLICATE_LEVEL_1, DUPLICATE_LEVEL_2};
    //Modifiers
    public static final Character ORE_DICTIONARY = '@';
    //Mapping
    public static final Character STRING_KEY = '^';
    //Structure modifiers
    public static final Character NEXT_LINE = '/';
    public static final Character NEXT_LEVEL = '\\';


    private StructureArray structureArray;
    private boolean rotatesAroundX = false;
    private boolean rotatesAroundY = true;
    private boolean rotatesAroundZ = false;

    public MultiBlock(Object... objects) {
        structureArray = InputHandler.getStructureArray(objects);
    }


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
     * Checks for a structure in world, in any possible position that overlaps x,y,z.
     * Returns null if it didn't find a structure.
     * */
    public Structure findStructure(World world, int x, int y, int z) {
        ArrayList<Structure> structure = findStructures(world, x, y, z, false, false);
        return structure.isEmpty() ? null : structure.get(0);
    }

    /**
     * Finds all possible structures in world that overlaps x,y,z.
     * Returns an empty array if none are found.
     * Excludes duplicate structures.
     * */
    public ArrayList<Structure> findStructures(World world, int x, int y, int z) {
        return findStructures(world, x, y, z, true);
    }

    /**
     * Finds all possible structures in world that overlaps x,y,z.
     * Returns an empty array if none are found.
     * If excludeDuplicates is true, then it won't add duplicate structures.
     * */
    public ArrayList<Structure> findStructures(World world, int x, int y, int z, boolean excludeDuplicates) {
        return findStructures(world, x, y, z, excludeDuplicates, true);
    }

    /**
     * Finds all possible structures in world that overlaps x,y,z.
     * Returns an empty array if none are found.
     * If excludeDuplicates is true, then it won't add duplicate structures.
     * If checkAllStructures is false, then it won't check for more structures after finding one.
     * */
    public ArrayList<Structure> findStructures(World world, int x, int y, int z, boolean excludeDuplicates, boolean checkAllStructures) {
        ArrayList<Structure> structures = new ArrayList<Structure>();

        if (y < 0 || y > 255)
            return structures;

        for (int ix = 0; ix < structureArray.sizeX(); ix++) {
            for (int iy = 0; iy < structureArray.sizeY(); iy++) {
                for (int iz = 0; iz < structureArray.sizeZ(); iz++) {

                    if (structureArray.get(ix, iy, iz).startCheckingForStructure(world, x, y, z)) {
                        //Looks at structureArray as a cube with 6 sides and 4 possible rotations per side.

                        //Top side, and if rotatesAroundX or rotatesAroundZ then the bottom side as well
                        for (int rotationX = 0; rotationX < (rotatesAroundX || rotatesAroundZ ? 4 : 1); rotationX += 2) {
                            for (int rotationY = 0; rotationY < (rotatesAroundY ? 4 : 1); rotationY++) {
                                Structure structure = validate(world, x, y, z, ix, iy, iz, rotationX, rotationY, 0, ShadowUtil.Y_AXIS_FACES);
                                if (structure != null && (!excludeDuplicates || !containsStructure(structures, structure))) {
                                    structures.add(structure);
                                    if (!checkAllStructures)
                                        return structures;
                                }
                            }
                        }

                        //Two of the four sides that are not top nor bottom, that are inaccessible by rotationZ
                        if (rotatesAroundX)
                            for (int rotationX = 1; rotationX < 4; rotationX += 2) {
                                for (int rotationZ = 0; rotationZ < (rotatesAroundZ ? 4 : 1); rotationZ++) {
                                    Structure structure = validate(world, x, y, z, ix, iy, iz, rotationX, 0, rotationZ, ShadowUtil.X_AXIS_FACES);
                                    if (structure != null && (!excludeDuplicates || !containsStructure(structures, structure))) {
                                        structures.add(structure);
                                        if (!checkAllStructures)
                                            return structures;
                                    }
                                }
                            }

                        //Two of the four sides that are not top nor bottom, that are inaccessible by rotationX
                        if (rotatesAroundZ)
                            for (int rotationZ = 1; rotationZ < 4; rotationZ += 2) {
                                for (int rotationX = 0; rotationX < (rotatesAroundX ? 4 : 1); rotationX++) {
                                    Structure structure = validate(world, x, y, z, ix, iy, iz, rotationX, 0, rotationZ, ShadowUtil.Z_AXIS_FACES);
                                    if (structure != null && (!excludeDuplicates || !containsStructure(structures, structure))) {
                                        structures.add(structure);
                                        if (!checkAllStructures)
                                            return structures;
                                    }
                                }
                            }
                    }
                }
            }
        }

        return structures;
    }


    /**
     * validates the side accessed by rotationX and rotationZ, in any rotationY.
     * */
    private Structure validate(World world, int x, int y, int z, int ix, int iy, int iz, int rotationX, int rotationY, int rotationZ, int flag) {
        Vec3 arrayPosition = rotate(Vec3.createVectorHelper(ix, iy, iz), rotationX, rotationY, rotationZ, flag);
        Vec3 startCorner = arrayPosition.subtract(Vec3.createVectorHelper(x, y, z));

        if (validate(world, startCorner, rotationX, rotationY, rotationZ, flag)) {
            return new Structure(this, world, startCorner, rotationX, rotationY, rotationZ, flag);
        }

        return null;
    }

    /**
     * Validates there's a structure in world, with specified corner and rotation
     * */
    public boolean validate(World world, Vec3 cornerPosition, int rotationX, int rotationY, int rotationZ, int flag) {
        Vec3 rotation = rotate(Vec3.createVectorHelper(1,1,1), rotationX, rotationY, rotationZ, flag);

        for (int x = 0; x < structureArray.sizeX(); x++) {
            for (int y = 0; y < structureArray.sizeY(); y++) {
                for (int z = 0; z < structureArray.sizeZ(); z++) {
                    Vec3 currentArrayPosition = Vec3.createVectorHelper(x,y,z);
                    Vec3 currentWorldPosition = Vec3.createVectorHelper(
                            cornerPosition.xCoord + rotation.xCoord*x,
                            cornerPosition.yCoord + rotation.yCoord*y,
                            cornerPosition.zCoord + rotation.zCoord*z
                    );

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


    private boolean containsStructure(ArrayList<Structure> structureList, Structure structure) {
        Vec3 arrayCornerInput = rotate(
                Vec3.createVectorHelper(sizeX(), sizeY(), sizeZ()),
                structure.getRotationX(),
                structure.getRotationY(),
                structure.getRotationZ(),
                structure.getFlag()
        );
        boolean bool_input_x = arrayCornerInput.xCoord < 0;
        boolean bool_input_y = arrayCornerInput.yCoord < 0;
        boolean bool_input_z = arrayCornerInput.zCoord < 0;

        for (Structure list_structure : structureList) {
            if (sameCorners(list_structure, structure)) {
                Vec3 arrayCornerList = rotate(
                        Vec3.createVectorHelper(sizeX(), sizeY(), sizeZ()),
                        list_structure.getRotationX(),
                        list_structure.getRotationY(),
                        list_structure.getRotationZ(),
                        list_structure.getFlag()
                );
                boolean bool_x = arrayCornerList.xCoord < 0;
                boolean bool_y = arrayCornerList.yCoord < 0;
                boolean bool_z = arrayCornerList.zCoord < 0;

                boolean bool = true;
                for (int x = 0; bool && x < sizeX(); x++) {
                    for (int y = 0; bool && y < sizeY(); y++) {
                        for (int z = 0; bool && z < sizeZ(); z++) {
                            int input_x = bool_input_x ? sizeX()-1 - x : x;
                            int input_y = bool_input_y ? sizeY()-1 - y : y;
                            int input_z = bool_input_z ? sizeZ()-1 - z : z;

                            int list_x = bool_x ? sizeX()-1 - x : x;
                            int list_y = bool_y ? sizeY()-1 - y : y;
                            int list_z = bool_z ? sizeZ()-1 - z : z;

                            if (!structureArray.get(input_x, input_y, input_z).equalsStructureBlock(structureArray.get(list_x,list_y,list_z)))
                                bool = false;

                        }
                    }
                }

                if (bool)
                    return true;
            }
        }

        return false;
    }

    private boolean sameCorners(Structure list_structure, Structure structure) {
        int[][] list_values = new int[][] {
                {list_structure.getStartXCoord(), list_structure.getEndXCoord()},
                {list_structure.getStartYCoord(), list_structure.getEndYCoord()},
                {list_structure.getStartZCoord(), list_structure.getEndZCoord()}
        };
        int[][] input_values = new int[][] {
                {structure.getStartXCoord(), structure.getEndXCoord()},
                {structure.getStartYCoord(), structure.getEndYCoord()},
                {structure.getStartZCoord(), structure.getEndZCoord()}
        };

        for (int axis = 0; axis < 3; axis++)
            if ((input_values[axis][0] != list_values[axis][0] || input_values[axis][1] != list_values[axis][1])
                    && (input_values[axis][0] != list_values[axis][1] || input_values[axis][1] != list_values[axis][0]))
                return false;

        return true;
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

