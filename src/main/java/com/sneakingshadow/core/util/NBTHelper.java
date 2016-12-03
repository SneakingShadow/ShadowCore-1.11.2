package com.sneakingshadow.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {

    public static NBTTagList itemsToNBT(ItemStack[] itemStacks){
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < itemStacks.length; ++i)
            if (itemStacks[i] != null)
            {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        return nbtTagList;
    }

    public static ItemStack[] nbtToItems(NBTTagList nbtTagList){
        ItemStack[] itemStacks = new ItemStack[nbtTagList.getCompoundTagAt(nbtTagList.tagCount()-1).getByte("Slot")];
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
            itemStacks[nbtTagCompound.getByte("Slot")] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
        }
        return itemStacks;
    }

}
