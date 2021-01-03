package com.goldenglow.common.battles.bosses.fights;

import com.goldenglow.common.battles.bosses.BossParticipant;
import com.goldenglow.common.battles.bosses.phase.Phase;
import com.goldenglow.common.battles.bosses.phase.Trigger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.Type1Spec;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.*;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.forms.EnumGenesect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BossBase {

    PokemonSpec spec;
    EnumType[] types;
    ItemStack heldItem;
    Stats stats;
    String nickname;
    Moveset moveset;

    List<Phase> phases = new ArrayList<>();

    BossBase(){}

    public BossBase(PokemonSpec spec, List<Phase> phases) {
        this.spec = spec;
        this.phases = phases;
    }

    public BossBase(PokemonSpec spec, String nickname, List<Phase> phases) {
        this(spec, phases);
        this.nickname = nickname;
    }

    public PokemonSpec getSpec() {
        return this.spec;
    }

    public static BossBase loadFromFile(File file) throws FileNotFoundException {
        BossBase boss = new BossBase();

        InputStream iStream = new FileInputStream(file);
        JsonObject json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonObject();

        //Load Base Pokemon
        JsonObject p = json.getAsJsonObject("pokemon");
        String species = p.get("species").getAsString();

        if (EnumSpecies.hasPokemonAnyCase(species)) {
            boss.spec = new PokemonSpec();
            boss.spec.name = p.get("species").getAsString();
            if (p.has("nickname"))
                boss.nickname = p.get("nickname").getAsString();
            if (p.has("level"))
                boss.spec.level = p.get("level").getAsInt();
            if (p.has("form"))
                boss.spec.form = (byte)p.get("form").getAsInt();
            if (p.has("gender")) {
                Gender gender = Gender.getGender(p.get("gender").getAsString());
                if(gender!=null)
                    boss.spec.gender = (byte)gender.ordinal();
            }
            if (p.has("ability"))
                boss.spec.ability = p.get("ability").getAsString();
            if (p.has("nature")) {
                EnumNature nature = EnumNature.natureFromString(p.get("nature").getAsString());
                if (nature != null)
                    boss.spec.nature = (byte)nature.index;
            }
            if (p.has("growth")) {
                EnumGrowth growth = EnumGrowth.growthFromString(p.get("growth").getAsString());
                if(growth != null)
                    boss.spec.growth = (byte)growth.index;
            }
            if (p.has("shiny"))
                boss.spec.shiny = p.get("shiny").getAsBoolean();
            if (p.has("customTexture")) {
                SpecValue s = PokemonSpec.getSpecForKey("customTexture").parse(p.get("customTexture").getAsString());
                boss.spec.extraSpecs.add(s);
            }
            if (p.has("heldItem"))
                boss.heldItem = new ItemStack(Item.getByNameOrId(p.get("heldItem").getAsString()));
            if (p.has("type")) {
                JsonArray array = p.getAsJsonArray("type");
                boss.types[0] = EnumType.parseType(array.get(0).getAsString());
                if(array.size()>1)
                    boss.types[1] = EnumType.parseType(array.get(1).getAsString());
            }
            if (p.has("stats")) {
                JsonObject j = p.getAsJsonObject("stats");
                Stats stats = new Stats();
                if (j.has("hp"))
                    stats.hp = j.get("hp").getAsInt();
                if (j.has("atk"))
                    stats.attack = j.get("atk").getAsInt();
                if (j.has("spAtk"))
                    stats.specialAttack = j.get("spAtk").getAsInt();
                if (j.has("def"))
                    stats.defence = j.get("def").getAsInt();
                if (j.has("spDef"))
                    stats.specialDefence = j.get("spDef").getAsInt();
                if (j.has("speed"))
                    stats.speed = j.get("speed").getAsInt();
                boss.stats = stats;
            }
            if (p.has("ivs")) {
                JsonObject j = p.getAsJsonObject("ivs");
                IVStore ivs = new IVStore();
                if (j.has("hp"))
                    ivs.hp = j.get("hp").getAsInt();
                if (j.has("atk"))
                    ivs.attack = j.get("atk").getAsInt();
                if (j.has("spAtk"))
                    ivs.specialAttack = j.get("spAtk").getAsInt();
                if (j.has("def"))
                    ivs.defence = j.get("def").getAsInt();
                if (j.has("spDef"))
                    ivs.specialDefence = j.get("spDef").getAsInt();
                if (j.has("speed"))
                    ivs.speed = j.get("speed").getAsInt();

                if (boss.stats == null)
                    createStats(boss);
                boss.stats.ivs = ivs;
            }
            if (p.has("evs")) {
                JsonObject j = p.getAsJsonObject("evs");
                EVStore evs = new EVStore();
                if (j.has("hp"))
                    evs.hp = j.get("hp").getAsInt();
                if (j.has("atk"))
                    evs.attack = j.get("atk").getAsInt();
                if (j.has("spAtk"))
                    evs.specialAttack = j.get("spAtk").getAsInt();
                if (j.has("def"))
                    evs.defence = j.get("def").getAsInt();
                if (j.has("spDef"))
                    evs.specialDefence = j.get("spDef").getAsInt();
                if (j.has("speed"))
                    evs.speed = j.get("speed").getAsInt();

                if (boss.stats == null)
                    createStats(boss);
                boss.stats.evs = evs;
            }
            if (p.has("moveset")) {
                JsonArray array = p.getAsJsonArray("moveset");
                boss.moveset = new Moveset();
                for (JsonElement e : array) {
                    boss.moveset.add(new Attack(e.getAsString()));
                }
            }

            //Load Phases
            if (json.has("phases")) {
                JsonArray phases = json.getAsJsonArray("phases");
                for (JsonElement e : phases) {
                    JsonObject phaseObj = e.getAsJsonObject();

                    JsonArray triggersArray = phaseObj.getAsJsonArray("triggers");
                    List<Trigger> triggers = new ArrayList<>();
                    for (JsonElement t : triggersArray) {
                        JsonObject triggerObj = t.getAsJsonObject();
                        Trigger trigger = new Trigger();
                        if (triggerObj.has("hpPercentage"))
                            trigger.hpPercentage = triggerObj.get("hpPercentage").getAsFloat();
                        if (triggerObj.has("status"))
                            trigger.status = StatusType.valueOf(triggerObj.get("status").getAsString());
                        if (triggerObj.has("hitByType"))
                            trigger.hitByType = EnumType.valueOf(triggerObj.get("hitByType").getAsString());
                        if (triggerObj.has("turn"))
                            trigger.turnNumber = triggerObj.get("turn").getAsInt();
                        triggers.add(trigger);
                    }

                    Phase phase = new Phase(triggers.toArray(new Trigger[0]));

                    if (phaseObj.has("nickname"))
                        phase.setNickname(phaseObj.get("nickname").getAsString());
                    if (phaseObj.has("ability"))
                        phase.setAbility(AbilityBase.getAbility(phaseObj.get("ability").getAsString()).get());
                    if (phaseObj.has("nature"))
                        phase.setNature(EnumNature.valueOf(phaseObj.get("nature").getAsString()));
                    if (phaseObj.has("form"))
                        phase.setForm(phaseObj.get("form").getAsInt());
                    if (phaseObj.has("growth"))
                        phase.setGrowth(EnumGrowth.growthFromString(phaseObj.get("growth").getAsString()));
                    if (phaseObj.has("heldItem"))
                        phase.setHeldItem(new ItemStack(Item.getByNameOrId(phaseObj.get("heldItem").getAsString())));
                    if (phaseObj.has("type")) {
                        JsonArray array = phaseObj.getAsJsonArray("type");
                        EnumType[] types = new EnumType[2];
                        for (int i = 0; i < types.length; i++) {
                            types[i] = EnumType.valueOf(array.get(i).getAsString());
                        }
                        phase.setType(types);
                    }
                    if (phaseObj.has("moveset")) {
                        JsonArray array = phaseObj.getAsJsonArray("moveset");
                        Moveset moveset = new Moveset();
                        for (JsonElement m : array) {
                            moveset.add(new Attack(m.getAsString()));
                        }
                        phase.setMoveset(moveset);
                    }

                    boss.phases.add(phase);
                }
            }
        }
        return boss;
    }

    static void createStats(BossBase boss) {
        boss.stats = new Stats();
        boss.stats.setLevelStats(EnumNature.getNatureFromIndex(boss.spec.nature), EnumSpecies.getFromNameAnyCase(boss.spec.name).getBaseStats(), boss.spec.level);
    }

    public void phaseCheck(BattleControllerBase bc, BossParticipant participant) {
        for(Phase p : this.phases) {
            p.checkTriggers(participant, participant.controlledPokemon.get(0));
        }
    }

}
