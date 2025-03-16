package net.willsbr.overstuffed.client;

import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.ArrayList;
import java.util.Queue;

public class ClientWeightBarData {

    public static int currentWeight = OverstuffedConfig.minWeight.get();
    //this is the percent for burstGain
    private static int lastWeightStage = 0;
    private static int amountThroughStage;

    private static int queuedWeight = 0;

    public static void setCurrentWeight(int newWeight) {

        ClientWeightBarData.currentWeight = newWeight;
    }

    public static int getPlayerWeight() {
        return currentWeight;
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

    public static int getQueuedWeight() {
        return queuedWeight;
    }

    public static void setQueuedWeight(int queuedWeight) {
        ClientWeightBarData.queuedWeight = queuedWeight;
    }
}
