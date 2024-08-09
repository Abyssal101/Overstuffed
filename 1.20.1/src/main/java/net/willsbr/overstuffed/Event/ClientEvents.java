package net.willsbr.overstuffed.Event;

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.willsbr.overstuffed.Command.*;
import net.willsbr.overstuffed.Entity.ModEntities;
import net.willsbr.overstuffed.Menu.ConfigScreen;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.Renderer.ScaleBER;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.client.HudOverlay;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.OverfullFoodC2SPacket;
import net.willsbr.overstuffed.networking.packet.OverstuffedEffectC2SPacket;
import net.willsbr.overstuffed.networking.packet.addWeightC2SPacket;
import net.willsbr.overstuffed.util.KeyBinding;

import java.util.HashMap;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid= OverStuffed.MODID,value= Dist.CLIENT)
    public static class ClientForgeEvents
    {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key input)
        {
            if (KeyBinding.STUFFED_CONFIG.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen());
            }
        }

        @SubscribeEvent
        public static void onFoodUse(LivingEntityUseItemEvent.Finish useItemEvent)
        {


            if(useItemEvent.getEntity() instanceof  Player)
            {
                Player currentPlayer=(Player)useItemEvent.getEntity();
                Level currentLevel=currentPlayer.level();
                if(currentLevel.isClientSide())
                {
                    ItemStack heldItem=useItemEvent.getItem();
                    //heldItem.getItem().getFoodProperties(heldItem, (LivingEntity) currentPlayer).
                    //322 is id for golden apple
                    if(heldItem.is(Items.GOLDEN_APPLE))
                    {
                        int duration=600;
                        int amplifier=0;
                        //  currentPlayer.addEffect(new MobEffectInstance(ModEffects.GOLDEN_DIET.get(), duration ,amplifier));
                        ModMessages.sendToServer(new OverstuffedEffectC2SPacket(0,duration,amplifier));

                    }
                    else if(heldItem.is(Items.GOLDEN_CARROT))
                    {
                        int duration=200;
                        int amplifier=0;
                        // currentPlayer.addEffect(new MobEffectInstance(ModEffects.GOLDEN_DIET.get(), duration ,0));
                        ModMessages.sendToServer(new OverstuffedEffectC2SPacket(0,duration,amplifier));
                    }
                    else if(heldItem.isEdible() && currentPlayer.getFoodData().getFoodLevel()>=20)
                    {


                        ModMessages.sendToServer(new OverfullFoodC2SPacket());
                        currentPlayer.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar ->
                        {
                            stuffedBar.updateNBTData();
                        });
                        //creating the weight change your gonna send, uses the base nutrition value
                        //this line gets it from the player
                        int weightForQueue=heldItem.getItem().getFoodProperties(heldItem,currentPlayer).getNutrition();
                        //Makes weight have more of an impact I guess
                        ModMessages.sendToServer(new addWeightC2SPacket(weightForQueue));
                    }

                }

            }

        }
        @SubscribeEvent
        public static void commandRegister(RegisterCommandsEvent event)
        {
            //TODO Fix all commands to be compatible with Overstuffed Config
            CommandDispatcher commands=event.getDispatcher();
            //SetLayer.register(commands, event.getBuildContext());
            //setMaxWeight.register(commands, event.getBuildContext());
            //setMinWeight.register(commands,event.getBuildContext());
            setCurrentWeight.register(commands,event.getBuildContext());
            //clearLayers.register(commands, event.getBuildContext());
            debugView.register(commands, event.getBuildContext());
            //setWGMethod.register(commands,event.getBuildContext());
            setBurpFrequency.register(commands, event.getBuildContext());
            setGurgleFrequency.register(commands, event.getBuildContext());

        }
    }
    @Mod.EventBusSubscriber(modid= OverStuffed.MODID,value= Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents
    {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event)
        {
            event.register(KeyBinding.STUFFED_CONFIG);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
           // event.registerAboveAll("thirst", ThirstHudOverlay.HUD_THIRST);

            event.registerAboveAll("stuffedbar", HudOverlay.HUD_STUFFEDBAR);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            //RENDERERS ARE CREATED CLIENT SIDE, GOOD TO KNOW


            //Block Entities
            event.registerBlockEntityRenderer(ModEntities.SCALE.get(), ScaleBER::new);

        }


    }


}
