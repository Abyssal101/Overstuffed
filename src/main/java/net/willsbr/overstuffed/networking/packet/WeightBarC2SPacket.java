package net.willsbr.overstuffed.networking.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.networking.ModMessages;

import java.util.function.Supplier;

public class WeightBarC2SPacket {
    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.WeightBar";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static  int foodWeight;
    public WeightBarC2SPacket(int foodCal){
        foodWeight=foodCal;

    }

    public WeightBarC2SPacket(FriendlyByteBuf buf){
        foodWeight=buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(foodWeight);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    ServerLevel level=player.getLevel();
                    if(!level.isClientSide)
                    {

                        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
                        {
                            //this adds the eaten food to the weight queue to get updated
                            weightBar.addWeightChanges(foodWeight);

                            //ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(foodWeight),player);
                        });
                    }

                }
        );
        return true;
    }
}
