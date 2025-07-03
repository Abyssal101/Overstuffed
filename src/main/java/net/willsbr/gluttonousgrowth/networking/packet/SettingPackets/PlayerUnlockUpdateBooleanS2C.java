package net.willsbr.gluttonousgrowth.networking.packet.SettingPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.client.ClientUnlockData;

import java.util.function.Supplier;

public class PlayerUnlockUpdateBooleanS2C {

   private boolean settingStatus;

   private int settingIndex;
   //sending data from server to client here


    public PlayerUnlockUpdateBooleanS2C(int index, boolean inputBoolean){
        this.settingStatus = inputBoolean;
        this.settingIndex=index;
    }

    public PlayerUnlockUpdateBooleanS2C(FriendlyByteBuf buf){
        this.settingStatus =buf.readBoolean();
        this.settingIndex=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(settingStatus);
        buf.writeInt(settingIndex);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
         //on client
            ClientUnlockData.setAdvancementStatus(this.settingIndex,this.settingIndex);

        });
        return true;
    }



}
