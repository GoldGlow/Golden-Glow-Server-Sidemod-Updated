package com.goldenglow.common.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.util.ArrayList;

public class OOPokedexEvent extends Event {
    public ArrayList<Integer> caughtList;
    public PlayerWrapper player;

    public OOPokedexEvent(PlayerWrapper player, ArrayList<Integer> caughtList){
        this.player=player;
        this.caughtList=caughtList;
    }
}
