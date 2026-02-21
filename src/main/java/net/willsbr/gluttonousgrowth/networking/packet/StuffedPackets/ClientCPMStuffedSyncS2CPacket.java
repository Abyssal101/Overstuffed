package net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMData;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;

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
            GluttonousClientConfig.setStuffedLayer(this.stuffedLayer);
            CPMData.checkIfUpdateCPM("stuffed");
            if(GluttonousClientConfig.stuffedLayerConfigEntry.get().contentEquals(this.stuffedLayer))
            {
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.overstuffed.stuffedupdatesuccess"));
            }
        });
        return true;
    }
}
