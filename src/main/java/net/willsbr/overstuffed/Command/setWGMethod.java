package net.willsbr.overstuffed.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sun.jdi.connect.Connector;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.AdvancementToggle.PlayerTogglesProvider;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.*;

public class setWGMethod {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("levelBasedGaining").then(Commands.argument("0-false,1-true", StringArgumentType.string()).executes((p_138618_) -> {
            return levelBasedGaining(p_138618_.getSource(),p_138618_.getSource().getPlayer(), StringArgumentType.getString(p_138618_,"0-false,1-true") );
        }))));
    }

    private static int levelBasedGaining(CommandSourceStack pSource, Player player, String input) throws CommandSyntaxException {

        player.getCapability(PlayerTogglesProvider.PLAYER_TOGGLES).ifPresent(playerToggles -> {
            if(input.toLowerCase().contentEquals("false"))
            {
                playerToggles.setToggle(0,false);
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    weightBar.setLastWeightStage(-1);
                        });

                ModMessages.sendToPlayer(new PlayerToggleUpdateS2C(0,false),(ServerPlayer) player);
            }
            else  if(input.toLowerCase().contentEquals("true"))
            {
                playerToggles.setToggle(0,true);
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    int calculatedPercentage=(int)(((double) ClientWeightBarData.getPlayerWeight())/ClientWeightBarData.getMaxWeight()*100);
                    int xOf5=calculatedPercentage/20;
                    weightBar.setLastWeightStage(xOf5);
                    ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()),(ServerPlayer) player);
                });
                ModMessages.sendToPlayer(new PlayerToggleUpdateS2C(0,true),(ServerPlayer) player);

            }
            else
            {
                player.sendSystemMessage(Component.literal("Invalid input, should be *true* or *false*"));
            }
        });
        return 0;
    }

}
