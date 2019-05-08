package com.goldenglow.common.teams;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public final String name;
    private ArrayList<Pokemon> members = new ArrayList<Pokemon>();

    public Team(String name) {
        this.name = name;
    }

    public ArrayList<Pokemon> getMembers() {
        return members;
    }

    public Pokemon getMember(int slot) {
        if(slot>members.size())
            return null;
        return members.get(slot);
    }

    public void addMember(Pokemon pixelmon) {
        if(members.size()<6)
            members.add(pixelmon);
    }
}
