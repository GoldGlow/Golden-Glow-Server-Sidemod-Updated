package com.goldenglow.common.util;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.routes.SpawnPokemon;
import com.goldenglow.common.teams.Team;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Remove;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.database.DatabaseAbilities;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SelectPokemonListPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.ShopKeeperPacket;
import com.pixelmonmod.pixelmon.config.StarterList;
import com.pixelmonmod.pixelmon.database.DatabaseMoves;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumSpecialTexture;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVsStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.*;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerExtraData;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import noppes.npcs.Server;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.constants.RoleType;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCFunctions {

    public static void openStarterGui(EntityPlayerMP player) {
        Pixelmon.instance.network.sendTo(new SelectPokemonListPacket(StarterList.getStarterList()), player);
    }

    public static void createCustomBattle(EntityPlayerMP player, String teamName, int winDialogID, int loseDialogID, EntityNPCInterface npc) {
        CustomBattleHandler.createCustomBattle(player, teamName, winDialogID, loseDialogID, npc);
    }

    public static void setCamera(EntityPlayerMP player, int posX, int posY, int posZ, int targetX, int targetY, int targetZ) {
        //Pixelmon.network.sendTo(new ClientCameraPacket(posX, posY, posZ, targetX, targetY, targetZ), player);
    }

    public static void releaseCamera(EntityPlayer player)
    {
        //BetterStorage.networkChannel.sendTo(new PacketGGSidemod(EnumGGPacketType.RESET_CAMERA), player);
    }

    public static void setShop(EntityPlayerMP player, int id){
        player.openGui(Pixelmon.instance, EnumGui.Shopkeeper.getIndex().intValue(), player.worldObj, id, 0, 0);
    }

    public static boolean hasItem(ItemStack item){
        try{
            item.getItem();
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
    }

    public static void factorySave(EntityPlayerMP player){
        try {
            File backup = new File(Reference.configDir+"/factory/"+player.getUUID(player.getGameProfile())+".json");
            if(!backup.exists())
                backup.createNewFile();
            JsonWriter json=new JsonWriter(new FileWriter(backup));
            PlayerStorage storage = PixelmonStorage.PokeballManager.getPlayerStorage(player);
            NBTTagCompound[] party=storage.partyPokemon;
            json.beginObject();
            json.name("pokemon");
            json.beginArray();
            for(NBTTagCompound pokemon:party){
                if(pokemon!=null){
                    json.beginObject();
                    json.name("Name").value(pokemon.getString("Name"));
                    json.name("Ability").value(pokemon.getString("Ability"));
                    json.name("EVAttack").value(pokemon.getInteger("EVAttack"));
                    json.name("EVDefence").value(pokemon.getInteger("EVDefence"));
                    json.name("EVHP").value(pokemon.getInteger("EVHP"));
                    json.name("EVSpecialAttack").value(pokemon.getInteger("EVSpecialAttack"));
                    json.name("EVSpecialDefence").value(pokemon.getInteger("EVSpecialDefense"));
                    json.name("EVSpeed").value(pokemon.getInteger("EVSpeed"));
                    json.name("EXP").value(pokemon.getInteger("EXP"));
                    json.name("Friendship").value(pokemon.getInteger("Friendship"));
                    json.name("Gender").value(pokemon.getShort("Gender"));
                    json.name("Growth").value(pokemon.getShort("Growth"));
                    json.name("IsShiny").value(pokemon.getBoolean("IsFainted"));
                    json.name("IVAttack").value(pokemon.getInteger("IVAttack"));
                    json.name("IVDefence").value(pokemon.getInteger("IVDefence"));
                    json.name("IVHP").value(pokemon.getInteger("IVHP"));
                    json.name("IVSpAtt").value(pokemon.getInteger("IVSpAtt"));
                    json.name("IVSpDef").value(pokemon.getInteger("IVSpDef"));
                    json.name("IVSpeed").value(pokemon.getInteger("IVSpeed"));
                    json.name("Level").value(pokemon.getInteger("Level"));
                    json.name("Nature").value(pokemon.getShort("Nature"));
                    json.name("originalTrainer").value(pokemon.getString("originalTrainer"));
                    json.name("PixelmonMoveID0").value(pokemon.getInteger("PokemonMoveID0"));
                    json.name("PixelmonMoveID1").value(pokemon.getInteger("PokemonMoveID1"));
                    json.name("PixelmonMoveID2").value(pokemon.getInteger("PokemonMoveID2"));
                    json.name("PixelmonMoveID3").value(pokemon.getInteger("PokemonMoveID3"));
                    json.endObject();
                }
            }
            json.endArray();
            json.endObject();
            json.close();
            for (int i=0;i<storage.partyPokemon.length;i++){
                storage.changePokemonAndAssignID(i,null);
            }
            storage.sendUpdatedList();
        } catch (PlayerNotLoadedException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void factoryLoadChoices(PlayerWrapper player){
        if(player.getFactionPoints(10)==0){
            player.addFactionPoints(10, GoldenGlow.instance.factoryManager.playerChoices.size()+1);
        }
        if(player.getFactionPoints(11)%7==0){

        }
        else{
            int[] party=GoldenGlow.instance.factoryManager.playerParties.get(player.getFactionPoints(10)-1);
        }
    }

    public static void factoryLeave(EntityPlayerMP player){
        try {
            File backup = new File(Reference.configDir+"/factory/"+player.getUUID(player.getGameProfile())+".json");
            if(!backup.exists())
                backup.createNewFile();
            InputStream istream= new FileInputStream(backup);
            PlayerStorage storage=PixelmonStorage.PokeballManager.getPlayerStorage(player);
            JsonObject file= new JsonParser().parse(new InputStreamReader(istream, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonArray party=file.get("pokemon").getAsJsonArray();
            for(int i=0;i<party.size();i++){
                JsonObject pokemon=party.get(i).getAsJsonObject();
                String name=pokemon.get("Name").getAsString();
                EntityPixelmon pixelmon=(EntityPixelmon) PixelmonEntityList.createEntityByName(name, player.getEntityWorld());
                pixelmon.setAbility(pokemon.get("Ability").getAsString());
                pixelmon.stats.EVs.HP=pokemon.get("EVHP").getAsInt();
                pixelmon.stats.EVs.Attack=pokemon.get("EVAttack").getAsInt();
                pixelmon.stats.EVs.Defence=pokemon.get("EVDefence").getAsInt();
                pixelmon.stats.EVs.SpecialAttack=pokemon.get("EVSpecialAttack").getAsInt();
                pixelmon.stats.EVs.SpecialDefence=pokemon.get("EVSpecialDefence").getAsInt();
                pixelmon.stats.EVs.Speed=pokemon.get("EVSpeed").getAsInt();
                pixelmon.friendship.setFriendship(pokemon.get("Friendship").getAsInt());
                short gender=pokemon.get("Gender").getAsShort();
                if(gender==0){
                    pixelmon.gender= Gender.Male;
                }
                else if(gender==1){
                    pixelmon.gender= Gender.Female;
                }
                else{
                    pixelmon.gender= Gender.None;
                }
                pixelmon.setGrowth(EnumGrowth.getGrowthFromIndex(pokemon.get("Growth").getAsShort()));
                pixelmon.setIsShiny(pokemon.get("IsShiny").getAsBoolean());
                pixelmon.stats.IVs.HP=pokemon.get("IVHP").getAsInt();
                pixelmon.stats.IVs.Attack=pokemon.get("IVAttack").getAsInt();
                pixelmon.stats.IVs.Defence=pokemon.get("IVDefence").getAsInt();
                pixelmon.stats.IVs.SpAtt=pokemon.get("IVSpAtt").getAsInt();
                pixelmon.stats.IVs.SpDef=pokemon.get("IVSpDef").getAsInt();
                pixelmon.stats.IVs.Speed=pokemon.get("IVSpeed").getAsInt();
                pixelmon.getLvl().setLevel(pokemon.get("Level").getAsInt());
                pixelmon.setNature(EnumNature.getNatureFromIndex(pokemon.get("Nature").getAsShort()));
                pixelmon.originalTrainer=pokemon.get("originalTrainer").getAsString();
                Moveset moveset = new Moveset();
                for(int j=0;j<4;j++) {
                    Attack attack = DatabaseMoves.getAttack(pokemon.get("PixelmonMoveID"+j).getAsInt());
                    moveset.add(attack);
                }
                pixelmon.setMoveset(moveset);
                storage.addToParty(pixelmon);
                NBTTagCompound nbt=storage.partyPokemon[i];
                nbt.setInteger("EXP",pokemon.get("EXP").getAsInt());
            }
            storage.sendUpdatedList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PlayerNotLoadedException e){
            e.printStackTrace();
        }
    }

    public static void safariZoneEnter(EntityPlayerMP player, int x, int y, int z){
        try {
            try {
                File pokeballs=new File(Reference.configDir+"/Safari/"+player.getUUID(player.getGameProfile())+".json");
                if(!pokeballs.exists())
                    pokeballs.createNewFile();
                JsonWriter json=new JsonWriter(new FileWriter(pokeballs));
                ItemStack[] inventory = player.inventory.mainInventory;
                json.beginArray();
                for(int i=0;i<36;i++){
                    ItemStack item=inventory[i];
                    if(item!=null) {
                        if (item.getItem() instanceof ItemPokeball) {
                            json.beginObject();
                            json.name("id").value(Item.getIdFromItem(item.getItem()));
                            json.name("amount").value(item.stackSize);
                            json.endObject();
                            player.inventory.clearMatchingItems(item.getItem(), 0, item.stackSize, null);
                        }
                    }
                }
                json.endArray();
                json.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(NullPointerException e){
                e.printStackTrace();
            }
            Item safariBalls=Item.getByNameOrId("pixelmon:item.Safari_Ball");
            player.inventory.addItemStackToInventory(new ItemStack(safariBalls, 30));
            player.inventoryContainer.detectAndSendChanges();
            PlayerStorage storage=PixelmonStorage.PokeballManager.getPlayerStorage(player);
            while (storage.hasSpace()){
                EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName("Charizard", player.getEntityWorld());
                pokemon.getLvl().setLevel(1);
                storage.addToParty(pokemon);
            }
            PixelmonStorage.PokeballManager.getPlayerStorage(player).recallAllPokemon();
            NBTTagCompound[] party = storage.partyPokemon;
            for(int i=0;i<6;i++){
                NBTTagCompound nbt= party[i];
                nbt.setBoolean("IsFainted", true);
            }
            storage.sendUpdatedList();
            player.setPosition(x, y, z);
        } catch (PlayerNotLoadedException e) {
            e.printStackTrace();
        }
    }

    public static void safariZoneExit(EntityPlayerMP player, int x, int y, int z){
        try {
            File pokeballs=new File(Reference.configDir+"/Safari/"+player.getUUID(player.getGameProfile())+".json");
            if(!pokeballs.exists())
                pokeballs.createNewFile();
            InputStream istream = new FileInputStream(pokeballs);
            JsonArray json= new JsonParser().parse(new InputStreamReader(istream, StandardCharsets.UTF_8)).getAsJsonArray();
            for(int i=0;i<json.size();i++){
                JsonObject ball = json.get(i).getAsJsonObject();
                int id=ball.get("id").getAsInt();
                int qty=ball.get("amount").getAsInt();
                Item oldball= Item.getItemById(id);
                player.inventory.addItemStackToInventory(new ItemStack(oldball, qty));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ItemStack[] inventory=player.inventory.mainInventory;
        for(ItemStack item:inventory){
            if(item!=null){
             if(item.getItem() instanceof ItemPokeball && ((ItemPokeball)item.getItem()).type.equals(EnumPokeballs.SafariBall)){
                 player.inventory.clearMatchingItems(item.getItem(), 0, item.stackSize, null);
             }
            }
        }
        player.inventoryContainer.detectAndSendChanges();
        try {
            PlayerStorage storage=PixelmonStorage.PokeballManager.getPlayerStorage(player);
            NBTTagCompound[] party= storage.partyPokemon;
            for(int i=0;i<6;i++){
                NBTTagCompound nbt=party[i];
                if(nbt.getString("Name").equals("Charizard")&&nbt.getInteger("Level")==1){
                    storage.changePokemonAndAssignID(i, null);
                }
                else if(nbt.getShort("Health")>0){
                    nbt.setBoolean("IsFainted", false);
                }
            }
            player.setPosition(x, y, z);
            storage.sendUpdatedList();
        } catch (PlayerNotLoadedException e) {
            e.printStackTrace();
        }
    }

    public static void customSpawner(IBlockScripted block, String species, int min, int max, int hiddenChance, int shinyChance) {
        if (GoldenGlow.instance.configHandler.spawner) {
            if (EnumPokemon.hasPokemonAnyCase(species)) {
                int minLvl = min;
                int maxLvl = max;
                int randomAbility = (int) (Math.random() * hiddenChance);
                int randomShiny = (int) (Math.random() * shinyChance);
                EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(species, block.getWorld().getMCWorld());
                int levelPlus = (int) (Math.random() * (maxLvl - minLvl + 1));
                pokemon.getLvl().setLevel((minLvl + levelPlus));
                if (randomShiny < 1) {
                    pokemon.setIsShiny(true);
                }
                if (randomAbility < 1) {
                    String[] abilities = DatabaseAbilities.getAbilities(species);
                    pokemon.setAbility(abilities[2]);
                }
                pokemon.setPosition(block.getX(), (block.getY() + 1), block.getZ());
                block.getWorld().getMCWorld().spawnEntityInWorld(pokemon);
            }
        }
    }

    public static void customSpawner(IBlockScripted block, String species, int hiddenChance, int shinyChance) {
        if (GoldenGlow.instance.configHandler.spawner) {
            if (EnumPokemon.hasPokemonAnyCase(species)) {
                int minLvl = 5;
                int maxLvl = 5;
                int randomAbility = (int) (Math.random() * hiddenChance);
                int randomShiny = (int) (Math.random() * shinyChance);
                int route = GoldenGlow.instance.routeManager.hasRoute(block.getX(), block.getY(), block.getZ());
                if (route != -1) {
                    Route wildRoute = RouteManager.routes.get(route);
                    for (SpawnPokemon pokemon : wildRoute.pokeList) {
                        if (pokemon.species.equals(species)) {
                            minLvl = pokemon.minLvl;
                            maxLvl = pokemon.maxLvl;
                        }
                    }
                }
                EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(species, block.getWorld().getMCWorld());
                int levelPlus = (int) (Math.random() * (maxLvl - minLvl + 1));
                pokemon.getLvl().setLevel((minLvl + levelPlus));
                if (randomShiny < 1) {
                    pokemon.setIsShiny(true);
                }
                if (randomAbility < 1) {
                    String[] abilities = DatabaseAbilities.getAbilities(species);
                    pokemon.setAbility(abilities[2]);
                }
                pokemon.setPosition(block.getX(), (block.getY() + 1), block.getZ());
                block.getWorld().getMCWorld().spawnEntityInWorld(pokemon);
            }
        }
    }

    public static void giveTeam(EntityPlayerMP player, String teamName) {
        Team team = GoldenGlow.instance.teamManager.getTeam(teamName);
        if(team !=null && player !=null)
        {
            for(EntityPixelmon pixelmon : team.getMembers())
            {
                try {
                    PixelmonStorage.PokeballManager.getPlayerStorage(player).addToParty(pixelmon);
                } catch (PlayerNotLoadedException e) {
                    e.printStackTrace();
                }
            }
            player.addChatMessage(new ChatComponentText(Reference.messagePrefix+"You were given team: "+team.name+"!"));
        }
    }

    public static void area(EntityNPCInterface npc, int minX, int minZ, int maxX, int maxZ, int areaid) {
        int[] x = {minX, maxX};
        int[] z = {minZ, maxZ};
        Arrays.sort(x);
        Arrays.sort(z);

        List playerList = npc.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.fromBounds(x[0], 0.0, z[0], x[1], npc.worldObj.getHeight(), z[1]));
        for(Object ob : playerList)
        {
            EntityPlayerMP player = (EntityPlayerMP)ob;
            ScoreObjective route = player.getWorldScoreboard().getObjective("route");
            if ((route == null) || (route.getCriteria().isReadOnly())) {
                return;
            }
            Score i = player.getWorldScoreboard().getValueFromObjective(player.getName(), route);
            if(i.getScorePoints() != areaid) {
                i.setScorePoints(areaid);
                Server.sendData(player, EnumPacketClient.MESSAGE, new Object[]{npc.display.getTitle(), npc.display.getName()});
            }
        }
    }

    public static void area(EntityNPCInterface npc, int radius, int areaid)
    {
        area(npc, (int) npc.posX - radius, (int) npc.posZ - radius, (int) npc.posX + radius, (int) npc.posZ + radius, areaid);
    }

    public static void createFollower(EntityPlayer player) {
        try {
            PlayerStorage playerStorage = PixelmonStorage.PokeballManager.getPlayerStorage((EntityPlayerMP)player);
            createFollower(player, playerStorage.getFirstAblePokemon(((EntityPlayerMP) player).worldObj));
        } catch (PlayerNotLoadedException e) {
            e.printStackTrace();
        }
    }

    public static void createFollower(EntityPlayer player, EntityPixelmon poke) {
        EntityCustomNpc follower = new EntityCustomNpc(player.worldObj);

        follower.modelData.setEntityClass((Class) EntityList.stringToClassMapping.get("pixelmon.Pixelmon"));
        follower.modelData.extra.setString("Name", poke.getName());

        if (poke.getIsShiny())
            follower.display.setSkinTexture("pixelmon:textures/pokemon/pokemon-shiny/shiny" + poke.getName().toLowerCase() + ".png");
        else if ((poke.getDataWatcher().getWatchableObjectShort(26) == (short) EnumSpecialTexture.Roasted.id))
            follower.display.setSkinTexture("pixelmon:textures/pokemon/pokemon-roasted/roasted" + poke.getName().toLowerCase() + ".png");
        else if ((poke.getDataWatcher().getWatchableObjectShort(26) == (short) EnumSpecialTexture.Zombie.id))
            follower.display.setSkinTexture("pixelmon:textures/pokemon/pokemon-zombie/zombie" + poke.getName().toLowerCase() + ".png");
        else if ((poke.getDataWatcher().getWatchableObjectShort(26) == (short) EnumSpecialTexture.Online.id))
            follower.display.setSkinTexture("pixelmon:textures/pokemon/pokemon-online/" + poke.getName().toLowerCase() + ".png");
        else
            follower.display.setSkinTexture("pixelmon:textures/pokemon/"+poke.getName().toLowerCase()+".png");

        follower.advanced.setRole(RoleType.FOLLOWER);
        ((RoleFollower)follower.roleInterface).setOwner(player);
        ((RoleFollower)follower.roleInterface).disableGui = true;
        ((RoleFollower)follower.roleInterface).infiniteDays = true;

        follower.display.setName(poke.getNickname());

        follower.setPosition(player.posX, player.posY, player.posZ);
        follower.ai.setWalkingSpeed(6);
        follower.ai.returnToStart = false;

        follower.script.setEnabled(true);
        ScriptContainer script = new ScriptContainer(follower.script);
        script.scripts.add("npcfunctions.js");
        script.scripts.add("follower-init.js");
        follower.script.getScripts().add(EnumScriptType.INIT.ordinal(), script);

        script = new ScriptContainer(follower.script);
        script.scripts.add("follower-tick.js");
        follower.script.getScripts().add(EnumScriptType.TICK.ordinal(), script);

        script = new ScriptContainer(follower.script);
        script.scripts.add("npcfunctions.js");
        script.scripts.add("follower-interact.js");
        follower.script.getScripts().add(EnumScriptType.INTERACT.ordinal(), script);

        player.worldObj.spawnEntityInWorld(follower);
    }

    /*public static void registerFollower(EntityPlayer player, ScriptNpc npc) {
        GoldenGlow.instance.followerHandler.followMap.put(player, npc);
    }*/

    /*public static void giveFollowerItem(EntityPlayer player, ScriptNpc npc) {
        double i = Math.random();
        ItemStack item;
        if(i<0.3)
            item = new ItemStack(PixelmonItems.potion);
        else if(i<0.6)
            if(i<0.4)
                item = new ItemStack(PixelmonItemsHeld.berryOran);
            else if(i<0.45)
                item = new ItemStack(PixelmonItemsHeld.berryLeppa);
            else if(i<0.5)
                item = new ItemStack(PixelmonItemsHeld.berryRawst);
            else if(i<0.55)
                item = new ItemStack(PixelmonItemsHeld.aguavBerry);
            else
                item = new ItemStack(PixelmonItemsHeld.apicotBerry);
        else
            item = new ItemStack(PixelmonItems.elixir);
        player.inventory.addItemStackToInventory(item);
        player.addChatComponentMessage(new ChatComponentText("Oh! "+npc.getName()+" was holding a "+item.getDisplayName()+"!"));
        npc.setStoredData("pickup", 0);
    }*/

    public static void huntTest(EntityPlayer player) {
        EntityCustomNpc npc = new EntityCustomNpc(player.worldObj);

        npc.display.setShowName(0);
        npc.advanced.interactLines.lines.clear();
        npc.ai.stopAndInteract = false;

        npc.modelData.setEntityClass((Class) EntityList.stringToClassMapping.get("pixelmon.Pixelmon"));
        npc.modelData.extra.setString("Name", "Vulpix");
        npc.display.setSkinTexture("minecraft:textures/blocks/bedrock.png");

        npc.setPosition(player.posX, player.posY, player.posZ);
        player.worldObj.spawnEntityInWorld(npc);
    }
}
