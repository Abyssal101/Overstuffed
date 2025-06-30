package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.GluttonousClientConfig;
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
            int max= GluttonousClientConfig.getMaxWeight();
            int min= GluttonousClientConfig.getMinWeight();
            LocalPlayer player= Minecraft.getInstance().player;
            player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.setMaxWeight(max);
                weightBar.setMinWeight(min);
            });

            ModMessages.sendToServer(new setMaxWeightDataSyncPacketC2S(max));
            ModMessages.sendToServer(new setMinWeightDataSyncPacketC2S(min));

        });
        return true;
    }



}
