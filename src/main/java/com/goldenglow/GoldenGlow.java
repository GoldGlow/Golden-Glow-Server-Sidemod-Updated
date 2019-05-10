package com.goldenglow;

import com.goldenglow.common.CommonProxy;
import com.goldenglow.common.command.CommandInstanceInv;
import com.goldenglow.common.handlers.ConfigHandler;
import com.goldenglow.common.handlers.GGEventHandler;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.teams.TeamManager;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.io.FileNotFoundException;

@Mod(modid="obscureobsidian", name="Obscure Obsidian", dependencies = "required-after:pixelmon;required-after:customnpcs;", acceptableRemoteVersions = "*")
public class GoldenGlow {

    public String VERSION = "1.0.0";

    @Mod.Instance("goldenglow")
    public static GoldenGlow instance;

    @SidedProxy(serverSide = "com.goldenglow.common.CommonProxy")
    public static CommonProxy proxy;

    public GGEventHandler eventHandler = new GGEventHandler();

    public static SongManager songManager=new SongManager();
    public static GGLogger logger = new GGLogger();
    public static TeamManager teamManager = new TeamManager();
    public ConfigHandler configHandler = new ConfigHandler();

    public GoldenGlow() throws FileNotFoundException {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Initializing GoldenGlow sidemod v"+VERSION+"...");
        configHandler.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Init section test");
        MinecraftForge.EVENT_BUS.register(eventHandler);
        MinecraftForge.EVENT_BUS.register(proxy);
        Pixelmon.EVENT_BUS.register(eventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        teamManager.init();
        event.registerServerCommand(new CommandInstanceInv());
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
    }
}