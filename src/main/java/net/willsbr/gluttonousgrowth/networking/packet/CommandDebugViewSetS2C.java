package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;

import java.util.function.Supplier;

public class CommandDebugViewSetS2C {

    //sending data from server to client here

    Boolean clientEnabled;

    public CommandDebugViewSetS2C(boolean enabled){

        clientEnabled=enabled;
    }

    public CommandDebugViewSetS2C(FriendlyByteBuf buf){

        this.clientEnabled=buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf){
       // buf.writeUtf(this.stuffedLayer);

        buf.writeBoolean(this.clientEnabled);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            GluttonousClientConfig.debugView.set(clientEnabled);
        });
        return true;
    }
}
