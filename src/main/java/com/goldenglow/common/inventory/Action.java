package com.goldenglow.common.inventory;

import com.goldenglow.common.util.Requirement;

/**
 * Created by JeanMarc on 6/19/2019.
 */
public class Action {
    ActionType actionType;
    String value;
    Requirement[] requirements;

    public Action(){
        this.actionType=ActionType.COMMAND;
        this.value="";
        this.requirements=null;
    }

    public ActionType getActionType(){
        return this.actionType;
    }

    public void setActionType(ActionType actionType){
        this.actionType=actionType;
    }

    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        this.value=value;
    }

    public Requirement[] getRequirements(){
        return this.requirements;
    }

    public void setRequirements(Requirement[] requirements){
        this.requirements=requirements;
    }

    public enum ActionType{
        COMMAND
    }
}
