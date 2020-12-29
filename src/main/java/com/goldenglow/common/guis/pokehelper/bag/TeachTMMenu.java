package com.goldenglow.common.guis.pokehelper.bag;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.PixelmonUtils;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.enums.ITechnicalMove;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.List;

public class TeachTMMenu extends EssentialsGuiBase {
    private String attackName;

    public TeachTMMenu(String attackName){
        super(6201);
        this.attackName=attackName;
    }

    public void init(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/choosepokemon.png");
        gui.addLabel(200, "Teach "+this.attackName+" to:", 20, 176, 128, 20);
        for(int i=0;i<6;i++){
            if(PixelmonUtils.getPartySlot(player, i)!=null){
                Pokemon pokemon=PixelmonUtils.getPartySlot(player, i);
                String texture=getTexture(pokemon);
                boolean canLearn=false;
                if(!pokemon.isEgg()){
                    if(!pokemon.getMoveset().hasAttack(new Attack(this.attackName)))
                        canLearn=canLearnTM(pokemon, this.attackName);
                }
                int[] coordinates=getCoordinates(i, canLearn);
                GGLogger.info(texture);
                gui.addTexturedRect(100+i, texture, coordinates[0], coordinates[1], 256, 256).setScale(0.09F);
                if(canLearn){
                    gui.addTexturedButton(500+i, "Able", coordinates[2], coordinates[3], coordinates[4], coordinates[5], "obscureobsidian:textures/gui/transparent.png");
                    this.addButton(new EssentialsButton(500+i, new ActionStringData("TEACH_MOVE", i+":"+this.attackName)));
                }
                else{
                    if(pokemon.getMoveset().hasAttack(new Attack(this.attackName))){
                        gui.addLabel(200+i, "Learned", coordinates[2], coordinates[3], 128, 20);
                    }
                    else {
                        gui.addLabel(200+i, "Not Able", coordinates[2], coordinates[3], 128, 20);
                    }
                }
            }
        }
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    public String getTexture(Pokemon pokemon){
        String texture="pixelmon:textures/sprites";
        if(pokemon.isEgg()) {
            texture += "/eggs/";
            if (pokemon.getSpecies().name.equalsIgnoreCase("togepi")) {
                return texture + "togepi1.png";
            } else if (pokemon.getSpecies().name.equalsIgnoreCase("Manaphy")) {
                return texture + "manaphy1.png";
            } else{
                return texture+"egg1.png";
            }
        }else if(pokemon.isShiny()){
            texture+="/shinypokemon/";
        }
        else{
            texture+="/pokemon/";
        }
        texture+=pokemon.getSpecies().getNationalPokedexNumber();
        if(pokemon.getForm()>0){
            texture+="-"+pokemon.getFormEnum().getSpriteSuffix();
        }
        return texture+".png";
    }

    public boolean canLearnTM(Pokemon pokemon, String moveName){
        List<Attack> tms= pokemon.getBaseStats().tmMoves;
        List<ITechnicalMove> gtms=pokemon.getBaseStats().getTMMoves();
        for(Attack tm:tms){
            if(tm.getActualMove().getAttackName().equals(moveName)){
                return true;
            }
        }
        for(ITechnicalMove move: gtms){
            if(move.getAttackName().equals(moveName)){
                return true;
            }
        }
        return false;
    }

    public int[] getCoordinates(int slot, boolean canLearn){
        int[] coord=new int[6];
        if(!canLearn){
            if(slot==0){
                coord[0]=9;
                coord[1]=26;
                coord[2]=32;
                coord[3]=57;
            }
            else{
                coord[0]=104;
                coord[1]=11+30*(slot-1);
                coord[2]=170;
                coord[3]=15+30*(slot-1);
            }
        }
        else{
            if(slot==0){
                coord[0]=9;
                coord[1]=26;
                coord[2]=8;
                coord[3]=38;
                coord[4]=89;
                coord[5]=52;
            }
            else{
                coord[0]=104;
                coord[1]=11+30*(slot-1);
                coord[2]=129;
                coord[3]=9+30*(slot-1);
                coord[4]=118;
                coord[5]=30;
            }
        }
        return coord;
    }
}
