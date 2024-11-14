package net.willsbr.overstuffed.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.networking.packet.*;
import net.willsbr.overstuffed.networking.packet.SettingPackets.*;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.ClientCPMStuffedSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.OverfullFoodC2SPacket;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.OverfullFoodDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.WeightPackets.*;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId=0;
    private static int id(){
        return packetId++;
    }
    public static void register()
    {
    SimpleChannel net= NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(OverStuffed.MODID, "messages")).networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions( s-> true)
            .serverAcceptedVersions(s->true)
            .simpleChannel();
    INSTANCE=net;

    //dupe this for new messages
//dif message

        //stuffed bar
        net.messageBuilder(OverfullFoodC2SPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(OverfullFoodC2SPacket::new)
                .encoder(OverfullFoodC2SPacket::toBytes)
                .consumerMainThread(OverfullFoodC2SPacket::handle)
                .add();

        net.messageBuilder(MovementUpdatesC2S.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MovementUpdatesC2S::new)
                .encoder(MovementUpdatesC2S::toBytes)
                .consumerMainThread(MovementUpdatesC2S::handle)
                .add();

        //since we are sending to client we need to change it to play to clinet
        net.messageBuilder(OverfullFoodDataSyncPacketS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OverfullFoodDataSyncPacketS2C::new)
                .encoder(OverfullFoodDataSyncPacketS2C::toBytes)
                .consumerMainThread(OverfullFoodDataSyncPacketS2C::handle)
                .add();

        net.messageBuilder(ClientCPMStuffedSyncS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientCPMStuffedSyncS2CPacket::new)
                .encoder(ClientCPMStuffedSyncS2CPacket::toBytes)
                .consumerMainThread(ClientCPMStuffedSyncS2CPacket::handle)
                .add();
        net.messageBuilder(ClientCPMWeightSyncS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientCPMWeightSyncS2CPacket::new)
                .encoder(ClientCPMWeightSyncS2CPacket::toBytes)
                .consumerMainThread(ClientCPMWeightSyncS2CPacket::handle)
                .add();
        net.messageBuilder(CPMDataC2SPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CPMDataC2SPacket::new)
                .encoder(CPMDataC2SPacket::toBytes)
                .consumerMainThread(CPMDataC2SPacket::handle)
                .add();

        net.messageBuilder(addWeightC2SPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(addWeightC2SPacket::new)
                .encoder(addWeightC2SPacket::toBytes)
                .consumerMainThread(addWeightC2SPacket::handle)
                .add();
        net.messageBuilder(setWeightC2SPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(setWeightC2SPacket::new)
                .encoder(setWeightC2SPacket::toBytes)
                .consumerMainThread(setWeightC2SPacket::handle)
                .add();


        net.messageBuilder(OverstuffedEffectC2SPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(OverstuffedEffectC2SPacket::new)
                .encoder(OverstuffedEffectC2SPacket::toBytes)
                .consumerMainThread(OverstuffedEffectC2SPacket::handle)
                .add();
        net.messageBuilder(WeightBarDataSyncPacketS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(WeightBarDataSyncPacketS2C::new)
                .encoder(WeightBarDataSyncPacketS2C::toBytes)
                .consumerMainThread(WeightBarDataSyncPacketS2C::handle)
                .add();

        net.messageBuilder(BurstGainDataSyncPacketS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BurstGainDataSyncPacketS2C::new)
                .encoder(BurstGainDataSyncPacketS2C::toBytes)
                .consumerMainThread(BurstGainDataSyncPacketS2C::handle)
                .add();
        net.messageBuilder(addWeightC2SPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(addWeightC2SPacket::new)
                .encoder(addWeightC2SPacket::toBytes)
                .consumerMainThread(addWeightC2SPacket::handle)
                .add();
        net.messageBuilder(setWeightS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(setWeightS2CPacket::new)
                .encoder(setWeightS2CPacket::toBytes)
                .consumerMainThread(setWeightS2CPacket::handle)
                .add();

        net.messageBuilder(setMinWeightDataSyncPacketC2S.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(setMinWeightDataSyncPacketC2S::new)
                .encoder(setMinWeightDataSyncPacketC2S::toBytes)
                .consumerMainThread(setMinWeightDataSyncPacketC2S::handle)
                .add();
        net.messageBuilder(setMinWeightDataSyncPacketS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(setMinWeightDataSyncPacketS2C::new)
                .encoder(setMinWeightDataSyncPacketS2C::toBytes)
                .consumerMainThread(setMinWeightDataSyncPacketS2C::handle)
                .add();
        net.messageBuilder(setMaxWeightDataSyncPacketC2S.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(setMaxWeightDataSyncPacketC2S::new)
                .encoder(setMaxWeightDataSyncPacketC2S::toBytes)
                .consumerMainThread(setMaxWeightDataSyncPacketC2S::handle)
                .add();
        net.messageBuilder(maxWeightDataSyncPacketS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(maxWeightDataSyncPacketS2C::new)
                .encoder(maxWeightDataSyncPacketS2C::toBytes)
                .consumerMainThread(maxWeightDataSyncPacketS2C::handle)
                .add();
        net.messageBuilder(PlayerToggleUpdateBooleanS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerToggleUpdateBooleanS2C::new)
                .encoder(PlayerToggleUpdateBooleanS2C::toBytes)
                .consumerMainThread(PlayerToggleUpdateBooleanS2C::handle)
                .add();

        net.messageBuilder(PlayerToggleUpdateBooleanC2S.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerToggleUpdateBooleanC2S::new)
                .encoder(PlayerToggleUpdateBooleanC2S::toBytes)
                .consumerMainThread(PlayerToggleUpdateBooleanC2S::handle)
                .add();
        net.messageBuilder(PlayerToggleUpdateIntegerS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerToggleUpdateIntegerS2C::new)
                .encoder(PlayerToggleUpdateIntegerS2C::toBytes)
                .consumerMainThread(PlayerToggleUpdateIntegerS2C::handle)
                .add();
        net.messageBuilder(PlayerUnlockUpdateBooleanS2C.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerUnlockUpdateBooleanS2C::new)
                .encoder(PlayerUnlockUpdateBooleanS2C::toBytes)
                .consumerMainThread(PlayerUnlockUpdateBooleanS2C::handle)
                .add();
        net.messageBuilder(WeightMomentumSyncS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(WeightMomentumSyncS2CPacket::new)
                .encoder(WeightMomentumSyncS2CPacket::toBytes)
                .consumerMainThread(WeightMomentumSyncS2CPacket::handle)
                .add();

    }


    public static <MSG> void sendToServer(MSG message)
    {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
    {

        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),message);
    }

}
