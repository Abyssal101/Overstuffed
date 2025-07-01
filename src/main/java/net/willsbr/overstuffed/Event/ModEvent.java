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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocks;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.Command.figuraNBTUpdateCommand;
import net.willsbr.overstuffed.Effects.ModEffects;
import net.willsbr.overstuffed.GluttonousGrowth;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettings;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.StuffedBar.PlayerCalorieMeter;
import net.willsbr.overstuffed.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.GluttonousWorldConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.SettingPackets.PlayerSyncAllSettingsPollS2C;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.CalorieMeterDelaySyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.OverfullFoodDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.WeightPackets.BurstGainDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.WeightPackets.QueuedWeightSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.WeightPackets.WeightBarDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.calIntervalUpdateS2CPacket;
import net.willsbr.overstuffed.sound.ModSounds;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid= GluttonousGrowth.MODID)
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
        if(event.getObject() instanceof Player)
        {
            if(!event.getObject().getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).isPresent()) {
                event.addCapability(new ResourceLocation(GluttonousGrowth.MODID, "calmeter"), new PlayerCalorieMeterProvider());
            }
            if(!event.getObject().getCapability(CPMDataProvider.PLAYER_CPM_DATA).isPresent()) {
                event.addCapability(new ResourceLocation(GluttonousGrowth.MODID, "configsettings"), new CPMDataProvider());
            }
            if(!event.getObject().getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).isPresent()) {
                event.addCapability(new ResourceLocation(GluttonousGrowth.MODID, "playerserversettings"), new PlayerServerSettingsProvider());
            }
            if(!event.getObject().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).isPresent()) {
                event.addCapability(new ResourceLocation(GluttonousGrowth.MODID, "weightbar"), new PlayerWeightBarProvider());

            }
            if(!event.getObject().getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).isPresent()) {
                event.addCapability(new ResourceLocation(GluttonousGrowth.MODID, "unlocks"), new PlayerUnlocksProvider());
            }
        }
    }

    //adds capability back when you die
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();

        if (event.getEntity() instanceof ServerPlayer && event.getOriginal() instanceof ServerPlayer)
        {

            event.getOriginal().getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(newStore -> {
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
        event.register(PlayerCalorieMeter.class);
        event.register(CPMData.class);
        event.register(PlayerWeightBar.class);
        event.register(PlayerUnlocks.class);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            //Making it a little more effcient
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
    public static void burstGain(PlayerWeightBar weightBar,PlayerServerSettings serverSettings,TickEvent.PlayerTickEvent event,int curWeightStage)
    {
        if(ModList.get().isLoaded("cpm"))
        {
            if(curWeightStage!=weightBar.getLastWeightStage())
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
                    int thresholdPercentage=(100/weightBar.getTotalStages());
                    int progressionCheck=(weightBar.getLastWeightStage()*thresholdPercentage+((int)percentageThroughStage));

                    if(progressionCheck%thresholdPercentage==0 && (progressionCheck/thresholdPercentage)==curWeightStage)
                    {
                        weightBar.setLastWeightStage(curWeightStage);
                        weightBar.setAmountThroughStage(0);
                        ModMessages.sendToPlayer(new BurstGainDataSyncPacketS2C(weightBar.getLastWeightStage(), weightBar.getAmountThroughStage()), (ServerPlayer) event.player);
                    }
                    else
                    {
                        if(curWeightStage>=weightBar.getLastWeightStage()+1)
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
                }
            }
        }

    }

    public static void weightSystem(TickEvent.PlayerTickEvent event)
    {
        event.player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            event.player.getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(playerUnlocks -> {
                event.player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {


                int curWeightStage = weightBar.calculateCurrentWeightStage();
                int lastWeightStage = weightBar.getLastWeightStage();
                //create weight updates here
                if (serverSettings.stageBasedGain())
                {
                    burstGain(weightBar,serverSettings, event, curWeightStage);
                }
                //Adding and removing health modifiers
                weightBarEffects(event,serverSettings, weightBar, curWeightStage, lastWeightStage);
                if(!serverSettings.stageBasedGain())
                {
                    weightBar.setLastWeightStage(curWeightStage);
                }

                ModSounds.playGurgle(event.player);

                if (weightBar.weightUpdateStatus()) {

                    if (weightBar.getQueuedWeight() <= 0) {
                        int foodCals = weightBar.getWeightChanges();
                        //this makes it so the weight change from a single food item gets added to the total amount
                        if (foodCals != 0)
                        {
                            weightBar.addChangetoQueue(foodCals);
                            int checkDelay = foodCals * GluttonousWorldConfig.multiplierForWGDelay.get();
                            if (checkDelay > GluttonousWorldConfig.maxWGTickDelay.get()) {
                                weightBar.setWeightUpdateDelay(GluttonousWorldConfig.maxWGTickDelay.get());
                            } else {
                                weightBar.setWeightUpdateDelay((int) (checkDelay * weightBar.getWeightUpdateDelayModifier()));
                                //weightBar.setWeightUpdateDelay(foodCals);
                            }
                        }
                    } else
                    {

                        weightBar.addWeight();
                        ModMessages.sendToPlayer(new QueuedWeightSyncS2CPacket(weightBar.getQueuedWeight(),
                                weightBar.getTotalWeightChanges()), (ServerPlayer) event.player);
                    }
                    ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()), (ServerPlayer) event.player);
                    weightBar.setWeightUpdateStatus(false);
                    weightBar.setWeightTick(event.player.tickCount);

                } else
                {
                    if ((event.player.tickCount - weightBar.getWeightTick()) >= weightBar.getWeightUpdateDelay()) {
                        weightBar.setWeightUpdateStatus(true);
                    }
                }

                //THE DREADED LOSE WEIGHT FUNCTIONALITY!
                if (event.player.getFoodData().getFoodLevel() < GluttonousWorldConfig.thresholdLoseWeight.get()
                        || event.player.hasEffect(ModEffects.GOLDEN_DIET.get())) {

                    if (weightBar.getSavedTickforWeightLoss() == -1)
                    {
                        weightBar.setSavedTickforWeightLoss(event.player.tickCount);
                        if ((event.player.hasEffect(ModEffects.GOLDEN_DIET.get())))
                        {
                            weightBar.setWeightLossDelay(GluttonousWorldConfig.goldenDietTickDelay.get());
                        }
                        else
                        {
                            //this calculation effectively asys that for your exhaustion at the moment you end up losing weight faster
                            int calculated = (int) ((event.player.getFoodData().getExhaustionLevel() * 5) * weightBar.getWeightUpdateDelayModifier());

                            //Max weight loss is some constant, currently 400 ticks meaning that if you were idling and 1 one,
                            //you would lose that 1 weight in 20 seconds
                            calculated=(int)(calculated*(1.0+weightBar.calculateCurrentWeightPercentage()));

                            calculated = GluttonousWorldConfig.maxWeightLossTime.get() - calculated;
                            calculated = Math.max(5, calculated);
                            weightBar.setWeightLossDelay(calculated);
                        }
                    } else if ((event.player.tickCount - weightBar.getSavedTickforWeightLoss() > weightBar.getWeightLossDelay()))
                    {
                        //
                        weightBar.loseWeight();
                        //this way you can lose more faster, maybe I should set that to some number that gets added.
                        if (event.player.hasEffect(ModEffects.GOLDEN_DIET.get()) &&
                                event.player.getFoodData().getFoodLevel() < GluttonousWorldConfig.thresholdLoseWeight.get())
                        {
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
        event.player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(calorieMeter -> {

            if(calorieMeter.checkClearCalories(event.player.tickCount))
            {
                double conversionPercentage=GluttonousWorldConfig.calConvertedPercentage.get();
                conversionPercentage=Math.min(conversionPercentage,1);
                conversionPercentage=Math.max(conversionPercentage,0);
                int totalCalories=(int)(calorieMeter.getCurrentCalories()*conversionPercentage);

                //Makes it so that at a minimum you gain 3 calories, however that won't do much for you because
                //ofn the conversion rate.
                totalCalories=Math.max(3,totalCalories);
                double max=calorieMeter.getMaxCalories();
                //20 is hardcoded in base, so it'll be hard-coded here too!
                int caloriesLostToHunger=(20-event.player.getFoodData().getFoodLevel())* GluttonousWorldConfig.calToHungerRate.get();

                if((totalCalories/max)>calorieMeter.getSlowMetabolismThres())
                {
                    totalCalories=(int)(totalCalories* GluttonousWorldConfig.slowMetabolismMultiplier.get());
                }
                else if((totalCalories/max)>calorieMeter.getModMetabolismThres())
                {
                    totalCalories=(int)(totalCalories* GluttonousWorldConfig.modMetabolismMultiplier.get());
                }

                totalCalories=totalCalories-caloriesLostToHunger;
                if(caloriesLostToHunger>0)
                {
                    if(event.player.getFoodData().getFoodLevel()<20)
                    {
                        int newFood=Math.min(20,event.player.getFoodData().getFoodLevel()+caloriesLostToHunger/6);
                        event.player.getFoodData().setFoodLevel(newFood);
                    }
                }

                //handles upgrading the max cap if you go past that interval
                if(calorieMeter.getCalLost()>=calorieMeter.getInterval())
                {
                    calorieMeter.setMaxCalories(Math.min(calorieMeter.getMaxCalories()+ GluttonousWorldConfig.calCapIncrement.get()
                    , GluttonousWorldConfig.absCalCap.get()));
                    calorieMeter.setInterval(calorieMeter.getInterval()+ GluttonousWorldConfig.intervalIncrease.get());
                    calorieMeter.setCalLost(0);
                }

                //This is the when we add our calories to the weightbar properly
                if(totalCalories>0)
                {
                    calorieMeter.addCalLost(totalCalories);
                    int finalTotalCalories = totalCalories;
                    event.player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                        weightBar.addWeightChanges(finalTotalCalories / GluttonousWorldConfig.calToWeightRate.get());
                    });
                }
                //All previous code modified totalCalories, this is bringing the removal to
                //mimic the minimum of 3 calories being removed, basically I dont want you to be stuck at 1 weight
                //because the percentage removed is so small.
                int calRemoved=(int)(calorieMeter.getCurrentCalories()*conversionPercentage);
                if(calRemoved<=3)
                {
                    calRemoved=3;
                }

                calorieMeter.setCurrentCalories(calorieMeter.getCurrentCalories()-calRemoved);
                calorieMeter.setFoodEatenTick((calorieMeter.getCurrentCalories()!=0) ? event.player.tickCount : -1);

                int newDelay=GluttonousWorldConfig.minCalClearDelay.get()+
                        (int)(((double)calorieMeter.getCurrentCalories()/calorieMeter.getMaxCalories())*GluttonousWorldConfig.maxCalClearDelay.get());
                calorieMeter.setCalClearDelay(newDelay);

                ModMessages.sendToPlayer(new calIntervalUpdateS2CPacket(calorieMeter.getCalLost(),calorieMeter.getInterval(),calorieMeter.getMaxCalories()),(ServerPlayer)event.player);

                ModSounds.playBurp(event.player);

                ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(calorieMeter.getCurrentCalories(), calorieMeter.getMaxCalories(),
                        calorieMeter.getModMetabolismThres(),
                        calorieMeter.getModMetabolismThres()),(ServerPlayer) event.player);
                ModMessages.sendToPlayer(new CalorieMeterDelaySyncPacketS2C(calorieMeter.getCalClearDelay(),calorieMeter.getFoodEatenTick()),(ServerPlayer) event.player);

            }
        });

    }


    public static void weightBarEffects(TickEvent.PlayerTickEvent event,PlayerServerSettings serverSettings, PlayerWeightBar weightBar, int xOf5, int lastWeightStage)
    {
            ServerPlayer player = (ServerPlayer) event.player;
            if(!serverSettings.weightEffects())
            {
                PlayerWeightBar.clearModifiers(player,weightBar);
                PlayerWeightBar.clearScaling(player,weightBar);
            }
            else
            {
                if(serverSettings.stageBasedGain())
                {
                    if(xOf5!=lastWeightStage)
                    {

                        //This handles changing the modifiers when somehow the last weight stage and the new weight stage are greater than a one value jump
                        //Maybe could make it just this, but slightly more effcient I think?

                        PlayerWeightBar.addCorrectModifier(player);
                        //handles adding the correct hitbox changes
                        PlayerWeightBar.addCorrectScaling(player);
                    }
                }
                else
                {
                    PlayerWeightBar.addCorrectModifier(player);
                    //handles adding the correct hitbox changes
                    if(serverSettings.isHitboxScalingEnabled())
                    {
                        PlayerWeightBar.addCorrectScaling(player);

                    }
                }

                if(player.getHealth()>player.getMaxHealth())
                {
                    player.setHealth(player.getMaxHealth());
                }

            }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {

        if (!event.getLevel().isClientSide()) {

            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(calorieMeter -> {
                    ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(calorieMeter.getCurrentCalories(), calorieMeter.getMaxCalories(),calorieMeter.getMaxCalories(),calorieMeter.getSlowMetabolismThres()), player);
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

    //Hopefully this fixes the player  constantly stacking buffs when you leave and rejoin
    @SubscribeEvent
    public static void onPlayerLeaveWorld(EntityLeaveLevelEvent event)
    {
        //TODO This doesn't work beacuse if you remove the mod you still have the debuff/buff
        if (!event.getLevel().isClientSide())
        {
            if (event.getEntity() instanceof ServerPlayer player)
            {
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    PlayerWeightBar.clearModifiers(player,weightBar);

                });
            }
        }
        else
        {
            if (event.getEntity() instanceof Player player)
            {
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    PlayerWeightBar.clearModifiers(player,weightBar);
                });
            }
        }
    }


}
