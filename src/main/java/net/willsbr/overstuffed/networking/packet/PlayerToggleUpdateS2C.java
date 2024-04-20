package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.client.ClientTogglesData;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class PlayerToggleUpdateS2C {

   private boolean settingStatus;

   private int settingIndex;
   //sending data from server to client here


    public PlayerToggleUpdateS2C(int index, boolean inputBoolean){
        this.settingStatus = inputBoolean;
        this.settingIndex=index;
    }

    public PlayerToggleUpdateS2C(FriendlyByteBuf buf){
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
            ClientTogglesData.setToggle(this.settingIndex,this.settingStatus);

            //specific code for client based
            if(this.settingIndex==0 && this.settingStatus==false)
            {
                ClientWeightBarData.setLastWeightStage(-1);
            }



        });
        return true;
    }



}
