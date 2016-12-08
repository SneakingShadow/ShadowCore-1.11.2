package com.sneakingshadow.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemShadowHoe extends ItemHoe
{
    public ItemShadowHoe(String unlocalizedName, ToolMaterial toolMaterial) {
        super(toolMaterial);
        this.setUnlocalizedName(unlocalizedName);
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabAllSearch);
    }

    public String getCleanUnlocalizedName() {
        return this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
    }
}
