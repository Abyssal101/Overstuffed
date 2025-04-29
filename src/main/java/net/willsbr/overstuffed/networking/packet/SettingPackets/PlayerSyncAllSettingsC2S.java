package net.willsbr.overstuffed.networking.packet.SettingPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.function.Supplier;

public class PlayerSyncAllSettingsC2S {

    private boolean weightEffects;

    private boolean stageBased;
    //sending data from server to client here
    private int burpFrequency=0;

    private int gurgleFrequency=0;



    public PlayerSyncAllSettingsC2S(boolean stage, boolean ef,int burp, int gurgle) {
        this.stageBased = stage;
        this.weightEffects = ef;
        this.burpFrequency=burp;
        this.gurgleFrequency=gurgle;
    }

    public PlayerSyncAllSettingsC2S(FriendlyByteBuf buf){
        this.weightEffects = buf.readBoolean();
        this.stageBased = buf.readBoolean();
        this.burpFrequency = buf.readInt();
        this.gurgleFrequency = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
       buf.writeBoolean(weightEffects);
       buf.writeBoolean(stageBased);
       buf.writeInt(burpFrequency);
       buf.writeInt(gurgleFrequency);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            ServerPlayer player=context.getSender();
            player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                serverSettings.setWeightEffects(weightEffects);
                serverSettings.setStageGain(stageBased);
                serverSettings.setBurpFrequency(burpFrequency);
                serverSettings.setGurgleFrequency(gurgleFrequency);
            });



        });
        return true;
    }
    public static PlayerSyncAllSettingsC2S setSpecifc(String name, boolean input)
    {
        if(name.equals("effects"))
        {
            return new PlayerSyncAllSettingsC2S(input,OverstuffedConfig.stageGain.get(),OverstuffedConfig.burpFrequency.get(),OverstuffedConfig.gurgleFrequency.get());
        }
        else if(name.equals("stage"))
        {
            return new PlayerSyncAllSettingsC2S(OverstuffedConfig.weightEffects.get(),input,OverstuffedConfig.burpFrequency.get(),OverstuffedConfig.gurgleFrequency.get());
        }
        return null;
    }




}
