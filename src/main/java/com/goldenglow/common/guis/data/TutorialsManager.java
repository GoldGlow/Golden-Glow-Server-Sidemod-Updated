package com.goldenglow.common.guis.data;

import java.util.ArrayList;

public class TutorialsManager {
    ArrayList<TutorialData> tutorials=new ArrayList<TutorialData>();

    public void init(){
        this.tutorials.add(TutorialData.createMarksTutorial());
    }

    public TutorialData getTutorial(String name){
        for(TutorialData tutorial: tutorials){
            if(tutorial.getName().equals(name)){
                return tutorial;
            }
        }
        return null;
    }

    public String[] getTutorialList(){
        String[] tutorials=new String[this.tutorials.size()];
        for(int i=0;i<this.tutorials.size();i++){
            tutorials[i]=this.tutorials.get(i).getName();
        }
        return tutorials;
    }
}
