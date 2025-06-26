package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.Objects;
import java.util.function.Supplier;

public class QueuedWeightSyncS2CPacket {

    private final int queuedWeight;
    private final int totalQueuedWeight;


    //sending data from server to client here


    public QueuedWeightSyncS2CPacket(int queue,int totalQueue){
            this.queuedWeight = queue;
            this.totalQueuedWeight = totalQueue;
    }

    public QueuedWeightSyncS2CPacket(FriendlyByteBuf buf){
            this.queuedWeight =buf.readInt();
            this.totalQueuedWeight =buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(this.queuedWeight);
        buf.writeInt(this.totalQueuedWeight);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            LocalPlayer player= Minecraft.getInstance().player;
            Objects.requireNonNull(player).getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.setQueuedWeight(queuedWeight);
            });
            ClientWeightBarData.setQueuedWeight(queuedWeight,totalQueuedWeight);
        });
        return true;
    }
}
