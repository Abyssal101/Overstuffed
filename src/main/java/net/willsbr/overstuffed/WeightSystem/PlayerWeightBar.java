package net.willsbr.overstuffed.WeightSystem;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.nbt.CompoundTag;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PlayerWeightBar {
    //

    private int minWeight= OverstuffedConfig.minWeight.get();
    private int currentWeight;

    //this is what options will determine for display
    private int curMaxWeight=OverstuffedConfig.getMaxWeight();

    //this is what should be used to set curMaxWeight, infinite options is hard to balance
    //food queue situation so you slowly gain weight
    private int queuedWeight;

    private ArrayList<Integer> weightChanges= new ArrayList<Integer>();

    private boolean readyToUpdateWeight=true;
    private long savedTickForWeight;

    private int weightUpdateDelay=40;
    private double weightUpdateDelayModifier=1;


    private int weightLossDelay;
    private long savedTickforWeightLoss;

    //this is the boolean controlling if it's gradual or if it rapidly increases in stages
    private int lastWeightStage;

    private int amountThroughStage;





    public int getCurrentWeight()
    {
        return this.currentWeight;
    }
    public void setCurrentWeight(int i)
    {this.currentWeight=i;}


    public void addWeight()
    {

        if(currentWeight+1<=this.curMaxWeight && queuedWeight>0)
        {
            currentWeight++;
            queuedWeight--;
        }
        else if(queuedWeight>0)
        {
            queuedWeight--;
        }
    }
    public void loseWeight()
    {
        if(currentWeight-1>this.minWeight)
        {
            currentWeight--;
        }
    }



    public int getCurMaxWeight()
    {
        return this.curMaxWeight;
    }
    public void setMaxWeight(int i)
    {
        this.curMaxWeight=i;
    }
    public void addWeightChanges(int input)
    {
        weightChanges.add(input);
    }

    public int getWeightChanges()
    {
        int change;
        if(weightChanges.isEmpty())
        {
            change=0;
        }
        else {
            change = weightChanges.remove(0);
        }
        return change;
    }

    public boolean weightUpdateStatus()
    {
        return readyToUpdateWeight;
    }
    public void setWeightUpdateStatus(boolean input)
    {
        readyToUpdateWeight=input;
    }

    public void setWeightTick(long tick)
    {
       savedTickForWeight=tick;
    }
    public long getWeightTick()
    {
        return savedTickForWeight;
    }

    public void addChangetoQueue(int weightChange)
    {
        queuedWeight+=weightChange;
    }

    public int getQueuedWeight()
    {
        return queuedWeight;
    }



    public void copyFrom(PlayerWeightBar source)
    {
       this.currentWeight=source.getCurrentWeight();
       this.curMaxWeight=source.getCurMaxWeight();
       this.minWeight=source.getMinWeight();
       this.queuedWeight=source.queuedWeight;
       this.weightChanges=source.weightChanges;
       this.readyToUpdateWeight=source.readyToUpdateWeight;
       this.savedTickForWeight=source.savedTickForWeight;
       this.weightUpdateDelay=source.weightUpdateDelay;
       this.lastWeightStage=source.lastWeightStage;
       this.weightUpdateDelayModifier=source.weightUpdateDelayModifier;

    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("currentweight", currentWeight);
        nbt.putInt("maxweight", curMaxWeight);
        nbt.putInt("minweight",minWeight);
        //call it stack weight because queing is atrocious to spell ever time
        nbt.putInt("stackweight", queuedWeight);
        int[] savingArray= new int[weightChanges.size()];
        for(int i=0;i<savingArray.length;i++)
        {
            savingArray[i]=this.weightChanges.get(i);
        }
        nbt.putIntArray("changestack", savingArray);

        nbt.putInt("weightstage", this.lastWeightStage);

        nbt.putDouble("weightupdatedelaymodifier", this.weightUpdateDelayModifier);
        //Probably not important because it'll just reset, max a few extra seconds for someone to change.
        //nbt.putBoolean("updateweight", this.readyToUpdateWeight);


    }
    public void loadNBTData(CompoundTag nbt)
    {
        this.currentWeight =nbt.getInt("currentweight");
        this.curMaxWeight =nbt.getInt("maxweight");
        this.minWeight=nbt.getInt("minweight");
        this.queuedWeight =nbt.getInt("stackweight");
        int[] savingArray= nbt.getIntArray("changestack");
        for(int i=0;i<savingArray.length;i++)
        {
            this.weightChanges.add(savingArray[i]);
        }

        this.lastWeightStage=nbt.getInt("weightstage");
        this.weightUpdateDelayModifier=nbt.getDouble("weightupdatedelaymodifier");
    }

    public int getWeightUpdateDelay() {
        return weightUpdateDelay;
    }

    public void setWeightUpdateDelay(int weightUpdateDelay) {
        this.weightUpdateDelay = weightUpdateDelay;
    }

    public int getWeightLossDelay() {
        return weightLossDelay;
    }

    public void setWeightLossDelay(int weightLossDelay) {
        this.weightLossDelay = weightLossDelay;
    }

    public long getSavedTickforWeightLoss() {
        return savedTickforWeightLoss;
    }

    public void setSavedTickforWeightLoss(int savedTickforWeightLoss) {
        this.savedTickforWeightLoss = savedTickforWeightLoss;
    }

    public void setLastWeightStage(int i)
    {
        this.lastWeightStage=i;
    }
    public int getLastWeightStage()
    {
        return this.lastWeightStage;
    }


    public int getAmountThroughStage() {
        return amountThroughStage;
    }

    public void setAmountThroughStage(int amountThroughStage) {
        this.amountThroughStage = amountThroughStage;
    }

    public int getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(int minWeight) {
        this.minWeight = minWeight;
    }

    public double getWeightUpdateDelayModifier() {
        return weightUpdateDelayModifier;
    }
    public void setWeightUpdateDelayModifier(double weightUpdateDelayModifier) {
        this.weightUpdateDelayModifier = weightUpdateDelayModifier;
    }
}
