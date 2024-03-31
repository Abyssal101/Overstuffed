package net.willsbr.overstuffed.StuffedBar;

import net.minecraft.nbt.CompoundTag;

public class PlayerStuffedBar {
    private int currentStuffedLevel;

    //this is the regular green ones, represent normal amount of overeating or whatever
    private int stuffedLevel =2;

    private int overstuffedLevel =2;

    //this represents when you've gone past your limit, you can't go beyond MaxStuffedLevel+currentOverStuffedLevel
    private int superstuffedLevel =2;
    private final int MIN_STUFFED_LEVEL =0;

    public long lastCallTime;

    public int lastFoodDuration;

    public int getCurrentStuffedLevel()
    {
        return currentStuffedLevel;
    }

    public int getStuffedMax()
    {
        return stuffedLevel;
    }

    public int getOverstuffedMax()
    {
        return this.overstuffedLevel;
    }

    public int getSuperStuffedMax()
    {
        return superstuffedLevel;
    }



    public void addStuffedLevel(int add)
    {
        this.currentStuffedLevel =Math.min(this.currentStuffedLevel +add, (stuffedLevel + superstuffedLevel+overstuffedLevel));
    }

    public void addStuffedLevel(int add, long time, int duration)
    {
        this.currentStuffedLevel =Math.min(this.currentStuffedLevel +add, (stuffedLevel + superstuffedLevel + overstuffedLevel));
        this.lastCallTime=time;
        this.lastFoodDuration=duration;
    }

    public void subStuffedLevel(int sub)
    {
        this.currentStuffedLevel =Math.max(this.currentStuffedLevel -sub, MIN_STUFFED_LEVEL);
    }

    public void copyFrom(PlayerStuffedBar source)
    {
        this.currentStuffedLevel =source.getCurrentStuffedLevel();
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("stuffedbar", currentStuffedLevel);
    }
    public void loadNBTData(CompoundTag nbt)
    {
        currentStuffedLevel =nbt.getInt("stuffedbar");
    }



}
