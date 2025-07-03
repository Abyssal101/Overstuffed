package net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.gluttonousgrowth.client.ClientCPMData;
import net.willsbr.gluttonousgrowth.client.ClientCalorieMeter;

import java.util.function.Supplier;

public class setMaxCaloriesPacketS2C {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.OverfullFood";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    public int max;

    public setMaxCaloriesPacketS2C(int newMax) {
       this.max=newMax;

    }

    public setMaxCaloriesPacketS2C(FriendlyByteBuf buf){
        this.max = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(max);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();

        context.enqueueWork(() ->
                {


                    LocalPlayer player= Minecraft.getInstance().player;
                    player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER)
                            .ifPresent(calorieMeter -> {
                       calorieMeter.setMaxCalories(max);

                    });
                    ClientCalorieMeter.setMaxCalories(max);

                    ClientCPMData.playStuffed();
                }
        );

        context.setPacketHandled(true);
        return true;
    }


}
