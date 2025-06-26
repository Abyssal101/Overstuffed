package net.willsbr.overstuffed.networking.packet.StuffedPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.overstuffed.config.OverstuffedWorldConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.sound.ModSounds;

import java.util.function.Supplier;

public class OverfullFoodC2SPacket {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.OverfullFood";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";
    public OverfullFoodC2SPacket(){

    }

    public OverfullFoodC2SPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    Level level=player.level();
                    if(!level.isClientSide)
                    {
                        player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(calorieMeter ->
                        {
                            ItemStack lastFood=player.getItemInHand(player.getUsedItemHand());
                            //This packet should NEVER be sent when the food isn't edible.

                            int calculatedCalories=lastFood.getItem().getFoodProperties().getNutrition();
                            calculatedCalories=calculatedCalories+(int)(calculatedCalories*lastFood.getItem().getFoodProperties().getSaturationModifier());
                            calculatedCalories=(int)(calculatedCalories*calorieMeter.getCalorieGainMultipler());
                            calorieMeter.addCalories(calculatedCalories);

                            //only does it on the first time it's been cosnumed
                             if(calorieMeter.getFoodEatenTick()==-1)
                            {
                                calorieMeter.setFoodEatenTick(player.tickCount);
                                calorieMeter.setCalClearDelay(OverstuffedWorldConfig.minCalClearDelay.get());
                            }
                            else
                            {
                                int timeToAdd=(int)(((double)calculatedCalories/calorieMeter.getMaxCalories()
                                *(OverstuffedWorldConfig.maxCalClearDelay.get()-OverstuffedWorldConfig.minCalClearDelay.get())));
                                calorieMeter.setCalClearDelay(calorieMeter.getCalClearDelay()+timeToAdd);
                            }

                            ModSounds.playBurp(player);
                            ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(calorieMeter.getCurrentCalories(),
                                    calorieMeter.getMaxCalories(),calorieMeter.getModMetabolismThres(),
                                    calorieMeter.getSlowMetabolismThres()),player);
                            ModMessages.sendToPlayer(new CalorieMeterDelaySyncPacketS2C(calorieMeter.getCalClearDelay(),calorieMeter.getFoodEatenTick()),player);
                        });
                    }
                }
        );
        return true;
    }


}
