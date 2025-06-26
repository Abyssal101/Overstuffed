package net.willsbr.overstuffed.Command.DisabledCommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.ClientCPMStuffedSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.WeightPackets.ClientCPMWeightSyncS2CPacket;

public class SetLayer {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("setLayer").then(Commands.literal("stuffed").then(Commands.argument("layerName",StringArgumentType.string()).executes((p_138618_) -> {
            return setStuffed(p_138618_.getSource(),p_138618_.getSource().getPlayer(), StringArgumentType.getString(p_138618_,"layerName")+"");
        })))));

        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("setLayer").then(Commands.literal("weight").then(Commands.argument("newLayerName",StringArgumentType.string()).executes((p_138618_) -> {
            return setWeight(p_138618_.getSource(),p_138618_.getSource().getPlayer(), StringArgumentType.getString(p_138618_,"newLayerName")+"");
        })))));
    }

    private static int setStuffed(CommandSourceStack pSource, Player player, String stuffedLayerName) throws CommandSyntaxException {
        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
            if(!stuffedLayerName.contentEquals(cpmData.getStuffedLayerName()))
            {
                cpmData.setStuffed(stuffedLayerName);
                ModMessages.sendToPlayer(new ClientCPMStuffedSyncS2CPacket(stuffedLayerName),(ServerPlayer) player);
            }
            else {
                player.sendSystemMessage(Component.translatable("commands.overstuffed.stuffedupdatesamename"));
            }
        });
        return Command.SINGLE_SUCCESS;
    }
    private static int setWeight(CommandSourceStack pSource, Player player, String weightLayerName) throws CommandSyntaxException {
        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
            if(!weightLayerName.contentEquals(cpmData.getWeightLayerName()))
            {
                cpmData.setWeightLayerName(weightLayerName);
                ModMessages.sendToPlayer(new ClientCPMWeightSyncS2CPacket(weightLayerName),(ServerPlayer) player);
            }
            else {
                player.sendSystemMessage(Component.translatable("commands.overstuffed.errorsamename"));
            }
        });
        //why does it need to be int???
        return Command.SINGLE_SUCCESS;
    }

}
