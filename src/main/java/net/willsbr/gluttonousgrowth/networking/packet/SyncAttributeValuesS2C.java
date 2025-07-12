package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.client.ClientWeightBarData;

import java.util.function.Supplier;

public class SyncAttributeValuesS2C {

    //sending data from server to client here

    private int weightHealthBoost;
    private double weightSpeedDecrease;

    private double currentAddedScale;
    private int scalingHealthBoost;


    //This is for sending the modified attribute values to the client to make debugging way way way easier for myself


    public SyncAttributeValuesS2C(int wHealth, double wSpeed, double addScale, int sHealth){
    weightHealthBoost=wHealth;
    weightSpeedDecrease=wSpeed;
    currentAddedScale=addScale;
    scalingHealthBoost=sHealth;
    }

    public SyncAttributeValuesS2C(FriendlyByteBuf buf){
        weightHealthBoost=buf.readInt();
        weightSpeedDecrease=buf.readDouble();
        currentAddedScale=buf.readDouble();
        scalingHealthBoost=buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
       // buf.writeUtf(this.stuffedLayer);
        buf.writeInt(weightHealthBoost);
        buf.writeDouble(weightSpeedDecrease);
        buf.writeDouble(currentAddedScale);
        buf.writeInt(scalingHealthBoost);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            ClientWeightBarData.setWeightHealthBoost(weightHealthBoost);
            ClientWeightBarData.setWeightSpeedLoss(weightSpeedDecrease);
            ClientWeightBarData.setCurrentAddedScale(currentAddedScale);
            ClientWeightBarData.setScalingHealthBoost(scalingHealthBoost);
        });
        return true;
    }
}
