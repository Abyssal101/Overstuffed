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
import net.minecraft.world.level.Level;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBar;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.setMaxStuffedS2CPacket;

public class setMaxStuffed {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setWeight.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register(Commands.literal("overstuffed")
                .then(Commands.literal("setMaxStuffed")
                .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("maxStuffed", IntegerArgumentType.integer())
                        .then(Commands.argument("interval", IntegerArgumentType.integer())
                        .executes(context ->
                                setMaxStuffed(context.getSource(),
                                        IntegerArgumentType.getInteger(context,"maxStuffed"),
                                        IntegerArgumentType.getInteger(context,"interval")))))));
    }

    private static int setMaxStuffed(CommandSourceStack pSource, int newStuffedLimit, int newInterval) throws CommandSyntaxException {
        if(pSource.hasPermission(2))
        {

            if(pSource.isPlayer())
            {
                //TODO make max stuffed based off a value

                if(newStuffedLimit>=4 && newStuffedLimit<=9)
                {
                    if(0<=newInterval && newInterval<=2)
                    {
                        //here we are on the server
                        if(pSource.getPlayer()!=null)
                        {
                            ServerPlayer player=pSource.getPlayer();
                            Level level=player.level();
                            if(!level.isClientSide)
                            {


                                player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
                                    int sum=stuffedBar.getStuffedLevel()+stuffedBar.getFullLevel()+stuffedBar.getOverstuffedLevel();
                                    int intervalDiff=newInterval-stuffedBar.getAddState();


                                    //this handles it resetting to baseline
                                    if(newStuffedLimit==4 && newInterval==0)
                                    {

                                        stuffedBar.resetLimits();
                                        ModMessages.sendToPlayer(new setMaxStuffedS2CPacket(PlayerStuffedBar.MIN_FULL_POINTS,
                                                PlayerStuffedBar.MIN_STUFFED_POINTS, PlayerStuffedBar.MIN_OVERSTUFFED_POINTS,0),(ServerPlayer) player);
                                    }
                                    else if(newStuffedLimit==4 && newInterval>0 && newInterval<3)
                                    {
                                        pSource.sendSystemMessage(Component.literal("Error: Only valid values for level 4 is zero"));

                                    }
                                    else
                                    {
                                            if(newInterval==1)
                                            {
                                                //Anytime interval is 1, the bar should have two reds
                                                stuffedBar.setOverstuffedLevel(2);
                                                stuffedBar.setStuffedLevel(1);
                                                stuffedBar.setFullLevel(newStuffedLimit-3);
                                                stuffedBar.setAddState(newInterval);
                                            }
                                            else if(newInterval==2)
                                            {
                                                //Anytime interval is 2, the bar should have two yellows
                                                stuffedBar.setOverstuffedLevel(1);
                                                stuffedBar.setStuffedLevel(2);
                                                stuffedBar.setFullLevel(newStuffedLimit-3);
                                                stuffedBar.setAddState(newInterval);
                                            }
                                            else if(newInterval==0)
                                            {
                                                //Anytime interval is 1, the bar should have two reds
                                                stuffedBar.setOverstuffedLevel(1);
                                                stuffedBar.setStuffedLevel(1);
                                                stuffedBar.setFullLevel(newStuffedLimit-2);
                                                stuffedBar.setAddState(newInterval);
                                            }

                                        ModMessages.sendToPlayer(new setMaxStuffedS2CPacket(stuffedBar.getFullLevel(),
                                                stuffedBar.getStuffedLevel(), stuffedBar.getOverstuffedLevel(), stuffedBar.getInterval()), (ServerPlayer) player);
                                    }
                                });

                            }
                        }



                    }
                    else
                    {
                        pSource.sendSystemMessage(Component.literal("Error: Value not in range: [0,2]"));
                    }

                }
                else {
                    pSource.sendSystemMessage(Component.literal("Error: Value not in range: [4,9]"));

                }
            }
        }
        else
        {
            pSource.sendSystemMessage(Component.literal("Error: You need admin permission to use this command"));
        }

        return 0;
    }

}
