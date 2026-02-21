package net.willsbr.gluttonousgrowth.Command.ActiveCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.networking.packet.CommandDebugViewSetS2C;
import net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets.setMaxCaloriesPacketS2C;

public class debugViewEnabled {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setWeight.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("ggconfig")
                .then(Commands.literal("debugview")
                .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("true/false", BoolArgumentType.bool())
                        .executes(context ->
                                setDebugView(context.getSource(),
                                        BoolArgumentType.getBool(context,"true/false"))))));
    }

    private static int setDebugView(CommandSourceStack pSource, Boolean enabled) throws CommandSyntaxException {
        if(pSource.hasPermission(2))
        {
            if(pSource.isPlayer())
            {
                //TODO Make it so you can target other people
                ServerPlayer player=pSource.getPlayer();
                ModMessages.sendToPlayer(new CommandDebugViewSetS2C(enabled), player);
            }
        }
        else
        {
            pSource.sendSystemMessage(Component.literal("Error: You need admin permission to use this command"));
        }

        return 0;
    }

}
