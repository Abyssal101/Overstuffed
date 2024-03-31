package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class WeightSettingsDataSyncPacketS2C {

   private int maxWeight;
   //sending data from server to client here


    public WeightSettingsDataSyncPacketS2C(int maxWeight){
        this.maxWeight =maxWeight;

    }

    public WeightSettingsDataSyncPacketS2C(FriendlyByteBuf buf){
        this.maxWeight =buf.readInt();


    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(maxWeight);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
           // ClientThirstData.set(stuffed_bar);
            ClientWeightBarData.setMaxWeight(this.maxWeight);
            //CPMData.checkIfUpdateCPM();
        });
        return true;
    }



}
