package net.willsbr.overstuffed.client;

public class ClientStuffedBarData {

    //Each player HAS THIS DATA ON A SERVER
    private static int playerStuffedBar;
    private static int currentSoftLimit;

    private static int currentFirmLimit;
    private static int currentHardLimit;

    public static void  set(int input, int soft,int firm, int hard)
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

}
