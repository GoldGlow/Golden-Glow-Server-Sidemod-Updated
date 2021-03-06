package com.goldenglow.common.guis.pokehelper.map;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class MapMenu extends EssentialsGuiBase {
    private Route route;

    public MapMenu() {
        super(6008);
        ActionData waterlilButtonAction = new ActionStringData("SET_LOCATION", "waterlil");
        EssentialsButton waterlilButton = new EssentialsButton(501, waterlilButtonAction);
        this.addButton(waterlilButton);
        ActionData valleyhillButtonAction = new ActionStringData("SET_LOCATION", "valleyhill");
        EssentialsButton valleyhillButton = new EssentialsButton(505, valleyhillButtonAction);
        this.addButton(valleyhillButton);
        ActionData acanthusButtonAction = new ActionStringData("SET_LOCATION", "acanthus");
        EssentialsButton acanthusButton = new EssentialsButton(502, acanthusButtonAction);
        this.addButton(acanthusButton);
        ActionData chateauButtonAction = new ActionStringData("SET_LOCATION", "chateau");
        EssentialsButton chateauButton = new EssentialsButton(503, chateauButtonAction);
        this.addButton(chateauButton);
        ActionData sakuraButtonAction = new ActionStringData("SET_LOCATION", "sakura");
        EssentialsButton sakuraButton = new EssentialsButton(504, sakuraButtonAction);
        this.addButton(sakuraButton);

        ActionData foundPokemonAction = new ActionStringData("OPEN_SPAWNS", "");
        EssentialsButton foundPokemonButton = new EssentialsButton(512, foundPokemonAction);
        this.addButton(foundPokemonButton);
    }

    public Route getRoute(){
        return this.route;
    }

    public void setRoute(String route){
        if(route.equalsIgnoreCase("waterlil")){
            this.route= GoldenGlow.routeManager.getRoute("Waterlil_Lake");
        }
        else if(route.equalsIgnoreCase("valleyhill")){
            this.route=GoldenGlow.routeManager.getRoute("Valleyhill_Trail");
        }
        else if(route.equalsIgnoreCase("acanthus")){
            this.route=GoldenGlow.routeManager.getRoute("Acanthus_Town");
        }
        else if(route.equalsIgnoreCase("chateau")){
            this.route=GoldenGlow.routeManager.getRoute("Haunted_House");
        }
        else if(route.equalsIgnoreCase("sakura")){
            this.route=GoldenGlow.routeManager.getRoute("Sakura_City");
        }
    }

    public void init(EntityPlayerMP player) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/first_part_map.png");
        gui.addTexturedButton(501, "", 44, 88, 16, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(502, "", 124, 116, 16, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(503, "", 142, 162, 16, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(504, "", 174, 132, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 160, 192);
        gui.addTexturedButton(505, "", 60, 88, 80, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(506, "", 124, 88, 16, 28, "obscureobsidian:textures/gui/oobuttons.png", 160, 196);
        gui.addTexturedButton(507, "", 124, 132, 16, 24, "obscureobsidian:textures/gui/oobuttons.png", 160, 200);
        gui.addTexturedButton(508, "", 124, 140, 50, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
