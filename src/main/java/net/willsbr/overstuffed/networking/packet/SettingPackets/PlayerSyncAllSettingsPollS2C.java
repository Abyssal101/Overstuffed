package net.willsbr.overstuffed.networking.packet.SettingPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.config.OverstuffedConfig;
import net.willsbr.overstuffed.networking.ModMessages;

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
