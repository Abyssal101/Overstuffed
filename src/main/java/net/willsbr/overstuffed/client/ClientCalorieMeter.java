package net.willsbr.overstuffed.client;

import net.willsbr.overstuffed.config.GluttonousWorldConfig;

public class ClientCalorieMeter {

    //Each player HAS THIS DATA ON A SERVER
    private static int currentCalories;

    private static int currentMax= GluttonousWorldConfig.baseCalCap.get();

    private static int currentLost;
    private static int interval;

    private static double modThreshold;
    private static double slowThreshold;

    private static int currentDelay;
    private static long currentSavedTick=-1;


    public static void set(int input, int max)
    {
        ClientCalorieMeter.currentCalories =Math.max(0,input);
        ClientCalorieMeter.currentMax = Math.max(0,max);
    }
    public static void setThresholds(double moderate,double slow){
        ClientCalorieMeter.modThreshold=moderate;
        ClientCalorieMeter.slowThreshold=slow;
    }



    public static int getCurrentCalories()
    {
        return currentCalories;
    }
    public static void setMaxCalories(int max)
    {
        ClientCalorieMeter.currentMax=max;
    }

    public static int getMax() {
        return currentMax;
    }

    public static int getCurrentLost() {
        return currentLost;
    }

    public static void setCurrentLost(int currentLost) {
        ClientCalorieMeter.currentLost = currentLost;
    }

    public static int getInterval() {
        return interval;
    }

    public static void setInterval(int interval) {
        ClientCalorieMeter.interval = interval;
    }

    public static int getCurrentDelay() {
        return currentDelay;
    }

    public static void setCurrentDelay(int currentDelay) {
        ClientCalorieMeter.currentDelay = currentDelay;
    }

    public static long getCurrentSavedTick() {
        return currentSavedTick;
    }

    public static void setCurrentSavedTick(long currentSavedTick) {
        ClientCalorieMeter.currentSavedTick = currentSavedTick;
    }
}
