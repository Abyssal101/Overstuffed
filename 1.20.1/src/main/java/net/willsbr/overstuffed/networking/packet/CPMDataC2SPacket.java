package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.networking.ModMessages;

import java.util.function.Supplier;

public class CPMDataC2SPacket {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.CPMData";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private String stuffedLayer;


    public CPMDataC2SPacket(String stuffedLayer){

        this.stuffedLayer = stuffedLayer;
    }

    public CPMDataC2SPacket(FriendlyByteBuf buf){
        this.stuffedLayer=buf.readUtf();

    }

    public void toBytes(FriendlyByteBuf buf){
       buf.writeUtf(this.stuffedLayer);


    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    ServerLevel level=player.serverLevel();
                    if(!level.isClientSide)
                    {
                        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(playerCPM ->
                        {
                            playerCPM.setStuffed(this.stuffedLayer);
                            ModMessages.sendToPlayer(new ClientCPMStuffedSyncS2CPacket(this.stuffedLayer),player);
                        });
                    }
                        //output current thirst level
                        //player.getFoodData().getFoodLevel();

                }
        );
        return true;
    }


}
