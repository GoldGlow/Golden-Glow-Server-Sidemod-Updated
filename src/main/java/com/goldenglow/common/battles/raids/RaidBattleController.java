package com.goldenglow.common.battles.raids;

import com.goldenglow.GoldenGlow;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedAbnormalEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;
import com.pixelmonmod.pixelmon.api.events.SpectateEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.*;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import com.pixelmonmod.pixelmon.battles.controller.log.FleeAction;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SwitchCamera;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EndSpectate;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.FailFleeSwitch;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.FormBattleUpdate;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.gui.SwitchOutPacket;
import com.pixelmonmod.pixelmon.config.EnumForceBattleResult;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Pickup;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.*;
import java.util.stream.Collectors;

import static com.pixelmonmod.pixelmon.battles.controller.CalcPriority.speedComparator;

public class RaidBattleController extends BattleControllerBase {

    public RaidBattleController(PlayerParticipant playerParticipant, WildPixelmonParticipant pixelmonParticipant, BattleRules rules) {
        super(new BattleParticipant[] {playerParticipant}, new BattleParticipant[] {pixelmonParticipant}, rules);
        this.participants.clear();
        for (BattleParticipant p : new BattleParticipant[] {playerParticipant}) {
            p.team = 0;
            this.participants.add(p);
        }
        for (BattleParticipant p : new BattleParticipant[] {pixelmonParticipant}) {
            p.team = 1;
            this.participants.add(p);
        }
        rules.validateRules();
        this.rules = rules;
        this.battleLog = new BattleLog(this);
    }

    public ArrayList<BattleParticipant> participants = new ArrayList();

    public GlobalStatusController globalStatusController = new GlobalStatusController(this);

    public ArrayList<Spectator> spectators = new ArrayList();

    public int battleTicks = 0;

    public int battleTurn = -1;

    public int playerNumber = 0;

    public boolean battleEnded = false;

    public int battleIndex;

    public BattleLog battleLog;

    public Attack lastAttack;

    public int numFainted;

    public BattleRules rules;

    public boolean simulateMode = false;

    public boolean sendMessages = true;

    public boolean wasFishing = false;

    public boolean calculatedTurnOrder = false;

    public List<PixelmonWrapper> switchingOut = new ArrayList();

    public static final int TICK_TOP = 20;

    public static ArrayList<AttackAnimation> currentAnimations = new ArrayList();

    public Set<Pokemon> checkedPokemon = Sets.newHashSet();

    public boolean init = false;

    public void initBattle() throws Exception {
        for (BattleParticipant p : this.participants) {
            if (!p.checkPokemon()) {
                throw new Exception("Battle could not start!");
            }
        }
        for (BattleParticipant p : this.participants) {
            p.startBattle(this);
        }

        modifyStats();
        List<PixelmonWrapper> turnOrder = getDefaultTurnOrder();
        for (PixelmonWrapper pw : turnOrder) {
            pw.getBattleAbility().beforeSwitch(pw);
        }

        for (BattleParticipant p : this.participants) {
            p.updateOtherPokemon();
            for (PixelmonWrapper pw : p.controlledPokemon) {
                pw.addAttackers();
            }
            for (PixelmonWrapper apw : p.allPokemon) {
                if (apw != null) {
                    for (BattleParticipant o : p.getOpponents()) {
                        for (PixelmonWrapper opw : o.controlledPokemon) {
                            apw.getBattleAbility().applyStartOfBattleHeadOfPartyEffect(apw, opw);
                        }
                    }
                    break;
                }
            }
        }
        for (BattleParticipant p : this.participants) {
            if (p instanceof PlayerParticipant) {
                ((PlayerParticipant)p).openGui();
                this.playerNumber++;
            }
        }


        this.battleTurn = 0;

        for (PixelmonWrapper pw : turnOrder) {
            pw.afterSwitch();
        }
        this.init = true;
    }

    public BattleStage stage = BattleStage.PICKACTION;

    public int turn = 0;

    public ArrayList<PixelmonWrapper> turnList = new ArrayList();

    public void update() {
        if (this.battleEnded) {
            return;
        }
        try {
            if (!this.init) {
                try {
                    initBattle();
                } catch (Exception e) {
                    BattleRegistry.deRegisterBattle(this);
                    e.printStackTrace();
                    this.init = false;
                    endBattle(EnumBattleEndCause.FORCE);

                    return;
                }
            }
            onUpdate();

            boolean hasAnimationsPlaying = (CollectionHelper.find(currentAnimations, animation -> (animation.bc == this)) != null);

            if (hasAnimationsPlaying || isEvolving() || isWaiting() || this.paused || this.simulateMode || this.participants.size() < 2) {
                return;
            }

            if (this.battleTicks++ > 20) {

                if (this.stage == BattleStage.PICKACTION) {
                    for (BattleParticipant p : this.participants) {
                        p.clearTurnVariables();
                        p.selectAction();
                    }
                    this.stage = BattleStage.DOACTION;
                    this.turn = 0;
                } else if (this.stage == BattleStage.DOACTION) {

                    modifyStats();
                    if (this.turn == 0 &&
                            !this.calculatedTurnOrder) {
                        try {
                            if (!this.simulateMode) {
                                for (PixelmonWrapper currentPokemon : getActivePokemon()) {
                                    boolean hasEvolved = false;
                                    if (currentPokemon.priority < 6.0F) {
                                        for (PixelmonWrapper pw : getDefaultTurnOrder()) {
                                            if (pw.willEvolve) {
                                                hasEvolved = (pw.megaEvolve() || hasEvolved);
                                            }
                                        }
                                    } else if (currentPokemon.attack != null && currentPokemon.willEvolve) {

                                        hasEvolved = currentPokemon.megaEvolve();
                                    }
                                    if (hasEvolved) {
                                        return;
                                    }
                                }
                            }

                            CalcPriority.checkMoveSpeed(this);
                            this.calculatedTurnOrder = true;
                        } catch (Exception exc) {
                            this.battleLog.onCrash(exc, "Problem checking move speed.");
                        }
                    }

                    if (this.turn < this.turnList.size()) {
                        modifyStatsCancellable((PixelmonWrapper)this.turnList.get(this.turn));
                    }
                    for (BattleParticipant p : this.participants) {
                        for (PixelmonWrapper poke : p.controlledPokemon) {
                            if (poke.entity != null && !poke.entity.isLoaded(true)) {
                                poke.entity.retrieve();
                                if (getPlayers().isEmpty()) {
                                    endBattle(EnumBattleEndCause.FORCE);
                                    return;
                                }
                                poke.entity.world.unloadEntities(Lists.newArrayList(new Entity[] { poke.entity }));

                                poke.entity.releaseFromPokeball();
                                poke.entity.hurtResistantTime = 0;
                            }
                        }
                    }

                    boolean endTurn = false;
                    int numTurns = this.turnList.size();
                    if (this.turn < numTurns) {


                        this.participants.stream().filter(bp -> (bp.getType() == ParticipantType.Player)).forEach(bp ->
                                bp.wait = true);

                        PixelmonWrapper currentPokemon = (PixelmonWrapper)this.turnList.get(this.turn);
                        takeTurn(currentPokemon);


                        numTurns = this.turnList.size();
                        this.turn++;
                        if (this.turn >= numTurns) {
                            endTurn = true;
                            for (BattleParticipant p : this.participants) {
                                for (PixelmonWrapper pw : p.controlledPokemon) {
                                    if (pw.newPokemonUUID != null && pw.isFainted()) {
                                        takeTurn(pw); continue;
                                    }  if (pw.nextSwitchIsMove) {
                                        endTurn = false;
                                    }
                                }
                            }
                        }
                    } else {
                        endTurn = true;
                    }
                    this.calculatedTurnOrder = false;

                    if (endTurn) {
                        applyRepeatedEffects();
                        endTurn();
                    }
                    checkPokemon();
                    if (endTurn) {
                        checkFaint();
                    }
                }
                this.battleTicks = 0;
            }
        } catch (Exception e) {
            if (this.battleLog != null) {
                this.battleLog.onCrash(e, "Caught error in battle. Continuing...");
            } else if (PixelmonConfig.printErrors) {
                Pixelmon.LOGGER.info("Caught error in battle. Continuing...");
                e.printStackTrace();
            }
        }
    }

    public boolean isEvolving() {
        for (BattleParticipant p : this.participants) {
            for (PixelmonWrapper pw : p.controlledPokemon) {
                if (pw.evolution != null) {
                    if (pw.evolution.isEnded()) {
                        pw.evolution = null;
                        p.wait = false;
                        pw.wait = false;

                        String path = "pixelmon.battletext.";
                        if (pw.entity.getSpecies().equals(EnumSpecies.Greninja)) {
                            path = path + "ashgreninja.evolve";
                        } else {
                            path = path + "megaevolve";
                        }
                        sendToAll(path, new Object[] { pw.getNickname(),
                                Entity1Base.getLocalizedName(pw.getPokemonName()) });

                        pw.getBattleAbility().applySwitchInEffect(pw); continue;
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public void modifyStats() {
        for (BattleParticipant p : this.participants)
            p.controlledPokemon.forEach(this::modifyStats);
    }

    public void modifyStats(PixelmonWrapper pw) {
        int[] stats = pw.getBattleStats().getBaseBattleStats();
        for (int i = 0; i < pw.getStatusSize(); i++) {
            stats = pw.getStatus(i).modifyStats(pw, stats);
        }
        stats = pw.getBattleAbility().modifyStats(pw, stats);
        for (PixelmonWrapper teammate : getTeamPokemon(pw)) {
            stats = teammate.getBattleAbility().modifyStatsTeammate(pw, stats);
        }
        stats = pw.getUsableHeldItem().modifyStats(pw, stats);
        for (GlobalStatusBase gsb : this.globalStatusController.getGlobalStatuses()) {
            stats = gsb.modifyStats(pw, stats);
        }
        pw.getBattleStats().setStatsForTurn(stats);
    }

    public void modifyStatsCancellable(PixelmonWrapper attacker) {
        for (BattleParticipant p : this.participants) {
            for (PixelmonWrapper pw : p.controlledPokemon) {
                int[] stats = pw.getBattleStats().getBattleStats();
                if (!AbilityBase.ignoreAbility(attacker, pw) || !attacker.targets.contains(pw)) {
                    stats = pw.getBattleAbility().modifyStatsCancellable(pw, stats);
                    for (PixelmonWrapper teammate : getTeamPokemon(pw)) {
                        stats = teammate.getBattleAbility().modifyStatsCancellableTeammate(pw, stats);
                    }
                }
                for (int i = 0; i < pw.getStatusSize(); i++) {
                    StatusBase status = pw.getStatus(i);
                    if (!status.ignoreStatus(attacker, pw)) {
                        stats = status.modifyStatsCancellable(pw, stats);
                    }
                }
                pw.getBattleStats().setStatsForTurn(stats);
            }
        }
    }

    public void participantReady(PlayerParticipant p) { p.wait = false; }

    public void setAllReady() {
        for (PlayerParticipant player : getPlayers()) {
            participantReady(player);
        }
    }

    public void applyRepeatedEffects() throws Exception {
        boolean teamAble = false;
        boolean opponentAble = false;
        BattleParticipant participant = (BattleParticipant)this.participants.get(0);
        for (PixelmonWrapper pw : getTeamPokemon(participant)) {
            if (pw.isAlive()) {
                teamAble = true;
                break;
            }
        }
        if (!teamAble) {
            for (BattleParticipant p : getTeam((BattleParticipant)this.participants.get(0))) {
                if (p.countAblePokemon() > 0) {
                    teamAble = true;
                    break;
                }
            }
        }
        for (PixelmonWrapper pw : getOpponentPokemon(participant)) {
            if (pw.isAlive()) {
                opponentAble = true;
                break;
            }
        }
        if (!opponentAble) {
            for (BattleParticipant p : getOpponents((BattleParticipant)this.participants.get(0))) {
                if (p.countAblePokemon() > 0) {
                    opponentAble = true;
                    break;
                }
            }
        }
        if (teamAble && opponentAble) {
            for (GlobalStatusBase gsb : this.globalStatusController.getGlobalStatuses()) {
                gsb.applyRepeatedEffect(this.globalStatusController);
            }
            for (PixelmonWrapper pw : getDefaultTurnOrder()) {
                pw.turnTick();
            }
        }
    }

    public void endTurn() throws Exception {
        GoldenGlow.logger.info("EndTurn");
        this.stage = BattleStage.PICKACTION;

        for (BattleParticipant p : this.participants) {
            p.faintedLastTurn = false;
        }

        this.battleLog.turnTick();
        this.battleTurn++;
    }

    public void checkReviveSendOut(BattleParticipant p) {
        if (this.rules.battleType.numPokemon > 1 &&
                p != null && p.getType() == ParticipantType.Player && getTeam(p).size() == 1 && p.controlledPokemon
                .size() < this.rules.battleType.numPokemon && p
                .countAblePokemon() > p.controlledPokemon.size()) {
            PlayerParticipant player = (PlayerParticipant)p;
            List<StatusBase> wholeTeamStatuses = new ArrayList<StatusBase>();
            Iterator iterator = p.controlledPokemon.iterator(); if (iterator.hasNext()) { PixelmonWrapper pw = (PixelmonWrapper)iterator.next();
                wholeTeamStatuses = new ArrayList<StatusBase>();
                for (StatusBase status : pw.getStatuses()) {
                    if (status.isWholeTeamStatus()) {
                        wholeTeamStatuses.add(status.copy());
                    }
                }  }


            int oldPosition = 0;
            int newPosition = 0;
            do {
                oldPosition = newPosition;
                for (PixelmonWrapper pw : p.controlledPokemon) {
                    if (newPosition == pw.battlePosition) {
                        newPosition++;
                    }
                }
            } while (oldPosition != newPosition);
            PixelmonWrapper revived = null;
            for (PixelmonWrapper pw : player.allPokemon) {
                if (pw.isAlive() && pw.entity == null) {
                    EntityPixelmon revivedPokemon = p.getStorage().find(pw.getPokemonUUID()).getOrSpawnPixelmon(player.player);
                    revivedPokemon.setHealth(pw.getHealth());
                    pw.update(new EnumUpdateType[] { EnumUpdateType.HP });
                    revivedPokemon.battleController = this;
                    revived = pw;
                    revived.entity = revivedPokemon;
                    break;
                }
            }
            if (revived == null) {
                return;
            }
            revived.battlePosition = newPosition;

            for (StatusBase status : wholeTeamStatuses) {
                revived.addStatus(status, revived);
            }

            player.controlledPokemon.add(newPosition, revived);
            revived.bc = player.bc;

            PlayerPartyStorage playerPartyStorage = player.getStorage();
            if (playerPartyStorage == null) {
                endBattle(EnumBattleEndCause.FORCE);

                return;
            }
            if (playerPartyStorage.find(revived.getPokemonUUID()).getPixelmonIfExists() == null) {
                revived.entity.setLocationAndAngles(player.player.posX, player.player.posY, player.player.posZ, player.player.rotationYaw, 0.0F);

                revived.entity.releaseFromPokeball();
            }

            if (!AbilityBase.ignoreAbility(revived)) {
                revived.getBattleAbility().beforeSwitch(revived);
            }

            ChatHandler.sendBattleMessage(player.player, "playerparticipant.go", new Object[] { revived.getNickname() });
            sendToOthers("battlecontroller.sendout", player, new Object[] { player.player.getDisplayName().getUnformattedText(), revived
                    .getNickname() });
            for (BattleParticipant p2 : this.participants) {
                p2.updateOtherPokemon();
            }
            revived.afterSwitch();
            revived.addAttackers();
            sendSwitchPacket(null, revived);
        }
    }

    void checkFaint() throws Exception {
        List<PixelmonWrapper> fainted = new ArrayList<PixelmonWrapper>();
        for (BattleParticipant p : this.participants) {
            List<PixelmonWrapper> toRemove = new ArrayList<PixelmonWrapper>();
            for (PixelmonWrapper pw : p.controlledPokemon) {
                if (pw.isFainted() && !pw.isSwitching) {
                    if (pw.removePrimaryStatus() != null) {
                        pw.entity.sendStatusPacket(-1);
                    }
                    pw.update(new EnumUpdateType[] { EnumUpdateType.Status });
                    p.updatePokemon(pw);
                    if (p.addSwitchingOut(pw)) {
                        fainted.add(pw); continue;
                    }
                    toRemove.add(pw);
                }
            }

            for (PixelmonWrapper pw : toRemove) {
                pw.isSwitching = false;
                pw.wait = false;
                p.controlledPokemon.remove(pw);
                updateRemovedPokemon(pw);
            }
        }
        this.switchingOut.addAll(fainted);
        for (PixelmonWrapper pw : fainted) {
            BattleParticipant p = pw.getParticipant();
            p.faintedLastTurn = true;
            pw.willTryFlee = false;
            p.wait = true;
            p.getNextPokemon(pw.battlePosition);
        }
    }

    public void onUpdate() throws Exception {
        this.participants.forEach(BattleParticipant::tick);

        if (isPvP()) {
            this.participants.stream().filter(p ->
                    (((PlayerParticipant)p).player == null || !((PlayerParticipant)p).player.isEntityAlive()))
                    .forEach(p -> endBattle(EnumBattleEndCause.FORCE));
        } else {
            this.participants.stream().filter(p -> (p.getType() == ParticipantType.Player))
                    .filter(p -> (((PlayerParticipant)p).player == null || ((PlayerParticipant)p).player.isDead))
                    .forEach(p -> endBattle(EnumBattleEndCause.FORCE));
        }
    }

    public HashMap<BattleParticipant, BattleResults> endBattle(EnumBattleEndCause cause) { return endBattle(cause, new HashMap()); }

    public HashMap<BattleParticipant, BattleResults> endBattle(EnumBattleEndCause cause, HashMap<BattleParticipant, BattleResults> results) {
        if (results == null) {
            results = new HashMap<BattleParticipant, BattleResults>();
        }
        this.spectators.forEach(spectator -> spectator.sendMessage(new EndSpectate()));

        boolean abnormal = false;
        this.battleEnded = true;
        this.globalStatusController.endBattle();
        for (BattleParticipant p : this.participants) {
            for (PixelmonWrapper pw : p.controlledPokemon) {
                pw.getBattleAbility().applyEndOfBattleEffect(pw);
                pw.resetOnSwitch();
                for (StatusBase status : pw.getStatuses())
                    status.applyEndOfBattleEffect(pw);
                if (cause != EnumBattleEndCause.NORMAL) {
                    pw.setHeldItem(pw.getInitialHeldItem());
                }
            }
            if (cause == EnumBattleEndCause.FLEE) {
                results.put(p, BattleResults.FLEE);
                if (p instanceof PlayerParticipant) {
                    Pixelmon.EVENT_BUS.post(new PlayerBattleEndedEvent(((PlayerParticipant)p).player, this, BattleResults.FLEE));
                }
            }
            else if ((cause != EnumBattleEndCause.FORCE || PixelmonConfig.forceEndBattleResult == EnumForceBattleResult.WINNER) && this.init && cause != EnumBattleEndCause.FORFEIT) {


                BattleResults result = BattleResults.DRAW;


                int allyCount = 0;
                int opponentCount = 0;
                for (BattleParticipant teamp : getTeam(p))
                    allyCount += teamp.countAblePokemon();
                for (BattleParticipant enemyp : getOpponents(p)) {
                    opponentCount += enemyp.countAblePokemon();
                }
                if (allyCount > opponentCount) {
                    result = BattleResults.VICTORY;
                } else if (opponentCount > allyCount) {
                    result = BattleResults.DEFEAT;
                } else {
                    float allyPercent = 0.0F;
                    float opponentPercent = 0.0F;
                    int allyParticipants = 0;
                    int opponentParticipants = 0;
                    for (BattleParticipant teamp : getTeam(p)) {
                        allyPercent += teamp.countHealthPercent();
                        allyParticipants++;
                    }
                    for (BattleParticipant enemyp : getOpponents(p)) {
                        opponentPercent += enemyp.countHealthPercent();
                        opponentParticipants++;
                    }

                    if (allyParticipants == 0) {
                        allyParticipants = 1;
                    }
                    if (opponentParticipants == 0) {
                        opponentParticipants = 1;
                    }
                    allyPercent /= allyParticipants;
                    opponentPercent /= opponentParticipants;
                    if (allyPercent > opponentPercent) {
                        result = BattleResults.VICTORY;
                    } else if (opponentPercent > allyPercent) {
                        result = BattleResults.DEFEAT;
                    } else {
                        result = BattleResults.DRAW;
                    }
                }  results.put(p, result);
            }
            else if (cause == EnumBattleEndCause.FORCE) {
                if (PixelmonConfig.forceEndBattleResult == EnumForceBattleResult.DRAW) {
                    results.put(p, BattleResults.DRAW);
                } else {
                    if (PixelmonConfig.forceEndBattleResult == EnumForceBattleResult.ABNORMAL) {
                        if (p instanceof PlayerParticipant)
                            Pixelmon.EVENT_BUS.post(new PlayerBattleEndedAbnormalEvent(((PlayerParticipant)p).player, this));
                        abnormal = true;
                    }
                    results.put(p, BattleResults.DRAW);
                }
            }

            p.endBattle();

            if (p instanceof PlayerParticipant) {
                Pixelmon.EVENT_BUS.post(new PlayerBattleEndedEvent(((PlayerParticipant)p).player, this, (BattleResults)results.get(p)));
                if (results.get(p) == BattleResults.VICTORY && cause == EnumBattleEndCause.NORMAL)
                    Pickup.pickupItem((PlayerParticipant)p);  continue;
            }
            if (p instanceof WildPixelmonParticipant) {
                resetAggression(p);
            }
        }
        if (this.playerNumber == 0) {
            BattleRegistry.deRegisterBattle(this);
        }
        BattleEndEvent event = new BattleEndEvent(this, cause, abnormal, results);
        Pixelmon.EVENT_BUS.post(event);
        checkEvolution();
        return results;
    }

    @Deprecated
    public void endBattleWithoutXP(HashMap<BattleParticipant, BattleResults> results) { endBattle(EnumBattleEndCause.FORCE, results); }

    public void endBattle() { endBattle(EnumBattleEndCause.NORMAL); }

    public void endBattleWithoutXP() { endBattle(EnumBattleEndCause.FORCE); }

    public void checkEvolution() {
        for (Pokemon pokemon : this.checkedPokemon) {
            pokemon.tryEvolution();
        }
    }

    public void resetAggression(BattleParticipant p) {
        if (p.getType() == ParticipantType.WildPokemon) {
            EntityLivingBase entity = p.getEntity();
            if (entity != null && !entity.isDead) {
                ((EntityPixelmon)entity).aggressionTimer = RandomHelper.getRandomNumberBetween(400, 1000);
            }
        }
    }

    public boolean isPvP() {
        for (BattleParticipant p : this.participants) {
            if (!(p instanceof PlayerParticipant)) {
                return false;
            }
        }
        return true;
    }

    boolean paused = false;

    public void pauseBattle() { this.paused = true; }

    public boolean isWaiting() {
        for (BattleParticipant p : this.participants) {
            if (p.getWait()) {
                return true;
            }
        }
        return false;
    }

    public void endPause() { this.paused = false; }

    public void takeTurn(PixelmonWrapper p) {
        if (tryFlee(p)) {
            return;
        }
        for (BattleParticipant part : this.participants) {
            if (part.bc == null) {
                endBattle();

                return;
            }
        }
        p.takeTurn();
    }

    public boolean tryFlee(PixelmonWrapper p) {
        for (int i = 0; i < this.turn; i++) {
            PixelmonWrapper current = (PixelmonWrapper)this.turnList.get(i);
            if (current.willTryFlee && current.getParticipant() == p.getParticipant()) {
                return false;
            }
        }
        if (p.willTryFlee) {
            forfeitOrFlee(p);
            p.priority = 6.0F;
            return true;
        }
        return false;
    }

    public void forfeitOrFlee(PixelmonWrapper p) {
        boolean isForfeit = false;
        for (BattleParticipant participant : getOpponents(p.getParticipant())) {
            if (participant.getType() != ParticipantType.WildPokemon) {
                isForfeit = true;
                break;
            }
        }
        if (isForfeit) {
            forfeitBattle(p);
        } else {
            calculateEscape(p);
        }
    }

    public void calculateEscape(PixelmonWrapper pw) {
        PixelmonWrapper opponentPixelmon = (PixelmonWrapper)pw.bc.getOpponentPokemon(pw.getParticipant()).get(0);

        double fleeingSpeed = (pw.getStats()).speed;
        double opponentSpeed = (opponentPixelmon.getStats()).speed;

        double escapeAttempts = ++pw.escapeAttempts;
        double calculatedFleeValue = fleeingSpeed * 128.0D / opponentSpeed + 30.0D * escapeAttempts;

        int random = RandomHelper.getRandomNumberBetween(1, 255);

        if (pw.getBattleAbility() instanceof com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RunAway) {
            sendToAll("pixelmon.abilities.runaway", new Object[] { pw.getNickname() });
            endBattle(EnumBattleEndCause.FLEE);
        } else if (pw.getHeldItem().getHeldItemType() == EnumHeldItems.smokeBall || pw.hasType(new EnumType[] { EnumType.Ghost })) {
            sendToAll("battlecontroller.escaped", new Object[] { pw.getNickname() });
            endBattle(EnumBattleEndCause.FLEE);
        } else if (calculatedFleeValue > 255.0D || random < calculatedFleeValue) {
            sendToAll("battlecontroller.escaped", new Object[] { pw.getNickname() });
            BattleParticipant fleeingParticipant = pw.getParticipant();
            if (fleeingParticipant != null) {
                for (PixelmonWrapper pw2 : fleeingParticipant.controlledPokemon) {
                    pw.bc.battleLog.addEvent(new FleeAction(pw.bc.battleTurn, pw.bc
                            .getPositionOfPokemon(pw2), pw));
                }
            }
            endBattle(EnumBattleEndCause.FLEE);
        } else {
            sendToAll("battlecontroller.!escaped", new Object[] { pw.getNickname() });
            BattleParticipant fleeingParticipant = pw.getParticipant();
            for (PixelmonWrapper pw2 : fleeingParticipant.controlledPokemon) {
                pw.bc.battleLog.addEvent(new FleeAction(pw.bc.battleTurn, pw.bc
                        .getPositionOfPokemon(pw2), pw));
            }
        }
    }

    public void forfeitBattle(PixelmonWrapper pw) {
        if (this.rules.hasClause("forfeit")) {
            return;
        }

        BattleParticipant forfeitParticipant = pw.getParticipant();
        boolean tie = false;
        ArrayList<BattleParticipant> opponents = getOpponents(forfeitParticipant);
        for (BattleParticipant opponent : opponents) {
            for (PixelmonWrapper opponentPokemon : opponent.controlledPokemon) {
                if (opponentPokemon.willTryFlee) {
                    tie = true;
                }
            }
        }

        if (tie) {
            sendToAll("battlecontroller.draw", new Object[0]);
        } else if (forfeitParticipant.getType() == ParticipantType.Player) {
            PlayerParticipant forfeitPlayer = (PlayerParticipant)forfeitParticipant;
            ChatHandler.sendBattleMessage(forfeitPlayer.player, "battlecontroller.forfeitself", new Object[0]);
            sendToOthers("battlecontroller.forfeit", forfeitPlayer, new Object[] { forfeitPlayer.getDisplayName() });
        }
        HashMap<BattleParticipant, BattleResults> results = new HashMap<BattleParticipant, BattleResults>();
        for (BattleParticipant participant : this.participants) {
            BattleResults result;
            if (tie) {
                result = BattleResults.DRAW;
            } else if (opponents.contains(participant)) {
                result = BattleResults.VICTORY;
            } else {
                result = BattleResults.DEFEAT;
            }
            results.put(participant, result);
        }
        endBattle(EnumBattleEndCause.FORFEIT, results);
    }

    public void checkPokemon() {
        boolean cameraSwitched = false;
        for (BattleParticipant p : this.participants) {
            p.resetMoveTimer();
            if (!this.battleEnded && !p.isDefeated) {
                checkReviveSendOut(p);
                List<PixelmonWrapper> faintedPokemon = new ArrayList<PixelmonWrapper>();
                for (PixelmonWrapper poke : p.controlledPokemon) {
                    if (poke.isFainted()) {
                        if (p.getType() == ParticipantType.Player) {
                            Pixelmon.network.sendTo(new SwitchCamera(), (EntityPlayerMP)p.getEntity());
                            cameraSwitched = true;
                        }



                        if (poke.newPokemonUUID == null && this.turnList.contains(poke) && this.turn <= this.turnList
                                .indexOf(poke)) {
                            this.turnList.remove(poke);
                        }
                        if (!poke.hasAwardedExp) {
                            Experience.awardExp(this.participants, p, poke);
                            poke.hasAwardedExp = true;

                            if (p instanceof WildPixelmonParticipant && this.participants.size() == 2) {
                                this.participants.stream()
                                        .filter(part -> (part != p && part instanceof PlayerParticipant))
                                        .forEach(part -> {
                                            (Pixelmon.storageManager.getParty(((PlayerParticipant)part).player)).stats.addKill();
                                            Pixelmon.EVENT_BUS.post(new BeatWildPixelmonEvent(((PlayerParticipant)part).player, (WildPixelmonParticipant)p));

                                            ((PlayerParticipant)part).checkPlayerItems();
                                        });
                            }
                        }
                        poke.entity.setHealth(0.0F);
                        poke.entity.setDead();
                        poke.entity.retrieve();
                        p.updatePokemon(poke);
                        if (!p.hasMorePokemonReserve()) {
                            faintedPokemon.add(poke);
                            updateRemovedPokemon(poke);
                        }
                    }
                }
                for (PixelmonWrapper pw : faintedPokemon) {
                    if (!this.battleEnded) {
                        checkDefeated(p, pw);
                    }
                    if (!this.battleEnded) {
                        p.controlledPokemon.remove(pw);
                        pw.entity = null;
                    }
                }
            }
        }
        if (cameraSwitched) {
            this.spectators.forEach(spectator -> spectator.sendMessage(new SwitchCamera()));
        }
    }

    public void updateRemovedPokemon(PixelmonWrapper poke) {
        UUID uuid = poke.getPokemonUUID();
        this.participants.stream().filter(p2 -> p2 instanceof PlayerParticipant)
                .forEach(p2 -> Pixelmon.network.sendTo(new SwitchOutPacket(uuid), ((PlayerParticipant)p2).player));
        this.spectators.forEach(spectator -> spectator.sendMessage(new SwitchOutPacket(uuid)));
    }

    public boolean isOneAlive(List<PixelmonWrapper> teamPokemon) {
        for (PixelmonWrapper pw : teamPokemon) {
            if (pw.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public void checkDefeated(BattleParticipant p, PixelmonWrapper poke) {
        if (isOneAlive(p.controlledPokemon)) {
            p.isDefeated = false;
        } else if (p.countAblePokemon() == 0 &&
                !p.isDefeated) {
            p.isDefeated = true;
            ChatHandler.sendBattleMessage(p.getEntity(), "battlecontroller.outofpokemon", new Object[0]);
            if (!isTeamDefeated(p)) {
                return;
            }
            this.participants.stream().filter(p2 -> getOpponents(p).contains(p2))
                    .forEach(p2 -> ChatHandler.sendBattleMessage(p2.getEntity(), "battlecontroller.win", new Object[0]));
            endBattle();
        }
    }

    public void sendToAll(String string, Object... data) {
        if (canSendMessages()) {
            ChatHandler.sendBattleMessage(this.participants, string, data);
        }
    }

    public void sendToAll(TextComponentTranslation message) {
        if (canSendMessages()) {
            ChatHandler.sendBattleMessage(this.participants, message);
        }
    }

    public void sendToOthers(String string, BattleParticipant battleParticipant, Object... data) {
        if (canSendMessages()) {
            this.participants.stream().filter(p -> (p != battleParticipant))
                    .forEach(p -> ChatHandler.sendBattleMessage(p.getEntity(), string, data));
            this.spectators.forEach(spectator -> ChatHandler.sendBattleMessage(spectator.getEntity(), string, data));
        }
    }

    public void sendToPlayer(EntityPlayer player, String string, Object... data) {
        if (canSendMessages()) {
            ChatHandler.sendBattleMessage(player, string, data);
        }
    }

    public void sendToPlayer(EntityPlayer player, TextComponentTranslation message) {
        if (canSendMessages()) {
            ChatHandler.sendBattleMessage(player, message);
        }
    }

    public boolean canSendMessages() { return (!this.simulateMode && this.sendMessages); }

    public void clearHurtTimer() {
        for (BattleParticipant part : this.participants) {
            for (PixelmonWrapper pokemon : part.controlledPokemon) {
                pokemon.entity.hurtTime = 0;
            }
        }
    }

    public void setUseItem(UUID pokemonUUID, EntityPlayer user, ItemStack usedStack, int additionalInfo) {
        BattleParticipant participant = (BattleParticipant)CollectionHelper.find(this.participants, bp -> (bp.getType() == ParticipantType.Player && bp.getEntity() == user));
        PixelmonWrapper userWrapper = participant.getPokemonFromUUID(pokemonUUID);
        userWrapper.willUseItemInStack = usedStack;
        userWrapper.willUseItemInStackInfo = additionalInfo;
        userWrapper.wait = false;
        if (usedStack.getItem() instanceof com.pixelmonmod.pixelmon.items.ItemPokeball) {
            for (PixelmonWrapper pw : participant.controlledPokemon) {
                if (pw != userWrapper) {
                    pw.wait = false;
                    pw.attack = null;
                }
            }
        }
    }

    public void setUseItem(UUID pokemonUUID, EntityPlayer player, ItemStack usedStack, UUID targetPokemonUUID) {
        for (BattleParticipant p : this.participants) {
            if (p.getType() == ParticipantType.Player && p.getEntity() == player) {
                PixelmonWrapper pw = p.getPokemonFromUUID(pokemonUUID);
                if (pw != null) {
                    pw.willUseItemInStack = usedStack;
                    pw.willUseItemPokemon = targetPokemonUUID;
                    pw.willUseItemInStackInfo = -1;
                    pw.wait = false;
                }
            }
        }
    }

    public void switchPokemon(UUID switchingPokemonUUID, UUID newPokemonUUID, boolean switchInstantly) {
        PixelmonWrapper pw = getPokemonFromUUID(switchingPokemonUUID);
        if (pw == null) {
            return;
        }
        BattleParticipant p = pw.getParticipant();
        pw.isSwitching = true;
        pw.newPokemonUUID = newPokemonUUID;
        boolean stopWait = true;
        boolean checkFaint = false;
        if (pw.isFainted()) {
            p.switchingIn.add(newPokemonUUID);
            if (!this.switchingOut.isEmpty()) {
                this.switchingOut.remove(pw);
                if (this.switchingOut.isEmpty()) {
                    for (BattleParticipant participant : this.participants) {
                        participant.switchAllFainted();
                        participant.wait = false;
                    }
                    checkFaint = true;
                } else {
                    stopWait = false;
                }
            }
        } else if (switchInstantly) {
            pw.doSwitch();
        }
        if (checkFaint) {
            checkPokemon();
            try {
                checkFaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (stopWait) {
            pw.wait = false;
            p.wait = false;
        }
    }

    public void setFlee(UUID fleeingUUID) {
        for (BattleParticipant p : this.participants) {
            PixelmonWrapper pw = p.getPokemonFromUUID(fleeingUUID);
            if (pw != null) {
                if (pw.isFainted() && p.getType() == ParticipantType.Player) {
                    forfeitOrFlee(pw);
                    if (!this.battleEnded)
                        Pixelmon.network.sendTo(new FailFleeSwitch(), ((PlayerParticipant)p).player);
                    continue;
                }
                p.wait = false;
                for (PixelmonWrapper pw2 : p.controlledPokemon) {
                    pw2.willTryFlee = true;
                    pw2.wait = false;
                }
            }
        }
    }

    public ParticipantType[][] getBattleType(BattleParticipant teammate) {
        ParticipantType[][] type = new ParticipantType[2][];
        ArrayList<ParticipantType> team1 = new ArrayList<ParticipantType>();
        ArrayList<ParticipantType> team2 = new ArrayList<ParticipantType>();
        for (BattleParticipant p : this.participants) {
            if (p.team == teammate.team) {
                team1.add(p.getType()); continue;
            }
            team2.add(p.getType());
        }

        type[0] = new ParticipantType[team1.size()];
        for (int i = 0; i < team1.size(); i++) {
            type[0][i] = (ParticipantType)team1.get(i);
        }
        type[1] = new ParticipantType[team2.size()];
        for (int i = 0; i < team2.size(); i++) {
            type[1][i] = (ParticipantType)team2.get(i);
        }
        return type;
    }

    public void updatePokemonHealth(EntityPixelmon entityPixelmon) {
        if (this.init) {
            this.participants.stream().filter(p -> p instanceof PlayerParticipant)
                    .forEach(p -> ((PlayerParticipant)p).updatePokemonHealth(entityPixelmon));
        }
    }

    public ArrayList<BattleParticipant> getOpponents(BattleParticipant participant) {
        return (ArrayList)this.participants.stream().filter(p -> (p.team != participant.team))
                .collect(Collectors.toList());
    }

    public ArrayList<BattleParticipant> getTeam(BattleParticipant participant) {
        if (participant == null) {
            return new ArrayList();
        }
        return (ArrayList)this.participants.stream().filter(p -> (p.team == participant.team))
                .collect(Collectors.toList());
    }

    public ArrayList<PixelmonWrapper> getActivePokemon() {
        ArrayList<PixelmonWrapper> activePokemon = new ArrayList<PixelmonWrapper>();
        for (BattleParticipant p : this.participants) {
            activePokemon.addAll(p.controlledPokemon);
        }
        return activePokemon;
    }

    public ArrayList<PixelmonWrapper> getActiveUnfaintedPokemon() {
        return (ArrayList)getActivePokemon().stream().filter(pw -> !pw.isFainted())
                .collect(Collectors.toList());
    }

    public ArrayList<PixelmonWrapper> getActiveFaintedPokemon() {
        return (ArrayList)getActivePokemon().stream().filter(pw -> pw.isFainted())
                .collect(Collectors.toList());
    }

    public ArrayList<PixelmonWrapper> getAdjacentPokemon(PixelmonWrapper pokemon) {
        return (ArrayList)getActiveUnfaintedPokemon().stream()
                .filter(pw -> (Math.abs(pw.battlePosition - pokemon.battlePosition) <= 1 && pw != pokemon))
                .collect(Collectors.toList());
    }

    public ArrayList<PixelmonWrapper> getTeamPokemon(BattleParticipant participant) {
        ArrayList<BattleParticipant> team = getTeam(participant);
        ArrayList<PixelmonWrapper> teamPokemon = new ArrayList<PixelmonWrapper>();
        for (BattleParticipant p : team) {
            for (PixelmonWrapper pw : p.controlledPokemon) {
                if (pw != null) {
                    teamPokemon.add(pw);
                }
            }
        }
        return teamPokemon;
    }

    public ArrayList<PixelmonWrapper> getTeamPokemon(PixelmonWrapper pokemon) { return getTeamPokemon(pokemon.getParticipant()); }

    public ArrayList<PixelmonWrapper> getTeamPokemon(EntityPixelmon pokemon) { return getTeamPokemon(pokemon.getParticipant()); }

    public ArrayList<PixelmonWrapper> getTeamPokemonExcludeSelf(PixelmonWrapper pokemon) {
        ArrayList<PixelmonWrapper> teamPokemon = getTeamPokemon(pokemon);
        teamPokemon.remove(pokemon);
        return teamPokemon;
    }

    public ArrayList<PixelmonWrapper> getOpponentPokemon(BattleParticipant participant) {
        ArrayList<BattleParticipant> opponents = getOpponents(participant);
        ArrayList<PixelmonWrapper> opponentPokemon = new ArrayList<PixelmonWrapper>();
        for (BattleParticipant p : opponents) {
            for (PixelmonWrapper pw : p.controlledPokemon) {
                opponentPokemon.add(pw);
            }
        }
        return opponentPokemon;
    }

    public ArrayList<PixelmonWrapper> getOpponentPokemon(PixelmonWrapper pw) { return getOpponentPokemon(pw.getParticipant()); }

    public boolean isInBattle(PixelmonWrapper pokemon) {
        for (PixelmonWrapper pw : getActivePokemon()) {
            if (pw == pokemon) {
                return true;
            }
        }
        return false;
    }

    public BattleParticipant getParticipantForEntity(EntityLivingBase entity) {
        for (BattleParticipant p : this.participants) {
            if (p.getEntity() == entity) {
                return p;
            }
        }
        return null;
    }

    public void sendDamagePacket(EntityPixelmon target, int damage) {
        for (BattleParticipant p : this.participants) {
            p.sendDamagePacket(target, damage);
        }
        this.spectators.forEach(spectator -> spectator.sendDamagePacket(target, damage));
    }

    public void sendHealPacket(EntityPixelmon target, int amount) {
        for (BattleParticipant p : this.participants) {
            p.sendHealPacket(target, amount);
        }
        this.spectators.forEach(spectator -> spectator.sendHealPacket(target, amount));
    }

    public PixelmonWrapper getOppositePokemon(PixelmonWrapper pw) {
        ArrayList<PixelmonWrapper> oppPokemon = pw.bc.getOpponentPokemon(pw.getParticipant());
        ArrayList<PixelmonWrapper> teamPokemon = pw.bc.getTeamPokemon(pw.getParticipant());
        int index = teamPokemon.indexOf(pw);
        while (index >= oppPokemon.size()) {
            index--;
        }
        return (PixelmonWrapper)oppPokemon.get(index);
    }

    public PixelmonWrapper getPokemonAtPosition(int position) {
        ArrayList<PixelmonWrapper> arr = new ArrayList<PixelmonWrapper>();
        for (BattleParticipant bp : this.participants) {
            arr.addAll(bp.controlledPokemon);
        }
        if (position >= arr.size()) {
            return null;
        }
        return (PixelmonWrapper)arr.get(position);
    }

    public int getPositionOfPokemon(PixelmonWrapper poke) {
        ArrayList<PixelmonWrapper> arr = new ArrayList<PixelmonWrapper>();
        for (BattleParticipant bp : this.participants) {
            arr.addAll(bp.controlledPokemon);
        }
        return arr.indexOf(poke);
    }

    public int getPositionOfPokemon(PixelmonWrapper poke, BattleParticipant bp) { return bp.controlledPokemon.indexOf(poke); }

    public BattleStage getStage() { return this.stage; }

    public void enableReturnHeldItems(PixelmonWrapper attacker, PixelmonWrapper target) {
        BattleParticipant targetParticipant = target.getParticipant();
        if (!this.simulateMode && !(targetParticipant instanceof WildPixelmonParticipant)) {
            target.enableReturnHeldItem();
            attacker.enableReturnHeldItem();
        }
    }

    public boolean checkValid() {
        if (this.battleEnded) {
            return false;
        }
        ArrayList<PixelmonWrapper> pokemon = new ArrayList<PixelmonWrapper>();
        ArrayList<PixelmonWrapper> activePokemon = getActivePokemon();
        if (activePokemon.size() <= 1) {
            return false;
        }

        for (PixelmonWrapper pw : activePokemon) {
            if (pokemon.contains(pw)) {
                endBattle(EnumBattleEndCause.FORCE);
                return false;
            }
            pokemon.add(pw);
        }
        return true;
    }

    public PlayerParticipant getPlayer(String name) {
        for (BattleParticipant p : this.participants) {
            if (p.getType() == ParticipantType.Player) {
                PlayerParticipant player = (PlayerParticipant)p;
                if (player.player.getDisplayNameString().equals(name)) {
                    return player;
                }
            }
        }
        return null;
    }

    public PlayerParticipant getPlayer(EntityPlayer player) {
        for (BattleParticipant p : this.participants) {
            if (p.getType() == ParticipantType.Player) {
                PlayerParticipant currentPlayer = (PlayerParticipant)p;
                if (player == currentPlayer.player) {
                    return currentPlayer;
                }
            }
        }
        return null;
    }

    public List<PlayerParticipant> getPlayers() {
        List<PlayerParticipant> players = new ArrayList<PlayerParticipant>(this.participants.size());
        for (BattleParticipant p : this.participants) {
            if (p.getType() == ParticipantType.Player) {
                players.add((PlayerParticipant)p);
            }
        }
        return players;
    }

    public boolean isTeamDefeated(BattleParticipant participant) {
        for (BattleParticipant p2 : getTeam(participant)) {
            if (!p2.isDefeated) {
                return false;
            }
        }
        return true;
    }

    public int getTurnForPokemon(PixelmonWrapper pokemon) {
        for (int i = 0; i < this.turnList.size(); i++) {
            if (this.turnList.get(i) == pokemon) {
                return i;
            }
        }
        return -1;
    }

    public BattleParticipant otherParticipant(BattleParticipant participant) {
        for (BattleParticipant p : this.participants) {
            if (p != participant) {
                return p;
            }
        }
        return null;
    }

    public PixelmonWrapper getFirstMover(PixelmonWrapper... pokemonList) {
        for (PixelmonWrapper turnPokemon : this.turnList) {
            for (PixelmonWrapper pokemon : pokemonList) {
                if (turnPokemon == pokemon) {
                    return pokemon;
                }
            }
        }
        return null;
    }

    public PixelmonWrapper getFirstMover(ArrayList<PixelmonWrapper> pokemonList) {
        for (PixelmonWrapper turnPokemon : this.turnList) {
            for (PixelmonWrapper pokemon : pokemonList) {
                if (turnPokemon == pokemon) {
                    return pokemon;
                }
            }
        }
        return null;
    }

    public PixelmonWrapper getLastMover(PixelmonWrapper... pokemonList) {
        for (int i = this.turnList.size() - 1; i >= 0; i--) {
            PixelmonWrapper turnPokemon = (PixelmonWrapper)this.turnList.get(i);
            for (PixelmonWrapper pokemon : pokemonList) {
                if (turnPokemon == pokemon) {
                    return pokemon;
                }
            }
        }
        return null;
    }

    public PixelmonWrapper getLastMover(ArrayList<PixelmonWrapper> pokemonList) {
        for (int i = this.turnList.size() - 1; i >= 0; i--) {
            PixelmonWrapper turnPokemon = (PixelmonWrapper)this.turnList.get(i);
            for (PixelmonWrapper pokemon : pokemonList) {
                if (turnPokemon == pokemon) {
                    return pokemon;
                }
            }
        }
        return null;
    }

    public void sendSwitchPacket(UUID oldUUID, PixelmonWrapper newPokemon) {
        for (BattleParticipant participant : this.participants) {
            if (participant instanceof PlayerParticipant) {
                Pixelmon.network.sendTo(new SwitchOutPacket(oldUUID, newPokemon), ((PlayerParticipant)participant).player);
            }
        }

        this.spectators.forEach(spectator -> spectator.sendMessage(new SwitchOutPacket(oldUUID, newPokemon)));
    }

    public void addSpectator(Spectator spectator) {
        this.spectators.add(spectator);
        BattleRegistry.registerSpectator(spectator, this);
    }

    public void removeSpectator(Spectator spectator) {
        this.spectators.remove(spectator);
        BattleRegistry.unregisterSpectator(spectator);
        Pixelmon.EVENT_BUS.post(new SpectateEvent.StopSpectate(spectator.getEntity(), this));
    }

    public boolean hasSpectator(EntityPlayer player) {
        for (Spectator spectator : this.spectators) {
            if (spectator.getEntity() == player) {
                return true;
            }
        }
        return false;
    }

    public boolean removeSpectator(EntityPlayerMP player) {
        for (int i = 0; i < this.spectators.size(); i++) {
            Spectator spectator = (Spectator)this.spectators.get(i);
            if (spectator.getEntity() == player) {
                this.spectators.remove(i);
                BattleRegistry.unregisterSpectator(spectator);
                Pixelmon.EVENT_BUS.post(new SpectateEvent.StopSpectate(player, this));
                return true;
            }
        }
        return false;
    }

    public ArrayList<Spectator> getPlayerSpectators(PlayerParticipant player) {
        ArrayList<Spectator> playerSpectators = new ArrayList<Spectator>(this.spectators.size());
        String playerName = player.player.getDisplayNameString();
        playerSpectators.addAll((Collection)this.spectators.stream().filter(spectator -> spectator.watchedName.equals(playerName))
                .collect(Collectors.toList()));
        return playerSpectators;
    }

    public PixelmonWrapper getPokemonFromUUID(UUID uuid) {
        PixelmonWrapper pw = null;
        for (BattleParticipant p : this.participants) {
            pw = p.getPokemonFromUUID(uuid);
            if (pw != null) {
                break;
            }
        }
        return pw;
    }

    public List<PixelmonWrapper> getDefaultTurnOrder() { return getDefaultTurnOrder(this); }

    static List<PixelmonWrapper> getDefaultTurnOrder(BattleControllerBase bc) {
        ArrayList<PixelmonWrapper> turnOrder = new ArrayList<PixelmonWrapper>(6);
        for (BattleParticipant p : bc.participants) {
            turnOrder.addAll(p.controlledPokemon);
        }
        turnOrder.sort(speedComparator);
        return turnOrder;
    }

    public void removeFromTurnList(PixelmonWrapper pw) {
        for (int i = this.turn + 1; i < this.turnList.size(); i++) {
            if (pw.equals(this.turnList.get(i))) {
                this.turnList.remove(i);
                return;
            }
        }
    }

    public boolean isLastMover() { return (this.turn >= this.turnList.size() - 1); }

    public boolean containsParticipantType(Class<? extends BattleParticipant> participantType) {
        for (BattleParticipant bp : this.participants) {
            if (bp.getClass() == participantType)
                return true;
        }  return false;
    }

    public void updateFormChange(EntityPixelmon pokemon) {
        this.participants.stream().filter(participant -> (participant.getType() == ParticipantType.Player))
                .forEach(participant -> Pixelmon.network.sendTo(new FormBattleUpdate(pokemon
                        .getPokemonData().getUUID(), pokemon.getFormIncludeTransformed()), ((PlayerParticipant)participant).player));

        this.spectators.forEach(spectator -> spectator
                .sendMessage(new FormBattleUpdate(pokemon.getPokemonData().getUUID(), pokemon.getFormIncludeTransformed())));
    }

    public boolean isLevelingDisabled() {
        if (!PixelmonConfig.allowPVPExperience) {
            boolean allPlayers = true;
            for (BattleParticipant p : this.participants) {
                if (p.getType() != ParticipantType.Player) {
                    allPlayers = false;
                }
            }
            if (allPlayers)
            {

                return true;
            }
        }
        if (!PixelmonConfig.allowTrainerExperience) {
            for (BattleParticipant p : this.participants) {
                if (p.getType() == ParticipantType.Trainer) {
                    return true;
                }
            }
        }
        if (this.rules.raiseToCap) {
            return true;
        }
        return false;
    }
}
