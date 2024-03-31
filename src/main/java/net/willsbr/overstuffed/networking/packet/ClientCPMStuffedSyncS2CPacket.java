package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientCPMConfigData;

import java.util.function.Supplier;

public class ClientCPMStuffedSyncS2CPacket {

    private final String stuffedLayer;


    //sending data from server to client here


    public ClientCPMStuffedSyncS2CPacket(String inputLayer){

            this.stuffedLayer = inputLayer;


    }

    public ClientCPMStuffedSyncS2CPacket(FriendlyByteBuf buf){
        String output=buf.readUtf();

            this.stuffedLayer = output;




    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeUtf(this.stuffedLayer);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            // ClientThirstData.set(stuffed_bar);
            ClientCPMConfigData.setStuffed(this.stuffedLayer);

            CPMData.checkIfUpdateCPM("stuffed");
        });
        return true;
    }
}
