package net.willsbr.overstuffed.networking.packet.StuffedPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.overstuffed.client.ClientCalorieMeter;

import java.util.function.Supplier;

public class OverfullFoodDataSyncPacketS2C {

   private int currentCalories;
   private int currentMaxCalories;
   private double modThreshold;
   private double slowThreshold;



    public OverfullFoodDataSyncPacketS2C(int currentCalories, int curMax, double modThreshold, double slowThreshold){
        this.currentCalories = currentCalories;
        this.currentMaxCalories =curMax;
        this.modThreshold = modThreshold;
        this.slowThreshold = slowThreshold;
    }

    public OverfullFoodDataSyncPacketS2C(FriendlyByteBuf buf){
        this.currentCalories =buf.readInt();
        this.currentMaxCalories =buf.readInt();
        this.modThreshold = buf.readDouble();
        this.slowThreshold = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(currentCalories);
        buf.writeInt(currentMaxCalories);
        buf.writeDouble(modThreshold);
        buf.writeDouble(slowThreshold);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
           // ClientThirstData.set(stuffed_bar);
            ClientCalorieMeter.set(currentCalories, currentMaxCalories);
            ClientCalorieMeter.setThresholds(modThreshold, slowThreshold);

            LocalPlayer player= Minecraft.getInstance().player;
            player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER)
                    .ifPresent(calorieMeter -> {
                       calorieMeter.setCurrentCalories(currentCalories);
                       calorieMeter.setMaxCalories(currentMaxCalories);
                       calorieMeter.setModMetabolismThres(modThreshold);
                       calorieMeter.setSlowMetabolismThres(slowThreshold);
                    });

            CPMData.checkIfUpdateCPM("stuffed");

        });
        return true;
    }



}
