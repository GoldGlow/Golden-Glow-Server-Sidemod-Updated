package com.goldenglow.common.guis;

import com.goldenglow.common.guis.config.BattleMusic;
import com.goldenglow.common.guis.config.RouteTheme;
import com.goldenglow.common.guis.config.ScoreboardsMenu;
import com.goldenglow.common.guis.config.battlemusic.TrainerBattleMusic;
import com.goldenglow.common.guis.config.battlemusic.WildBattleMusic;
import com.goldenglow.common.guis.helperSkins.Phone;
import com.pixelmonessentials.PixelmonEssentials;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public class GuiHandler {

    public static void init(){
        new Phone();
        PixelmonEssentials.essentialsGuisHandler.addGui(new ConfigMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new InfoMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new MapMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new PhoneMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TutorialsMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new RouteTheme());
        PixelmonEssentials.essentialsGuisHandler.addGui(new BattleMusic());
        PixelmonEssentials.essentialsGuisHandler.addGui(new WildBattleMusic());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TrainerBattleMusic());
        PixelmonEssentials.essentialsGuisHandler.addGui(new ScoreboardsMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new BagMenu());
        PixelmonEssentials.essentialsGuisHandler.addGui(new TeachTMMenu(""));
        PixelmonEssentials.essentialsGuisHandler.addGui(new PlayerProfileMenu(null));
        PixelmonEssentials.essentialsGuisHandler.addGui(new FriendRequestsMenu());
    }
}
