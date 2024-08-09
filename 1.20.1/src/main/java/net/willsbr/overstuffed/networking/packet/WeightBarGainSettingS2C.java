package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.function.Supplier;

public class WeightBarGainSettingS2C {

   private boolean levelBased;
   //sending data from server to client here


    public WeightBarGainSettingS2C(boolean levelBased){
        this.levelBased = levelBased;
    }

    public WeightBarGainSettingS2C(FriendlyByteBuf buf){
        this.levelBased =buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(levelBased);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            //on client
            ClientWeightBarData.setBurstGain(this.levelBased);
        });
        return true;
    }



}
