package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.function.Supplier;

public class PlayerToggleUpdateBooleanS2C {

   private boolean settingStatus;

   private int settingIndex;
   //sending data from server to client here


    public PlayerToggleUpdateBooleanS2C(int index, boolean inputBoolean){
        this.settingStatus = inputBoolean;
        this.settingIndex=index;
    }

    public PlayerToggleUpdateBooleanS2C(FriendlyByteBuf buf){
        this.settingStatus =buf.readBoolean();
        this.settingIndex=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(settingStatus);
        buf.writeInt(settingIndex);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
         //on client
            OverstuffedConfig.setSetting(this.settingIndex,this.settingStatus);
            if(this.settingIndex==0 && this.settingStatus==false)
            {
                ClientWeightBarData.setLastWeightStage(-1);
            }



        });
        return true;
    }



}
