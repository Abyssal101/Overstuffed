package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class WeightBarDataSyncPacketS2C {

   private int weight;
   //sending data from server to client here


    public WeightBarDataSyncPacketS2C(int incomingWeight){
        this.weight =incomingWeight;

    }

    public WeightBarDataSyncPacketS2C(FriendlyByteBuf buf){
        this.weight =buf.readInt();


    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(weight);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
           // ClientThirstData.set(stuffed_bar);
            ClientWeightBarData.setCurrentWeight(weight);
            CPMData.checkIfUpdateCPM("weight");

            //CPMData.checkIfUpdateCPM();
        });
        return true;
    }



}
