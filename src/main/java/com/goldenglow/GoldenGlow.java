package com.goldenglow;

import com.goldenglow.common.CommonProxy;
import com.goldenglow.common.battles.bosses.BossManager;
import com.goldenglow.common.battles.raids.CommandRaidDebug;
import com.goldenglow.common.battles.raids.RaidEventHandler;
import com.goldenglow.common.command.*;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerStorage;
import com.goldenglow.common.gyms.GymManager;
import com.goldenglow.common.handlers.*;
import com.goldenglow.common.handlers.events.*;
import com.goldenglow.common.inventory.BetterTrading.TradeManager;
import com.goldenglow.common.inventory.CustomInventoryData;
import com.goldenglow.common.inventory.CustomInventoryHandler;
import com.goldenglow.common.inventory.shops.CustomShopHandler;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.seals.SealManager;
import com.goldenglow.common.teams.TeamManager;
import com.goldenglow.common.tiles.*;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.ShopPacketHandler;
import com.goldenglow.http.OOStatsServer;
import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.ShopKeeperPacket;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import org.spongepowered.api.Sponge;

@Mod(modid="obscureobsidian", name="Obscure Obsidian", dependencies = "required-after:pixelmon;required-after:customnpcs;required-after:worldedit;required-after:armourers_workshop;required-after:cfm", acceptableRemoteVersions = "*")
public class GoldenGlow {

    public String VERSION = "1.0.2";

    @Mod.Instance("goldenglow")
    public static GoldenGlow instance;

    @SidedProxy(serverSide = "com.goldenglow.common.CommonProxy")
    public static CommonProxy proxy;

    public GGEventHandler eventHandler = new GGEventHandler();
    public RaidEventHandler raidEventHandler = new RaidEventHandler();
    public TickEventHandler tickEventHandler = new TickEventHandler();

    public static GGLogger logger = new GGLogger();
    public static ConfigHandler configHandler = new ConfigHandler();
    public static RightClickBlacklistHandler rightClickBlacklistHandler=new RightClickBlacklistHandler();

    public static SongManager songManager = new SongManager();
    public static TeamManager teamManager = new TeamManager();
    public static GymManager gymManager = new GymManager();
    public static RouteManager routeManager = new RouteManager();
    public static PixelmonSpawnerHandler pixelmonSpawnerHandler = new PixelmonSpawnerHandler();
    public static TradeManager tradeManager = new TradeManager();
    public static CustomInventoryHandler customInventoryHandler = new CustomInventoryHandler();
    public static CustomShopHandler customShopHandler = new CustomShopHandler();

    public static CommandDispatcher<ICommandSender> commandDispatcher = new CommandDispatcher<>();

    public static Thread statsServer;

    public GoldenGlow() {
        statsServer = new Thread(new OOStatsServer());
        statsServer.start();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GGLogger.info("Obscure Obsidian v"+VERSION);
        GGLogger.info("Pre-Initialization...");
        configHandler.init();
        rightClickBlacklistHandler.init();

        GameRegistry.registerTileEntity(TileEntityCustomApricornTree.class, new ResourceLocation("obscureobsidian", "custom_apricorn_tree"));
        GameRegistry.registerTileEntity(TileEntityCustomBerryTree.class, new ResourceLocation("obscureobsidian", "custom_berry_tree"));
        GameRegistry.registerTileEntity(TileEntityCustomAW.class, new ResourceLocation("obscureobsidian", "custom_aw"));
        GameRegistry.registerTileEntity(TileEntityCustomScripted.class, new ResourceLocation("obscureobsidian", "custom_scripted"));
        GameRegistry.registerTileEntity(TileEntityCustomFridge.class, new ResourceLocation("obscureobsidian", "custom_fridge"));

        CapabilityManager.INSTANCE.register(IPlayerData.class, new OOPlayerStorage(), OOPlayerData::new);

        SealManager.init();
        BossManager.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GGLogger.info("Initialising..");
        //Minecraft
        MinecraftForge.EVENT_BUS.register(eventHandler);
        MinecraftForge.EVENT_BUS.register(TickEventHandler.class);
        MinecraftForge.EVENT_BUS.register(PlayerEventHandler.class);
        //Pixelmon
        Pixelmon.EVENT_BUS.register(PixelmonEventHandler.class);
        Pixelmon.EVENT_BUS.register(raidEventHandler);
        Pixelmon.EVENT_BUS.register(HuntHandler.class);
        Pixelmon.EVENT_BUS.register(BossManager.class);
        Pixelmon.EVENT_BUS.register(BattleEventHandler.class);
        //CNPCs
        WrapperNpcAPI.EVENT_BUS.register(TickEventHandler.class);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        GGLogger.info("Post-Initialization...");
        Pixelmon.network.registerMessage(ShopPacketHandler.class, ShopKeeperPacket.class, 118, Side.SERVER);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        teamManager.init();
        pixelmonSpawnerHandler.init();
        songManager.init();
        event.registerServerCommand(new CommandInstanceInv());
        event.registerServerCommand(new CommandPhone());
        event.registerServerCommand(new CommandRouteNotificationOption());
        event.registerServerCommand(new CommandSetPvpMusicOption());
        event.registerServerCommand(new CommandSetTheme());
        event.registerServerCommand(new CommandMoneyreward());
        event.registerServerCommand(new CommandBattleReward());
        event.registerServerCommand(new CommandCustomChest());
        event.registerServerCommand(new CommandShop());
        event.registerServerCommand(new CommandTradeTest());
        event.registerServerCommand(new CommandShare());

        event.registerServerCommand(new CommandRaidDebug());
        event.registerServerCommand(new CommandDebug());
        event.registerServerCommand(new CommandScriptable());

        event.registerServerCommand(new CommandRoutes());
        event.registerServerCommand(new CommandBoss());
        CommandRoutes.register(commandDispatcher);
    }

    @Mod.EventHandler
    public void serverLoaded(FMLServerStartedEvent event){
        routeManager.init();
        gymManager.init();
        customInventoryHandler.init();
        String inventories="Inventories: ";
        for(CustomInventoryData data: customInventoryHandler.inventories){
            inventories+=data.getName()+" ";
        }
        GGLogger.info(inventories);
        customShopHandler.init();
        if(Loader.isModLoaded("spongeforge"))
            Sponge.getEventManager().registerListeners(this, eventHandler);
        String routes="Routes: ";
        for(Route route:routeManager.getRoutes()) {
            routes+=route.unlocalizedName+" ";
        }
        GGLogger.info(routes);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        routeManager.saveRoutes();
    }
}