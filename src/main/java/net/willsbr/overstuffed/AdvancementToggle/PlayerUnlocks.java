package net.willsbr.overstuffed.AdvancementToggle;

import net.minecraft.nbt.CompoundTag;

public class PlayerUnlocks {
    //so this is how this is ogoing to work
    //Two Array of booleans
    //One for if a setting is enabled, another for seeing if that settings is unlocked
    //I'll make note of what each index is.

    //this is an int array because I can't store a boolean array, so I'll just make getters and setters set it accordingly
    int[] unlocks;

    int[] settingStatus;
    public PlayerUnlocks()
    {
        //FIXME MAKE UNLOCKS ACTUALLY BE USED
        //should all default to false?
        //Pre-emptively do 10 for my sake
        unlocks =new int[10];
        settingStatus=new int[10];
        //Index 0- Stage Based Gain

    }


    public void setToggle(int index, boolean input)
    {
        settingStatus[index]=booleanToInt(input);
    }
    public boolean getToggle(int index)
    {
        //this handles the 0-10 logic
        return intToBoolean(settingStatus[index]);
    }

    //for situations in where I don't use the 0-1 situation and actually store a value
    public int getToggleValue(int index)
    {
            return settingStatus[index];
    }
    public void setToggleValue(int index, int input)
    {
        if(index==1 || index==2)
        {
            settingStatus[index]=input;
        }
        else {
            System.out.println("The unlock for index "+index+" is not setup");
        }
    }



    public int getLength()
    {
        return settingStatus.length;
    }


    public void copyFrom(PlayerUnlocks source)
    {
        this.settingStatus =source.settingStatus;
    }

    public void saveNBTData(CompoundTag nbt)
    {
       // nbt.putInt("currentweight", currentWeight);
        nbt.putIntArray("settingarray", this.settingStatus);

    }
    public void loadNBTData(CompoundTag nbt)
    {
        this.settingStatus =nbt.getIntArray("settingarray");
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
