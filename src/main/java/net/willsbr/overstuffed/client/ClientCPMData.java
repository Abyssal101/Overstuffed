package net.willsbr.overstuffed.client;

import com.tom.cpm.api.IClientAPI;
import net.minecraftforge.fml.ModList;
import net.willsbr.overstuffed.config.OverstuffedConfig;

public class ClientCPMData {

    //just static helper methods to help converse with the config file and cpm
    private static IClientAPI playersAPI;


    private static int totalWeightFrames;
    private static int totalStuffedFrames;


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
        if(ModList.get().isLoaded("cpm"))
        {
            return playersAPI;

        }
        return null;
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
                String layerName=OverstuffedConfig.stuffedLayerConfigEntry.get();
                Double percentFull= ((double)ClientStuffedBarData.getPlayerStuffedBar()/ClientStuffedBarData.getMax());
                int outOfMax=(int)(percentFull*playersAPI.getAnimationMaxValue(layerName));
                playersAPI.playAnimation(layerName,outOfMax);
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
            if (!OverstuffedConfig.stageGain.get()) {
                String layerName=OverstuffedConfig.weightLayerConfigEntry.get();
                double weightRatio = ((double) ClientWeightBarData.getPlayerWeight() - OverstuffedConfig.minWeight.get());
                weightRatio = weightRatio / (OverstuffedConfig.maxWeight.get() - OverstuffedConfig.minWeight.get());
                int outofMax = (int) (weightRatio * playersAPI.getAnimationMaxValue(layerName));
                playersAPI.playAnimation(layerName, outofMax);
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
                String layerName=OverstuffedConfig.weightLayerConfigEntry.get();
                //this is the starting point, the stage if you will
                playersAPI.playAnimation(OverstuffedConfig.lastWeightLayer, 0);

                int outOfMax = (int) (ClientWeightBarData.getLastWeightStage() * 0.2 * playersAPI.getAnimationMaxValue(layerName));
                //System.out.println("Amount through stage "+ClientWeightBarData.getAmountThroughStage());
                if(outOfMax!=-1)
                {
                    playersAPI.playAnimation(OverstuffedConfig.weightLayerConfigEntry.get(), outOfMax + ClientWeightBarData.getAmountThroughStage());
                }

                return true;
            }

        }
        return false;
      }

    public static int getTotalWeightFrames() {

        if(ModList.get().isLoaded("cpm")) {
            if (!OverstuffedConfig.weightLayerConfigEntry.get().contentEquals("")) {
                totalWeightFrames=playersAPI.getAnimationMaxValue(OverstuffedConfig.weightLayerConfigEntry.get());
            }
        }

        return totalWeightFrames;
    }

    public static int getTotalStuffedFrames() {
        if(ModList.get().isLoaded("cpm")) {
            if (!OverstuffedConfig.stuffedLayerConfigEntry.get().contentEquals("")) {
                totalStuffedFrames=playersAPI.getAnimationMaxValue(OverstuffedConfig.stuffedLayerConfigEntry.get());
            }
        }
        return totalStuffedFrames;
    }
}


