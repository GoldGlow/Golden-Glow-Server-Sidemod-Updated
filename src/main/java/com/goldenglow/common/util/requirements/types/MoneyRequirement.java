package com.goldenglow.common.util.requirements.types;

import com.goldenglow.common.util.requirements.Requirement;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.entity.IPlayer;

public class MoneyRequirement implements Requirement {
    private final String name="MONEY";

    public String getName(){
        return this.name;
    }

    public boolean hasRequirement(String data, EntityPlayerMP player){
        IPixelmonBankAccount bankAccount=(IPixelmonBankAccount) Pixelmon.moneyManager.getBankAccount(player).orElse(null);
        int amount=Integer.parseInt(data);
        if(bankAccount!=null){
            return bankAccount.getMoney() >= amount;
        }
        return false;
    }
}
