package net.willsbr.overstuffed.networking.packet.WeightPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;

import java.util.function.Supplier;

public class addWeightC2SPacket {
    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.WeightBar";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static  int queuedWeight;
    public addWeightC2SPacket(int inputWeight){
        queuedWeight =inputWeight;

    }

    public addWeightC2SPacket(FriendlyByteBuf buf){
        queuedWeight =buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(queuedWeight);

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
                            weightBar.addWeightChanges(queuedWeight);

                        });
                    }

                }
        );
        return true;
    }
}
