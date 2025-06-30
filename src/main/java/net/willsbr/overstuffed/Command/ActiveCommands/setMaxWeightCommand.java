package net.willsbr.overstuffed.Command.ActiveCommands;

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
import net.willsbr.overstuffed.networking.packet.WeightPackets.maxWeightDataSyncPacketS2C;

public class setMaxWeightCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("ggconfig").then(Commands.literal("setMaxWeight").then(Commands.argument("Max Weight", IntegerArgumentType.integer()).executes((p_138618_) -> {
            return setMaxWeight(p_138618_.getSource(),p_138618_.getSource().getPlayer(), IntegerArgumentType.getInteger(p_138618_,"Max Weight"));
        }))));
    }

    private static int setMaxWeight(CommandSourceStack pSource, Player player, int index) throws CommandSyntaxException {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            if(index>0 && index>weightBar.getMinWeight())
            {
                weightBar.setMaxWeight(index);
                ModMessages.sendToPlayer(new maxWeightDataSyncPacketS2C(weightBar.getCurMaxWeight()),(ServerPlayer) player);
            }
            else{
                if(index<0)
                {
                    player.sendSystemMessage(Component.translatable("commands.overstuffed.setbelowzero"));
                }
                else {
                    player.sendSystemMessage(Component.translatable("commands.overstuffed.maxbelowmin"));

                }

            }


        });
        //why does it need to be int???
        return 0;
    }

}
