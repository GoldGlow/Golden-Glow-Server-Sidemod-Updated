package com.goldenglow.common.guis.data;

import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;

import java.util.ArrayList;

public class TutorialData {
    private ArrayList<TutorialsInfo> tutorialPages=new ArrayList<>();
    private String description;
    private String name;

    public TutorialData(String name){
        this.name=name;
        this.description="";
    }

    public TutorialData(String name, String description){
        this.name=name;
        this.description=description;
    }

    public static TutorialData createMarksTutorial(){
        TutorialData marks=new TutorialData("Marks", "This is a longer description to show the fact that labels now works differently than before.");
        marks.tutorialPages.add(new TutorialsInfo(new CustomGuiTexturedRectWrapper(100, "obscureobsidian:textures/gui/tutorials/marks_tutorial.png", 64, 48, 128, 128, 0, 128), "Throughout your adventure, you will find NPCs with marks like these above their heads.", 1));
        marks.tutorialPages.add(new TutorialsInfo(new CustomGuiTexturedRectWrapper(100, "obscureobsidian:textures/gui/tutorials/marks_tutorial.png", 64, 48, 128, 128, 0, 128), "Question mark means that talking to this NPC can start a new quest.", 2));
        marks.tutorialPages.add(new TutorialsInfo(new CustomGuiTexturedRectWrapper(100, "obscureobsidian:textures/gui/tutorials/marks_tutorial.png", 64, 48, 128, 128, 128, 0), "Exclamation mark means that talking to this NPC will advance an already started quest.", 3));
        marks.tutorialPages.add(new TutorialsInfo(new CustomGuiTexturedRectWrapper(100, "obscureobsidian:textures/gui/tutorials/marks_tutorial.png", 64, 48, 128, 128, 0, 0), "The star mark means that talking to this NPC will complete a quest.", 4));
        marks.tutorialPages.add(new TutorialsInfo(new CustomGuiTexturedRectWrapper(100, "obscureobsidian:textures/gui/tutorials/marks_tutorial.png", 64, 48, 128, 128, 0, 0), "Be on the lookout for those marks because some might appear depending on your progress!", 5));
        return marks;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public void addPage(TutorialsInfo info){
        this.tutorialPages.add(info);
    }

    public int getPageTotal(){
        return this.tutorialPages.size();
    }

    public TutorialsInfo getTutorialPage(int pageNbr){
        for(TutorialsInfo page:this.tutorialPages){
            if(page.getPage()==pageNbr){
                return page;
            }
        }
        return null;
    }
}
