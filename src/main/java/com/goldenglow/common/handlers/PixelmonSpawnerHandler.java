package com.goldenglow.common.handlers;

import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by JeanMarc on 6/5/2019.
 */
public class PixelmonSpawnerHandler {
    public ArrayList<String> dayPokemon=new ArrayList<>();
    public ArrayList<String> nightPokemon=new ArrayList<>();
    File dayFile;
    File nightFile;

    public void init(){
        dayFile = new File(Reference.routeDir, "dayPokemon.cfg");
        if(!dayFile.exists()) {
            if (!dayFile.getParentFile().exists())
                dayFile.getParentFile().mkdirs();
            dayFile.mkdirs();
        }
        nightFile = new File(Reference.routeDir, "nightPokemon.cfg");
        if(!nightFile.exists()) {
            if (!nightFile.getParentFile().exists())
                nightFile.getParentFile().mkdirs();
            nightFile.mkdirs();
        }
        try {
            loadTimePokemon(dayFile, dayPokemon);
            loadTimePokemon(nightFile, nightPokemon);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadTimePokemon(File pokemon, ArrayList<String> list) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(pokemon));
        String readLine;
        while((readLine=reader.readLine())!=null){
            if(EnumSpecies.hasPokemonAnyCase(readLine)){
                list.add(readLine);
            }
        }
    }
}
