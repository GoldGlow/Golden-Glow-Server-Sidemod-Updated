package com.goldenglow.common.guis.data;

import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;

public class TutorialsInfo {
    private CustomGuiTexturedRectWrapper picture;
    private String firstLine;
    private String secondLine;
    private int page;

    public TutorialsInfo(CustomGuiTexturedRectWrapper picture, String firstLine, String secondLine, int page){
        this.picture=picture;
        this.firstLine=firstLine;
        this.secondLine=secondLine;
        this.page=page;
    }

    public CustomGuiTexturedRectWrapper getPicture(){
        return this.picture;
    }

    public String getFirstLine(){
        return this.firstLine;
    }

    public String getSecondLine(){
        return this.secondLine;
    }

    public int getPage(){
        return this.page;
    }
}
