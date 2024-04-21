package net.willsbr.overstuffed.Event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.willsbr.overstuffed.Command.*;
import net.willsbr.overstuffed.Menu.ConfigScreen;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.client.HudOverlay;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.OverfullFoodC2SPacket;
import net.willsbr.overstuffed.networking.packet.WeightBarC2SPacket;
import net.willsbr.overstuffed.util.KeyBinding;

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


               Player currentPlayer=(Player)useItemEvent.getEntity();
                Level currentLevel=currentPlayer.getLevel();
                if(currentLevel.isClientSide())
                {
                    ItemStack heldItem=useItemEvent.getItem();
                    if(heldItem.isEdible() && currentPlayer.getFoodData().getFoodLevel()>=20)
                    {
                        //currentPlayer.startUsingItem(currentPlayer.getUsedItemHand());
                        //InteractionResultHolder.consume(heldItem);
                       // heldItem.shrink(1);

                        ModMessages.sendToServer(new OverfullFoodC2SPacket());
                        //creating the weight change your gonna send, uses the base nutrition value
                        //this line gets it from the player
                        int weightForQueue=heldItem.getItem().getFoodProperties(heldItem,currentPlayer).getNutrition();
                        //Makes weight have more of an impact I guess
                        ModMessages.sendToServer(new WeightBarC2SPacket(weightForQueue));



                        //    currentPlayer.stopUsingItem();
                    }

                }


        }
        @SubscribeEvent
        public static void commandRegister(RegisterCommandsEvent event)
        {
            CommandDispatcher commands=event.getDispatcher();
            SetLayer.register(commands, event.getBuildContext());
            setMaxWeight.register(commands, event.getBuildContext());
            setMinWeight.register(commands,event.getBuildContext());
            clearLayers.register(commands, event.getBuildContext());
            viewLayers.register(commands, event.getBuildContext());
            setWGMethod.register(commands,event.getBuildContext());
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

    }


}
