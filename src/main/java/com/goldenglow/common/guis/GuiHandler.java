package com.goldenglow.common.guis;

import com.goldenglow.common.guis.essentials.KeyItemGui;
import com.goldenglow.common.guis.essentials.OOTrainerGui;
import com.goldenglow.common.guis.pokehelper.*;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.guis.pokehelper.bag.TeachTMMenu;
import com.goldenglow.common.guis.pokehelper.config.ConfigMenu;
import com.goldenglow.common.guis.pokehelper.config.OptionListMenu;
import com.goldenglow.common.guis.pokehelper.config.general.GeneralConfig;
import com.goldenglow.common.guis.pokehelper.config.music.MusicConfig;
import com.goldenglow.common.guis.pokehelper.config.social.ChatSettings;
import com.goldenglow.common.guis.pokehelper.config.social.DmSettings;
import com.goldenglow.common.guis.pokehelper.config.social.SocialConfig;
import com.goldenglow.common.guis.pokehelper.config.social.VisibilitySettings;
import com.goldenglow.common.guis.pokehelper.config.visual.VisualConfig;
import com.goldenglow.common.guis.pokehelper.info.InfoMenu;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialsMenu;
import com.goldenglow.common.guis.pokehelper.map.AreaDexMenu;
import com.goldenglow.common.guis.pokehelper.map.MapMenu;
import com.goldenglow.common.guis.pokehelper.social.FriendRequestsMenu;
import com.goldenglow.common.guis.social.PlayerProfileMenu;
import com.goldenglow.common.guis.trading.TradeOfferGui;
import com.goldenglow.common.keyItems.KeyItem;
import com.goldenglow.common.util.ReflectionHelper;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.guis.TrainerDataGui;

import java.util.ArrayList;

public class GuiHandler {

    public static void init(){
        //6000-6010
        PixelmonEssentials.essentialsGuisHandler.addGui(new PhoneMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new InfoMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TutorialsMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new KeyItemGui());
        PixelmonEssentials.essentialsGuisHandler.addGui(new PlayerProfileMenu(null));
        PixelmonEssentials.essentialsGuisHandler.addGui(new FriendRequestsMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new MapMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new AreaDexMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TradeOfferGui());

        //6100-6108
        PixelmonEssentials.essentialsGuisHandler.addGui(new ConfigMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new OptionListMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new GeneralConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new VisualConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new SocialConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new MusicConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new ChatSettings());
        PixelmonEssentials.essentialsGuisHandler.addGui(new DmSettings());
        PixelmonEssentials.essentialsGuisHandler.addGui(new VisibilitySettings());

        //6200-6201
        PixelmonEssentials.essentialsGuisHandler.addGui(new BagMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TeachTMMenu(""));

        ArrayList<EssentialsGuis> guis=ReflectionHelper.getPrivateValue(PixelmonEssentials.essentialsGuisHandler, "basicGuis");
        guis.remove(PixelmonEssentials.essentialsGuisHandler.getGui(1001));
        ReflectionHelper.setPrivateValue(PixelmonEssentials.essentialsGuisHandler, "basicGuis", guis);
        PixelmonEssentials.essentialsGuisHandler.addGui(new OOTrainerGui());
    }
}
