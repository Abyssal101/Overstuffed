package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;

import java.util.function.Supplier;

public class setMaxWeightDataSyncPacketC2S {

   private int maxWeight;
   //sending data from Client to Server here


    public setMaxWeightDataSyncPacketC2S(int weight){
        this.maxWeight =weight;

    }

    public setMaxWeightDataSyncPacketC2S(FriendlyByteBuf buf){
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

            context.getSender().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
            {
                weightBar.setMaxWeight(this.maxWeight);

            });
            //CPMData.checkIfUpdateCPM();
        });
        return true;
    }



}
