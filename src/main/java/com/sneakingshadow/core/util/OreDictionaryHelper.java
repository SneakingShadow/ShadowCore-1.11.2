package com.sneakingshadow.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

public class OreDictionaryHelper {

    public static ItemStack getMatchingItem(String oreDictValue, ItemStack itemStack) {
        if (itemStack == null)
            return null;

        ArrayList<ItemStack> ores = OreDictionary.getOres(oreDictValue);
        for (ItemStack stack : ores)
            if ( stack.getItem() == itemStack.getItem() && ( stack.getItemDamage() == WILDCARD_VALUE || stack.getItemDamage() == itemStack.getItemDamage() ) )
                return stack.copy();

        return null;
    }

    public static boolean isValidItem(String oreDictValue, ItemStack itemStack) {
        return getMatchingItem(oreDictValue, itemStack) != null;
    }

    public static boolean isValidItem(ArrayList<String> oreDictEntries, ItemStack itemStack) {
        for (String oreDictValue : oreDictEntries)
            if (isValidItem(oreDictValue, itemStack))
                return true;

        return false;
    }

    //-------------------

    public static boolean isValidItem(String oreDictValue, Block block) {
        return isValidItem(oreDictValue, block, 0);
    }

    public static boolean isValidItem(String oreDictValue, Block block, int metadata) {
        return isValidItem(oreDictValue, new ItemStack(block, 1, metadata) );
    }

    public static boolean isValidItem(String oreDictValue, World world, int x, int y, int z) {
        return isValidItem(oreDictValue, world.getBlock(x,y,z), world.getBlockMetadata(x,y,z) );
    }

    public static boolean isValidItem(String oreDictValue, Item item) {
        return isValidItem(oreDictValue, item, 0);
    }

    public static boolean isValidItem(String oreDictValue, Item item, int damage) {
        return isValidItem(oreDictValue, new ItemStack(item, 1, damage) );
    }

    //-------------------

    public static boolean isValidItem(ArrayList<String> oreDictEntries, Block block) {
        return isValidItem(oreDictEntries, block, 0);
    }

    public static boolean isValidItem(ArrayList<String> oreDictEntries, Block block, int metadata) {
        return isValidItem(oreDictEntries, new ItemStack(block, 1, metadata) );
    }

    public static boolean isValidItem(ArrayList<String> oreDictEntries, World world, int x, int y, int z) {
        return isValidItem(oreDictEntries, world.getBlock(x,y,z), world.getBlockMetadata(x,y,z) );
    }

    public static boolean isValidItem(ArrayList<String> oreDictEntries, Item item) {
        return isValidItem(oreDictEntries, item, 0);
    }

    public static boolean isValidItem(ArrayList<String> oreDictEntries, Item item, int damage) {
        return isValidItem(oreDictEntries, new ItemStack(item, 1, damage) );
    }

}
