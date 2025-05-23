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
import net.willsbr.overstuffed.networking.packet.StuffedPackets.*;
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
        net.messageBuilder(setMaxStuffedS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(setMaxStuffedS2CPacket::new)
                .encoder(setMaxStuffedS2CPacket::toBytes)
                .consumerMainThread(setMaxStuffedS2CPacket::handle)
                .add();

        net.messageBuilder(ClientCPMWeightSyncS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientCPMWeightSyncS2CPacket::new)
                .encoder(ClientCPMWeightSyncS2CPacket::toBytes)
                .consumerMainThread(ClientCPMWeightSyncS2CPacket::handle)
                .add();
        net.messageBuilder(QueuedWeightSyncS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(QueuedWeightSyncS2CPacket::new)
                .encoder(QueuedWeightSyncS2CPacket::toBytes)
                .consumerMainThread(QueuedWeightSyncS2CPacket::handle)
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

        net.messageBuilder(WeightMaxMinPollS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(WeightMaxMinPollS2C::new)
                .encoder(WeightMaxMinPollS2C::toBytes)
                .consumerMainThread(WeightMaxMinPollS2C::handle)
                .add();
        net.messageBuilder(stuffedIntervalUpdateS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(stuffedIntervalUpdateS2CPacket::new)
                .encoder(stuffedIntervalUpdateS2CPacket::toBytes)
                .consumerMainThread(stuffedIntervalUpdateS2CPacket::handle)
                .add();

        //Server Sync Settings
        net.messageBuilder(PlayerSyncAllSettingsC2S.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerSyncAllSettingsC2S::new)
                .encoder(PlayerSyncAllSettingsC2S::toBytes)
                .consumerMainThread(PlayerSyncAllSettingsC2S::handle)
                .add();
        net.messageBuilder(PlayerSyncAllSettingsPollS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerSyncAllSettingsPollS2C::new)
                .encoder(PlayerSyncAllSettingsPollS2C::toBytes)
                .consumerMainThread(PlayerSyncAllSettingsPollS2C::handle)
                .add();
        net.messageBuilder(SyncCapsS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncCapsS2C::new)
                .encoder(SyncCapsS2C::toBytes)
                .consumerMainThread(SyncCapsS2C::handle)
                .add();
        net.messageBuilder(SyncCPMDataCapS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncCPMDataCapS2C::new)
                .encoder(SyncCPMDataCapS2C::toBytes)
                .consumerMainThread(SyncCPMDataCapS2C::handle)
                .add();
        net.messageBuilder(SyncServerSettingCapS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncServerSettingCapS2C::new)
                .encoder(SyncServerSettingCapS2C::toBytes)
                .consumerMainThread(SyncServerSettingCapS2C::handle)
                .add();
        net.messageBuilder(SyncStuffedCapS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncStuffedCapS2C::new)
                .encoder(SyncStuffedCapS2C::toBytes)
                .consumerMainThread(SyncStuffedCapS2C::handle)
                .add();
        net.messageBuilder(SyncWeightCapS2C.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncWeightCapS2C::new)
                .encoder(SyncWeightCapS2C::toBytes)
                .consumerMainThread(SyncWeightCapS2C::handle)
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
