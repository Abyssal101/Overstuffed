package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;

import java.util.function.Supplier;

public class SyncStuffedCapS2C {

    //sending data from server to client here

    CompoundTag stuffedBar;


    public SyncStuffedCapS2C(CompoundTag stuffed){
        stuffedBar=stuffed;
    }

    public SyncStuffedCapS2C(FriendlyByteBuf buf){
         this.stuffedBar=buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf){
       // buf.writeUtf(this.stuffedLayer);
        buf.writeNbt(this.stuffedBar);


    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            Minecraft.getInstance().player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(stuffedBar -> {
                stuffedBar.loadNBTData(this.stuffedBar);
            });

        });
        return true;
    }
}
