package com.goldenglow.common.guis.trading;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.trading.TradeManager;
import com.goldenglow.common.trading.TradingOffer;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsFormGui;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsFormGuiBase;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import com.pixelmonessentials.common.util.GuiUtils;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import java.util.List;

public class TradeOfferGui extends EssentialsFormGuiBase {
    TradingOffer offer;

    public TradeOfferGui(){
        super(6010, 500);
        this.addButton(new EssentialsButton(501, new ActionData("")));
        this.addButton(new EssentialsButton(502, new ActionData("")));

    }

    @Override
    public void init(EntityPlayerMP player){
        this.offer=GoldenGlow.tradeManager.getPlayerTrade(player).getOffer(player);
        CustomGuiWrapper gui=getGui(player);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        playerWrapper.showCustomGui(gui);
    }

    public CustomGuiWrapper getGui(EntityPlayerMP player){
        CustomGuiWrapper gui =new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 0, 0, 256, 20);
        gui.addLabel(200, "Your Offer", 100, 0, 128, 20);
        gui=addOfferedPokemon(gui, player);
        gui.addTexturedRect(101, "obscureobsidian:textures/gui/grey_square.png", 40, 30, 174, 32);
        gui.addLabel(201, "$", 42, 197, 20, 10);
        gui.addTextField(400, 52, 191, 80, 20);
        gui.addLabel(202, "(Max "+ Pixelmon.storageManager.getParty(player).getMoney()+")", 140, 197, 128, 10);
        gui.addLabel(203, "Items", 110, 78, 128, 10);
        gui.addScroll(300, 60, 90, 130, 60, new String[0]);
        gui.addButton(502, "Edit", 100, 155, 48, 20);
        gui.addButton(500, "Submit", 64, 224, 48, 20);
        gui.addButton(501, "Cancel", 144, 224, 48, 20);
        return  gui;
    }

    public CustomGuiWrapper addOfferedPokemon(CustomGuiWrapper gui, EntityPlayerMP player){
        List<Pokemon> offeredPokemon= offer.getPokemon();
        for(int i=0;i<5;i++){
            if(offeredPokemon.size()<i){
                GuiUtils.getPokemonSprite(offeredPokemon.get(i));
            }
            else{
                gui.addTexturedRect(102+i, "pixelmon:textures/items/pokeballs/pokeball.png", 42+34*i, 30, 256, 256).setScale(0.125f);
            }
        }
        return gui;
    }
}
