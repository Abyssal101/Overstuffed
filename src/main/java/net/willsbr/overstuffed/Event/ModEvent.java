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
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBar;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.client.ClientCPMConfigData;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.ClientCPMStuffedSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.ClientCPMWeightSyncS2CPacket;
import net.willsbr.overstuffed.networking.packet.OverfullFoodDataSyncPacketS2C;
import net.willsbr.overstuffed.networking.packet.WeightBarDataSyncPacketS2C;

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

            event.getOriginal().getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(oldStore -> {
                event.getEntity().getCapability(CPMDataProvider.PLAYER_CPM_DATA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);

                    ModMessages.sendToPlayer(new ClientCPMStuffedSyncS2CPacket(oldStore.getStuffedLayerName()),(ServerPlayer) event.getEntity());
                    ModMessages.sendToPlayer(new ClientCPMWeightSyncS2CPacket(oldStore.getWeightLayerName()),(ServerPlayer) event.getEntity());

                });
            });

            event.getOriginal().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });

        }

    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        //stuffedbar
        event.register(PlayerStuffedBar.class);
        event.register(CPMData.class);
        event.register(PlayerWeightBar.class);
    }
    //END OF ABSOULTE MIN, PLAYERTICK IS GONNA BE USEFUL THOUGH

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            //stuffed filling hunger bar logic
            event.player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar -> {
                if(stuffedBar.getCurrentStuffedLevel() > 0 && event.player.getRandom().nextFloat() < 0.005f
                 && event.player.getFoodData().getFoodLevel()<20) { // Once Every 10 Seconds on Avg

                    stuffedBar.subStuffedLevel(1);
                    event.player.getFoodData().setFoodLevel(event.player.getFoodData().getFoodLevel()+1);
                    event.player.sendSystemMessage(Component.literal("Subtracted Hunger:"+stuffedBar.getCurrentStuffedLevel()));
                    //adagsdagasgd
                    ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getStuffedMax(), stuffedBar.getOverstuffedMax(),
                            stuffedBar.getSuperStuffedMax()),(ServerPlayer) event.player);

                    //create weight updates here


                    //CPMData.checkIfUpdateCPM("stuffed");
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

                    //create weight updates here
                   if(weightBar.weightUpdateStatus())
                   {
                       if(weightBar.getQueuedWeight()<=0)
                       {
                           int foodCals=weightBar.getWeightChanges();
                           //this makes it so the weight chance from a single food item gets added to the total amount
                           if(foodCals!=0)
                           {
                               weightBar.addChangetoQueue(foodCals*2);
                               weightBar.setWeightUpdateDelay(foodCals*10);
                           }
                       }
                       else {
                           weightBar.addWeight();

                       }

                       ModMessages.sendToPlayer(new WeightBarDataSyncPacketS2C(weightBar.getCurrentWeight()),(ServerPlayer) event.player);
                       weightBar.setWeightUpdateStatus(false);
                       weightBar.setWeightTick(event.player.tickCount);
                   }
                   else {
                    if((event.player.tickCount-weightBar.getWeightTick())>=weightBar.getWeightUpdateDelay())
                    {
                        weightBar.setWeightUpdateStatus(true);
                       // System.out.println("Weight update but not");
                    }
                   }
                //THE DREADED LOSE WEIGHT FUNCTIONALITY!
                //this sees if the player has less than 5 food bars
                if(event.player.getFoodData().getFoodLevel()<18 )
                {
                    if(weightBar.getSavedTickforWeightLoss()==-1)
                    {
                        weightBar.setSavedTickforWeightLoss(event.player.tickCount);
                        System.out.println("Weight Delay"+(int)(100-event.player.getFoodData().getExhaustionLevel()*5));
                        weightBar.setWeightLossDelay((int)(200-event.player.getFoodData().getExhaustionLevel()*5));
                        //weightBar.setWeightLossDelay(10);
                    }  else if ((event.player.tickCount-weightBar.getSavedTickforWeightLoss()>weightBar.getWeightLossDelay())){
                        //
                        System.out.println(weightBar.getWeightUpdateDelay());

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

//                    ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getStuffedMax(),
//                            stuffedBar.getSuperStuffedMax()), player);
                });

            }


        }



    }



}
