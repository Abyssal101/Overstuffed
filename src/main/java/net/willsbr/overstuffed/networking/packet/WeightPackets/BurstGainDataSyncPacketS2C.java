package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class BurstGainDataSyncPacketS2C {

   private int stage;
   private int amountThrough;
   //sending data from server to client here

    public BurstGainDataSyncPacketS2C(int incomingStage, int amountThrough){
        this.stage =incomingStage;
        this.amountThrough=amountThrough;
    }

    public BurstGainDataSyncPacketS2C(FriendlyByteBuf buf){
        this.stage =buf.readInt();
        this.amountThrough=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(stage);
        buf.writeInt(amountThrough);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            ClientWeightBarData.setLastWeightStage(stage);
            ClientWeightBarData.setAmountThroughStage(amountThrough);
            CPMData.checkIfUpdateCPM("weight");
        });
        return true;
    }



}
