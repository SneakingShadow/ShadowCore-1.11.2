package com.sneakingshadow.core.util;

import net.minecraft.util.Vec3;

/**
 * Created by SneakingShadow on 27.11.2016.
 */
public class ShadowUtil {

    public static final int Y_AXIS_FACES = 0;
    public static final int X_AXIS_FACES = 1;
    public static final int Z_AXIS_FACES = 2;

    /**
     * What faces are rotated to with each flag.
     *
     * flag = 0:  y axis faces
     * flag = 1:  x axis faces
     * flag = 2:  z axis faces
     * */
    public static Vec3 rotate(Vec3 input, int rotateX, int rotateY, int rotateZ, int flag) {
        Vec3 vector = Vec3.createVectorHelper(input.xCoord, input.yCoord, input.zCoord);

        if (flag == Y_AXIS_FACES) {
            vector.rotateAroundX(quarterRotationValue(rotateX));
            vector.rotateAroundY(quarterRotationValue(rotateY));
        } else if (flag == X_AXIS_FACES) {
            vector.rotateAroundX(quarterRotationValue(rotateX));
            vector.rotateAroundY(quarterRotationValue(rotateZ));
        } else if (flag == Z_AXIS_FACES) {
            vector.rotateAroundZ(quarterRotationValue(rotateZ));
            vector.rotateAroundY(quarterRotationValue(rotateX));
        }

        vector.xCoord = Math.round(vector.xCoord);
        vector.yCoord = Math.round(vector.yCoord);
        vector.zCoord = Math.round(vector.zCoord);

        return vector;
    }

    /**
     * What faces are rotated to with each flag.
     *
     * flag = 0:  y axis faces
     * flag = 1:  x axis faces
     * flag = 2:  z axis faces
     * */
    public static Vec3 rotateAround(Vec3 rotationPoint, Vec3 vector, int rotateX, int rotateY, int rotateZ, int flag) {
        Vec3 vec3 = rotate(rotationPoint.subtract(vector), rotateX, rotateY, rotateZ, flag);

        return rotationPoint.addVector(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    /**
     * return 90 degrees * i
     * */
    public static float quarterRotationValue(int i) {
        return getPieceRotationValue(4,i);
    }

    /**
     * return (360 degrees/parts) * i
     * */
    public static float getPieceRotationValue(int parts, int i) {
        return (float) (i * 2*Math.PI/parts);
    }

}
