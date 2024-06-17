package net.willsbr.overstuffed.Event;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocks;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.Effects.ModEffects;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBar;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.OverstuffedConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.*;
import net.willsbr.overstuffed.sound.ModSounds;

@Mod.EventBusSubscriber(modid= OverStuffed.MODID)
public class ModEvent {

    //START OF STUFF NEEDED FOR A CAPABILITY
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {

        //StuffedBar
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).isPresent()) {
                event.addCapability(new ResourceLocation(OverStuffed.MODID, "properties"), new PlayerStuffedBarProvider());
            }
            if(!event.getObject().getCapability(CPMDataProvider.PLAYER_CPM_DATA).isPresent()) {
                event.addCapability(new ResourceLocation(OverStuffed.MODID, "configsettings"), new CPMDataProvider());
            }
            if(!event.getObject().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).isPresent()) {
                event.addCapability(new ResourceLocation(OverStuffed.MODID, "weightbar"), new PlayerWeightBarProvider());
            }
            if(!event.getObject().getCapability(PlayerUnlocksProvider.PLAYER_TOGGLES).isPresent()) {
                event.addCapability(new ResourceLocation(OverStuffed.MODID, "overstuffedtoggles"), new PlayerUnlocksProvider());
            }
        }
    }

    //adds capability back when you die
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        //stuffedbar
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(oldStore -> {
                event.getEntity().getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);

                    ModMessages.sendToPlayer(new ClientCPMStuffedSyncS2CPacket(oldStore.getStuffedLayerName()),(ServerPlayer) event.getEntity());
                    ModMessages.sendToPlayer(new ClientCPMWeightSyncS2CPacket(oldStore.getWeightLayerName()),(ServerPlayer) event.getEntity());

                });
            });

            event.getOriginal().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });

            event.getOriginal().getCapability(PlayerUnlocksProvider.PLAYER_TOGGLES).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerUnlocksProvider.PLAYER_TOGGLES).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().invalidateCaps();

        }

    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        //stuffedbar
        event.register(PlayerStuffedBar.class);
        event.register(CPMData.class);
        event.register(PlayerWeightBar.class);
        event.register(PlayerUnlocks.class);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {

            //ServerPlayer serverplayer=(ServerPlayer)event.player;
            //serverplayer.getAdvancements()
            //stuffed filling hunger bar logic
            event.player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
                if(stuffedBar.getCurrentStuffedLevel() > 0 && event.player.getRandom().nextFloat() < 0.005f
                 && event.player.getFoodData().getFoodLevel()<20) { // Once Every 10 Seconds on Avg

                    stuffedBar.subStuffedLevel(1);
                    event.player.getFoodData().setFoodLevel(event.player.getFoodData().getFoodLevel()+1);
                    stuffedBar.addStuffedLossed();
                    if( stuffedBar.getCurrentStuffedLevel()+1>stuffedBar.getFullPoints() && stuffedBar.getStuffedLossed()>= stuffedBar.getInterval())
                    {
                        if(stuffedBar.getCurrentStuffedLevel()+1>(stuffedBar.getFullPoints()+stuffedBar.getStuffedPoints()))
                        stuffedBar.addStuffedLossed();
                    }
                    stuffedBar.addStuffedLossed();
                    event.player.sendSystemMessage(Component.literal("Subtracted Hunger:"+stuffedBar.getCurrentStuffedLevel()));
                    //adagsdagasgd

                    //Playing sound logic
                    event.player.getCapability(PlayerUnlocksProvider.PLAYER_TOGGLES).ifPresent(playerUnlocks -> {


                        //effectively if the random number is LOWER than the set frequency, it works! 0 should disable,a and 10 should be max
                        if(event.player.getRandom().nextIntBetweenInclusive(0,10)< OverstuffedConfig.burpFrequency.get())
                        {
                            event.player.getLevel().playSound(null, event.player.blockPosition(),ModSounds.BURP_SOUNDS.get(
                                    event.player.getRandom().nextIntBetweenInclusive(1,ModSounds.BURP_SOUNDS.size())-1).get(),
                                    event.player.getSoundSource(), 1f, 1f);
                        }
                            });
                    //sound logic end

                    ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getStuffedMax(), stuffedBar.getOverstuffedMax(),
                            stuffedBar.getSuperStuffedMax()),(ServerPlayer) event.player);

                }
//                if((stuffedBar.lastCallTime!=-1 && (event.player.level.getGameTime()- stuffedBar.lastCallTime)>stuffedBar.lastFoodDuration))
//                {
////                    event.player.sendSystemMessage(Component.literal("SOMETHING"));
//                    event.player.stopUsingItem();
//                    //event.player.
//                    stuffedBar.lastFoodDuration=0;
//                    stuffedBar.lastCallTime=-1;
//
//                }

            });

            //make it so that the visible weight bar begins to update here
            event.player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                event.player.getCapability(PlayerUnlocksProvider.PLAYER_TOGGLES).ifPresent(playerUnlocks -> {
                            //create weight updates here

                            //System.out.println(playerUnlocks.getToggle(0));
                            if(playerUnlocks.getToggle(0))
                            {
                                    burstGain(weightBar,event);
                            }

                            if(!(playerUnlocks.getToggleValue(2)==0) & weightBar.getLastWeightStage()>1 && event.player.getRandom().nextFloat() < (0.0005f*Math.sqrt(OverstuffedConfig.gurgleFrequency.get())))
                            {
                                event.player.getLevel().playSound(null, event.player.blockPosition(),ModSounds.GURGLE_SOUNDS.get(
                                        event.player.getRandom().nextIntBetweenInclusive(1,ModSounds.GURGLE_SOUNDS.size())-1).get(),
                                event.player.getSoundSource(), 0.5f, 1f);
                            }


                            if (weightBar.weightUpdateStatus()) {
                                if (weightBar.getQueuedWeight() <= 0) {
                                    int foodCals = weightBar.getWeightChanges();
                                    //this makes it so the weight chance from a single food item gets added to the total amount
                                    if (foodCals != 0) {
                                        weightBar.addChangetoQueue(foodCals * 2);
                                        int checkDelay = foodCals * 10;
                                        if (checkDelay > 1000) {
                                            weightBar.setWeightUpdateDelay(1000);
                                        } else {
                                            weightBar.setWeightUpdateDelay(foodCals * 10);
                                        }


                                    }
                                } else {
                                    weightBar.addWeight();

                                }
                                    ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()), (ServerPlayer) event.player);
                                    weightBar.setWeightUpdateStatus(false);
                                    weightBar.setWeightTick(event.player.tickCount);

                                //this is where I gotta write the function to make it spam
                            } else {
                                if ((event.player.tickCount - weightBar.getWeightTick()) >= weightBar.getWeightUpdateDelay()) {
                                    weightBar.setWeightUpdateStatus(true);
                                    // System.out.println("Weight update but not");
                                }
                            }




                        });
                //THE DREADED LOSE WEIGHT FUNCTIONALITY!
                //this sees if the player has less than 5 food bars
                if(event.player.getFoodData().getFoodLevel()<18 || event.player.hasEffect(ModEffects.GOLDEN_DIET.get()) )
                {
                    if(weightBar.getSavedTickforWeightLoss()==-1)
                    {
                        weightBar.setSavedTickforWeightLoss(event.player.tickCount);
                        //System.out.println("Weight Delay"+(int)(100-event.player.getFoodData().getExhaustionLevel()*5));
                        if((event.player.hasEffect(ModEffects.GOLDEN_DIET.get())))
                        {
                            weightBar.setWeightLossDelay(20);
                        }
                        else {
                            weightBar.setWeightLossDelay((int)(200-event.player.getFoodData().getExhaustionLevel()*5));

                        }
                    }  else if ((event.player.tickCount-weightBar.getSavedTickforWeightLoss()>weightBar.getWeightLossDelay())){
                        //
                        weightBar.loseWeight();
                    ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()),(ServerPlayer) event.player);
                    weightBar.setSavedTickforWeightLoss(-1);
                    }

                }

                });
//                if((stuffedBar.lastCallTime!=-1 && (event.player.level.getGameTime()- stuffedBar.lastCallTime)>stuffedBar.lastFoodDuration))
//                {
////                    event.player.sendSystemMessage(Component.literal("SOMETHING"));
//                    event.player.stopUsingItem();
//                    //event.player.
//                    stuffedBar.lastFoodDuration=0;
//                    stuffedBar.lastCallTime=-1;
//
//                }



        }
        //this is for ice movement logics
    }
    public static void burstGain(PlayerWeightBar weightBar,TickEvent.PlayerTickEvent event)
    {
        int calculatedPercentage=(int)((((double)(weightBar.getCurrentWeight()-weightBar.getMinWeight()))/(weightBar.getCurMaxWeight()- weightBar.getMinWeight()))*100);
        System.out.println("Percentage for Burst"+calculatedPercentage);
        int xOf5=calculatedPercentage/20;
        //System.out.println("of5:"+xOf5+"  percent:"+calculatedPercentage);
        //System.out.println(weightBar.getLastWeightStage()+" Last stage");
        if(xOf5!=weightBar.getLastWeightStage())
        {


            //Come back to if really jarring
            if((event.player.tickCount&5)==0)
            {
                if(xOf5==weightBar.getLastWeightStage()+1)
                {
                    //checks if we reached new stage
                    if(((weightBar.getLastWeightStage()*20+((int)((((double)weightBar.getAmountThroughStage())/255)*100)))/20)==xOf5)
                    {
                        weightBar.setLastWeightStage(xOf5);
                        weightBar.setAmountThroughStage(0);
                        ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);

                    }
                    else
                    {
                        weightBar.setAmountThroughStage(weightBar.getAmountThroughStage()+1);
                        ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);
                    }
                }
                else if(xOf5==(weightBar.getLastWeightStage()-1)){
                    if(((weightBar.getLastWeightStage()*20+((int)((((double)weightBar.getAmountThroughStage())/255)*100))))==(xOf5*20))
                    {

                        //System.out.println("AT THE RESETTING OF THE VALUE"+((weightBar.getLastWeightStage()*20+((int)((((double)weightBar.getAmountThroughStage())/255)*100)))/20));
                        weightBar.setLastWeightStage(xOf5);
                        weightBar.setAmountThroughStage(0);
                        ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);

                    }
                    else
                    {

                        weightBar.setAmountThroughStage(weightBar.getAmountThroughStage()-1);
                        ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);
                    }

                }
                else
                {
                    weightBar.setLastWeightStage(xOf5);
                    ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), 0), (ServerPlayer) event.player);
                    System.out.println("Error weight weight stages, increase or decreasing by greater than 1 stage");
                }
            }


        }
    }



    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {

        //stuffedbar
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
                    ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getStuffedMax(),stuffedBar.getOverstuffedMax(),
                            stuffedBar.getSuperStuffedMax()), player);
                });
                player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
                    ModMessages.sendToPlayer(new ClientCPMStuffedSyncS2CPacket(cpmData.getStuffedLayerName()),player);
                });
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                        ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()),player);
                });
                player.getCapability(PlayerUnlocksProvider.PLAYER_TOGGLES).ifPresent(playerUnlocks -> {
                    for(int i = 0; i< playerUnlocks.getLength(); i++)
                    {
                        ModMessages.sendToPlayer(new PlayerToggleUpdateBooleanS2C(i, playerUnlocks.getToggle(i)) ,player);
                    }

                });

            }


        }



    }



}
