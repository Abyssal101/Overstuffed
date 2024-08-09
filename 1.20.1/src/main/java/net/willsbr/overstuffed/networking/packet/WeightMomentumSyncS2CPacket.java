package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.networking.ModMessages;

import java.util.function.Supplier;

public class WeightMomentumSyncS2CPacket {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.CPMData";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private int momentumValue;


    public WeightMomentumSyncS2CPacket(int movement){

        this.momentumValue = movement;
    }

    public WeightMomentumSyncS2CPacket(FriendlyByteBuf buf){
        this.momentumValue =buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
       buf.writeInt(this.momentumValue);


    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    ServerLevel level=player.serverLevel();
                    if(level.isClientSide)
                    {
                        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
                        {
                            //player.setDeltaMovement(player.get);
                        });
                    }
                        //output current thirst level
                        //player.getFoodData().getFoodLevel();

                }
        );
        return true;
    }


}
