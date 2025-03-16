package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientStuffedBarData;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class weightIntervalUpdateS2CPacket {
    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.WeightBar";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static  int currentLost;
    private static  int interval;

    public weightIntervalUpdateS2CPacket(int currentL, int intervl){
        currentLost = currentL;
        interval = intervl;

    }

    public weightIntervalUpdateS2CPacket(FriendlyByteBuf buf){
        currentLost =buf.readInt();
        interval = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(currentLost);
        buf.writeInt(interval);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {
                    //here we are on the client

                    ClientStuffedBarData.setInterval(interval);
                    ClientStuffedBarData.setCurrentLost(currentLost);
                }
        );
        return true;
    }
}
