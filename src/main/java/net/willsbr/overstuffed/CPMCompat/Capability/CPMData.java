package net.willsbr.overstuffed.CPMCompat.Capability;

import com.tom.cpl.nbt.NBTTagCompound;
import com.tom.cpm.api.IClientAPI;
import net.minecraft.nbt.CompoundTag;
import net.willsbr.overstuffed.CPMCompat.CPMMessageSenders;
import net.willsbr.overstuffed.client.ClientCPMConfigData;
import net.willsbr.overstuffed.client.ClientStuffedBarData;
import net.willsbr.overstuffed.client.ClientWeightBarData;

public class CPMData {

    //this is the fucken tempalte

    private String stuffedLayerName;
    private String lastStuffedLayerName;

    private String weightLayerName;

    private String lastWeightLayerName;

    public CPMData(){
        this.stuffedLayerName="";
        this.lastStuffedLayerName="";
        this.weightLayerName="";
        this.lastWeightLayerName="";


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
    public static void checkIfUpdateCPM(String type)
    {
//        if(lastSavedStuffed!=ClientStuffedBarData.getPlayerStuffedBar())
//        {
        //System.out.println("FUck");

           // Player.player.sendSystemMessage(Component.literal("SOMETHING"));

        //ALL THIS SHOULD BE EXECUTED ON THE CLIENT
            if(type.contentEquals("stuffed"))
            {
                ClientCPMConfigData.playStuffed();
            }
            else if(type.contentEquals("weight")){
                //System.out.println("playerStuffedBarO");
                ClientCPMConfigData.playWeight();
            }
            


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

    }
    public void loadNBTData(CompoundTag nbt)
    {
        this.stuffedLayerName =nbt.getString("stuffedlayer");
        this.weightLayerName = nbt.getString("weightlayer");
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
}
