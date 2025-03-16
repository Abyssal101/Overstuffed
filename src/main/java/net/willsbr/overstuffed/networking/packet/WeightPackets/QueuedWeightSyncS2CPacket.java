package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.function.Supplier;

public class QueuedWeightSyncS2CPacket {

    private final int queuedWeight;


    //sending data from server to client here


    public QueuedWeightSyncS2CPacket(int queue){
            this.queuedWeight = queue;
    }

    public QueuedWeightSyncS2CPacket(FriendlyByteBuf buf){

            this.queuedWeight =buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(this.queuedWeight);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            ClientWeightBarData.setQueuedWeight(queuedWeight);
        });
        return true;
    }
}
