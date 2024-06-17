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
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.setMinWeightDataSyncPacketC2S;

public class setMinWeight {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("setMinWeight")).then(Commands.argument("minWeight", IntegerArgumentType.integer()).executes((p_138618_) -> {
            return setMinWeight(p_138618_.getSource(),p_138618_.getSource().getPlayer(), IntegerArgumentType.getInteger(p_138618_,"minWeight"));
        })));
    }

    private static int setMinWeight(CommandSourceStack pSource, Player player, int index) throws CommandSyntaxException {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
           // weightBar.set
            ModMessages.sendToPlayer(new setMinWeightDataSyncPacketC2S(index),(ServerPlayer) player);
        });



        //why does it need to be int???
        return 0;
    }

}
