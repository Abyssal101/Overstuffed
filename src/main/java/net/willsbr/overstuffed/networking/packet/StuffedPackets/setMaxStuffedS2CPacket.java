package net.willsbr.overstuffed.networking.packet.StuffedPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.client.ClientCPMData;
import net.willsbr.overstuffed.client.ClientStuffedBarData;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.util.function.Supplier;

public class setMaxStuffedS2CPacket {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.OverfullFood";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private int newFullLimit;
    private int newStuffedLimit;
    private int newOverstuffedLimit;
    private int newAddState;

    public setMaxStuffedS2CPacket(int fullLim, int stuffLim, int overLim, int addState) {
       this.newFullLimit = fullLim;
       this.newStuffedLimit = stuffLim;
       this.newOverstuffedLimit = overLim;
       this.newAddState = addState;

    }

    public setMaxStuffedS2CPacket(FriendlyByteBuf buf){
        this.newFullLimit = buf.readInt();
        this.newStuffedLimit = buf.readInt();
        this.newOverstuffedLimit = buf.readInt();
        this.newAddState = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(newFullLimit);
        buf.writeInt(newStuffedLimit);
        buf.writeInt(newOverstuffedLimit);
        buf.writeInt(newAddState);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();

        context.enqueueWork(() ->
                {
                        int sum=newFullLimit+newStuffedLimit+newOverstuffedLimit;
                        ClientStuffedBarData.set(Math.min(ClientStuffedBarData.getPlayerStuffedBar(),sum),this.newFullLimit,
                                this.newStuffedLimit,this.newOverstuffedLimit);
                    ClientCPMData.playStuffed();
                }
        );

        context.setPacketHandled(true);
        return true;
    }


}
