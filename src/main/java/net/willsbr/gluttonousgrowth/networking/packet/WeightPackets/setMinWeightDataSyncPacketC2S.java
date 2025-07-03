package net.willsbr.gluttonousgrowth.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;

import java.util.function.Supplier;

public class setMinWeightDataSyncPacketC2S {

   private int minWeight;
   //sending data from Client to Server here


    public setMinWeightDataSyncPacketC2S(int weight){
        this.minWeight =weight;

    }

    public setMinWeightDataSyncPacketC2S(FriendlyByteBuf buf){
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
            //here we are on the server!
           // ClientThirstData.set(stuffed_bar);

            context.getSender().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
            {
                weightBar.setMinWeight(this.minWeight);

            });
            //CPMData.checkIfUpdateCPM();setMinWeightDataSyncPacketC2S
        });
        return true;
    }



}
