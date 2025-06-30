package net.willsbr.overstuffed.Command.DisabledCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.GluttonousClientConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.WeightPackets.WeightBarDataSyncPacketS2C;

public class setWGMethod {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("levelBasedGaining").then(Commands.argument("0-false,1-true", StringArgumentType.string()).executes((p_138618_) -> {
            return levelBasedGaining(p_138618_.getSource(),p_138618_.getSource().getPlayer(), StringArgumentType.getString(p_138618_,"0-false,1-true") );
        }))));
    }

    private static int levelBasedGaining(CommandSourceStack pSource, Player player, String input) throws CommandSyntaxException {

        player.getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(playerToggles -> {
            player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                if(input.toLowerCase().contentEquals("false"))
                {
                    serverSettings.setStageGain(false);
                    player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                        weightBar.setLastWeightStage(-1);
                    });

                }
                else  if(input.toLowerCase().contentEquals("true"))
                {
                    player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                        int calculatedPercentage=(int)(((double) ClientWeightBarData.getPlayerWeight()- GluttonousClientConfig.minWeight.get())/(GluttonousClientConfig.maxWeight.get()- GluttonousClientConfig.minWeight.get())*100);
                        int xOf5=calculatedPercentage/20;
                        weightBar.setLastWeightStage(xOf5);
                        ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()),(ServerPlayer) player);
                    });
                    //ModMessages.sendToPlayer(new PlayerToggleUpdateBooleanS2C(0,true),(ServerPlayer) player);

                }
                else
                {
                    player.sendSystemMessage(Component.literal("Invalid input, should be *true* or *false*"));
                }
            });

        });
        return 0;
    }

}
