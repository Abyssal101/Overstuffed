package net.willsbr.overstuffed.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.config.OverstuffedConfig;

public class debugView {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setLayer.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed").then(Commands.literal("debugView").executes((p_138618_) -> {
            return viewEverything(p_138618_.getSource(),p_138618_.getSource().getPlayer());
        })));
    }

    private static int viewEverything(CommandSourceStack pSource, Player player) throws CommandSyntaxException {

        player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {

            player.sendSystemMessage(Component.literal("Current Config CPM Layers"));
            player.sendSystemMessage(Component.literal("Current Stuffed Layer: "+ OverstuffedConfig.stuffedLayerConfigEntry.get()));
            player.sendSystemMessage(Component.literal("Last Stuffed Layer: "+OverstuffedConfig.lastStuffedLayer));
            player.sendSystemMessage(Component.literal("Current Weight Layer: "+OverstuffedConfig.weightLayerConfigEntry.get()));
            player.sendSystemMessage(Component.literal("Last Weight Layer: "+OverstuffedConfig.lastWeightLayer));

            player.sendSystemMessage(Component.literal("-------------------------------"));
        });

        player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {

            player.sendSystemMessage(Component.literal("Stuffed Level: "+stuffedBar.getCurrentStuffedLevel()));
            player.sendSystemMessage(Component.literal("Full points: "+stuffedBar.getFullPoints()));
            player.sendSystemMessage(Component.literal("Stuffed points: "+stuffedBar.getStuffedPoints()));
            player.sendSystemMessage(Component.literal("Overstuffed points: "+stuffedBar.getOverstuffedPoints()));
            player.sendSystemMessage(Component.literal("Progress to next Point: "+stuffedBar.getStuffedLossed()));
            player.sendSystemMessage(Component.literal("Add State: "+stuffedBar.getAddState()));
            player.sendSystemMessage(Component.literal("Interval between points: "+stuffedBar.getInterval()));

            player.sendSystemMessage(Component.literal("-------------------------------"));
        });





        return 0;
    }

}
