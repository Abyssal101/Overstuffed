package net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.config.GluttonousWorldConfig;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.sound.ModSounds;

import java.util.concurrent.atomic.AtomicBoolean;
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
                            player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {

                                AtomicBoolean stageGain=new AtomicBoolean(false);

                                player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                                    stageGain.set(serverSettings.stageBasedGain());
                                });
                                ItemStack lastFood=player.getItemInHand(player.getUsedItemHand());
                                //This packet should NEVER be sent when the food isn't edible.

                                int calculatedCalories=lastFood.getItem().getFoodProperties().getNutrition();
                                calculatedCalories=calculatedCalories+(int)(calculatedCalories*lastFood.getItem().getFoodProperties().getSaturationModifier());
                                calculatedCalories=(int)(calculatedCalories*calorieMeter.getCalorieGainMultipler());

                                double calReductionFromWeight=0;

                                if(stageGain.get())
                                {
                                    double currentStagePercentage=weightBar.calculateCurrentWeightStage()*(100.0/weightBar.getTotalStages());
                                    calReductionFromWeight=(1-currentStagePercentage*0.5);
                                }
                                else
                                {
                                    calReductionFromWeight=(1-weightBar.calculateCurrentWeightPercentage()*0.5);
                                }


                                calculatedCalories=(int)(calculatedCalories*calReductionFromWeight);
                                calculatedCalories=Math.max(1,calculatedCalories);

                                calorieMeter.addCalories(calculatedCalories);

                                //only does it on the first time it's been cosnumed
                                if(calorieMeter.getFoodEatenTick()==-1)
                                {
                                    calorieMeter.setFoodEatenTick(player.tickCount);
                                    calorieMeter.setCalClearDelay(GluttonousWorldConfig.minCalClearDelay.get());
                                }
                                else
                                {
                                    int timeToAdd=(int)(((double)calculatedCalories/calorieMeter.getMaxCalories()
                                            *(GluttonousWorldConfig.maxCalClearDelay.get()- GluttonousWorldConfig.minCalClearDelay.get())));
                                    timeToAdd+=GluttonousWorldConfig.minCalClearDelay.get();
                                    calorieMeter.setCalClearDelay(calorieMeter.getCalClearDelay()+timeToAdd);
                                }

                                ModSounds.playBurp(player);
                                ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(calorieMeter.getCurrentCalories(),
                                        calorieMeter.getMaxCalories(),calorieMeter.getModMetabolismThres(),
                                        calorieMeter.getSlowMetabolismThres()),player);
                                ModMessages.sendToPlayer(new CalorieMeterDelaySyncPacketS2C(calorieMeter.getCalClearDelay(),calorieMeter.getFoodEatenTick()),player);
                            });

                        });
                    }
                }
        );
        return true;
    }


}
