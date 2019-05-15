package com.goldenglow.common.music;

/**
 * Created by JeanMarc on 5/9/2019.
 */
public class Song {
    public int priority;
    public String name;
    public String path;

    public Song(int priority, String name, String path){
        this.priority=priority;
        this.name=name;
        this.path=path;
    }

    public String getName(){
        return this.name;
    }

    public String getPath(){
        return this.path;
    }

    public int getPriority(){
        return this.priority;
    }
}
