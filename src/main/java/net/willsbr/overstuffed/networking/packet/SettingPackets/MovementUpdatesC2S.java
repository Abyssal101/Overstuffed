package net.willsbr.overstuffed.networking.packet.SettingPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MovementUpdatesC2S {

    private static final String MOVEMENT_FAILED ="message.overstuffed.movement";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private int velocity;

    public MovementUpdatesC2S(int addedVelocity){
        this.velocity=addedVelocity;
    }

    public MovementUpdatesC2S(FriendlyByteBuf buf){
            this.velocity=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
       buf.writeInt(this.velocity);


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
                        player.setDeltaMovement(player.getDeltaMovement().add(velocity,velocity,velocity));
                    }
                        //output current thirst level
                        //player.getFoodData().getFoodLevel();

                }
        );
        return true;
    }


}
