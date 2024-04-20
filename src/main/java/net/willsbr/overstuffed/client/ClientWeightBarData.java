package net.willsbr.overstuffed.client;

import java.util.ArrayList;
import java.util.Queue;

public class ClientWeightBarData {

    private static int currentWeight;

    private static int maxWeight=200;

    private static int minWeight;

    private static boolean burstGain;

    //this is the percent for burstGain
    private static int lastWeightStage=0;

    private static int amountThroughStage;



    public static void  setCurrentWeight(int newWeight)
    {
       ClientWeightBarData.currentWeight=newWeight;
    }

    public static int getPlayerWeight()
    {
        return currentWeight;
    }


    public static int getMaxWeight() {
        return maxWeight;
    }

    public static void setMaxWeight(int maxWeight) {
        ClientWeightBarData.maxWeight = maxWeight;
    }

    public static int getMinWeight() {
        return minWeight;
    }

    public static void setMinWeight(int minWeight) {
        ClientWeightBarData.minWeight = minWeight;
    }

    public static boolean isBurstGain() {
        return burstGain;
    }

    public static void setBurstGain(boolean burstGain) {
        ClientWeightBarData.burstGain = burstGain;
    }

    public static int getLastWeightStage() {
        return lastWeightStage;
    }

    public static void setLastWeightStage(int lastWeightStage) {
        ClientWeightBarData.lastWeightStage = lastWeightStage;
    }

    public static int getAmountThroughStage() {
        return amountThroughStage;
    }

    public static void setAmountThroughStage(int amountThroughStage) {
        ClientWeightBarData.amountThroughStage = amountThroughStage;
    }
}
