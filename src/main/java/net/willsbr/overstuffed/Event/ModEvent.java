package net.willsbr.overstuffed.Event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocks;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.Command.figuraNBTUpdateCommand;
import net.willsbr.overstuffed.Effects.ModEffects;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettings;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBar;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.OverstuffedConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.SettingPackets.PlayerSyncAllSettingsPollS2C;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.OverfullFoodDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.WeightPackets.BurstGainDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.WeightPackets.QueuedWeightSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.WeightPackets.WeightBarDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.stuffedIntervalUpdateS2CPacket;
import net.willsbr.overstuffed.sound.ModSounds;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid= OverStuffed.MODID)
public class ModEvent {

    @SubscribeEvent
    public static void commandRegister(RegisterCommandsEvent event)
    {
        CommonEventMethods.registerCommands(event);
    }

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
            if(!event.getObject().getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).isPresent()) {
                event.addCapability(new ResourceLocation(OverStuffed.MODID, "playerserversettings"), new PlayerServerSettingsProvider());
            }
            if(!event.getObject().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).isPresent()) {
                event.addCapability(new ResourceLocation(OverStuffed.MODID, "weightbar"), new PlayerWeightBarProvider());

            }
            if(!event.getObject().getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).isPresent()) {
                event.addCapability(new ResourceLocation(OverStuffed.MODID, "overstuffedtoggles"), new PlayerUnlocksProvider());
            }
        }
    }

    //adds capability back when you die
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        //stuffedbar
        event.getOriginal().reviveCaps();

        if (event.getEntity() instanceof ServerPlayer && event.getOriginal() instanceof ServerPlayer)
        {

            event.getOriginal().getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);

                });
            });

            //put all shit that should always copy below this
            event.getOriginal().getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(oldStore -> {
                event.getEntity().getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);

                    //ModMessages.sendToPlayer(new ClientCPMStuffedSyncS2CPacket(oldStore.getStuffedLayerName()),(ServerPlayer) event.getEntity());
                    //ModMessages.sendToPlayer(new ClientCPMWeightSyncS2CPacket(oldStore.getWeightLayerName()),(ServerPlayer) event.getEntity());

                });
            });

            event.getOriginal().getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });



            event.getOriginal().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });

            event.getOriginal().getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(newStore -> {
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
            //Making it a little more effcient
           //TODO FIGURE OUT A WAY TO NICE DISABLE SPRINTING PROBABLY MIXIN TO SETSPRINTING CLASS
            if((event.player.tickCount&3)==0)
            {
                stuffedSystem(event);
            }
            if((event.player.tickCount&2)==0)
            {
                weightSystem(event);
            }
        }
    }
    public static void burstGain(PlayerWeightBar weightBar,PlayerServerSettings serverSettings,TickEvent.PlayerTickEvent event,int xOf5)
    {

        if(xOf5!=weightBar.getLastWeightStage())
        {

            //Come back to if really jarring
            if((event.player.tickCount&5)==0)
            {
                AtomicInteger totalWeightFrames = new AtomicInteger(100); // Default value

                event.player.getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(cpmData -> {
                    // Now you can safely use cpmData here
                    totalWeightFrames.set(cpmData.getWeightLayerFrames());
                });
                double percentageThroughStage=(((double)weightBar.getAmountThroughStage())/totalWeightFrames.get())*100;
                int progressionCheck=(weightBar.getLastWeightStage()*20+((int)percentageThroughStage));
                //TODO Change from xOf5 and any instance of 20 or 5 or otherwise to modular
                if(progressionCheck%20==0 && (progressionCheck/20)==xOf5)
                {
                    weightBar.setLastWeightStage(xOf5);
                    weightBar.setAmountThroughStage(0);
                    ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);
                }
                else {
                    if(xOf5>=weightBar.getLastWeightStage()+1)
                    {
                        weightBar.setAmountThroughStage(weightBar.getAmountThroughStage()+1);
                        ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);

                    }
                    else
                    {
                        weightBar.setAmountThroughStage(weightBar.getAmountThroughStage()-1);
                        ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);
                    }
                }

//                else
//                {
//                    weightBar.setLastWeightStage(xOf5);
//                    ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), 0), (ServerPlayer) event.player);
//                    System.out.println("Error weight weight stages, increase or decreasing by greater than 1 stage");
//                }
            }


        }
    }

    public static void weightSystem(TickEvent.PlayerTickEvent event)
    {

        event.player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            event.player.getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(playerUnlocks -> {
                event.player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {


                int xOf5 = weightBar.calculateCurrentWeightStage();

                int lastWeightStage = weightBar.getLastWeightStage();
                //create weight updates here
                if (serverSettings.stageBasedGain()) {
                    burstGain(weightBar,serverSettings, event, xOf5);
                }
                if(!serverSettings.stageBasedGain())
                {
                        weightBar.setLastWeightStage(xOf5);
                }


                //Adding and removing health modifiers

                weightBarEffects(event,serverSettings, weightBar, xOf5, lastWeightStage);


                if (serverSettings.getGurgleFrequency() > 0 & weightBar.getLastWeightStage() >= 1 &&
                        event.player.getRandom().nextFloat() < (0.001f * Math.sqrt(serverSettings.getGurgleFrequency() * weightBar.getLastWeightStage()))) {
                    event.player.level().playSound(null, event.player.blockPosition(), ModSounds.GURGLE_SOUNDS.get(
                                    event.player.getRandom().nextIntBetweenInclusive(1, ModSounds.GURGLE_SOUNDS.size()) - 1).get(),
                            event.player.getSoundSource(), 0.5f, 1f);
                }

                if (weightBar.weightUpdateStatus()) {

                    if (weightBar.getQueuedWeight() <= 0) {
                        int foodCals = weightBar.getWeightChanges();
                        //this makes it so the weight chance from a single food item gets added to the total amount
                        if (foodCals != 0) {
                            weightBar.addChangetoQueue(foodCals);
                            int checkDelay = foodCals * 10;
                            if (checkDelay > 1000) {
                                weightBar.setWeightUpdateDelay(1000);
                            } else {
                                weightBar.setWeightUpdateDelay((int) (checkDelay * weightBar.getWeightUpdateDelayModifier()));
                                //weightBar.setWeightUpdateDelay(foodCals);
                            }


                        }
                    } else {

                        weightBar.addWeight();
                        ModMessages.sendToPlayer(new QueuedWeightSyncS2CPacket(weightBar.getQueuedWeight(), weightBar.getTotalWeightChanges()), (ServerPlayer) event.player);

                    }
                    ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()), (ServerPlayer) event.player);
                    weightBar.setWeightUpdateStatus(false);
                    weightBar.setWeightTick(event.player.tickCount);

                } else {
                    if ((event.player.tickCount - weightBar.getWeightTick()) >= weightBar.getWeightUpdateDelay()) {
                        weightBar.setWeightUpdateStatus(true);
                    }
                }
                //THE DREADED LOSE WEIGHT FUNCTIONALITY!
                //this sees if the player has less than 5 food bars
                if (event.player.getFoodData().getFoodLevel() < 18 || event.player.hasEffect(ModEffects.GOLDEN_DIET.get())) {
                    if (weightBar.getSavedTickforWeightLoss() == -1) {
                        weightBar.setSavedTickforWeightLoss(event.player.tickCount);
                        //System.out.println("Weight Delay"+(int)(100-event.player.getFoodData().getExhaustionLevel()*5));
                        if ((event.player.hasEffect(ModEffects.GOLDEN_DIET.get()))) {
                            weightBar.setWeightLossDelay(20);
                        } else {
                            int calculated = (int) ((event.player.getFoodData().getExhaustionLevel() * 5) * weightBar.getWeightUpdateDelayModifier());
                            calculated = 200 - calculated;
                            calculated = Math.max(0, calculated);
                            weightBar.setWeightLossDelay(calculated);
                        }
                    } else if ((event.player.tickCount - weightBar.getSavedTickforWeightLoss() > weightBar.getWeightLossDelay())) {
                        //
                        weightBar.loseWeight();
                        //this way you can lose more faster, maybe I should set that to some number that gets added.
                        if (event.player.hasEffect(ModEffects.GOLDEN_DIET.get()) && event.player.getFoodData().getFoodLevel() < 18) {
                            weightBar.loseWeight();
                        }
                        ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()), (ServerPlayer) event.player);
                        weightBar.setSavedTickforWeightLoss(-1);
                    }

                }

            });
            });

        });
    }
    public static void stuffedSystem(TickEvent.PlayerTickEvent event)
    {
        event.player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
            if(stuffedBar.getCurrentStuffedLevel() > 0 && event.player.getRandom().nextFloat() < (0.01f*Math.max(10-event.player.getFoodData().getFoodLevel(),1))
                    && event.player.getFoodData().getFoodLevel()<20) { // Once Every 10 Seconds on Avg

                stuffedBar.subStuffedLevel(1);
                event.player.getFoodData().setFoodLevel(event.player.getFoodData().getFoodLevel()+1);
                stuffedBar.addStuffedLossed();
                if(stuffedBar.getStuffedLossed()>= stuffedBar.getInterval())
                {
                    stuffedBar.addStuffedPoint();
                }
                ModMessages.sendToPlayer(new stuffedIntervalUpdateS2CPacket(stuffedBar.getStuffedLossed(),stuffedBar.getInterval()),(ServerPlayer)event.player);


                //Playing sound logic
                    //effectively if the random number is LOWER than the set frequency, it works! 0 should disable,a and 10 should be max
                    if((event.player.getRandom().nextIntBetweenInclusive(0,10)*2)< OverstuffedConfig.burpFrequency.get()) {

                        event.player.level().playSound(null, event.player.blockPosition(), ModSounds.BURP_SOUNDS.get(
                                        event.player.getRandom().nextIntBetweenInclusive(1, ModSounds.BURP_SOUNDS.size()) - 1).get(),
                                event.player.getSoundSource(), 1f, 1f);
                    }
                //sound logic end




                ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getFullLevel(), stuffedBar.getStuffedLevel(),
                        stuffedBar.getOverstuffedLevel()),(ServerPlayer) event.player);

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

    }


    public static void weightBarEffects(TickEvent.PlayerTickEvent event,PlayerServerSettings serverSettings, PlayerWeightBar weightBar, int xOf5, int lastWeightStage)
    {

            if(!serverSettings.weightEffects())
            {
                PlayerWeightBar.clearModifiers(event.player);
            }
            else
            {
                if(xOf5!=lastWeightStage)
                {

                    //This handles changing the modifiers when somehow the last weight stage and the new weight stage are greater than a one value jump
                    //Maybe could make it just this, but slightly more effcient I think?
                    PlayerWeightBar.addCorrectModifier((ServerPlayer)event.player);
                    //TODO see if moving this below this method call causes issues
//                    if(serverSettings.stageBasedGain())
//                    {
//                        weightBar.setLastWeightStage(xOf5);
//                    }


                }
            }






    }








    //TODO CHECK THAT INFO SAVES ON DIMENSION SWAP

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {

        //stuffedbar

        if (!event.getLevel().isClientSide()) {

            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
                    ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getFullLevel()
                            ,stuffedBar.getStuffedLevel(),
                            stuffedBar.getOverstuffedLevel()), player);


                });
                player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                    ModMessages.sendToPlayer(new PlayerSyncAllSettingsPollS2C(),player);
                });

                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    weightBar.setCurrentWeight(Math.max(weightBar.getCurrentWeight(),weightBar.getMinWeight()));
                    ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()),player);
                    //OverstuffedConfig.saveConfig();

                   PlayerWeightBar.addCorrectModifier(player);

                });
                player.getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(playerUnlocks -> {

                });
                figuraNBTUpdateCommand.updateNBT(player);
            }


        }



    }
    //

    //Hopefully this fixes the player  constantly stacking buffs when you leave and rejoin
    @SubscribeEvent
    public static void onPlayerLeaveWorld(EntityLeaveLevelEvent event)
    {
        if (!event.getLevel().isClientSide())
        {
            if (event.getEntity() instanceof ServerPlayer player)
            {
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    PlayerWeightBar.clearModifiers(player);
                });
            }
        }
        else
        {
            if (event.getEntity() instanceof ServerPlayer player)
            {
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    PlayerWeightBar.clearModifiers(player);
                });
            }
        }
    }


}
