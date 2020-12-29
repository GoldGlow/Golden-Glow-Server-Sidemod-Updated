package com.goldenglow.common.guis.essentials;

import com.goldenglow.common.battles.npc.OOTrainerData;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionNpcGuiData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.guis.TrainerDataGui;
import com.pixelmonessentials.common.util.NpcScriptDataManipulator;
import com.pixelmonessentials.common.util.ReflectionHelper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

public class OOTrainerGui extends TrainerDataGui {
    private String category;
    private String rules;
    private String team;
    EntityNPCInterface npc;
    private boolean losBattle;

    public OOTrainerGui(){
        super();
        this.category="";
        this.rules="default";
        this.team="";
    }

    @Override
    public void saveTeam(String category, String team){
        this.category=category;
        this.team=team;
    }

    @Override
    public void setNpc(EntityNPCInterface npc){
        this.npc=npc;
    }

    @Override
    public void setRules(String rules){
        this.rules=rules;
    }

    @Override
    public void init(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=this.getGui();
        this.addButton(new EssentialsButton(500, new ActionNpcGuiData("SAVE_TRAINER", this.npc, 1001)));
        this.addButton(new EssentialsButton(501, new ActionData("CLOSE_GUI")));
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    @Override
    public CustomGuiWrapper getGui(){
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 256, 256, false);
        this.losBattle=true;
        ScriptObjectMirror object = NpcScriptDataManipulator.getJavascriptObject(new NPCWrapper(this.npc), "ooTrainerData");
        Object dialogObject = NpcScriptDataManipulator.getJavascriptVariable(new NPCWrapper(this.npc), "losDialog");
        int dialog = 0;
        if (object != null) {
            int initDialog = (Integer)object.get("initDialog");
            if (dialogObject != null) {
                dialog = (Integer)dialogObject;
                if(initDialog != dialog && dialog != 0){
                    this.flipLos();
                }
            } else {
                this.flipLos();
            }
            int winDialog = (Integer)object.get("winDialog");
            int lossDialog = (Integer)object.get("lossDialog");
            String team = (String)object.get("team");
            String category = (String)object.get("category");
            String rules = (String)object.get("rules");
            String initTheme=(String)object.get("initTheme");
            String battleTheme=(String)object.get("battleTheme");
            String winTheme=(String)object.get("winTheme");

            if(initTheme!=null){
                gui.addTextField(405, 110, 145, 100, 20).setText(initTheme);
            }
            else{
                gui.addTextField(405, 110, 145, 100, 20);
            }

            if(battleTheme!=null){
                gui.addTextField(406, 110, 170, 100, 20).setText(battleTheme);
            }
            else{
                gui.addTextField(406, 110, 170, 100, 20);
            }

            if(winTheme!=null){
                gui.addTextField(407, 110, 195, 100, 20).setText(winTheme);
            }
            else{
                gui.addTextField(407, 110, 195, 100, 20);
            }

            if (rules != null && this.rules.equals("default")) {
                this.rules = rules;
            }

            if (team != null && this.team.equals("")) {
                this.team = team;
            }

            if (category != null && this.category.equals("")) {
                this.category = category;
            }

            if (initDialog != 0) {
                gui.addTextField(401, 90, 40, 30, 20).setText(initDialog + "");
            } else {
                gui.addTextField(401, 90, 40, 30, 20);
            }

            if (winDialog != 0) {
                gui.addTextField(402, 90, 70, 30, 20).setText(winDialog + "");
            } else {
                gui.addTextField(402, 90, 70, 30, 20);
            }

            if (lossDialog != 0) {
                gui.addTextField(403, 90, 100, 30, 20).setText(lossDialog + "");
            } else {
                gui.addTextField(403, 90, 100, 30, 20);
            }
        } else {
            gui.addTextField(401, 90, 40, 30, 20);
            gui.addTextField(402, 90, 70, 30, 20);
            gui.addTextField(403, 90, 100, 30, 20);
            gui.addTextField(405, 110, 145, 100, 20);
            gui.addTextField(406, 110, 170, 100, 20);
            gui.addTextField(407, 110, 195, 100, 20);
        }
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(200, "Trainer Data", 90, 10, 200, 20);
        gui.addLabel(201, "Init Dialog ID", 10, 40, 200, 20);
        gui.addLabel(202, "Win Dialog ID", 10, 70, 200, 20);
        gui.addLabel(203, "Loss Dialog ID", 10, 100, 200, 20);
        gui.addLabel(204, "Team:", 130, 50, 100, 20);
        gui.addLabel(241, this.team, 160, 50, 100, 20);
        gui.addButton(502, "Select", 130, 70, 50, 20);
        gui.addLabel(205, "Rules:", 130, 95, 100, 20);
        gui.addLabel(251, this.rules, 162, 95, 100, 20);
        gui.addButton(503, "Select", 130, 115, 40, 20);
        gui.addButton(504, "Clear", 175, 115, 34, 20);
        gui.addLabel(206, "Battle on sight:", 130, 30, 100, 20);
        if (this.losBattle) {
            gui.addButton(505, "Yes", 210, 30, 30, 20);
        } else {
            gui.addButton(505, "No", 210, 30, 30, 20);
        }
        gui.addLabel(210, "Encounter Theme", 10, 145, 200, 20);
        gui.addLabel(211, "Battle Theme", 10, 170, 200, 20);
        gui.addLabel(212, "Victory Theme", 10, 195, 200, 20);
        gui.addButton(500, "Save", 50, 225, 50, 20);
        gui.addButton(501, "Cancel", 150, 225, 50, 20);
        return gui;
    }

    @Override
    public void sendForm(EntityPlayerMP playerMP){
        String errorMessage = "";
        CustomGuiWrapper gui = CustomGuiController.getOpenGui(playerMP);
        int state = 0;
        OOTrainerData npcData=new OOTrainerData();
        boolean losBattle=ReflectionHelper.getPrivateValue(this, "losBattle");
        String category=ReflectionHelper.getPrivateValue(this, "category");
        String rules=ReflectionHelper.getPrivateValue(this, "rules");

        while(errorMessage.equals("") && state < 5) {
            int dialog;
            if (state == 0) {
                try {
                    dialog = Integer.parseInt(((CustomGuiTextFieldWrapper)gui.getComponent(401)).getText());
                    if (dialog <= 0) {
                        errorMessage = "Init dialog isn't a number higher than 0, and can't be a dialog!";
                    } else {
                        npcData.setInitDialogId(dialog);
                        if (losBattle) {
                            NpcScriptDataManipulator.setJavascriptVariable(new NPCWrapper(this.npc), "losDialog", npcData.getInitDialogId());
                        }

                        ++state;
                    }
                } catch (NumberFormatException var9) {
                    errorMessage = "Init dialog isn't a number!";
                }
            } else if (state == 1) {
                try {
                    dialog = Integer.parseInt(((CustomGuiTextFieldWrapper)gui.getComponent(402)).getText());
                    if (dialog <= 0) {
                        errorMessage = "Win dialog isn't a number higher than 0, and can't be a dialog!";
                    } else {
                        npcData.setWinDialogId(dialog);
                        ++state;
                    }
                } catch (NumberFormatException var8) {
                    errorMessage = "Win dialog isn't a number!";
                }
            } else if (state == 2) {
                try {
                    dialog = Integer.parseInt(((CustomGuiTextFieldWrapper)gui.getComponent(403)).getText());
                    if (dialog <= 0) {
                        errorMessage = "Loss dialog isn't a number higher than 0, and can't be a dialog!";
                    } else {
                        npcData.setLossDialogId(dialog);
                        ++state;
                    }
                } catch (NumberFormatException var7) {
                    errorMessage = "Loss dialog isn't a number!";
                }
            } else if (state == 3) {
                if (this.team.equals("")) {
                    errorMessage = "No team was selected!";
                } else {
                    npcData.setCategoryName(category);
                    npcData.setTeam(this.team);
                    ++state;
                }
            } else if (state == 4) {
                if (!rules.equals("default")) {
                    npcData.setRules(rules);
                } else {
                    npcData.setRules("");
                }

                ++state;
            }
        }

        if (!errorMessage.equals("")) {
            gui.addLabel(210, errorMessage, 5, 20, 240, 20, 16711680);
            gui.update(new PlayerWrapper(playerMP));
            return;
        }

        if(((CustomGuiTextFieldWrapper)gui.getComponent(405)).getText()!=null){
            npcData.setEncounterTheme(((CustomGuiTextFieldWrapper)gui.getComponent(405)).getText());
        }
        if(((CustomGuiTextFieldWrapper)gui.getComponent(406)).getText()!=null){
            npcData.setBattleTheme(((CustomGuiTextFieldWrapper)gui.getComponent(406)).getText());
        }
        if(((CustomGuiTextFieldWrapper)gui.getComponent(407)).getText()!=null){
            npcData.setVictoryTheme(((CustomGuiTextFieldWrapper)gui.getComponent(407)).getText());
        }

        npcData.setNpcTrainerData(this.npc);
        playerMP.closeScreen();
    }
}
