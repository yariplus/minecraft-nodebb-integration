package com.radiofreederp.nodebbintegration;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = NodeBBIntegrationForge.MODID, version = NodeBBIntegrationForge.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class NodeBBIntegrationForge
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
