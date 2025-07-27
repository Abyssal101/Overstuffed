package net.willsbr.gluttonousgrowth.Command.ActiveCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.HashMap;
import java.util.Map;

public class clearWeightHitbox {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setHitbox.failed"));
    private static final SimpleCommandExceptionType ERROR_NO_PEHKUI = new SimpleCommandExceptionType(Component.translatable("commands.setHitbox.nopehkui"));

    // Define scale type configurations


    //Basically if you forcibly change your hitbox, this lets GG know that whatever your hitbox is now is the default size
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        // First create the base command
        LiteralArgumentBuilder<CommandSourceStack> baseCommand = Commands.literal("ggconfig");

        // Then create the setHitbox subcommand
        if(ModList.get().isLoaded("pehkui"))
        {
            // Add reset command
            LiteralArgumentBuilder<CommandSourceStack> resetCommand = Commands.literal("clearWeightHitbox")
                    .executes(context -> resetScales(context.getSource()));
            baseCommand.then(resetCommand);

            // Add the setHitbox command to the base command

            // Register the complete command tree
            pDispatcher.register(baseCommand);

        }


    }



    private static int resetScales(CommandSourceStack source) throws CommandSyntaxException {
        // Get the player executing the command
        ServerPlayer player = source.getPlayerOrException();

        if (!ModList.get().isLoaded("pehkui")) {
            throw ERROR_NO_PEHKUI.create();
        }

        try {
            player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                weightBar.setLastBaseScale(ScaleTypes.BASE.getScaleData(player).getBaseScale());
                weightBar.setCurrentHitboxIncrease(0);
                weightBar.setLastHitboxIncrease(0);
            });

            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Failed to reset scales: " + e.getMessage())
                    .withStyle(ChatFormatting.RED));
            throw ERROR_FAILED.create();
        }
    }

    // Format scale name for better display (camelCase to Space Separated Title Case)
    private static String formatScaleName(String name) {
        // First handle camelCase by inserting spaces
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                result.append(' ');
            }
            result.append(i == 0 ? Character.toUpperCase(c) : c);
        }

        // Special case for "hitbox" to capitalize "Box"
        return result.toString().replace("hitbox ", "Hitbox ");
    }

    // Helper class to group related scale configuration
    private static class ScaleConfig {
        final ScaleType scaleType;
        final double minValue;
        final double maxValue;

        ScaleConfig(ScaleType scaleType, double minValue, double maxValue) {
            this.scaleType = scaleType;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }
}