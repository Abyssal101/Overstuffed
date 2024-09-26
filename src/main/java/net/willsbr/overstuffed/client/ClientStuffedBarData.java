package net.willsbr.overstuffed.client;

public class ClientStuffedBarData {

    //Each player HAS THIS DATA ON A SERVER
    public static int playerStuffedBar;
    public static int currentSoftLimit;

    public static int currentFirmLimit;
    public static int currentHardLimit;

    private static int max=9;

    public static void set(int input, int soft,int firm, int hard)
    {
        ClientStuffedBarData.playerStuffedBar=input;
        ClientStuffedBarData.currentSoftLimit=soft;
        ClientStuffedBarData.currentFirmLimit=firm;
        ClientStuffedBarData.currentHardLimit=hard;

    }

    public static int getPlayerStuffedBar()
    {
        return playerStuffedBar;
    }
    public static int getSoftLimit()
    {
        return currentSoftLimit;
    }
    public static int getHardLimit()
    {
        return currentHardLimit;
    }

    public static int getCurrentFirmLimit(){return currentFirmLimit;}

    public static int getMax() {
        return max;
    }
}
