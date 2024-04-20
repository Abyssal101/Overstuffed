package net.willsbr.overstuffed.AdvancementToggle;

import net.minecraft.nbt.CompoundTag;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerToggles {
    //so this is how this is ogoing to work
    //Two Array of booleans
    //One for if a setting is enabled, another for seeing if that settings is unlocked
    //I'll make note of what each index is.

    //this is an int array because I can't store a boolean array, so I'll just make getters and setters set it accordingly
    int[] settings;
    public PlayerToggles()
    {
        //should all default to false?
        //Pre-emptively do 10 for my sake
        settings=new int[10];
        //Index 0- Stage Based Gain(If true, enabled, if false, sets to gradual)
        //Index 1-
    }


    public void setToggle(int index, boolean input)
    {
        settings[index]=booleanToInt(input);
    }
    public boolean getToggle(int index)
    {
        return intToBoolean(settings[index]);
    }
    public int getLength()
    {
        return settings.length;
    }


    public void copyFrom(PlayerToggles source)
    {
        this.settings=source.settings;
    }

    public void saveNBTData(CompoundTag nbt)
    {
       // nbt.putInt("currentweight", currentWeight);
        nbt.putIntArray("settingarray", this.settings);

    }
    public void loadNBTData(CompoundTag nbt)
    {
        this.settings=nbt.getIntArray("settingarray");
       // this.burstGain=nbt.getBoolean("burst");
    }

    public boolean intToBoolean(int inputInt)
    {
        if(inputInt==1)
        {
            return true;
        }
        else if(inputInt==0)
        {
            return false;
        }
        else {
            System.out.println("You fuckkkeeed the toggle somehow");
            return false;
        }
    }
    public int booleanToInt(boolean input)
    {
        if(input)
        {
            return 1;
        }
        return 0;

    }


}
