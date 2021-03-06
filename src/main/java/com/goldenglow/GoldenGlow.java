package com.goldenglow;

import com.goldenglow.common.CommonProxy;
import com.goldenglow.common.battles.BattleManager;
import com.goldenglow.common.battles.raid.CommandRaidDebug;
import com.goldenglow.common.battles.raid.RaidEventHandler;
import com.goldenglow.common.command.*;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerStorage;
import com.goldenglow.common.guis.GuiHandler;
import com.goldenglow.common.guis.pokehelper.bag.CategoryManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.helperSkins.Phone;
import com.goldenglow.common.guis.pokehelper.map.data.LocationList;
import com.goldenglow.common.guis.pokehelper.info.data.TutorialsManager;
import com.goldenglow.common.gyms.GymManager;
import com.goldenglow.common.handlers.*;
import com.goldenglow.common.handlers.events.*;
import com.goldenglow.common.trading.TradeManager;
import com.goldenglow.common.inventory.CustomInventoryData;
import com.goldenglow.common.inventory.CustomInventoryHandler;
import com.goldenglow.common.inventory.shops.CustomShopHandler;
import com.goldenglow.common.keyItems.CustomItemManager;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.seals.SealManager;
import com.goldenglow.common.teams.TeamManager;
import com.goldenglow.common.tiles.*;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.PermissionUtils;
import com.goldenglow.common.util.ShopPacketHandler;
import com.goldenglow.common.util.actions.ActionHandler;
import com.goldenglow.common.util.objectives.ObjectiveHandler;
import com.goldenglow.common.util.requirements.RequirementHandler;
import com.goldenglow.http.OOStatsServer;
import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.ShopKeeperPacket;
import net.minecraft.command.ICommandSender;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.wrapper.WrapperNpcAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(modid="obscureobsidian", name="Obscure Obsidian", dependencies = "required-after:pixelmon;required-after:customnpcs;required-after:worldedit;required-after:armourers_workshop;required-after:cfm;required-after:pixelmonessentials;", acceptableRemoteVersions = "*")
public class GoldenGlow{

    public String VERSION = "1.0.2";

    @Mod.Instance("goldenglow")
    public static GoldenGlow instance;

    @SidedProxy(serverSide = "com.goldenglow.common.CommonProxy")
    public static CommonProxy proxy;

    public BattleEventHandler battleEventHandler=new BattleEventHandler();
    public BlockEventHandler blockEventHandler=new BlockEventHandler();
    public ItemEventHandler itemEventHandler=new ItemEventHandler();
    public LoginLogoutEventHandler loginLogoutEventHandler=new LoginLogoutEventHandler();
    public OtherEventHandler otherEventHandler=new OtherEventHandler();
    public SoundRelatedEventHandler soundEventHandler=new SoundRelatedEventHandler();

    public RaidEventHandler raidEventHandler = new RaidEventHandler();
    public TickHandler tickHandler=new TickHandler();
    public static PermissionUtils permissionUtils=null;
    public static CustomItemManager customItemManager =new CustomItemManager();

    public static GGLogger logger = new GGLogger();
    public static ConfigHandler configHandler = new ConfigHandler();
    public static RightClickBlacklistHandler rightClickBlacklistHandler=new RightClickBlacklistHandler();
    public static OptionTypeManager optionTypeManager=new OptionTypeManager();

    public static SongManager songManager= new SongManager();
    public static TeamManager teamManager = new TeamManager();
    public static GymManager gymManager=new GymManager();
    public static RouteManager routeManager = new RouteManager();
    public static TradeManager tradeManager=new TradeManager();
    public static CustomInventoryHandler customInventoryHandler=new CustomInventoryHandler();
    public static CustomShopHandler customShopHandler=new CustomShopHandler();
    public static LocationList locationList=new LocationList();
    public static TutorialsManager tutorialsManager=new TutorialsManager();
    public static CategoryManager categoryManager=new CategoryManager();

    public static CommandDispatcher<ICommandSender> commandDispatcher = new CommandDispatcher<>();

    public static Thread statsServer;

    public GoldenGlow() {
        statsServer = new Thread(new OOStatsServer());
        statsServer.start();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Initializing GoldenGlow sidemod v"+VERSION+"...");
        ActionHandler.init();
        RequirementHandler.init();
        GuiHandler.init();
        ObjectiveHandler.init();
        configHandler.init();
        rightClickBlacklistHandler.init();
        locationList.init();
        tutorialsManager.init();
        customItemManager.init();
        categoryManager.init();
        BattleManager.init();

        GameRegistry.registerTileEntity(TileEntityCustomApricornTree.class, new ResourceLocation("obscureobsidian", "custom_apricorn_tree"));
        GameRegistry.registerTileEntity(TileEntityCustomBerryTree.class, new ResourceLocation("obscureobsidian", "custom_berry_tree"));
        GameRegistry.registerTileEntity(TileEntityCustomAW.class, new ResourceLocation("obscureobsidian", "custom_aw"));
        GameRegistry.registerTileEntity(TileEntityCustomScripted.class, new ResourceLocation("obscureobsidian", "custom_scripted"));
        GameRegistry.registerTileEntity(TileEntityCustomFridge.class, new ResourceLocation("obscureobsidian", "custom_fridge"));

        CapabilityManager.INSTANCE.register(IPlayerData.class, new OOPlayerStorage(), OOPlayerData::new);

        SealManager.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Init section test");
        MinecraftForge.EVENT_BUS.register(blockEventHandler);
        MinecraftForge.EVENT_BUS.register(otherEventHandler);
        MinecraftForge.EVENT_BUS.register(itemEventHandler);
        MinecraftForge.EVENT_BUS.register(loginLogoutEventHandler);
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(TickHandler.class);
        Pixelmon.EVENT_BUS.register(battleEventHandler);
        Pixelmon.EVENT_BUS.register(blockEventHandler);
        Pixelmon.EVENT_BUS.register(otherEventHandler);
        Pixelmon.EVENT_BUS.register(raidEventHandler);
        Pixelmon.EVENT_BUS.register(soundEventHandler);
        Pixelmon.EVENT_BUS.register(HuntHandler.class);
        Pixelmon.EVENT_BUS.register(BattleManager.class);
        WrapperNpcAPI.EVENT_BUS.register(TickHandler.class);
        WrapperNpcAPI.EVENT_BUS.register(otherEventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CustomNpcs.Channel.register(battleEventHandler);
        CustomNpcs.Channel.register(blockEventHandler);
        CustomNpcs.Channel.register(otherEventHandler);
        Pixelmon.network.registerMessage(ShopPacketHandler.class, ShopKeeperPacket.class, 118, Side.SERVER);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        teamManager.init();
        songManager.init();
        Phone.init();

        event.registerServerCommand(new CommandPhone());
        event.registerServerCommand(new CommandRouteNotificationOption());
        event.registerServerCommand(new CommandSetPvpMusicOption());
        event.registerServerCommand(new CommandSetTheme());
        event.registerServerCommand(new CommandMoneyreward());
        event.registerServerCommand(new CommandBattleReward());
        event.registerServerCommand(new CommandCustomChest());
        event.registerServerCommand(new CommandShop());
        event.registerServerCommand(new CommandTradeTest());
        event.registerServerCommand(new CommandKeyItem());
        event.registerServerCommand(new CommandRaidDebug());
        event.registerServerCommand(new CommandDebug());
        event.registerServerCommand(new CommandScriptable());
        event.registerServerCommand(new CommandBoss());
        event.registerServerCommand(new CommandRoutes());
        CommandRoutes.register(commandDispatcher);

        try {
            Field f = Profiler.class.getField("ENABLED");
            f.setAccessible(true);
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            f.set(event.getServer().profiler, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        String routes="Routes: ";
        for(Route route:routeManager.getRoutes()) {
            routes+=route.unlocalizedName+" ";
        }
        GGLogger.info(routes);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        routeManager.saveRoutes();
        customItemManager.saveKeyItems();
    }
}