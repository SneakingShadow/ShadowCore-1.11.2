package com.sneakingshadow.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class DirectionUtil {

    /**
     * Gets the direction the entity is facing.
     *
     * South = 0
     * West  = 1
     * North = 2
     * East  = 3
     *
     * */
    public static int direction(Entity entity) {
        return ( ( (int)entity.rotationYaw%360 + 405 )/90 )&3;
    }

    /**
     * Gets the direction the entity is looking.
     *
     * South = 0
     * West  = 1
     * North = 2
     * East  = 3
     *
     * */
    public static int lookDirection(EntityLivingBase entity) {
        return ( ( (int)entity.rotationYawHead%360 + 405 )/90 )&3;
    }

}
