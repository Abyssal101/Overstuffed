package net.willsbr.overstuffed.Command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setMinWeightDataSyncPacketS2C;

public class setMinWeightCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("setMinWeight").then(Commands.argument("Minimum Weight", IntegerArgumentType.integer()).executes((p_138618_) -> {
            return setMinWeight(p_138618_.getSource(),p_138618_.getSource().getPlayer(), IntegerArgumentType.getInteger(p_138618_,"Minimum Weight"));
        }))));
    }

    private static int setMinWeight(CommandSourceStack pSource, Player player, int index) throws CommandSyntaxException {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            if(index>0 && index<weightBar.getCurMaxWeight())
            {
                weightBar.setMinWeight(index);
                ModMessages.sendToPlayer(new setMinWeightDataSyncPacketS2C(index),(ServerPlayer) player);
            }
            else{
                if (index<0)
                {
                    player.sendSystemMessage(Component.translatable("commands.overstuffed.setbelowzero"));
                }
                else{
                    player.sendSystemMessage(Component.translatable("commands.overstuffed.minabovemax"));
                }
            }
        });
        return Command.SINGLE_SUCCESS;
    }

}
