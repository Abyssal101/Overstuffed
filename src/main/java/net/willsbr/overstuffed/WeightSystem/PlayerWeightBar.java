package net.willsbr.overstuffed.WeightSystem;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.nbt.CompoundTag;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerWeightBar {
    //

    private int currentWeight;

    private int minWeight=100;

    //this is what options will determine for display
    private int curMaxWeight=200;

    //this is what should be used to set curMaxWeight, infinite options is hard to balance
    private int[] maxWeightSettings= {200,300,500,1000};

    //food queue situation so you slowly gain weight
    private int queuedWeight;

    private Queue<Integer> weightChanges=new LinkedList<Integer>();;

    private boolean readyToUpdateWeight=true;
    private long savedTickForWeight;

    private int weightUpdateDelay=40;

    private int weightLossDelay;
    private long savedTickforWeightLoss;



    public int getCurrentWeight()
    {
        return this.currentWeight;
    }

    public void addWeight()
    {
        //System.out.println("This should be adding weight"+ currentWeight+" and "+" queued weight2" + queuedWeight +" compared to " +curMaxWeight);
        if(currentWeight+1<=this.curMaxWeight && queuedWeight>0)
        {
            currentWeight++;
            queuedWeight--;
        }
        //do this so you can't store like infinte amounts of weight

    }
    public void loseWeight()
    {
        if(currentWeight-1>0)
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
        if(i>=0 && i<=3)
        {
            this.curMaxWeight=maxWeightSettings[i];
        }
    }
    public void addWeightChanges(int input)
    {
        weightChanges.add(input);
    }

    public int getWeightChanges()
    {
        int change;
        if(weightChanges.peek()==null)
        {
            change=0;
        }
        else {
            change = weightChanges.poll();
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
       this.queuedWeight=source.queuedWeight;
       this.weightChanges=source.weightChanges;
       this.readyToUpdateWeight=source.readyToUpdateWeight;
       this.savedTickForWeight=source.savedTickForWeight;
       this.weightUpdateDelay=source.weightUpdateDelay;
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("currentweight", currentWeight);
        nbt.putInt("maxweight", curMaxWeight);
        //call it stack weight because queing is atrocious to spell ever time
        nbt.putInt("stackweight", queuedWeight);
        int[] savingArray= new int[weightChanges.size()];
        for(int i=0;i<savingArray.length;i++)
        {
            savingArray[i]=this.weightChanges.poll();
        }
        nbt.putIntArray("changestack", savingArray);

        //Probably not important because it'll just reset, max a few extra seconds for someone to change.
        //nbt.putBoolean("updateweight", this.readyToUpdateWeight);

    }
    public void loadNBTData(CompoundTag nbt)
    {
        this.currentWeight =nbt.getInt("currentweight");
        this.curMaxWeight =nbt.getInt("maxweight");
        this.queuedWeight =nbt.getInt("stackweight");
        int[] savingArray= nbt.getIntArray("changestack");
        for(int i=0;i<savingArray.length;i++)
        {
            this.weightChanges.add(savingArray[i]);
        }


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
}
