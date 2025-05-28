package net.willsbr.overstuffed.networking.packet.StuffedPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.client.ClientStuffedBarData;

import java.util.Objects;
import java.util.function.Supplier;

public class stuffedIntervalUpdateS2CPacket {
    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.WeightBar";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static  int currentLost;
    private static  int interval;

    public stuffedIntervalUpdateS2CPacket(int currentL, int intervl){
        currentLost = currentL;
        interval = intervl;

    }

    public stuffedIntervalUpdateS2CPacket(FriendlyByteBuf buf){
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
                    LocalPlayer player= Minecraft.getInstance().player;

                    Objects.requireNonNull(player).getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
                        stuffedBar.setInterval(interval);
                        stuffedBar.setStuffedLossed(currentLost);
                    });
                    ClientStuffedBarData.setInterval(interval);
                    ClientStuffedBarData.setCurrentLost(currentLost);
                }
        );
        return true;
    }
}
