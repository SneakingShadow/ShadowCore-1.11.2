package com.sneakingshadow.core.debug;

import com.sneakingshadow.core.Reference;
import com.sneakingshadow.core.multiblock.MultiBlock;
import com.sneakingshadow.core.util.ArrayListHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * Created by SneakingShadow on 08.12.2016.
 */
public class BlockDebug extends Block {

    public BlockDebug() {
        super(Material.ground);
        this.setBlockName(Reference.MOD_ID+":debug");
        this.setCreativeTab(CreativeTabs.tabAllSearch);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote) {
            MultiBlock multiBlock = new MultiBlock(
                    "wcw/c c/wcw",
                    'c', "@cobblestone@", '&', Blocks.cobblestone, '|', Blocks.sand,
                    'w', '@', "logWood"
            );
            System.out.println(multiBlock);
            System.out.println();
            System.out.println(
                    ArrayListHelper.arrayToString(
                            multiBlock.findStructures(world, x, y, z)
                    )
            );
        }

        return true;
    }

}
