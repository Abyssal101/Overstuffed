package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;

import java.util.function.Supplier;

public class SyncWeightCapS2C {

    //sending data from server to client here
    CompoundTag weightBar;


    public SyncWeightCapS2C(CompoundTag weight){
        weightBar=weight;
    }

    public SyncWeightCapS2C(FriendlyByteBuf buf){
        this.weightBar=buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf){
       // buf.writeUtf(this.stuffedLayer);
        buf.writeNbt(this.weightBar);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //here we are on the client!

            Minecraft.getInstance().player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.loadNBTData(this.weightBar);
            });

        });
        return true;
    }
}
