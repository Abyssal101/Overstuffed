package net.willsbr.overstuffed.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;

import java.util.function.Supplier;

public class SyncClientSettingsC2S {

    //sending data from server to client here

    private boolean weightEffects=true;
    private boolean stageBased=false;

    private int burpFrequency=0;
    private int gurgleFrequency=0;

    private float maxHitboxWidth=1.0f;
    private boolean hitboxScaling=false;
    private int totalStages=5;


    public SyncClientSettingsC2S(){
        this.weightEffects=OverstuffedClientConfig.weightEffects.get();
        this.stageBased=OverstuffedClientConfig.stageGain.get();
        this.burpFrequency=OverstuffedClientConfig.burpFrequency.get();
        this.gurgleFrequency=OverstuffedClientConfig.gurgleFrequency.get();
        this.maxHitboxWidth=OverstuffedClientConfig.maxHitboxWidth.get();
        this.hitboxScaling=OverstuffedClientConfig.hitBoxScalingEnabled.get();
        this.totalStages=OverstuffedClientConfig.totalStages.get();

    }

    public SyncClientSettingsC2S(FriendlyByteBuf buf){
         this.weightEffects=buf.readBoolean();
         this.stageBased=buf.readBoolean();
         this.burpFrequency=buf.readInt();
         this.gurgleFrequency=buf.readInt();
         this.maxHitboxWidth=buf.readFloat();
         this.hitboxScaling=buf.readBoolean();
         this.totalStages=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
       // buf.writeUtf(this.stuffedLayer);

       buf.writeBoolean(weightEffects);
       buf.writeBoolean(stageBased);
       buf.writeInt(burpFrequency);
       buf.writeInt(gurgleFrequency);
       buf.writeFloat(maxHitboxWidth);
       buf.writeBoolean(hitboxScaling);
       buf.writeInt(totalStages);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {

            context.getSender().getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                serverSettings.setWeightEffects(weightEffects);
                serverSettings.setStageGain(stageBased);
                serverSettings.setBurpFrequency(burpFrequency);
                serverSettings.setGurgleFrequency(gurgleFrequency);
                serverSettings.setMaxHitboxWidth(maxHitboxWidth);
                serverSettings.setHitboxScalingEnabled(hitboxScaling);
            });
            context.getSender().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.setTotalStages(totalStages);
            });

        });
        return true;
    }
}
