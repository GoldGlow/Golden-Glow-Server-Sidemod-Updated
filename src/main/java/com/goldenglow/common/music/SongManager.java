package com.goldenglow.common.music;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JeanMarc on 5/9/2019.
 */
public class SongManager {

    static File config = new File(Reference.configDir, "songs/config.cfg");
    public static String wildDefault,trainerDefault,encounterDefault,victoryDefault,evolutionDefault,levelUpDefault="obscureobsidian:sound.level_up";

    public static void init() {
        GoldenGlow.logger.info("Loading Song Config...");
        try {
            if(!config.createNewFile()) {
                BufferedReader reader = new BufferedReader(new FileReader(config));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("wildBattleSong="))
                        wildDefault = line.replace("wildBattleSong=", "").replace(" ", "");
                    if (line.startsWith("trainerBattleSong="))
                        trainerDefault = line.replace("trainerBattleSong=", "").replace(" ", "");
                    if (line.startsWith("encounterSong="))
                        encounterDefault = line.replace("encounterSong=", "").replace(" ", "");
                    if (line.startsWith("victorySong="))
                        victoryDefault = line.replace("victorySong=", "").replace(" ", "");
                    if (line.startsWith("evolutionSong="))
                        evolutionDefault = line.replace("evolutionSong=", "").replace(" ", "");
                    if (line.startsWith("levelUpSound="))
                        levelUpDefault = line.replace("levelUpSound=", "").replace(" ", "");
                }
            } else {
                FileOutputStream os = new FileOutputStream(config);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                writer.write("wildBattleSong=obscureobsidian:wild.lgpe");
                writer.newLine();
                writer.write("trainerBattleSong=obscureobsidian:trainer.lgpe");
                writer.newLine();
                writer.write("encounterSong=obscureobsidian:encounter.rival_lgpe");
                writer.newLine();
                writer.write("victorySong=obscureobsidian:victory.lgpe");
                writer.newLine();
                writer.write("evolutionSong=obscureobsidian:sound.evolution");
                writer.newLine();
                writer.write("levelUpSound=obscureobsidian:sound.level_up");
                writer.newLine();
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playSound(EntityPlayerMP player, String source, String path) {
        player.connection.sendPacket(new SPacketCustomSound(path, SoundCategory.getByName(source), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), 1000, 1));
    }

    public static void setCurrentSong(EntityPlayerMP player, String newSong){
        player.getCapability(OOPlayerProvider.OO_DATA, null).setSong(newSong);
        Server.sendData(player, EnumPacketClient.PLAY_MUSIC, newSong);
    }

    public static void setRouteSong(EntityPlayerMP player) {
        setCurrentSong(player, player.getCapability(OOPlayerProvider.OO_DATA, null).getRoute().song);
    }

    public static void setToTrainerMusic(EntityPlayerMP player){
        setCurrentSong(player, player.getCapability(OOPlayerProvider.OO_DATA, null).getTrainerTheme());
    }

    public static void setToWildMusic(EntityPlayerMP player){
        setCurrentSong(player, player.getCapability(OOPlayerProvider.OO_DATA, null).getWildTheme());
    }

    public static void setToPvpMusic(EntityPlayerMP player, BattleParticipant[] opponents){
        /*
        int pvpOption=player.getEntityData().getInteger("PvpOption");
        String song="";
        if(pvpOption==0){
            for(BattleParticipant opponent:opponents){
                if(opponent instanceof PlayerParticipant){
                    song=getPvpSong(((PlayerParticipant) opponent).player);
                    if(song.startsWith("obscureobsidian:custom")){
                        setCurrentSong(player, song);
                        return;
                    }
                }
            }
        }
        else if(pvpOption==1){
            for(BattleParticipant opponent:opponents){
                if(opponent instanceof PlayerParticipant){
                    if(!getPvpSong(((PlayerParticipant) opponent).player).equals(GoldenGlow.instance.songManager.trainerBattleSong))
                        song=getPvpSong(((PlayerParticipant) opponent).player);
                    if(song.startsWith("obscureobsidian:custom")){
                        setCurrentSong(player, song);
                        return;
                    }
                }
            }
            if(song.equals("")){
                song=getPvpSong(player);
            }
        }
        else if(pvpOption==2){
            for(BattleParticipant opponent:opponents){
                if(opponent instanceof PlayerParticipant){
                    if(song.startsWith("obscureobsidian:custom")){
                        setCurrentSong(player, song);
                        return;
                    }
                }
            }
            if(song.equals("")){
                setCurrentSong(player, getPvpSong(player));
            }
        }
        else if(pvpOption==3){
            song=getPvpSong(player);
        }
        setCurrentSong(player, song);
         */
    }
}
