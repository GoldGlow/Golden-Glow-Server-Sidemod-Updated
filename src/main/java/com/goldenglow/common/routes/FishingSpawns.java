package com.goldenglow.common.routes;

import com.pixelmonmod.pixelmon.enums.items.EnumRodType;

import java.util.ArrayList;

public class FishingSpawns {
    private ArrayList<SpawnPokemon> oldRodSpawns=new ArrayList<>();
    private ArrayList<SpawnPokemon> goodRodSpawns=new ArrayList<>();
    private ArrayList<SpawnPokemon> superRodSpawns=new ArrayList<>();

    public FishingSpawns(){
    }

    public ArrayList<SpawnPokemon> getOldRodSpawns(){
        return this.oldRodSpawns;
    }

    public void addOldRodSpawn(SpawnPokemon pokemon){
        this.oldRodSpawns.add(pokemon);
    }

    public ArrayList<SpawnPokemon> getGoodRodSpawns(){
        return this.goodRodSpawns;
    }

    public void addGoodRodSpawn(SpawnPokemon pokemon){
        this.goodRodSpawns.add(pokemon);
    }

    public ArrayList<SpawnPokemon> getSuperRodSpawns(){
        return this.superRodSpawns;
    }

    public void addSuperRodSpawn(SpawnPokemon pokemon){
        this.superRodSpawns.add(pokemon);
    }

    public ArrayList<SpawnPokemon> getSpawnsFromRod(EnumRodType rodType){
        switch(rodType){
            case SuperRod:
                return this.superRodSpawns;
            case GoodRod:
                return this.goodRodSpawns;
            case OldRod:
                return this.oldRodSpawns;
            default:
                return null;
        }
    }
}
