package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.OverstuffedConfig;
import net.willsbr.overstuffed.networking.ModMessages;

import java.util.function.Supplier;

public class WeightMaxMinPollS2C {

   //sending data from server to client here


    public WeightMaxMinPollS2C(){

    }

    public WeightMaxMinPollS2C(FriendlyByteBuf buf){


    }

    public void toBytes(FriendlyByteBuf buf){

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            int max=OverstuffedConfig.getMaxWeight();
            int min=OverstuffedConfig.getMinWeight();
            ModMessages.sendToServer(new setMaxWeightDataSyncPacketC2S(max));
            ModMessages.sendToServer(new setMinWeightDataSyncPacketC2S(min));

        });
        return true;
    }



}
