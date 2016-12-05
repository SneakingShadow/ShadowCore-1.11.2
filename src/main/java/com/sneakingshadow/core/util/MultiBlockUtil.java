package com.sneakingshadow.core.util;

import net.minecraft.util.Vec3;

/**
 * Created by SneakingShadow on 27.11.2016.
 */
public class MultiBlockUtil {

    /**
     * flag = 0:  top face
     * flag = 1:  x side face
     * flag = 2:  z side face
     * */
    public static Vec3 rotate(Vec3 vector, int rotateX, int rotateY, int rotateZ, int flag) {
        if (flag == 0) {
            vector.rotateAroundX(getRotationValue(rotateX));
            vector.rotateAroundY(getRotationValue(rotateY));
        } else if (flag == 1) {
            vector.rotateAroundX(getRotationValue(rotateX));
            vector.rotateAroundY(getRotationValue(rotateZ));
        } else {
            vector.rotateAroundZ(getRotationValue(rotateZ));
            vector.rotateAroundY(getRotationValue(rotateX));
        }

        vector.xCoord = Math.round(vector.xCoord);
        vector.yCoord = Math.round(vector.yCoord);
        vector.zCoord = Math.round(vector.zCoord);

        return vector;
    }

    public static float getRotationValue(int i) {
        return (float) (((float)i)/2 * Math.PI);
    }

}
