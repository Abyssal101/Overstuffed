package net.willsbr.overstuffed.CPMCompat.Capability;

import net.minecraft.nbt.CompoundTag;
import net.willsbr.overstuffed.client.ClientCPMData;

public class CPMData {

    //this is the fucken tempalte

    private String stuffedLayerName;
    private String lastStuffedLayerName;

    private String weightLayerName;

    private String lastWeightLayerName;

    private int stuffedLayerFrames;
    private int weightLayerFrames;


    public CPMData(){
        this.stuffedLayerName="";
        this.lastStuffedLayerName="";
        this.weightLayerName="";
        this.lastWeightLayerName="";
        this.stuffedLayerFrames=100;
        this.weightLayerFrames=100;

    }

    public void setInfo(String belly)
    {
        this.stuffedLayerName =belly;
    }
    public void setStuffed(String stuffedInput)
    {
        if(this.stuffedLayerName!=null)
        {
           this.lastStuffedLayerName=this.stuffedLayerName;
        }

        this.stuffedLayerName=stuffedInput;
       // checkIfUpdateCPM("stuffed");
    }
    public String getStuffedLayerName()
    {
        return stuffedLayerName;
    }
    public static boolean checkIfUpdateCPM(String type)
    {
//        if(lastSavedStuffed!=ClientStuffedBarData.getPlayerStuffedBar())
//        {

        //ALL THIS SHOULD BE EXECUTED ON THE CLIENT
            if(type.contentEquals("stuffed"))
            {
                return ClientCPMData.playStuffed();
            }
            else if(type.contentEquals("weight")){
                return ClientCPMData.playWeight();
            }
            return false;

    }


    public void copyFrom(CPMData source)
    {

        this.stuffedLayerName =source.getStuffedLayerName();
        //this.lastStuffedLayerName=source.getLastStuffedLayerName();
        this.weightLayerName=source.getWeightLayerName();
        //this.lastWeightLayerName=source.getLastWeightLayerName();

        //add more as neccesatra
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putString("stuffedlayer", stuffedLayerName);
        nbt.putString("weightlayer", weightLayerName);
        nbt.putInt("stuffedlayerframes", stuffedLayerFrames);
        nbt.putInt("weightlayerframes", weightLayerFrames);

    }
    public void loadNBTData(CompoundTag nbt)
    {
        this.stuffedLayerName =nbt.getString("stuffedlayer");
        this.weightLayerName = nbt.getString("weightlayer");
        this.stuffedLayerFrames = nbt.getInt("stuffedlayerframes");
        this.weightLayerFrames = nbt.getInt("weightlayerframes");
    }

    public String getWeightLayerName() {
        return weightLayerName;
    }

    public void setWeightLayerName(String weightLayerName) {
        this.weightLayerName = weightLayerName;
    }

    public String getLastWeightLayerName() {
        return lastWeightLayerName;
    }

    public void setLastWeightLayerName(String lastWeightLayerName) {
        this.lastWeightLayerName = lastWeightLayerName;
    }

    public String getLastStuffedLayerName() {
        return lastStuffedLayerName;
    }

    public void setLastStuffedLayerName(String lastStuffedLayerName) {
        this.lastStuffedLayerName = lastStuffedLayerName;
    }

    public void setStuffedFrames(int frames)
    {
        this.stuffedLayerFrames = frames;
    }
    public int getStuffedLayerFrames()
    {
        return this.stuffedLayerFrames;
    }
    public void setWeightFrames(int frames)
    {
        this.weightLayerFrames = frames;
    }
    public int getWeightLayerFrames()
    {
        return this.weightLayerFrames;
    }


}
