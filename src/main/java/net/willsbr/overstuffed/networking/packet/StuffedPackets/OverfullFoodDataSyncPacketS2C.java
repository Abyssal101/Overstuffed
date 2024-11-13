package net.willsbr.overstuffed.networking.packet.StuffedPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientStuffedBarData;

import java.util.function.Supplier;

public class OverfullFoodDataSyncPacketS2C {

   private final int stuffed_bar;
   private final int currentSoftLimit;

   private final int currentFirmLimit;
   private final int currentHardLimit;
   //sending data from server to client here


    public OverfullFoodDataSyncPacketS2C(int stuffed_bar, int soft, int firm, int hard){
        this.stuffed_bar = stuffed_bar;
        this.currentSoftLimit=soft;
        this.currentFirmLimit=firm;
        this.currentHardLimit=hard;

    }

    public OverfullFoodDataSyncPacketS2C(FriendlyByteBuf buf){
        this.stuffed_bar =buf.readInt();
        this.currentSoftLimit=buf.readInt();
        this.currentFirmLimit=buf.readInt();
        this.currentHardLimit=buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(stuffed_bar);
        buf.writeInt(currentSoftLimit);
        buf.writeInt(this.currentFirmLimit);
        buf.writeInt(currentHardLimit);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
           // ClientThirstData.set(stuffed_bar);
            ClientStuffedBarData.set(stuffed_bar,currentSoftLimit,currentFirmLimit,currentHardLimit);
            CPMData.checkIfUpdateCPM("stuffed");

        });
        return true;
    }



}
