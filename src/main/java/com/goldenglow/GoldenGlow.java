package com.goldenglow;

import com.goldenglow.common.CommonProxy;
import com.goldenglow.common.battles.raids.CommandRaidDebug;
import com.goldenglow.common.battles.raids.RaidHandler;
import com.goldenglow.common.command.*;
import com.goldenglow.common.handlers.ConfigHandler;
import com.goldenglow.common.handlers.GGEventHandler;
import com.goldenglow.common.handlers.PixelmonSpawnerHandler;
import com.goldenglow.common.handlers.TickHandler;
import com.goldenglow.common.command.CommandInstanceInv;
import com.goldenglow.common.command.CommandPhone;
import com.goldenglow.common.command.CommandRoutes;
import com.goldenglow.common.command.CommandRouteNotificationOption;
import com.goldenglow.common.handlers.*;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.teams.TeamManager;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.PhoneItemListHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.spongepowered.api.Sponge;

@Mod(modid="obscureobsidian", name="Obscure Obsidian", dependencies = "required-after:pixelmon;required-after:customnpcs;required-after:worldedit", acceptableRemoteVersions = "*")
public class GoldenGlow {

    public String VERSION = "1.0.0";

    @Mod.Instance("goldenglow")
    public static GoldenGlow instance;

    @SidedProxy(serverSide = "com.goldenglow.common.CommonProxy")
    public static CommonProxy proxy;

    public GGEventHandler eventHandler = new GGEventHandler();
    public TickHandler tickHandler=new TickHandler();

    public static GGLogger logger = new GGLogger();
    public static SongManager songManager = new SongManager();
    public static ConfigHandler configHandler = new ConfigHandler();
    public static PhoneItemListHandler phoneItemListHandler=new PhoneItemListHandler();

    public static TeamManager teamManager = new TeamManager();
    public static RouteManager routeManager = new RouteManager();
    public static PixelmonSpawnerHandler pixelmonSpawnerHandler = new PixelmonSpawnerHandler();
    public static RaidHandler raidHandler = new RaidHandler();
    public static DataHandler dataHandler = new DataHandler();

    public static CommandDispatcher<ICommandSender> commandDispatcher = new CommandDispatcher<>();

    public GoldenGlow() {
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
        MinecraftForge.EVENT_BUS.register(TickHandler.class);
        Pixelmon.EVENT_BUS.register(eventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        teamManager.init();
        songManager.init();
        pixelmonSpawnerHandler.init();
        event.registerServerCommand(new CommandInstanceInv());
        event.registerServerCommand(new CommandPhone());
        event.registerServerCommand(new CommandRouteNotificationOption());
        event.registerServerCommand(new CommandSetPvpMusicOption());
        event.registerServerCommand(new CommandSetTheme());
        event.registerServerCommand(new CommandRaidDebug());
        event.registerServerCommand(new CommandDebug());

        event.registerServerCommand(new CommandRoutes());
        CommandRoutes.register(commandDispatcher);
    }

    @Mod.EventHandler
    public void serverLoaded(FMLServerStartedEvent event){
        routeManager.init();
        phoneItemListHandler.init();
        if(Loader.isModLoaded("spongeforge"))
            Sponge.getEventManager().registerListeners(this, new GGEventHandler());
        GGLogger.info("Routes:");
        for(Route route:routeManager.getRoutes()){
            GGLogger.info(route.unlocalizedName);
        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        routeManager.saveRoutes();
    }
}