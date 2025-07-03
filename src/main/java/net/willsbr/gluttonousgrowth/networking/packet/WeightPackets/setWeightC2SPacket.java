package net.willsbr.gluttonousgrowth.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;

import java.util.function.Supplier;

public class setWeightC2SPacket {
    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.WeightBar";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static  int weight;
    public setWeightC2SPacket(int inputWeight){
        weight =inputWeight;

    }

    public setWeightC2SPacket(FriendlyByteBuf buf){
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

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    Level level=player.level();
                    if(!level.isClientSide)
                    {

                        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
                        {
                            //this adds the eaten food to the weight queue to get updated
                            weightBar.setCurrentWeight(this.weight);
                        });
                    }

                }
        );
        return true;
    }
}
