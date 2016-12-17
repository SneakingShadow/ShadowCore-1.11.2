package com.sneakingshadow.core.debug;

import com.sneakingshadow.core.Reference;
import com.sneakingshadow.core.multiblock.MultiBlock;
import com.sneakingshadow.core.multiblock.Structure;
import com.sneakingshadow.core.util.ArrayListHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by SneakingShadow on 08.12.2016.
 */
public class BlockDebug extends Block {

    public BlockDebug() {
        super(Material.ground);
        this.setBlockName(Reference.MOD_ID + ":debug");
        this.setCreativeTab(CreativeTabs.tabAllSearch);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            MultiBlock multiBlock = new MultiBlock(
                    "c \\c \\  ", 'c', Blocks.glass
            ).setRotationYAxis(true).setRotationXAxis(false).setRotationZAxis(false);
            ArrayList<Structure> arrayList = multiBlock.findStructures(world, x, y, z, false);
            System.out.println(
                    "findStructures:\n" + ArrayListHelper.arrayToString(arrayList) + "\nSize: " + arrayList.size()
            );
        }

        return true;
    }

}
