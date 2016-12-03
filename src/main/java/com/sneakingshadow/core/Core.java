package com.sneakingshadow.core;

import com.sneakingshadow.core.multiblock.MultiBlockInit;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import static com.sneakingshadow.core.Core.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION)
public class Core
{
    public static final String MOD_ID = "sneakingshadow_core";
    public static final String MOD_NAME = "Shadow's Core";
    public static final String VERSION = "1.7.10-1.0";

    @Mod.Instance(MOD_ID)
    public static Core instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MultiBlockInit.init();
    }

}