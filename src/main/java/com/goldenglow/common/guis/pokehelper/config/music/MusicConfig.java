package com.goldenglow.common.guis.pokehelper.config.music;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionPvpBattle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionPvpSelection;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionTrainerBattle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionWildBattle;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class MusicConfig extends EssentialsGuiBase {

    public MusicConfig(){
        super(6105);
        ActionData backButtonAction=new ActionIdData("OPEN_GUI", 6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData wildButtonAction=new ActionStringData("OPEN_OPTION", "WILD_THEME");
        this.addButton(new EssentialsButton(501, wildButtonAction));
        ActionData trainerButtonAction=new ActionStringData("OPEN_OPTION", "TRAINER_THEME");
        this.addButton(new EssentialsButton(502, trainerButtonAction));
        ActionData pvpThemeButtonAction=new ActionStringData("OPEN_OPTION", "PVP_THEME");
        this.addButton(new EssentialsButton(503, pvpThemeButtonAction));
        ActionData pvpSelectionButtonAction=new ActionStringData("OPEN_OPTION", "PVP_SELECTION");
        this.addButton(new EssentialsButton(504, pvpSelectionButtonAction));
    }

    @Override
    public void init(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        CustomGuiWrapper gui= new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "Music Settings", 85, 4, 128, 20);
        gui.addLabel(101, "Wild Battle Theme", 32, 30, 128, 20);
        gui.addTexturedButton(501, ((OptionWildBattle)GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.WILD_THEME)).getOptionFromValue(data.getWildTheme()).getName(), 122, 30, 112, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(102, "Trainer Battle Theme", 12, 55, 128, 20);
        gui.addTexturedButton(502, ((OptionTrainerBattle)GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.TRAINER_THEME)).getOptionFromValue(data.getTrainerTheme()).getName(), 122, 55, 112, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(103, "PVP Battle Theme", 32, 80, 128, 20);
        gui.addTexturedButton(503, ((OptionPvpBattle)GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.PVP_THEME)).getOptionFromValue(data.getPVPTheme()).getName(), 122, 80, 112, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(104, "PVP Theme Selection", 16, 105, 128, 20);
        gui.addTexturedButton(504, ((OptionPvpSelection)GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.PVP_SELECTION)).getOptionFromValue(data.getPvpThemeOption()).getName(), 122, 105, 112, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addButton(500, "Back", 30, 216, 64, 20);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
