package com.wetDude.warp;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod(modid = WarpMod.MODID, version = WarpMod.VERSION/*, serverSideOnly = true*/)
public class WarpMod
{
    public static final String MODID = "warp";
    public static final String VERSION = "0.1";

    private CmdWarp m_cmdWarp;
    private WarpManager m_manager;
    private File m_file;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) throws Exception
    {
        m_file = new File(DimensionManager.getCurrentSaveRootDirectory(), "warps.json");

        m_manager = new WarpManager(m_file);

        m_cmdWarp = new CmdWarp(m_manager);
        event.registerServerCommand(m_cmdWarp);
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) throws Exception
    {
        m_manager.saveData(m_file);
    }
}
