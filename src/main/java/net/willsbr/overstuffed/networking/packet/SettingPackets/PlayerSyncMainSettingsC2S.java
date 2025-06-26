package net.willsbr.overstuffed.networking.packet.SettingPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;

import java.util.function.Supplier;

public class PlayerSyncMainSettingsC2S {

    private boolean weightEffects;

    private boolean stageBased;
    //sending data from server to client here

    public PlayerSyncMainSettingsC2S(boolean stage, boolean ef) {
        this.stageBased = stage;
        this.weightEffects = ef;

    }

    public PlayerSyncMainSettingsC2S(FriendlyByteBuf buf){
        this.weightEffects = buf.readBoolean();
        this.stageBased = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf){
       buf.writeBoolean(weightEffects);
       buf.writeBoolean(stageBased);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();
        ServerPlayer player = context.getSender();

        if (player == null) {
            return false;
        }

        context.enqueueWork(() -> {
            player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                serverSettings.setWeightEffects(weightEffects);
                serverSettings.setStageGain(stageBased);
            });
        });

        context.setPacketHandled(true);
        return true;

    }





}
