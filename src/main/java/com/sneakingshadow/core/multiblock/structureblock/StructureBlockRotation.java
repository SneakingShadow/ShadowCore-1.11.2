package com.sneakingshadow.core.multiblock.structureblock;

public abstract class StructureBlockRotation extends StructureBlock {

    private Direction[] directions = {Direction.NORTH};

    private static int north = 2;
    private static int east = 1;
    private static int south = 3;
    private static int west = 0;

    public enum Direction {
        NORTH, SOUTH, EAST, WEST;

        public boolean correctOrientation(int meta) {
            return (this == NORTH && meta == north)
                    || (this == SOUTH && meta == south)
                    || (this == EAST && meta == east)
                    || (this == WEST && meta == west);
        }

        public Direction rotate(int rotation) {
            Direction direction = this;
            for (int i = 0; i < rotation; i++) {
                direction = rotate();
            }
            return direction;
        }

        private Direction rotate() {
            return this == NORTH ? EAST :
                    this == EAST ? SOUTH :
                            this == SOUTH ? WEST :
                                    NORTH;
        }
    }

    public Direction[] getDirections() {
        return directions;
    }

    //------------set functions------------//
    /*
    * Used for selecting what direction a valid stair should be facing
    * */
    public StructureBlockRotation setNorth() {
        return setDir(new Direction[] {Direction.NORTH});
    }
    public StructureBlockRotation setSouth() {
        return setDir(new Direction[] {Direction.SOUTH});
    }
    public StructureBlockRotation setEast() {
        return setDir(new Direction[] {Direction.EAST});
    }
    public StructureBlockRotation setWest() {
        return setDir(new Direction[] {Direction.WEST});
    }
    /*
    * set functions below are used for corners that are valid for two positions
    * */
    public StructureBlockRotation setNorthEast() {
        return setDir(new Direction[] {Direction.NORTH, Direction.EAST});
    }
    public StructureBlockRotation setNorthWest() {
        return setDir(new Direction[] {Direction.NORTH, Direction.WEST});
    }
    public StructureBlockRotation setSouthEast() {
        return setDir(new Direction[] {Direction.SOUTH, Direction.EAST});
    }
    public StructureBlockRotation setSouthWest() {
        return setDir(new Direction[] {Direction.SOUTH, Direction.WEST});
    }

    //------------set metadata values functions------------//
    /*
    * Used for defining what values each direction has
    * */
    //Default 2
    public StructureBlockRotation setNorthMetadata(int meta) {
        north = meta;
        return this;
    }
    //Default 3
    public StructureBlockRotation setSouthMetadata(int meta) {
        south = meta;
        return this;
    }
    //Default 1
    public StructureBlockRotation setEastMetadata(int meta) {
        east = meta;
        return this;
    }
    //Default 0
    public StructureBlockRotation setWestMetadata(int meta) {
        west = meta;
        return this;
    }
    /**
     * Order: north, west, south, east
     * Default: 2, 0, 3, 1
     * */
    public StructureBlockRotation setMetadata(int[] meta) {
        north = meta[0];
        west = meta[1];
        south = meta[2];
        east = meta[3];
        return this;
    }



    //--------------------------------------//

    private StructureBlockRotation setDir(Direction[] directions) {
        this.directions = directions;
        return this;
    }

}
