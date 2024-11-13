package net.willsbr.overstuffed.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.config.OverstuffedConfig;

public class setBurpFrequency {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("setBurpFrequency").then(Commands.argument("Value from 0-10", IntegerArgumentType.integer()).executes((p_138618_) -> {
            return setBurpFrequency(p_138618_.getSource(),p_138618_.getSource().getPlayer(), IntegerArgumentType.getInteger(p_138618_,"Value from 0-10"));
        }))));
    }

    private static int setBurpFrequency(CommandSourceStack pSource, Player player, int value) throws CommandSyntaxException {
            if(value>=0 && value<=10)
            {
                //playerToggles.setToggleValue(1,value);
                OverstuffedConfig.burpFrequency.set(value);
            }
            else {
                player.sendSystemMessage(Component.literal("Error: Value outside of 1-10"));
            }

        return 0;
    }

}
