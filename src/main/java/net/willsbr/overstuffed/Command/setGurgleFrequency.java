package net.willsbr.overstuffed.Command;

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
import net.willsbr.overstuffed.AdvancementToggle.PlayerTogglesProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.PlayerToggleUpdateIntegerS2C;

public class setGurgleFrequency {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("setGurgleFrequency").then(Commands.argument("Value from 0-10", IntegerArgumentType.integer()).executes((p_138618_) -> {
            return setGurgleFrequency(p_138618_.getSource(),p_138618_.getSource().getPlayer(), IntegerArgumentType.getInteger(p_138618_,"Value from 0-10"));
        }))));
    }

    private static int setGurgleFrequency(CommandSourceStack pSource, Player player, int value) throws CommandSyntaxException {
        player.getCapability(PlayerTogglesProvider.PLAYER_TOGGLES).ifPresent(playerToggles -> {
            if(value>=0 && value<=10)
            {
                playerToggles.setToggleValue(2,value);
                ModMessages.sendToPlayer(new PlayerToggleUpdateIntegerS2C(2, value),  (ServerPlayer) player);
            }
            else {
                player.sendSystemMessage(Component.literal("Error: Value outside of 1-10"));
            }

        });
        return 0;
    }

}
