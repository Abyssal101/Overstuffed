package net.willsbr.overstuffed.StuffedBar;

import net.minecraft.nbt.CompoundTag;

public class PlayerStuffedBar {
    private int currentStuffedLevel;

    //this is the regular green ones, represent normal amount of overeating or whatever
    private int fullLevel =2;

    private int stuffedLevel =1;

    //this represents when you've gone past your limit, you can't go beyond MaxStuffedLevel+currentOverStuffedLevel
    private int overstuffedLevel =1;
    private final int MIN_STUFFED_LEVEL =0;

    private int stuffedLost=0;
    private int addState=0;

    //what the amount of stuffed lost should be before it adds new
    private int interval=30;


    public long lastCallTime;

    public int lastFoodDuration;

    public int getCurrentStuffedLevel()
    {
        return currentStuffedLevel;
    }



    public void addStuffedLevel(int add)
    {
        this.currentStuffedLevel =Math.min(this.currentStuffedLevel +add, (fullLevel + overstuffedLevel + stuffedLevel));
    }

    public void addStuffedLevel(int add, long time, int duration)
    {
        this.currentStuffedLevel =Math.min(this.currentStuffedLevel +add, (fullLevel + overstuffedLevel + stuffedLevel));
        this.lastCallTime=time;
        this.lastFoodDuration=duration;
    }

    public void subStuffedLevel(int sub)
    {
        this.currentStuffedLevel =Math.max(this.currentStuffedLevel -sub, MIN_STUFFED_LEVEL);
    }
    public int addStuffedPoint()
    {
        //retturns the new state, done really so I don't use break statemetns
        //this logic should occur every time at 30
        stuffedLost=0;
        if(fullLevel + stuffedLevel +overstuffedLevel<9)
        {
            if(addState==0)
            {
                overstuffedLevel++;
                addState++;
                return addState;
            }
            else if(addState==1)
            {
                overstuffedLevel--;
                stuffedLevel++;
                addState++;
                return addState;
            }
            else if(addState==2)
            {
                stuffedLevel--;
                fullLevel++;
                addState=0;
                return addState;
            }
        }

        return addState;
    }

    public void copyFrom(PlayerStuffedBar source)
    {
        this.currentStuffedLevel =source.getCurrentStuffedLevel();
        this.fullLevel =source.getFullPoints();
        this.stuffedLevel =source.getStuffedPoints();
        this.overstuffedLevel=source.getOverstuffedPoints();
        this.stuffedLost=source.getStuffedLossed();
        this.addState=source.addState;
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("stuffedbar", currentStuffedLevel);
        nbt.putInt("full", this.fullLevel);
        nbt.putInt("stuffed", this.stuffedLevel);
        nbt.putInt("overstuffed", this.overstuffedLevel);
        nbt.putInt("stuffedlost", this.stuffedLost);
        nbt.putInt("addstate",this.addState);

    }
    public void loadNBTData(CompoundTag nbt)
    {
        currentStuffedLevel =nbt.getInt("stuffedbar");
        fullLevel =nbt.getInt("full");
        stuffedLevel =nbt.getInt("stuffed");
        overstuffedLevel=nbt.getInt("overstuffed");
        stuffedLost=nbt.getInt("stuffedlost");
        addState=nbt.getInt("addstate");
    }


    public CompoundTag updateNBTData()
    {
        CompoundTag nbt= new CompoundTag();
        nbt.putInt("stuffedbar", currentStuffedLevel);
        nbt.putInt("full", this.fullLevel);
        nbt.putInt("stuffed", this.stuffedLevel);
        nbt.putInt("overstuffed", this.overstuffedLevel);
        nbt.putInt("stuffedlost", this.stuffedLost);
        nbt.putInt("addstate",this.addState);
        return nbt;
    }


    public int getStuffedLossed() {
        return stuffedLost;
    }

    public void addStuffedLossed() {
        this.stuffedLost++;
    }
    public int getFullPoints()
    {
        return this.fullLevel;
    }
    public int getStuffedPoints()
    {
        return this.stuffedLevel;
    }
    public int getOverstuffedPoints()
    {
        return this.overstuffedLevel;
    }

    public int getInterval() {
        return interval;
    }

    public int getAddState()
    {
        return addState;
    }
}
