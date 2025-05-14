package net.willsbr.overstuffed.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.function.Supplier;

public class SyncCapsS2C {

    //sending data from server to client here
    CompoundTag weightBar;
    CompoundTag stuffedBar;
    CompoundTag cpmData;
    CompoundTag settingsData;

    public SyncCapsS2C(CompoundTag weight, CompoundTag stuffed, CompoundTag cpm, CompoundTag settings){

        weightBar=weight;
        stuffedBar=stuffed;
        cpmData=cpm;
        settingsData=settings;


    }

    public SyncCapsS2C(FriendlyByteBuf buf){
        String output=buf.readUtf();
        this.weightBar=buf.readNbt();
         this.stuffedBar=buf.readNbt();
         this.cpmData=buf.readNbt();
         this.settingsData=buf.readNbt();




    }

    public void toBytes(FriendlyByteBuf buf){
       // buf.writeUtf(this.stuffedLayer);
        buf.writeNbt(this.weightBar);
        buf.writeNbt(this.stuffedBar);
        buf.writeNbt(this.cpmData);
        buf.writeNbt(this.settingsData);


    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!

            Minecraft.getInstance().player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
                cpmData.loadNBTData(this.cpmData);
            });

            Minecraft.getInstance().player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
                stuffedBar.loadNBTData(this.stuffedBar);
            });
            Minecraft.getInstance().player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.loadNBTData(this.weightBar);
            });
            Minecraft.getInstance().player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                serverSettings.loadNBTData(this.settingsData);
            });

        });
        return true;
    }
}
