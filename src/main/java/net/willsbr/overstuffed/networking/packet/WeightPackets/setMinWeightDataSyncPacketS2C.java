package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.function.Supplier;

public class setMinWeightDataSyncPacketS2C {

   private int minWeight;
   //sending data from Server to client here


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
            OverstuffedConfig.minWeight.set(minWeight);
            CPMData.checkIfUpdateCPM("weight");
            context.getSender().sendSystemMessage(Component.translatable("commands.overstuffed.minweightupdatesuccess"));
        });
        return true;
    }



}
