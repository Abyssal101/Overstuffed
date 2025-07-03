package net.willsbr.gluttonousgrowth.networking.packet.SettingPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerSyncAllSettingsPollS2C {

    public PlayerSyncAllSettingsPollS2C() {

    }

    public PlayerSyncAllSettingsPollS2C(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
//            if(context.getSender()==null)
//            {
//                ModMessages.sendToServer(new PlayerSyncAllSettingsC2S(OverstuffedConfig.stageGain.get(),OverstuffedConfig.weightEffects.get(),
//                        OverstuffedConfig.burpFrequency.get(),OverstuffedConfig.gurgleFrequency.get()));
//            }

        });
        return true;
    }

}
