package com.sneakingshadow.core.item;

import net.minecraft.item.Item;

public class ItemShadowPickaxe extends ItemShadowTool
{

    public ItemShadowPickaxe(String unlocalizedName, Item.ToolMaterial toolMaterial) {
        super(unlocalizedName, toolMaterial, 2.0F);
        this.setPickaxe(toolMaterial.getHarvestLevel());
    }

}