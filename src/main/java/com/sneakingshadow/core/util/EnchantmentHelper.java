package com.sneakingshadow.core.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentHelper {

    //TODO Make sure this class works properly.

    public static int getLevel(int id, NBTTagList tagList) {
        if(tagList != null){
            for (int i = 0; i < tagList.tagCount(); i++) {
                if (tagList.getCompoundTagAt(i).getShort("ID") == id) {
                    return tagList.getCompoundTagAt(i).getShort("lvl");
                }
            }
        }
        return 0;
    }
    public static int getLevel(int id, ItemStack itemStack){ return getLevel(id, itemStack.getEnchantmentTagList()); }
    public static int getLevel(Enchantment id, NBTTagList tagList){ return getLevel(id.effectId, tagList); }
    public static int getLevel(Enchantment id, ItemStack itemStack){ return getLevel(id, itemStack.getEnchantmentTagList()); }

    public static void setLevel(Enchantment enchantment, int level, ItemStack itemStack){
        NBTTagList tagList = itemStack.getEnchantmentTagList();

        if(tagList != null){
            boolean booly = true;
            for (int i = 0; i < tagList.tagCount(); i++) {
                if (tagList.getCompoundTagAt(i).getShort("ID") == enchantment.effectId) {
                    tagList.getCompoundTagAt(i).setShort("lvl", (short) level);

                    booly = false;
                }
            }
            if(booly){
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setShort("ID", (short)enchantment.effectId);
                tagCompound.setShort("lvl", (short)level);
                tagList.appendTag( tagCompound );
            }
        }else{
            itemStack.addEnchantment(enchantment, level);
        }
    }

    public static void remove(int id, NBTTagList tagList, NBTTagCompound stackTagCompound){

        if(tagList != null) {
            for (int i = 0; i < tagList.tagCount(); i++) {
                if(tagList.getCompoundTagAt(i).getShort("ID") == id) {
                    tagList.removeTag(i);
                }
            }
        }
        checkEnchanted(tagList, stackTagCompound);
    }
    public static void remove(Enchantment enchantment, ItemStack itemStack){ remove(enchantment.effectId, itemStack.getEnchantmentTagList(), itemStack.stackTagCompound); }
    public static void remove(int id, ItemStack itemStack){ remove(id, itemStack.getEnchantmentTagList(), itemStack.stackTagCompound); }
    public static void remove(Enchantment enchantment, NBTTagList tagList, NBTTagCompound stackTagCompound){ remove(enchantment.effectId, tagList, stackTagCompound); }

    private static void checkEnchanted(NBTTagList tagList, NBTTagCompound stackTagCompound){
        //stackTagCompound.setTag("ench", null)
        if(tagList.tagCount() == 0) stackTagCompound.removeTag("ench");
    }

    public static void removeAll(ItemStack itemStack){ itemStack.stackTagCompound.removeTag("ench"); }
    public static void removeAll(NBTTagCompound stackTagCompound){ stackTagCompound.removeTag("ench"); }

    public static boolean hasEnchant(int id, NBTTagList tagList){
        if(tagList != null) {
            for (int i = 0; i < tagList.tagCount(); i++) {
                if(tagList.getCompoundTagAt(i).getShort("ID") == id) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean hasEnchant(Enchantment enchantment, ItemStack itemStack) { return hasEnchant(enchantment.effectId, itemStack.getEnchantmentTagList()); }
    public static boolean hasEnchant(int id, ItemStack itemStack){ return hasEnchant(id, itemStack.getEnchantmentTagList()); }
    public static boolean hasEnchant(Enchantment enchantment, NBTTagList tagList){ return hasEnchant(enchantment.effectId, tagList); }

    public static boolean hasEnchantOtherThan(int id, NBTTagList tagList){
        if(tagList != null) {
            for (int i = 0; i < tagList.tagCount(); i++) {
                if(tagList.getCompoundTagAt(i).getShort("ID") != id) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean hasEnchantOtherThan(Enchantment enchantment, ItemStack itemStack){ return hasEnchantOtherThan(enchantment.effectId, itemStack.getEnchantmentTagList()); }
    public static boolean hasEnchantOtherThan(int id, ItemStack itemStack){ return hasEnchantOtherThan(id, itemStack.getEnchantmentTagList()); }
    public static boolean hasEnchantOtherThan(Enchantment enchantment, NBTTagList tagList){ return hasEnchantOtherThan(enchantment.effectId, tagList); }

    public static List<Short> getEnchants(NBTTagList tagList){
        List<Short> list = new ArrayList<Short>();
        for(int i = tagList.tagCount()-1; i >= 0; i--){
            list.add(tagList.getCompoundTagAt(i).getShort("ID"));
        }
        return list;
    }
    public static List<Short> getEnchants(ItemStack itemStack) { return getEnchants(itemStack.getEnchantmentTagList()); }

    public static Short getEnchant(NBTTagList tagList){ return tagList.getCompoundTagAt(0).getShort("ID"); }
    public static Short getEnchant(ItemStack itemStack) { return getEnchant( itemStack.getEnchantmentTagList() ); }

}
