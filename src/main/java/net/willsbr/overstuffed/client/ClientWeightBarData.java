package net.willsbr.overstuffed.client;

import java.util.ArrayList;
import java.util.Queue;

public class ClientWeightBarData {

    private static int currentWeight;

    private static int maxWeight=200;

    private static int minWeight;



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
}
