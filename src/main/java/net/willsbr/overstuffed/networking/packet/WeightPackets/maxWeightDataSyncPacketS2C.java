package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;

import java.util.Objects;
import java.util.function.Supplier;

public class maxWeightDataSyncPacketS2C {

   private int maxWeight;
   //sending data from server to client here


    public maxWeightDataSyncPacketS2C(int maxWeight){
        this.maxWeight =maxWeight;

    }

    public maxWeightDataSyncPacketS2C(FriendlyByteBuf buf){
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
            LocalPlayer player= Minecraft.getInstance().player;
            Objects.requireNonNull(player).getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.setMaxWeight(this.maxWeight);
            });
            OverstuffedClientConfig.maxWeight.set(this.maxWeight);
            CPMData.checkIfUpdateCPM("weight");
            context.getSender().sendSystemMessage(Component.translatable("maxweightupdatesuccess"));
        });
        return true;
    }



}
