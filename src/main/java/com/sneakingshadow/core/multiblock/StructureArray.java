package com.sneakingshadow.core.multiblock;

import com.sneakingshadow.core.multiblock.structureblock.StructureBlock;
import com.sneakingshadow.core.multiblock.structureblock.special.SBlockNull;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by SneakingShadow on 22.11.2016.
 */
class StructureArray {

    private ArrayList<ArrayList<ArrayList<StructureBlock>>> structure = new ArrayList<ArrayList<ArrayList<StructureBlock>>>();

    StructureArray() {
        structure.add(new ArrayList<ArrayList<StructureBlock>>());
        structure.get(0).add(new ArrayList<StructureBlock>());
        structure.get(0).get(0).add(new SBlockNull());
    }


    void set(int x, int y, int z, StructureBlock structureBlock) {
        ensureCapacity(x,y,z);
        structure.get(x).get(y).set(z, structureBlock == null ? new SBlockNull() : structureBlock);
    }

    StructureBlock get(int x, int y, int z) {
        if (x < sizeX() && y < sizeY() && z < sizeZ())
            return structure.get(x).get(y).get(z);
        return new SBlockNull();
    }

    boolean blockIsValid(World world, Vec3 worldPosition, Vec3 arrayPosition, int rotationX, int rotationY, int rotationZ) {
        return get((int)arrayPosition.xCoord, (int)arrayPosition.yCoord, (int)arrayPosition.zCoord)
                .blockIsValid(world, worldPosition, arrayPosition, rotationX, rotationY, rotationZ);
    }


    int sizeX() {
        return structure.size();
    }

    int sizeY() {
        return structure.get(0).size();
    }

    int sizeZ() {
        return structure.get(0).get(0).size();
    }


    void reset() {
        for (int x = 0; x < sizeX(); x++) {
            for (int y = 0; y < sizeY(); y++) {
                for (int z = 0; z < sizeZ(); z++) {
                    structure.get(x).get(y).get(z).reset();
                }
            }
        }
    }


    private void ensureCapacity(int x, int y, int z) {
        boolean x_bool = x >= sizeX(); // x is too large
        boolean y_bool = y >= sizeY(); // y is too large
        boolean z_bool = z >= sizeZ(); // z is too large

        //Doesn't need to go through anything
        if (!x_bool && !y_bool && !z_bool)
            return;

        //Doesn't need to go through x array unless y array or z array are too small
        if (y_bool || z_bool)
            //Go through and ensure current x array
            for (int ix = 0; ix < structure.size(); ix++)
            {
                //Doesn't need to go through y array unless z is too small
                if (z_bool)
                    for (int iy = 0; iy < structure.get(ix).size(); iy++)
                        //Ensure z array
                        for (int iz = structure.get(ix).get(iy).size(); iz < z + 1; iz++)
                            structure.get(ix).get(iy).add(new SBlockNull());

                //Ensure y array. Doesn't need to check if y_bool, as iy < y+1 serves this function
                for (int iy = structure.get(ix).size(); iy < y+1; iy++)
                    structure.get(ix).add(getEmptyArray(z));
            }

        //Ensure structure
        for (int ix = structure.size(); ix < x+1; ix++)
            structure.add(getEmptyArray(y, z));
    }

    private int maxY(int y) {
        return Math.max(y+1, sizeY());
    }

    private int maxZ(int z) {
        return Math.max(z+1, sizeZ());
    }

    private ArrayList<StructureBlock> getEmptyArray(int z) {
        ArrayList<StructureBlock> arrayList = new ArrayList<StructureBlock>();
        for (int iz = 0; iz < maxZ(z); iz++) {
            arrayList.add(new SBlockNull());
        }
        return arrayList;
    }

    private ArrayList<ArrayList<StructureBlock>> getEmptyArray(int y, int z) {
        ArrayList<ArrayList<StructureBlock>> arrayList = new ArrayList<ArrayList<StructureBlock>>();
        for (int iy = 0; iy < maxY(y); iy++) {
            arrayList.add(getEmptyArray(z));
        }
        return arrayList;
    }

    /**
     * outputs the structure in string form.
     * Will try to optimize readability by putting the axis with the lowest size first.
     * */
    @Override
    public String toString() {
        String string = "";
        String whitespace = "  ";

        String[] order = order();
        for (int i = 0; i < sizeFromString(order[0]); i++) {
            string += order[0] + "=" + i + "{\n";

            for (int j = 0; j < sizeFromString(order[1]); j++) {
                string += whitespace + order[1] + "=" + j + "{\n";

                for (int k = 0; k < sizeFromString(order[2]); k++) {
                    int[] valuesXYZ = valuesXYZ(order.clone(),new int[]{i,j,k});

                    string += whitespace+whitespace + order[2] + "=" + k + ": "
                            + get(valuesXYZ[0],valuesXYZ[1],valuesXYZ[2]).toString() + "\n";
                }
                string += "    }\n";
            }
            string += "}\n";
        }

        return string;
    }

    //---------------Used by toString---------------//

    /**
     * sorts size of array in x, y and z direction by size, starting with smallest.
     * */
    private String[] order() {
        String[] strings = {"y","x","z"};

        //Swap first and second element if first element is larger
        if (sizeFromString(strings[0]) > sizeFromString(strings[1])) {
            String string = strings[0];
            strings[0] = strings[1];
            strings[1] = string;
        }
        //Swap second and third element if second element is larger
        if (sizeFromString(strings[1]) > sizeFromString(strings[2])) {
            String string = strings[1];
            strings[1] = strings[2];
            strings[2] = string;
        }

        return strings;
    }

    /**
     * returns size of array dimensions, based on string inputted.
     * "x" -> sizeX()
     * "y" -> sizeY()
     * default -> sizeZ()
     * */
    private int sizeFromString(String string) {
        if ("x".equals(string))
            return sizeX();
        else if ("y".equals(string))
            return sizeY();
        else
            return sizeZ();
    }

    private int[] valuesXYZ(String[] strings, int[] sizes) {
        //Check x
        for (int i = 0; i < 3; i++) {
            if ("x".equals(strings[i])) {
                String string = strings[0];
                strings[0] = strings[i];
                strings[i] = string;

                int size = sizes[0];
                sizes[0] = sizes[i];
                sizes[i] = size;
                break;
            }
        }
        //Check y
        for (int i = 1; i < 3; i++) {
            if ("y".equals(strings[i])) {
                String string = strings[1];
                strings[1] = strings[i];
                strings[i] = string;

                int size = sizes[1];
                sizes[1] = sizes[i];
                sizes[i] = size;
                break;
            }
        }

        return sizes;
    }

    //---------------Debug---------------//

    /**
     * Use in case you suspect there's a bug with the given sizes of the array.
     * */
    void debug() {
        System.out.println("size x: " + structure.size());
        for (int x = 0; x < structure.size(); x++) {
            System.out.println("x="+x+": size y: " + structure.get(x).size());
            for (int y = 0; y < structure.get(x).size(); y++) {
                System.out.println("x="+x+": y="+y+": size z;" + structure.get(x).get(y).size());
            }
        }
    }

}
