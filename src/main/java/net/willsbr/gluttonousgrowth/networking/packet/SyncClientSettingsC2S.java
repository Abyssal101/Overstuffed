package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;

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
        this.weightEffects= GluttonousClientConfig.weightEffects.get();
        this.stageBased= GluttonousClientConfig.stageGain.get();
        this.burpFrequency= GluttonousClientConfig.burpFrequency.get();
        this.gurgleFrequency= GluttonousClientConfig.gurgleFrequency.get();
        this.maxHitboxWidth= GluttonousClientConfig.maxHitboxWidth.get();
        this.hitboxScaling= GluttonousClientConfig.hitBoxScalingEnabled.get();
        this.totalStages= GluttonousClientConfig.totalStages.get();

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
                weightBar.setEffectsReady(true);
            });

        });
        return true;
    }
}
