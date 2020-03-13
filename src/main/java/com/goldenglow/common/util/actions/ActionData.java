package com.goldenglow.common.util.actions;

import com.goldenglow.common.util.requirements.RequirementData;

public class ActionData {
    public String name;
    public String value;
    public boolean closeInv;
    public RequirementData[] requirements;

    public ActionData(String name, String value){
        this.name=name;
        this.value=value;
    }

    public ActionData(){
    }
}
