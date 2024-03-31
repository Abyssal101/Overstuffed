package net.willsbr.overstuffed.Command;

import com.mojang.brigadier.CommandDispatcher;
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
import net.willsbr.overstuffed.networking.packet.ClientCPMStuffedSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.ClientCPMWeightSyncS2CPacket;

public class viewLayers {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("viewLayers").executes((p_138618_) -> {
            return viewLayers(p_138618_.getSource(),p_138618_.getSource().getPlayer());
        })));
    }

    private static int viewLayers(CommandSourceStack pSource, Player player) throws CommandSyntaxException {

        //this will clear all values for layers, hopefully helps bugtesting
        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {

            player.sendSystemMessage(Component.literal("Current Stuffed Layer:"+cpmData.getStuffedLayerName()));
            player.sendSystemMessage(Component.literal("Last Stuffed Layer:"+cpmData.getLastStuffedLayerName()));
            player.sendSystemMessage(Component.literal("Current Weight Layer:"+cpmData.getWeightLayerName()));
            player.sendSystemMessage(Component.literal("Last Weight Layer:"+cpmData.getLastWeightLayerName()));


        });
        //why does it need to be int???
        return 0;
    }

}
