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
import net.willsbr.overstuffed.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.setMaxCaloriesPacketS2C;

public class setMaxCalories {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setWeight.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed")
                .then(Commands.literal("setMaxCalories")
                .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("maxCal", IntegerArgumentType.integer())
                        .executes(context ->
                                setMaxStuffed(context.getSource(),
                                        IntegerArgumentType.getInteger(context,"maxCal"))))));
    }

    private static int setMaxStuffed(CommandSourceStack pSource, int newMaxCal) throws CommandSyntaxException {
        if(pSource.hasPermission(2))
        {

            if(pSource.isPlayer())
            {
                //TODO Make it so you can target other people
                ServerPlayer player=pSource.getPlayer();
                player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(calorieMeter -> {
                    calorieMeter.setMaxCalories(newMaxCal);
                });
                ModMessages.sendToPlayer(new setMaxCaloriesPacketS2C(newMaxCal), player);
            }
        }
        else
        {
            pSource.sendSystemMessage(Component.literal("Error: You need admin permission to use this command"));
        }

        return 0;
    }

}
