package com.sneakingshadow.core.item;

import net.minecraft.item.Item;

public class ItemShadowAxe extends ItemShadowTool
{

    public ItemShadowAxe(String unlocalizedName, Item.ToolMaterial toolMaterial) {
        super(unlocalizedName, toolMaterial, 3.0F);
        this.setAxe(toolMaterial.getHarvestLevel());
    }

}
