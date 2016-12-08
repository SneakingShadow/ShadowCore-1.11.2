package com.sneakingshadow.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;

public class ItemShadow extends Item
{
    public ItemShadow(String unlocalizedName)
    {
        super();
        this.setUnlocalizedName(unlocalizedName);
    }

    public String getUnwrappedUnlocalizedName() {
        return this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getIconString()
    {
        return getUnwrappedUnlocalizedName();
    }

    //------------------------TOOL------------------------//

    private boolean isTool = false;

    private Item.ToolMaterial toolMaterial = null;
    private int enchantability = 0;

    /**
     * Return the enchantability factor of the item.
     * Based on material if material is set.
     */
    @Override
    public int getItemEnchantability()
    {
        return toolMaterial != null ? toolMaterial.getEnchantability() : 0;
    }

}

