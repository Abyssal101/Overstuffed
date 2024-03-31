package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class setMinWeightDataSyncPacketS2C {

   private int minWeight;
   //sending data from server to client here


    public setMinWeightDataSyncPacketS2C(int weight){
        this.minWeight =weight;

    }

    public setMinWeightDataSyncPacketS2C(FriendlyByteBuf buf){
        this.minWeight =buf.readInt();


    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(minWeight);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
           // ClientThirstData.set(stuffed_bar);
            ClientWeightBarData.setMinWeight(this.minWeight);
            //CPMData.checkIfUpdateCPM();
        });
        return true;
    }



}
