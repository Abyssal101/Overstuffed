package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;

import java.util.function.Supplier;

public class SyncServerSettingCapS2C {

    //sending data from server to client here

    CompoundTag settingsData;

    public SyncServerSettingCapS2C(CompoundTag settings){

        settingsData=settings;
    }

    public SyncServerSettingCapS2C(FriendlyByteBuf buf){
         this.settingsData=buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf){
       // buf.writeUtf(this.stuffedLayer);

        buf.writeNbt(this.settingsData);


    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!
            Minecraft.getInstance().player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                serverSettings.loadNBTData(this.settingsData);
            });

        });
        return true;
    }
}
