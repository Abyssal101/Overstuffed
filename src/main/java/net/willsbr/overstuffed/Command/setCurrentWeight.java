package net.willsbr.overstuffed.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setWeightS2CPacket;

public class setCurrentWeight {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setWeight.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed")
                .then(Commands.literal("setCurrentWeight")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.players())
                        .then(Commands.argument("newWeight", IntegerArgumentType.integer())
                        .executes(context ->
                                setWeight(context.getSource(), EntityArgument.getPlayer(context, "target"),IntegerArgumentType.getInteger(context,"newWeight")))).
                executes(context -> setWeight(context.getSource(), context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context,"newWeight"))))));
    }

    private static int setWeight(CommandSourceStack pSource, ServerPlayer player, int index) throws CommandSyntaxException {
        if(player.hasPermissions(2))
        {
            player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                if(index>weightBar.getCurMaxWeight() || index<weightBar.getMinWeight())
                {
                    player.sendSystemMessage(Component.literal("Error: Set weight out of range of their max/min values"));

                }
                else{
                    System.out.println(weightBar.getCurrentWeight());
                    weightBar.setCurrentWeight(index);
                    ModMessages.sendToPlayer(new setWeightS2CPacket(index),(ServerPlayer) player);
                    PlayerWeightBar.addCorrectModifier(player);
                    player.sendSystemMessage(Component.translatable("commands.overstuffed.setweightsuccess",Component.literal(player.getDisplayName().getString()).withStyle(ChatFormatting.DARK_GRAY),Component.literal(index+"").withStyle(ChatFormatting.DARK_GRAY)));
                }

            });
        }
        else
        {
            player.sendSystemMessage(Component.literal("Error: You need admin permission to use this command"));
        }

        return 0;
    }

}
