package com.sneakingshadow.core.item;

import net.minecraft.item.Item;

public class ItemShadowSpade extends ItemShadowTool
{

    public ItemShadowSpade(String unlocalizedName, Item.ToolMaterial toolMaterial) {
        super(unlocalizedName, toolMaterial, 1.0F);
        this.setShovel(toolMaterial.getHarvestLevel());
    }

}