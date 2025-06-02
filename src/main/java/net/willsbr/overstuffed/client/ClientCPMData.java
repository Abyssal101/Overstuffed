package net.willsbr.overstuffed.client;

import com.tom.cpm.api.IClientAPI;
import net.minecraftforge.fml.ModList;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;

public class ClientCPMData {

    //just static helper methods to help converse with the config file and cpm
    private static IClientAPI playersAPI;


    private static int totalWeightFrames;
    private static int totalStuffedFrames;

    public static final String minCPMVersion="0.6.20a";
    private static String loadedCPMVersion="0.0.0";


    public static void setStuffed(String inputStuffed)
    {
        if(OverstuffedClientConfig.stuffedLayerConfigEntry !=null)
        {
            OverstuffedClientConfig.lastStuffedLayer= OverstuffedClientConfig.stuffedLayerConfigEntry.get();
        }
        OverstuffedClientConfig.setStuffedLayer(inputStuffed);


    }

    public static void setWeight(String inputWeight)
    {
        if(OverstuffedClientConfig.weightLayerConfigEntry!=null && !inputWeight.contentEquals(OverstuffedClientConfig.weightLayerConfigEntry.get()))
        {
            OverstuffedClientConfig.lastWeightLayer= OverstuffedClientConfig.weightLayerConfigEntry.get();
        }
        OverstuffedClientConfig.setWeightLayer(inputWeight);
    }

    //checks to see if CPM is a high enough version
    public static int CPMUpdated()
    {
        //inital check to make certain CPM is loaded, otherwise it's always gonna be 0.0.0
        if(ModList.get().isLoaded("cpm") && loadedCPMVersion.contentEquals("0.0.0"))
        {
            loadedCPMVersion=ModList.get().getModContainerById("cpm").get().getModInfo().getVersion().toString();
        }
        return compareVersions(loadedCPMVersion,minCPMVersion);
    }

    public static IClientAPI getPlayersAPI() {
        if(ModList.get().isLoaded("cpm"))
        {
            if(CPMUpdated()!=-1)
            {
                return playersAPI;
            }
        }
        return null;
    }

    public static void setPlayersAPI(IClientAPI playersAPI) {
        ClientCPMData.playersAPI = playersAPI;
    }

    public static boolean playStuffed()
    {
        if(ModList.get().isLoaded("cpm") && getPlayersAPI()!=null)
        {
            if(!OverstuffedClientConfig.stuffedLayerConfigEntry.get().contentEquals(""))
            {
                String layerName= OverstuffedClientConfig.stuffedLayerConfigEntry.get();
                Double percentFull= ((double)ClientStuffedBarData.getPlayerStuffedBar()/ClientStuffedBarData.getMax());
                int outOfMax=(int)(percentFull*playersAPI.getAnimationMaxValue(layerName));
                playersAPI.playAnimation(layerName,outOfMax);
                playersAPI.playAnimation(OverstuffedClientConfig.lastStuffedLayer,0);
            }
            return true;
        }
        else {
            return false;
        }

    }
    public static boolean playWeight() {
        if (ModList.get().isLoaded("cpm") && getPlayersAPI() != null) {

            if (!OverstuffedClientConfig.stageGain.get()) {
                String layerName= OverstuffedClientConfig.weightLayerConfigEntry.get();
                double weightRatio = ((double) ClientWeightBarData.getPlayerWeight() - OverstuffedClientConfig.minWeight.get());
                weightRatio = weightRatio / (OverstuffedClientConfig.maxWeight.get() - OverstuffedClientConfig.minWeight.get());
                int outofMax = (int) (weightRatio * playersAPI.getAnimationMaxValue(layerName));
                playersAPI.playAnimation(layerName, outofMax);
                return true;
            } else {
                //This is stage based gaining
                if (ClientWeightBarData.getLastWeightStage() == -1) {
                    double weightRatio = ((double) ClientWeightBarData.getPlayerWeight() - OverstuffedClientConfig.minWeight.get());
                    weightRatio = weightRatio / (OverstuffedClientConfig.maxWeight.get() - OverstuffedClientConfig.minWeight.get());
                    int calculatedPercentage = (int) ((weightRatio * 100));
                    int xOf5 = calculatedPercentage / 20;

                    ClientWeightBarData.setLastWeightStage(xOf5);
                }
                String layerName= OverstuffedClientConfig.weightLayerConfigEntry.get();
                //this is the starting point, the stage if you will
                playersAPI.playAnimation(OverstuffedClientConfig.lastWeightLayer, 0);

                int outOfMax = (int) (ClientWeightBarData.getLastWeightStage() * 0.2 * playersAPI.getAnimationMaxValue(layerName));
                //System.out.println("Amount through stage "+ClientWeightBarData.getAmountThroughStage());
                if(outOfMax!=-1)
                {
                    playersAPI.playAnimation(OverstuffedClientConfig.weightLayerConfigEntry.get(), outOfMax + ClientWeightBarData.getAmountThroughStage());
                }

                return true;
            }

        }
        return false;
      }

    public static int getTotalWeightFrames() {

        if(ModList.get().isLoaded("cpm")) {
            if (!OverstuffedClientConfig.weightLayerConfigEntry.get().contentEquals("")) {
                totalWeightFrames=playersAPI.getAnimationMaxValue(OverstuffedClientConfig.weightLayerConfigEntry.get());
            }
        }

        return totalWeightFrames;
    }

    public static int getTotalStuffedFrames() {
        if(ModList.get().isLoaded("cpm")) {
            if (!OverstuffedClientConfig.stuffedLayerConfigEntry.get().contentEquals("")) {
                totalStuffedFrames=playersAPI.getAnimationMaxValue(OverstuffedClientConfig.stuffedLayerConfigEntry.get());
            }
        }
        return totalStuffedFrames;
    }


    //utilized to check if CPM is at least the latest version
    //if first is greater than second returns one
    public static int compareVersions(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");

        //does the min, ideally they are always the same but this way they don't crash.
        int length = Math.min(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            try
            {
                int v1=0;
                int v2=0;
                for(int e=0;e<v1Parts[i].length();e++)
                {
                    v1=0;
                    v2=0;
                    v1+=v1Parts[i].charAt(e);
                    v2+=v2Parts[i].charAt(e);
                    if (v1 < v2) return -1;
                    if (v1 > v2) return 1;
                }


            } catch (NumberFormatException e) {

            }



        }
        return 0;
    }
}


