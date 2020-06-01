package com.goldenglow.common.guis;

import com.goldenglow.common.guis.config.battlemusic.WildBattleMusic;
import com.goldenglow.common.guis.pokehelper.*;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.guis.pokehelper.bag.TeachTMMenu;
import com.goldenglow.common.guis.pokehelper.config.ConfigMenu;
import com.goldenglow.common.guis.pokehelper.config.OptionListMenu;
import com.goldenglow.common.guis.pokehelper.config.RouteTheme;
import com.goldenglow.common.guis.pokehelper.config.BattleMusic;
import com.goldenglow.common.guis.pokehelper.config.battlemusic.TrainerBattleMusic;
import com.goldenglow.common.guis.pokehelper.config.general.GeneralConfig;
import com.goldenglow.common.guis.pokehelper.config.music.MusicConfig;
import com.goldenglow.common.guis.pokehelper.config.social.ChatSettings;
import com.goldenglow.common.guis.pokehelper.config.social.DmSettings;
import com.goldenglow.common.guis.pokehelper.config.social.SocialConfig;
import com.goldenglow.common.guis.pokehelper.config.social.VisibilitySettings;
import com.goldenglow.common.guis.pokehelper.config.visual.VisualConfig;
import com.goldenglow.common.guis.pokehelper.helperSkins.Phone;
import com.goldenglow.common.guis.pokehelper.info.InfoMenu;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialsMenu;
import com.goldenglow.common.guis.pokehelper.map.AreaDexMenu;
import com.goldenglow.common.guis.pokehelper.map.MapMenu;
import com.goldenglow.common.guis.pokehelper.social.FriendRequestsMenu;
import com.goldenglow.common.guis.social.PlayerProfileMenu;
import com.pixelmonessentials.PixelmonEssentials;

public class GuiHandler {

    public static void init(){
        new Phone();
        PixelmonEssentials.essentialsGuisHandler.addGui(new InfoMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new MapMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new AreaDexMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new PhoneMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TutorialsMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new RouteTheme());
        PixelmonEssentials.essentialsGuisHandler.addGui(new BattleMusic());
        PixelmonEssentials.essentialsGuisHandler.addGui(new WildBattleMusic());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TrainerBattleMusic());
        PixelmonEssentials.essentialsGuisHandler.addGui(new BagMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TeachTMMenu(""));
        PixelmonEssentials.essentialsGuisHandler.addGui(new PlayerProfileMenu(null));
        PixelmonEssentials.essentialsGuisHandler.addGui(new FriendRequestsMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new ConfigMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new OptionListMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new GeneralConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new VisualConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new SocialConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new MusicConfig());
        PixelmonEssentials.essentialsGuisHandler.addGui(new ChatSettings());
        PixelmonEssentials.essentialsGuisHandler.addGui(new DmSettings());
        PixelmonEssentials.essentialsGuisHandler.addGui(new VisibilitySettings());
    }
}
