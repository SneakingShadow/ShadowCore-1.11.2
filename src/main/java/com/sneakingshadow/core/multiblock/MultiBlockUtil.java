package com.sneakingshadow.core.multiblock;

import net.minecraft.util.Vec3;

/**
 * Created by SneakingShadow on 27.11.2016.
 */
public class MultiBlockUtil {

    public static Vec3 rotate(Vec3 vector, int rotateX, int rotateY, int rotateZ) {
        vector.rotateAroundX((float) (((float)rotateX)/2 * Math.PI));
        vector.rotateAroundY((float) (((float)rotateY)/2 * Math.PI));
        vector.rotateAroundZ((float) (((float)rotateZ)/2 * Math.PI));

        vector.xCoord = Math.round(vector.xCoord);
        vector.yCoord = Math.round(vector.yCoord);
        vector.zCoord = Math.round(vector.zCoord);

        return vector;
    }

}
