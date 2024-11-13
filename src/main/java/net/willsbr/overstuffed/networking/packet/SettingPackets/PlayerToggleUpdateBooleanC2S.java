package net.willsbr.overstuffed.networking.packet.SettingPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;

import java.util.function.Supplier;

public class PlayerToggleUpdateBooleanC2S {

   private boolean settingStatus;

   private int settingIndex;
   //sending data from server to client here


    public PlayerToggleUpdateBooleanC2S(int index, boolean inputBoolean){
        this.settingStatus = inputBoolean;
        this.settingIndex=index;
    }

    public PlayerToggleUpdateBooleanC2S(FriendlyByteBuf buf){
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
         //on Server
            context.getSender().getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(settings ->
                    {
                        settings.setToggle(this.settingIndex,this.settingStatus);
                    });



        });
        return true;
    }



}
