package net.willsbr.overstuffed.Command.ActiveCommands;

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
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.HashMap;
import java.util.Map;

public class setHitbox {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setHitbox.failed"));
    private static final SimpleCommandExceptionType ERROR_NO_PEHKUI = new SimpleCommandExceptionType(Component.translatable("commands.setHitbox.nopehkui"));

    // Define scale type configurations
    private static final Map<String, ScaleConfig> SCALE_CONFIGS = new HashMap<>();

    static {
        if(ModList.get().isLoaded("pehkui"))
        {
            SCALE_CONFIGS.put("width", new ScaleConfig(ScaleTypes.WIDTH, 0.2, 16.0));
            SCALE_CONFIGS.put("hitboxWidth", new ScaleConfig(ScaleTypes.HITBOX_WIDTH, 0.5, 16));
            SCALE_CONFIGS.put("height", new ScaleConfig(ScaleTypes.HEIGHT, 0.2, 16.0));
            SCALE_CONFIGS.put("hitboxHeight", new ScaleConfig(ScaleTypes.HITBOX_HEIGHT, 0.2, 16));
            SCALE_CONFIGS.put("motion", new ScaleConfig(ScaleTypes.MOTION, 0.2, 1.0));
            SCALE_CONFIGS.put("eyeHeight", new ScaleConfig(ScaleTypes.EYE_HEIGHT, 0.2, 16.0));
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        // First create the base command
        LiteralArgumentBuilder<CommandSourceStack> baseCommand = Commands.literal("overstuffed");

        // Then create the setHitbox subcommand
        LiteralArgumentBuilder<CommandSourceStack> hitboxCommand = Commands.literal("setScale");
        if(ModList.get().isLoaded("pehkui"))
        {
            // Add width command with value argument
            LiteralArgumentBuilder<CommandSourceStack> widthCommand = Commands.literal("width")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                            .executes(context -> setScale(
                                    context.getSource(),
                                    "width",
                                    SCALE_CONFIGS.get("width"),
                                    DoubleArgumentType.getDouble(context, "value"))));
            hitboxCommand.then(widthCommand);

            // Add height command
            LiteralArgumentBuilder<CommandSourceStack> heightCommand = Commands.literal("height")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                            .executes(context -> setScale(
                                    context.getSource(),
                                    "height",
                                    SCALE_CONFIGS.get("height"),
                                    DoubleArgumentType.getDouble(context, "value"))));
            hitboxCommand.then(heightCommand);

            // Add hitboxWidth command
            LiteralArgumentBuilder<CommandSourceStack> hitboxWidthCommand = Commands.literal("hitboxWidth")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                            .executes(context -> setScale(
                                    context.getSource(),
                                    "hitboxWidth",
                                    SCALE_CONFIGS.get("hitboxWidth"),
                                    DoubleArgumentType.getDouble(context, "value"))));
            hitboxCommand.then(hitboxWidthCommand);

            // Add hitboxHeight command
            LiteralArgumentBuilder<CommandSourceStack> hitboxHeightCommand = Commands.literal("hitboxHeight")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                            .executes(context -> setScale(
                                    context.getSource(),
                                    "hitboxHeight",
                                    SCALE_CONFIGS.get("hitboxHeight"),
                                    DoubleArgumentType.getDouble(context, "value"))));
            hitboxCommand.then(hitboxHeightCommand);

            // Add motion command
            LiteralArgumentBuilder<CommandSourceStack> motionCommand = Commands.literal("motion")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                            .executes(context -> setScale(
                                    context.getSource(),
                                    "motion",
                                    SCALE_CONFIGS.get("motion"),
                                    DoubleArgumentType.getDouble(context, "value"))));
            hitboxCommand.then(motionCommand);

            // Add eyeHeight command
            LiteralArgumentBuilder<CommandSourceStack> eyeHeightCommand = Commands.literal("eyeHeight")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                            .executes(context -> setScale(
                                    context.getSource(),
                                    "eyeHeight",
                                    SCALE_CONFIGS.get("eyeHeight"),
                                    DoubleArgumentType.getDouble(context, "value"))));
            hitboxCommand.then(eyeHeightCommand);

            // Add reset command
            LiteralArgumentBuilder<CommandSourceStack> resetCommand = Commands.literal("reset")
                    .executes(context -> resetScales(context.getSource()));
            hitboxCommand.then(resetCommand);

            // Add the setHitbox command to the base command
            baseCommand.then(hitboxCommand);

            // Register the complete command tree
            pDispatcher.register(baseCommand);

        }
        else{
            hitboxCommand.executes( context -> {
                context.getSource().getPlayer().sendSystemMessage(Component.literal("Pekhui is not loaded").withStyle(ChatFormatting.RED)); return 0;

            });
            baseCommand.then(hitboxCommand);

            // Register the complete command tree
            pDispatcher.register(baseCommand);
        }

    }

    private static int setScale(CommandSourceStack source, String scaleName, ScaleConfig config, double value) throws CommandSyntaxException {
        // Get the player executing the command
        ServerPlayer player = source.getPlayerOrException();

        // Validate range
        if (value < config.minValue || value > config.maxValue) {
            source.sendFailure(Component.literal(
                            String.format("%s must be between %.1f and %.1f",
                                    formatScaleName(scaleName),
                                    config.minValue, config.maxValue))
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        if (!ModList.get().isLoaded("pehkui")) {
            throw ERROR_NO_PEHKUI.create();
        }

        try {
            ScaleData scaleData = config.scaleType.getScaleData(player);
            scaleData.setScale((float) value);
            scaleData.setScaleTickDelay(20);

            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Failed to set " + formatScaleName(scaleName).toLowerCase() + ": " + e.getMessage())
                    .withStyle(ChatFormatting.RED));
            throw ERROR_FAILED.create();
        }
    }

    private static int resetScales(CommandSourceStack source) throws CommandSyntaxException {
        // Get the player executing the command
        ServerPlayer player = source.getPlayerOrException();

        if (!ModList.get().isLoaded("pehkui")) {
            throw ERROR_NO_PEHKUI.create();
        }

        try {
            // Reset all scale types defined in our map
            for (ScaleConfig config : SCALE_CONFIGS.values()) {
                config.scaleType.getScaleData(player).setScale(1.0f);
            }

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