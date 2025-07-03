package net.willsbr.gluttonousgrowth.client;

import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;

public class ClientWeightBarData {

    public static int currentWeight = GluttonousClientConfig.minWeight.get();
    //this is the percent for burstGain
    private static int lastWeightStage = 0;
    private static int amountThroughStage;

    private static int queuedWeight = 0;
    private static int totalQueuedWeight = 0;

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

    public static void setQueuedWeight(int queuedWeight, int totalQueuedWeight) {
        ClientWeightBarData.queuedWeight = queuedWeight;
        ClientWeightBarData.totalQueuedWeight = totalQueuedWeight;
    }

    public static int getTotalQueuedWeight() {
        return totalQueuedWeight;
    }

    public static void setTotalQueuedWeight(int totalQueuedWeight) {
        ClientWeightBarData.totalQueuedWeight = totalQueuedWeight;
    }
}
