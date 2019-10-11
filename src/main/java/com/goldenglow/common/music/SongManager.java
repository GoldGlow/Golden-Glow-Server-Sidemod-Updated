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
    public String wildDefault,trainerDefault,encounterDefault,victoryDefault,evolutionDefault,levelUpDefault;

    public void init() {
        GoldenGlow.logger.info("Loading Song Config...");
        try {
            if(!config.createNewFile()) {
                BufferedReader reader = new BufferedReader(new FileReader(config));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("wildBattleSong="))
                        this.wildDefault = line.replace("wildBattleSong=", "").replace(" ", "");
                    if (line.startsWith("trainerBattleSong="))
                        this.trainerDefault = line.replace("trainerBattleSong=", "").replace(" ", "");
                    if (line.startsWith("encounterSong="))
                        this.encounterDefault = line.replace("encounterSong=", "").replace(" ", "");
                    if (line.startsWith("victorySong="))
                        this.victoryDefault = line.replace("victorySong=", "").replace(" ", "");
                    if (line.startsWith("evolutionSong="))
                        this.evolutionDefault = line.replace("evolutionSong=", "").replace(" ", "");
                    if (line.startsWith("levelUpSound="))
                        this.levelUpDefault = line.replace("levelUpSound=", "").replace(" ", "");
                }
            } else {
                FileOutputStream os = new FileOutputStream(config);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                this.wildDefault="obscureobsidian:wild.lgpe";
                writer.write("wildBattleSong="+wildDefault);
                writer.newLine();
                this.trainerDefault="obscureobsidian:trainer.lgpe";
                writer.write("trainerBattleSong="+trainerDefault);
                writer.newLine();
                this.encounterDefault="obscureobsidian:encounter.rival_lgpe";
                writer.write("encounterSong="+encounterDefault);
                writer.newLine();
                this.victoryDefault="obscureobsidian:victory.lgpe";
                writer.write("victorySong="+victoryDefault);
                writer.newLine();
                this.evolutionDefault="obscureobsidian:sound.evolution";
                writer.write("evolutionSong="+evolutionDefault);
                writer.newLine();
                this.levelUpDefault="obscureobsidian:sound.level_up";
                writer.write("levelUpSound="+levelUpDefault);
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
