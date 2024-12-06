package net.willsbr.overstuffed.client;

import com.tom.cpm.api.IClientAPI;
import net.minecraftforge.fml.ModList;
import net.willsbr.overstuffed.config.OverstuffedConfig;

public class ClientCPMData {

    //just static helper methods to help converse with the config file and cpm
    private static IClientAPI playersAPI;



    public static void setStuffed(String inputStuffed)
    {
        if(OverstuffedConfig.stuffedLayerConfigEntry !=null)
        {
            OverstuffedConfig.lastStuffedLayer=OverstuffedConfig.stuffedLayerConfigEntry.get();
        }
        OverstuffedConfig.setStuffedLayer(inputStuffed);
    }

    public static void setWeight(String inputWeight)
    {
        if(OverstuffedConfig.weightLayerConfigEntry!=null && !inputWeight.contentEquals(OverstuffedConfig.weightLayerConfigEntry.get()))
        {
            OverstuffedConfig.lastWeightLayer=OverstuffedConfig.weightLayerConfigEntry.get();
        }
        OverstuffedConfig.setWeightLayer(inputWeight);
    }




    public static IClientAPI getPlayersAPI() {
        return playersAPI;
    }

    public static void setPlayersAPI(IClientAPI playersAPI) {
        ClientCPMData.playersAPI = playersAPI;
    }

    public static boolean playStuffed()
    {
        if(ModList.get().isLoaded("cpm"))
        {
            if(!OverstuffedConfig.stuffedLayerConfigEntry.get().contentEquals(""))
            {
                Double percentFull= ((double)ClientStuffedBarData.getPlayerStuffedBar()/ClientStuffedBarData.getMax());
                int outOf255=(int)(percentFull*255);
                playersAPI.playAnimation(OverstuffedConfig.stuffedLayerConfigEntry.get(),outOf255);
                playersAPI.playAnimation(OverstuffedConfig.lastStuffedLayer,0);
            }
            return true;
        }
        else {
            return false;
        }

    }
    public static boolean playWeight() {
        if (ModList.get().isLoaded("cpm")) {
            if (OverstuffedConfig.returnSetting(0) == false) {

                double weightRatio = ((double) ClientWeightBarData.getPlayerWeight() - OverstuffedConfig.minWeight.get());
                weightRatio = weightRatio / (OverstuffedConfig.maxWeight.get() - OverstuffedConfig.minWeight.get());
                int outof255 = (int) (weightRatio * 255);
                playersAPI.playAnimation(OverstuffedConfig.weightLayerConfigEntry.get(), outof255);
                return true;
            } else {
                //This is stage based gaining
                if (ClientWeightBarData.getLastWeightStage() == -1) {
                    double weightRatio = ((double) ClientWeightBarData.getPlayerWeight() - OverstuffedConfig.minWeight.get());
                    weightRatio = weightRatio / (OverstuffedConfig.maxWeight.get() - OverstuffedConfig.minWeight.get());
                    int calculatedPercentage = (int) ((weightRatio * 100));
                    int xOf5 = calculatedPercentage / 20;

                    ClientWeightBarData.setLastWeightStage(xOf5);
                }
                //this is the starting point, the stage if you will
                playersAPI.playAnimation(OverstuffedConfig.lastWeightLayer, 0);
                int outOf255 = (int) (ClientWeightBarData.getLastWeightStage() * 0.2 * 255);
                //System.out.println("Amount through stage "+ClientWeightBarData.getAmountThroughStage());
                playersAPI.playAnimation(OverstuffedConfig.weightLayerConfigEntry.get(), outOf255 + ClientWeightBarData.getAmountThroughStage());

                return true;
            }

        }
        return false;
      }
    }


