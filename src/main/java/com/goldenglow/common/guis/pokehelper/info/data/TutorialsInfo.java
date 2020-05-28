package com.goldenglow.common.guis.pokehelper.info.data;

import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;

public class TutorialsInfo {
    private CustomGuiTexturedRectWrapper picture;
    private String text;
    private int page;

    public TutorialsInfo(CustomGuiTexturedRectWrapper picture, String text, int page){
        this.picture=picture;
        this.text=text;
        this.page=page;
    }

    public CustomGuiTexturedRectWrapper getPicture(){
        return this.picture;
    }

    public String getText(){
        return this.text;
    }

    public int getPage(){
        return this.page;
    }
}
