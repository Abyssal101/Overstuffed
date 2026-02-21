package net.willsbr.gluttonousgrowth.Command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;

public class CommandHandler {

    private static ArrayList<Class> activeCommands = new ArrayList<>();
    public static void generateCommands
            (CommandDispatcher<CommandSourceStack> source, CommandBuildContext buildContext)
    {
        //The goal here is that this generateCommands method will go into every class that is inside active commands and
        //will take the logic method inside of it and generate the commands
//        public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
//        pDispatcher.register(Commands.literal("overstuffed")
//                .then(Commands.literal("setCurrentWeight")
//                        .requires(source -> source.hasPermission(2))
//                        .then(Commands.argument("target", EntityArgument.players())
//                                .then(Commands.argument("newWeight", IntegerArgumentType.integer())
//                                        .executes(context ->
//                                                setWeight(context.getSource(), EntityArgument.getPlayer(context, "target"),IntegerArgumentType.getInteger(context,"newWeight")))).
//                                executes(context -> setWeight(context.getSource(), context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context,"newWeight"))))));
//    }
//
//        private static int setWeight(CommandSourceStack pSource, ServerPlayer player, int index) throws
//        CommandSyntaxException {
        //this would look like this normally




    }

    public static void addCommand(Class commandClass)
    {
        activeCommands.add(commandClass);
    }


}
