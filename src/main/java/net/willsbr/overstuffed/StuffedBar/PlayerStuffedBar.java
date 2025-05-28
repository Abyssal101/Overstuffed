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

    public static final int MIN_FULL_POINTS=2;
    public static final int MIN_STUFFED_POINTS=1;
    public static final int MIN_OVERSTUFFED_POINTS=1;




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
    public void setCurrentStuffedLevel(int i)
    {
        this.currentStuffedLevel =i;
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
        if((fullLevel + stuffedLevel +overstuffedLevel)<9)
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
    public int subStuffedPoint()
    {
        //retturns the new state, done really so I don't use break statemetns
        //this logic should occur every time at 30
        stuffedLost=0;
        if((fullLevel + stuffedLevel +overstuffedLevel-1)>=4)
        {
            if(addState==0)
            {
                overstuffedLevel--;
                addState=2;
                return addState;
            }
            else if(addState==1)
            {
                overstuffedLevel++;
                stuffedLevel--;
                addState--;
                return addState;
            }
            else if(addState==2)
            {
                stuffedLevel++;
                fullLevel--;
                addState--;
                return addState;
            }
        }

        return addState;
    }
    public void setAddState(int state)
    {
        this.addState=state;
    }


    public void copyFrom(PlayerStuffedBar source)
    {
        //this.currentStuffedLevel =source.getCurrentStuffedLevel();
        this.fullLevel =source.getFullLevel();
        this.stuffedLevel =source.getStuffedLevel();
        this.overstuffedLevel=source.getOverstuffedLevel();
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
    public void setStuffedLossed(int stuffedLost) {
        this.stuffedLost = stuffedLost;
    }


    public void addStuffedLossed() {
        this.stuffedLost++;
    }
    public int getFullLevel()
    {
        return this.fullLevel;
    }
    public void setFullLevel(int fullPoints)
    {
        this.fullLevel = fullPoints;
    }
    public int getStuffedLevel()
    {
        return this.stuffedLevel;
    }
    public void setStuffedLevel(int stuffedLevel)
    {
        this.stuffedLevel = stuffedLevel;
    }

    public int getOverstuffedLevel()
    {
        return this.overstuffedLevel;
    }

    public void setOverstuffedLevel(int overstuffedLevel)
    {
        this.overstuffedLevel=overstuffedLevel;
    }

    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getAddState()
    {
        return addState;
    }

    public void resetLimits()
    {
        fullLevel=MIN_FULL_POINTS;
        stuffedLevel=MIN_STUFFED_POINTS;
        overstuffedLevel=MIN_OVERSTUFFED_POINTS;
        //this just makes it so it'll go reset if stuffed is too hgih
        this.addState=0;


        this.addStuffedLevel(0);
    }

}
