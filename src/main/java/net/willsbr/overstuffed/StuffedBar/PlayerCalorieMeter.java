package net.willsbr.overstuffed.StuffedBar;

import net.minecraft.nbt.CompoundTag;
import net.willsbr.overstuffed.config.OverstuffedWorldConfig;

public class PlayerCalorieMeter {

    private int maxCalories= OverstuffedWorldConfig.baseCalCap.get();
    private int currentCalories=0;

    private double calorieGainMultipler=OverstuffedWorldConfig.calorieGainMultipler.get();

    private double modMetabolismThres=OverstuffedWorldConfig.modMetabolismThres.get();
    private double slowMetabolismThres=OverstuffedWorldConfig.slowMetabolismThres.get();


    private int calLost =0;
    private int addState=0;

    //what the amount of stuffed lost should be before it adds new
    private int interval=OverstuffedWorldConfig.capacityIncreaseInterval.get();

    private int calClearDelay=-1;
    private long foodEatenTick =-1;


    public void copyFrom(PlayerCalorieMeter source)
    {
        this.maxCalories=source.getMaxCalories();
        this.currentCalories=source.getCurrentCalories();
        this.modMetabolismThres=source.getModMetabolismThres();
        this.slowMetabolismThres=source.getSlowMetabolismThres();
        this.calLost =source.getCalLost();
        this.addState=source.addState;
        this.interval=source.interval;
        this.calClearDelay=source.calClearDelay;
        this.foodEatenTick =source.foodEatenTick;

    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("curcalories", this.currentCalories);
        nbt.putInt("maxcalories", this.maxCalories);
        nbt.putDouble("modmetabolismthreshold", this.modMetabolismThres);
        nbt.putDouble("slowmetabolismthreshold", this.slowMetabolismThres);
        nbt.putInt("stuffedlost", this.calLost);
        nbt.putInt("addstate",this.addState);

        nbt.putInt("calcleardelay",this.calClearDelay);
        nbt.putLong("lastfoodeatentick", this.foodEatenTick);
    }
    public void loadNBTData(CompoundTag nbt)
    {
        currentCalories=nbt.getInt("curcalories");
        maxCalories=nbt.getInt("maxcalories");
        modMetabolismThres=OverstuffedWorldConfig.modMetabolismThres.get();
        slowMetabolismThres=OverstuffedWorldConfig.slowMetabolismThres.get();
        calLost =nbt.getInt("stuffedlost");
        addState=nbt.getInt("addstate");
        calClearDelay=nbt.getInt("calcleardelay");
        foodEatenTick =nbt.getLong("lastfoodeatentick");
    }


    public int getCalLost() {
        return calLost;
    }
    public void setCalLost(int stuffedLost) {
        this.calLost = stuffedLost;
    }


    public void addCalLost(int calories)
    {
        this.calLost+=calories;
    }

    public boolean checkClearCalories(long tick)
    {
//        double percentageDelay=(double)this.currentCalories/this.maxCalories;
//        int delay=(int)((OverstuffedWorldConfig.maxCalClearDelay.get()-OverstuffedWorldConfig.minCalClearDelay.get())*percentageDelay);
//        delay+=OverstuffedWorldConfig.minCalClearDelay.get();
//        this.calClearDelay=delay;
        System.out.println(""+(tick - foodEatenTick));
        return foodEatenTick != -1 && tick - foodEatenTick > calClearDelay;
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


    public int getMaxCalories()
    {
        return maxCalories;
    }

    public void setMaxCalories(int maxCalories)
    {
        this.maxCalories = maxCalories;
    }

    public int getCurrentCalories()
    {
        return currentCalories;
    }

    public void setCurrentCalories(int currentCalories) {
        this.currentCalories = Math.max(0,currentCalories);
    }
    public void addCalories(int calories)
    {
        this.currentCalories+=calories;
    }



    public double getModMetabolismThres() {
        return modMetabolismThres;
    }

    public void setModMetabolismThres(double modMetabolismThres) {
        this.modMetabolismThres = modMetabolismThres;
    }

    public double getSlowMetabolismThres() {
        return slowMetabolismThres;
    }

    public void setSlowMetabolismThres(double slowMetabolismThres) {
        this.slowMetabolismThres = slowMetabolismThres;
    }

    public int getCalClearDelay() {
        return calClearDelay;
    }

    public void setCalClearDelay(int calClearDelay) {
        this.calClearDelay = calClearDelay;
    }

    public long getFoodEatenTick() {
        return foodEatenTick;
    }

    public void setFoodEatenTick(long foodEatenTick)
    {
        this.foodEatenTick = foodEatenTick;
    }

    public double getCalorieGainMultipler() {
        return calorieGainMultipler;
    }

    public void setCalorieGainMultipler(double calorieGainMultipler) {
        this.calorieGainMultipler = calorieGainMultipler;
    }
}
