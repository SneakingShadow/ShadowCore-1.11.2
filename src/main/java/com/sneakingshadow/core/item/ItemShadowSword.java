package com.sneakingshadow.core.item;

import net.minecraft.item.Item;

public class ItemShadowSword extends ItemShadowTool
{

    public ItemShadowSword(String unlocalizedName, Item.ToolMaterial toolMaterial) {
        super(unlocalizedName, toolMaterial, 4.0F);
        this.setSword();
    }

}
