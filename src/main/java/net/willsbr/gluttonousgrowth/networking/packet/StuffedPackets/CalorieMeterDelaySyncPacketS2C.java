package net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMData;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.gluttonousgrowth.client.ClientCalorieMeter;

import java.util.function.Supplier;

public class CalorieMeterDelaySyncPacketS2C {

   private int delay;
   private long foodTick;


    public CalorieMeterDelaySyncPacketS2C(int delay, long foodTick){
        this.delay = delay;
        this.foodTick =foodTick;

    }

    public CalorieMeterDelaySyncPacketS2C(FriendlyByteBuf buf){
        this.delay =buf.readInt();
        this.foodTick =buf.readLong();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(delay);
        buf.writeLong(foodTick);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
           // ClientThirstData.set(stuffed_bar);
          ClientCalorieMeter.setCurrentDelay(delay);
          ClientCalorieMeter.setCurrentSavedTick(foodTick);

            LocalPlayer player= Minecraft.getInstance().player;
            player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER)
                    .ifPresent(calorieMeter -> {
                        calorieMeter.setCalClearDelay(delay);
                       calorieMeter.setFoodEatenTick(foodTick);
                    });

            CPMData.checkIfUpdateCPM("stuffed");

        });
        return true;
    }



}
