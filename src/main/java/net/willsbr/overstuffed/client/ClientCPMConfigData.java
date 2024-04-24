package net.willsbr.overstuffed.client;

import com.tom.cpm.api.IClientAPI;
import net.willsbr.overstuffed.CPMCompat.StuffedSender;

public class ClientCPMConfigData {

    private static String stuffedBellyLayer="";
    private static String lastStuffedBellyLayer="";

    private static String weightLayer="";

    private static String lastWeightLayer="";

    //AN array of Strings for all toggleable settings just so it's visually easier

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
            playersAPI.playAnimation(ClientCPMConfigData.getStuffedBellyLayer(),outOf255);
            //System.out.println(outOf255+"Is this properly playing: "+playersAPI.getAnimationPlaying(ClientCPMConfigData.getStuffedBellyLayer()));
            playersAPI.playAnimation(ClientCPMConfigData.getLastStuffedBellyLayer(),0);
        }
    }
    public static void playWeight()
    {
//        if(!ClientCPMConfigData.getWeightLayer().contentEquals(""))
            if(ClientTogglesData.getToggle(0)==false)
            {
                int outof255=(int)((((double)ClientWeightBarData.getPlayerWeight())/ClientWeightBarData.getMaxWeight())*255);
                 playersAPI.playAnimation(ClientCPMConfigData.getWeightLayer(),outof255);
                playersAPI.playAnimation(ClientCPMConfigData.getLastWeightLayer(),0);
            }
            else {
                if(ClientWeightBarData.getLastWeightStage()==-1)
                {

                   int calculatedPercentage=(int)((double)(ClientWeightBarData.getPlayerWeight()/ClientWeightBarData.getMaxWeight()*100));
                   int xOf5=calculatedPercentage/20;
                   ClientWeightBarData.setLastWeightStage(xOf5);
                }
                    //this is the starting point, the stage if you will
                    playersAPI.playAnimation(ClientCPMConfigData.getLastWeightLayer(),0);

                    int outOf255=(int)(ClientWeightBarData.getLastWeightStage()*0.2*255);
                     //System.out.println("Stage 255"+outOf255);
                    //System.out.println("Amount through stage "+ClientWeightBarData.getAmountThroughStage());
                    playersAPI.playAnimation(ClientCPMConfigData.getWeightLayer(),outOf255+ ClientWeightBarData.getAmountThroughStage());


                }
            }
    }


