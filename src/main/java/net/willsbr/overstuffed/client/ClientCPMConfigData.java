package net.willsbr.overstuffed.client;

import com.tom.cpm.api.IClientAPI;
import net.willsbr.overstuffed.CPMCompat.StuffedSender;

public class ClientCPMConfigData {

    private static String stuffedBellyLayer="";
    private static String lastStuffedBellyLayer="";

    private static String weightLayer="";

    private static String lastWeightLayer="";

    //just data

    private static IClientAPI playersAPI;


    public static void setStuffed(String inputStuffed)
    {
        if(stuffedBellyLayer!=null)
        {
            ClientCPMConfigData.lastStuffedBellyLayer=ClientCPMConfigData.stuffedBellyLayer;
        }
        ClientCPMConfigData.stuffedBellyLayer=inputStuffed;
    }

    public static void setWeight(String inputWeight)
    {
        if(weightLayer!=null && !inputWeight.contentEquals(ClientCPMConfigData.getWeightLayer()))
        {
            ClientCPMConfigData.lastWeightLayer=ClientCPMConfigData.weightLayer;
        }
        ClientCPMConfigData.weightLayer=inputWeight;
    }



    public static String getStuffedBellyLayer() {
        return stuffedBellyLayer;
    }
    public static String getLastStuffedBellyLayer() {
        return lastStuffedBellyLayer;
    }

    public static String getWeightLayer() {return weightLayer;}

    public static String getLastWeightLayer() {return lastWeightLayer;}


    public static IClientAPI getPlayersAPI() {
        return playersAPI;
    }

    public static void setPlayersAPI(IClientAPI playersAPI) {
        ClientCPMConfigData.playersAPI = playersAPI;
    }

    public static void playStuffed()
    {
        if(!ClientCPMConfigData.getStuffedBellyLayer().contentEquals(""))
        {
            Double percentFull= ClientStuffedBarData.getPlayerStuffedBar()/(ClientStuffedBarData.getSoftLimit()+ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getCurrentFirmLimit()*1.0);
            int outOf255=(int)(percentFull*255);
            System.out.println("Test"+playersAPI.playAnimation(ClientCPMConfigData.getStuffedBellyLayer(),outOf255));
            playersAPI.playAnimation(ClientCPMConfigData.getLastStuffedBellyLayer(),0);
        }
    }
    public static void playWeight()
    {
//        if(!ClientCPMConfigData.getWeightLayer().contentEquals(""))
//        {
            int outof255=(int)((((double)ClientWeightBarData.getPlayerWeight())/ClientWeightBarData.getMaxWeight())*255);
            System.out.println(outof255+" dddd "+ playersAPI.playAnimation(ClientCPMConfigData.getWeightLayer(),outof255));
            playersAPI.playAnimation(ClientCPMConfigData.getLastWeightLayer(),0);

        // }
    }

}
