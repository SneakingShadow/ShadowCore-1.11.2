package com.sneakingshadow.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemArmor;

public class ItemShadowArmor extends ItemArmor
{
    private String texture;
    private int armor;

    public ItemShadowArmor(String unlocalizedName, String textureName, ArmorMaterial material, int armorType)
    {
        this(unlocalizedName, textureName, material, armorType, 0);
    }

    public ItemShadowArmor(String unlocalizedName, String textureName, ArmorMaterial material, int armorType, int renderIndex)
    {
        super(material, renderIndex, armorType);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabAllSearch);
        texture = textureName;
        armor = armorType;
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

    /*@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return Reference.RESOURCE_PREFIX+"textures/armor/" + this.texture + "_" + (this.armor == 2 ? "2" : "1") + ".png";
    }
    */
}
