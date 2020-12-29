package com.goldenglow.common.battles.npc;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.teams.Team;
import com.goldenglow.common.util.Reference;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.battles.CustomNPCBattle;
import com.pixelmonessentials.common.battles.rules.CustomRules;
import com.pixelmonessentials.common.handler.TeamPreviewTimerTask;
import com.pixelmonessentials.common.teams.TeamCategory;
import com.pixelmonessentials.common.util.NpcScriptDataManipulator;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class CustomBattleHandler
{
    static GoldenGlow mod;
    public static CustomBattleHandler instance;

    public static List<CustomNPCBattle> battles = new ArrayList<CustomNPCBattle>();

    public CustomBattleHandler()
    {
        this.mod = GoldenGlow.instance;
        this.instance = this;
    }

    public static void createCustomBattle(EntityPlayerMP player, EntityNPCInterface npc){
        if (BattleRegistry.getBattle(player) != null) {
            player.sendMessage(new TextComponentString(com.pixelmonessentials.common.util.Reference.red + "Cannot Battle!"));
        }
        else {
            EntityPixelmon pixelmon = Pixelmon.storageManager.getParty(player).getAndSendOutFirstAblePokemon(player);
            if (pixelmon == null) {
                player.sendMessage(new TextComponentString(com.pixelmonessentials.common.util.Reference.red + "You have no pokemon that are able to battle!"));
            } else {
                ScriptObjectMirror object = NpcScriptDataManipulator.getJavascriptObject(new NPCWrapper(npc), "trainerData");
                com.pixelmonessentials.common.teams.Team npcTeam = null;
                String category = (String)object.get("category");
                String teamName = (String)object.get("team");
                if (category != null) {
                    if(GoldenGlow.permissionUtils.checkPermission(player, "hard")){
                        category+="-hard";
                    }
                    TeamCategory teamCategory = PixelmonEssentials.teamManager.getCategory(category);
                    if (teamCategory != null && teamName != null) {
                        npcTeam = teamCategory.getTeam(teamName);
                    }
                }

                if (npcTeam == null) {
                    player.sendMessage(new TextComponentString(com.pixelmonessentials.common.util.Reference.red + "NPC doesn't have a team"));
                } else {
                    String rules = (String)object.get("rules");
                    BattleRules battleRules = null;
                    if (rules != null) {
                        CustomRules customRules = PixelmonEssentials.customRulesManager.getRules(rules);
                        if (customRules != null) {
                            battleRules = customRules.getRules();
                        }
                    }

                    CustomNPCBattle battle;
                    if (battleRules != null) {
                        battle = new CustomNPCBattle(npc, battleRules);
                    } else {
                        battle = new CustomNPCBattle(npc);
                    }

                    int[] levels = new int[6];

                    for(int i = 0; i < npcTeam.getMembers().size(); ++i) {
                        levels[i] = npcTeam.getMember(i).getLevel();
                    }

                    NPCTrainer trainer = (NPCTrainer)PixelmonEntityList.createEntityByName(npc.display.getName(), player.getEntityWorld());
                    SetTrainerData data = new SetTrainerData(battle.getNpc().display.getName(), " ", " ", " ", 0, (ItemStack[])null);
                    trainer.update(data);
                    trainer.loadPokemon(npcTeam.getMembers());
                    trainer.setPosition(npc.posX, npc.posY, npc.posZ);
                    PlayerParticipant playerParticipant = new PlayerParticipant(player, Pixelmon.storageManager.getParty(player).getTeam(), battle.battleType.numPokemon);
                    TrainerParticipant trainerParticipant = new TrainerParticipant(trainer, player, battle.battleType.numPokemon, trainer.getPokemonStorage().getTeam());
                    battle.setRemainingNPCPokemon(trainerParticipant.countAblePokemon());

                    for(int i = 0; i < npcTeam.getMembers().size(); ++i) {
                        trainer.getPokemonStorage().get(i).setLevel(levels[i]);
                    }

                    if (battle.teamPreview) {
                        Timer timer = new Timer();
                        timer.schedule(new TeamPreviewTimerTask(battle, new PartyStorage[]{trainerParticipant.getStorage(), playerParticipant.getStorage()}), 100L);
                    } else {
                        BattleRegistry.startBattle(new BattleParticipant[]{playerParticipant}, new BattleParticipant[]{trainerParticipant}, battle);
                    }

                }
            }
        }
    }

    public static void createCustomNPCBattle(EntityNPCInterface firstNpc, String firstTeam, EntityNPCInterface secondNpc, String secondTeam){
        World world=firstNpc.getEntityWorld();
        Team firstNpcTeam;
        Team secondNpcTeam;
        EntityPixelmon firstPokemon;
        EntityPixelmon secondPokemon;
        if(firstTeam!=null) {
            Pokemon first = GoldenGlow.instance.teamManager.getTeam(firstTeam).getMember(0);
            firstPokemon= new EntityPixelmon(world);
            firstPokemon.setPokemon(first);
            firstPokemon.canDespawn=false;
            firstPokemon.setPosition(firstNpc.posX, firstNpc.posY, firstNpc.posZ);
            firstPokemon.setSpawnLocation(firstPokemon.getDefaultSpawnLocation());
        }else{
            firstPokemon=null;
        }
        if(secondTeam!=null) {
            Pokemon first = GoldenGlow.instance.teamManager.getTeam(secondTeam).getMember(0);
            secondPokemon= new EntityPixelmon(world);
            secondPokemon.setPokemon(first);
            firstPokemon.canDespawn=false;
            secondPokemon.setSpawnLocation(secondPokemon.getDefaultSpawnLocation());
            secondPokemon.setSpawnLocation(secondPokemon.getDefaultSpawnLocation());
            secondPokemon.setPosition(secondNpc.posX, secondNpc.posY, secondNpc.posZ);
        }else{
            secondPokemon=null;
        }
        try {
            if(firstPokemon!=null&&secondPokemon!=null) {
                WildPixelmonParticipant firstTrainer = new WildPixelmonParticipant(firstPokemon);
                firstTrainer.setNewPositions(firstNpc.getPosition());
                WildPixelmonParticipant secondTrainer = new WildPixelmonParticipant(secondPokemon);
                secondTrainer.setNewPositions(secondNpc.getPosition());
                if (!firstNpc.getEntityData().hasKey("inBattle")) {
                    firstNpc.getEntityData().setBoolean("inBattle", false);
                }
                if (!secondNpc.getEntityData().hasKey("inBattle")) {
                    secondNpc.getEntityData().setBoolean("inBattle", false);
                }

                DoubleNPCBattle rules = new DoubleNPCBattle(firstNpc, firstPokemon, secondNpc, secondPokemon);

                if (!firstNpc.getEntityData().getBoolean("inBattle") && !secondNpc.getEntityData().getBoolean("inBattle")) {
                    world.spawnEntity(firstPokemon);
                    world.spawnEntity(secondPokemon);
                    firstNpc.getEntityData().setBoolean("inBattle", true);
                    secondNpc.getEntityData().setBoolean("inBattle", true);
                    BattleRegistry.startBattle(new BattleParticipant[]{firstTrainer}, new BattleParticipant[]{secondTrainer}, rules);
                }
            }
        }catch (Exception e){
        }
    }
}