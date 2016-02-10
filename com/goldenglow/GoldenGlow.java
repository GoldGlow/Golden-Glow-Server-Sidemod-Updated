package com.goldenglow;

import com.goldenglow.common.CommonProxy;
import com.goldenglow.common.commands.CommandGym;
import com.goldenglow.common.commands.CommandRoute;
import com.goldenglow.common.gyms.GymManager;
import com.goldenglow.common.handlers.FollowerHandler;
import com.goldenglow.common.handlers.GGEventHandler;
import com.goldenglow.common.handlers.GGTickHandler;
import com.goldenglow.common.handlers.TempHandler;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.teams.TeamManager;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonmod.pixelmon.Pixelmon;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid="goldenglow", name="GoldenGlow", dependencies="required-after:pixelmon;required-after:customnpcs;required-after:betterstorage", acceptableRemoteVersions="*")
public class GoldenGlow
{
    public String VERSION = "2.0.0";
    @Mod.Instance("goldenglow")
    public static GoldenGlow instance;
    @SidedProxy(serverSide="com.goldenglow.common.CommonProxy")
    public static CommonProxy proxy;
    public GGEventHandler eventHandler = new GGEventHandler();
    public GGTickHandler tickHandler = new GGTickHandler();
    public GGLogger logger = new GGLogger();
    public TeamManager teamManager = new TeamManager();
    public FollowerHandler followerHandler = new FollowerHandler();
    public RouteManager routeManager = new RouteManager();
    public GymManager gymManager = new GymManager();
    public TempHandler tempHandler = new TempHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GGLogger.info("Initializing GoldenGlow sidemod v" + this.VERSION + "...");
        this.routeManager.init();
        this.teamManager.init();
        this.gymManager.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this.eventHandler);
        MinecraftForge.EVENT_BUS.register(proxy);
        FMLCommonHandler.instance().bus().register(this.tickHandler);
        Pixelmon.EVENT_BUS.register(this.eventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandRoute());
        event.registerServerCommand(new CommandGym());
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
        this.routeManager.saveRoutes();
        this.gymManager.saveGyms();
    }
}
