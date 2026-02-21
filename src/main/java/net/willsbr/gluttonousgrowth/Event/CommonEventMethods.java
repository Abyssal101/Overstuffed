package net.willsbr.gluttonousgrowth.Event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.willsbr.gluttonousgrowth.Command.ActiveCommands.*;

public class CommonEventMethods {

    public static void registerCommonCommands(RegisterCommandsEvent event)
    {
        //todo seperate so certain commands cannot be called on server
        CommandDispatcher<CommandSourceStack> commands = event.getDispatcher();
        //SetLayer.register(commands, event.getBuildContext());
        //setMaxWeightCommand.register(commands, event.getBuildContext());
        //setMinWeightCommand.register(commands,event.getBuildContext());
        setCurrentWeight.register(commands,event.getBuildContext());
        //clearLayers.register(commands, event.getBuildContext());
        //debugViewCommand.register(commands, event.getBuildContext());
        setMaxCalories.register(commands, event.getBuildContext());
        debugViewEnabled.register(commands, event.getBuildContext());
        figuraNBTUpdateCommand.register(commands, event.getBuildContext());
        clearWeightHitbox.register(commands, event.getBuildContext());
        setHitbox.register(commands, event.getBuildContext());

        //setWGMethod.register(commands,event.getBuildContext());
        //setBurpFrequency.register(commands, event.getBuildContext());
        //setGurgleFrequency.register(commands, event.getBuildContext());

    }
//    public static void registerClientCommands(RegisterClientCommandsEvent event)
//    {
//        CommandDispatcher<CommandSourceStack> commands = event.getDispatcher();
//        setHitbox.register(commands, event.getBuildContext());
//    }
}
