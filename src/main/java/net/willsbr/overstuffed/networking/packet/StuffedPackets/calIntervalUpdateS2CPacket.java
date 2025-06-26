package net.willsbr.overstuffed.networking.packet.StuffedPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.overstuffed.client.ClientCalorieMeter;

import java.util.Objects;
import java.util.function.Supplier;

public class calIntervalUpdateS2CPacket {
    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.WeightBar";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static  int currentLost;
    private static  int interval;

    public calIntervalUpdateS2CPacket(int currentL, int intervl){
        currentLost = currentL;
        interval = intervl;

    }

    public calIntervalUpdateS2CPacket(FriendlyByteBuf buf){
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

                    Objects.requireNonNull(player).getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(stuffedBar -> {
                        stuffedBar.setInterval(interval);
                        stuffedBar.setCalLost(currentLost);
                    });
                    ClientCalorieMeter.setInterval(interval);
                    ClientCalorieMeter.setCurrentLost(currentLost);
                }
        );
        return true;
    }
}
