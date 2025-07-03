package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMDataProvider;

import java.util.function.Supplier;

public class CPMDataC2SPacket {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.CPMData";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private String stuffedLayer;
    private String weightLayer;
    private int stuffedFrames;
    private int weightFrames;


    public CPMDataC2SPacket(String stuffedLayer, String weight, int sFrame, int wFrames){

        this.stuffedLayer = stuffedLayer;
        this.weightLayer=weight;
        this.stuffedFrames=sFrame;
        this.weightFrames=wFrames;
    }

    public CPMDataC2SPacket(FriendlyByteBuf buf){
        this.stuffedLayer=buf.readUtf();
        this.weightLayer=buf.readUtf();
        this.stuffedFrames=buf.readInt();
        this.weightFrames=buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
       buf.writeUtf(this.stuffedLayer);
       buf.writeUtf(this.weightLayer);
       buf.writeInt(this.stuffedFrames);
       buf.writeInt(this.weightFrames);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {


                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    assert player != null;
                    Level level=player.level();
                    if(!level.isClientSide)
                    {
                        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(playerCPM ->
                        {
                            playerCPM.setStuffed(this.stuffedLayer);
                            playerCPM.setWeightLayerName(this.weightLayer);
                            playerCPM.setStuffedFrames(this.stuffedFrames);
                            playerCPM.setWeightFrames(this.weightFrames);
                        });
                    }
                        //output current thirst level
                        //player.getFoodData().getFoodLevel();

                }
        );
        return true;
    }


}
