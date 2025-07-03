package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMDataProvider;

import java.util.function.Supplier;

public class SyncCPMDataCapS2C {

    //sending data from server to client here

    CompoundTag cpmData;


    public SyncCPMDataCapS2C(CompoundTag cpm){


        cpmData=cpm;


    }

    public SyncCPMDataCapS2C(FriendlyByteBuf buf){
         this.cpmData=buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(this.cpmData);



    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!

            Minecraft.getInstance().player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
                cpmData.loadNBTData(this.cpmData);
            });


        });
        return true;
    }
}
