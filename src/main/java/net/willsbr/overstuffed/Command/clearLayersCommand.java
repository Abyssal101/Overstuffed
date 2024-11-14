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
import net.willsbr.overstuffed.networking.packet.StuffedPackets.ClientCPMStuffedSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.WeightPackets.ClientCPMWeightSyncS2CPacket;

public class clearLayersCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("clearLayers").executes((p_138618_) -> {
            return clearLayers(p_138618_.getSource(),p_138618_.getSource().getPlayer());
        })));
    }

    private static int clearLayers(CommandSourceStack pSource, Player player) throws CommandSyntaxException {

        //this will clear all values for layers, hopefully helps bugtesting
        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
            cpmData.setStuffed("");
           cpmData.setLastStuffedLayerName("");
           cpmData.setWeightLayerName("");
           cpmData.setLastWeightLayerName("");

            ModMessages.sendToPlayer(new ClientCPMStuffedSyncS2CPacket(""),(ServerPlayer) player);
            ModMessages.sendToPlayer(new ClientCPMWeightSyncS2CPacket(""), (ServerPlayer) player);
            player.sendSystemMessage(Component.literal("Overstuffed layers cleared"));


        });
        //why does it need to be int???
        return 0;
    }

}
