package net.willsbr.gluttonousgrowth.networking.packet.WeightPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMData;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;

import java.util.Objects;
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
            GluttonousClientConfig.minWeight.set(minWeight);
            LocalPlayer player= Minecraft.getInstance().player;
            Objects.requireNonNull(player).getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.setMinWeight(minWeight);
            });
            CPMData.checkIfUpdateCPM("weight");
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.overstuffed.minweightupdatesuccess"));
        });
        return true;
    }



}
