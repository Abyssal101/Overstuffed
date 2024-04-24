package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.client.ClientTogglesData;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class PlayerToggleUpdateIntegerS2C {

   private int settingValue;

   private int settingIndex;
   //sending data from server to client here


    public PlayerToggleUpdateIntegerS2C(int index, int value){
        this.settingValue = value;
        this.settingIndex=index;
    }

    public PlayerToggleUpdateIntegerS2C(FriendlyByteBuf buf){
        this.settingValue =buf.readInt();
        this.settingIndex=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(settingValue);
        buf.writeInt(settingIndex);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
         //on client
            ClientTogglesData.setValueToggle(this.settingIndex,this.settingValue);
        });
        return true;
    }



}
