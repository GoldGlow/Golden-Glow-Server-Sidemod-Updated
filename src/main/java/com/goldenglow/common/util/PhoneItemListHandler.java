package com.goldenglow.common.util;

import com.goldenglow.GoldenGlow;
import net.minecraft.item.Item;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by JeanMarc on 5/20/2019.
 */
public class PhoneItemListHandler {
    public ArrayList<String> itemIDs;
    public File itemsConfigFile=new File(Reference.configDir, "phoneItems.cfg");

    public PhoneItemListHandler(){
        itemIDs=new ArrayList<String>();
    }

    public void init(){
        GoldenGlow.logger.info("Loading phone items!");
        if(!itemsConfigFile.exists()) {
            if (!itemsConfigFile.getParentFile().exists())
                itemsConfigFile.getParentFile().mkdirs();
            itemsConfigFile.mkdir();
        }
        try {
            this.loadItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadItems() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(itemsConfigFile));
        String readLine;
        while((readLine=reader.readLine())!=null)
        {
            Item item=Item.getByNameOrId(readLine);
            if(item!=null){
                this.itemIDs.add(readLine);
                GGLogger.info("Loaded "+readLine);
            }
        }
    }
}
