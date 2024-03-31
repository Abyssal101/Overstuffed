package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.client.ClientCPMConfigData;

import java.util.function.Supplier;

public class ClientCPMWeightSyncS2CPacket {

    private final String weightLayer;


    //sending data from server to client here


    public ClientCPMWeightSyncS2CPacket(String inputLayer){

            this.weightLayer = inputLayer;


    }

    public ClientCPMWeightSyncS2CPacket(FriendlyByteBuf buf){
        String output=buf.readUtf();

            this.weightLayer = output;




    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeUtf(this.weightLayer);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {

            //here we are on the client!
            // ClientThirstData.set(stuffed_bar);
            ClientCPMConfigData.setWeight(this.weightLayer);
            CPMData.checkIfUpdateCPM("weight");
            if(ClientCPMConfigData.getWeightLayer().contentEquals(this.weightLayer))
            {
                context.getSender().sendSystemMessage(Component.literal("Weight Layer sucessfully updated"));
            }

        });
        return true;
    }
}
