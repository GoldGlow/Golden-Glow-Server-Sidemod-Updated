package com.goldenglow;

import com.goldenglow.common.CommonProxy;
import com.goldenglow.common.commands.CommandGym;
import com.goldenglow.common.commands.CommandRoute;
import com.goldenglow.common.factory.FactoryManager;
import com.goldenglow.common.gyms.GymManager;
import com.goldenglow.common.handlers.*;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.teams.TeamManager;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.wrapper.WrapperNpcAPI;

import java.io.FileNotFoundException;

@Mod(modid="goldenglow", name="GoldenGlow", dependencies = "required-after:pixelmon;required-after:customnpcs;required-after:betterstorage", acceptableRemoteVersions = "*")
public class GoldenGlow {

    public String VERSION = "2.0.0";

    @Mod.Instance("goldenglow")
    public static GoldenGlow instance;

    @SidedProxy(serverSide = "com.goldenglow.common.CommonProxy")
    public static CommonProxy proxy;

    public GGEventHandler eventHandler = new GGEventHandler();
    public GGTickHandler tickHandler = new GGTickHandler();

    public GGLogger logger = new GGLogger();
    public TeamManager teamManager = new TeamManager();
    public FollowerHandler followerHandler = new FollowerHandler();
    public RouteManager routeManager = new RouteManager();
    public GymManager gymManager = new GymManager();
    public TempHandler tempHandler = new TempHandler();
    public ConfigHandler configHandler = new ConfigHandler();
    public ShopKeeperHandler shopKeeperHandler = new ShopKeeperHandler();
    public FactoryManager factoryManager = new FactoryManager();

    public GoldenGlow() throws FileNotFoundException {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Initializing GoldenGlow sidemod v"+VERSION+"...");
        routeManager.init();
        gymManager.init();
        configHandler.init();
        shopKeeperHandler.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(eventHandler);
        MinecraftForge.EVENT_BUS.register(proxy);
        FMLCommonHandler.instance().bus().register(tickHandler);
        Pixelmon.EVENT_BUS.register(eventHandler);
        WrapperNpcAPI.EVENT_BUS.register(eventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRoute());
        event.registerServerCommand(new CommandGym());
        teamManager.init();
        factoryManager.init();
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        routeManager.saveRoutes();
        gymManager.saveGyms();
        factoryManager.saveArrays();
    }
}