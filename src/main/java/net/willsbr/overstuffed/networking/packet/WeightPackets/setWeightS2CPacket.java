package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.client.ClientWeightBarData;

import java.util.Objects;
import java.util.function.Supplier;

public class setWeightS2CPacket {
    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.WeightBar";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static  int weight;
    public setWeightS2CPacket(int inputWeight){
        weight =inputWeight;

    }

    public setWeightS2CPacket(FriendlyByteBuf buf){
        weight =buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(weight);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {
                    //here we are on the client
                    LocalPlayer player= Minecraft.getInstance().player;
                    Objects.requireNonNull(player).getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                        weightBar.setCurrentWeight(this.weight);
                    });

                        ClientWeightBarData.setCurrentWeight(this.weight);
                        CPMData.checkIfUpdateCPM("weight");


                }
        );
        return true;
    }
}
