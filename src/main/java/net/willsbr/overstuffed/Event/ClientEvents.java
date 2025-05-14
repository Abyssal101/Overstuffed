package net.willsbr.overstuffed.Event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.Command.*;
import net.willsbr.overstuffed.Entity.ModEntities;
import net.willsbr.overstuffed.Menu.ConfigScreen;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.Renderer.ScaleBER;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.client.HudOverlay;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.StuffedPackets.OverfullFoodC2SPacket;
import net.willsbr.overstuffed.networking.packet.OverstuffedEffectC2SPacket;
import net.willsbr.overstuffed.networking.packet.WeightPackets.addWeightC2SPacket;
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
                    else if(!currentPlayer.isCreative() && heldItem.isEdible() && currentPlayer.getFoodData().getFoodLevel()>=20)
                    {

                        ModMessages.sendToServer(new OverfullFoodC2SPacket());
                        currentPlayer.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar ->
                        {
                            stuffedBar.addStuffedLevel(1, useItemEvent.getEntity().level().getGameTime(), heldItem.getUseDuration());
                        });
                        //creating the weight change your gonna send, uses the base nutrition value
                        //this line gets it from the player
                        int weightForQueue=0;
                       try{
                           weightForQueue=heldItem.getItem().getFoodProperties(heldItem,currentPlayer).getNutrition();
                       }
                       catch(Exception e)
                       {

                       }

                        //Makes weight have more of an impact I guess
                        ModMessages.sendToServer(new addWeightC2SPacket(weightForQueue));
                    }

                }

            }

        }

        @SubscribeEvent
        public static void onClientPlayerTick(LivingEvent.LivingTickEvent event)
        {
            //Minecraft.getInstance().player.sendSystemMessage(Component.literal("EEE"));
            if(event.getEntity() instanceof Player)
            {
                Player player=(Player)event.getEntity();
                //player.sendSystemMessage(Component.literal("EEE"));
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    player.getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(playerUnlocks -> {

                        if(event.getEntity().tickCount % 20 == 0)
                        {
                            if(Math.random()<weightBar.calculateCurrentWeightStage()*0.05)
                            {
                                xWedgeCheck(player,Minecraft.getInstance().level,0.3);
                                zWedgeCheck(player,Minecraft.getInstance().level,0.3);
                            }

                        }
                    });

                });
            }


        }
        public static void xWedgeCheck(Player player, Level playerLevel, double addedHitboxSize)
        {
            BlockPos feetBlock=player.blockPosition();
            AABB playerBoundBox=player.getBoundingBox();
            double playerWidth=playerBoundBox.getXsize();
            int totalBlocksX=(int)Math.ceil(playerWidth);

            double playerHeight=playerBoundBox.getYsize();
            int totalBlocksY=(int)Math.ceil(playerHeight);



            BlockPos positiveXPos[]= new BlockPos[Math.max(1,(totalBlocksX))];
            BlockPos negativeXPos[]= new BlockPos[Math.max(1,(totalBlocksX))];

            for(int i=0;i<positiveXPos.length;i++)
            {
                positiveXPos[i]=feetBlock.offset(i+1,0,0);
                negativeXPos[i]=feetBlock.offset(-(i+1),0,0);
            }

            //todo in order to solve the issue of trying to figure out if something is on the left or right of the player when your inside a block
            //make two hitboxes that are half the players hitbox, and move them so they are on either side of the center of the player
            //by doing that you can have a marker saying that if the rightside has intersectd ANYTHING in range it's all good
            //then it has to check the other side.

            playerBoundBox=playerBoundBox.inflate(addedHitboxSize);
            AABB playerRightBB=playerBoundBox.move(playerBoundBox.getXsize()/2,0,0);
            AABB playerLeftBB=playerBoundBox.move(-playerBoundBox.getXsize()/2,0,0);

            //Testing Code creates a platform of brich wood
//        for(int i=0;i<10;i++)
//        {
//            for(int j=0;j<10;j++)
//            {
//                BlockPos copy=feetBlock;
//                copy=copy.offset(i-5,-1,j-5);
//                playerLevel.setBlockAndUpdate(copy,Blocks.BIRCH_PLANKS.defaultBlockState());
//            }
//        }


            //this effectively starts at the center, and checks to see if First the right side of the player is intersecting with
            //the block they are standing in, then moves outward till it finds some interection or goes beyond the width being
            //tested


            boolean noOpenSpace=true;
            for(int y=0;y<totalBlocksY && noOpenSpace ;y++)
            {
                boolean intersectedRight=false;
                boolean intersectedLeft=false;



                for(int i = -1; !intersectedRight && i<positiveXPos.length; i++)
                {
                    if(i==-1)
                    {
                        VoxelShape blockShapeXPos=playerLevel.getBlockState(feetBlock).getCollisionShape(playerLevel,feetBlock);
//                playerLevel.setBlockAndUpdate(feetBlock.below(), Blocks.OAK_PLANKS.defaultBlockState());

                        if(!blockShapeXPos.isEmpty())
                        {

                            blockShapeXPos=blockShapeXPos.move(feetBlock.getX(),feetBlock.getY(),feetBlock.getZ());
                            if(playerRightBB.intersects(blockShapeXPos.bounds()))
                            {
                                intersectedRight=true;
                            }
                        }
                    }
                    else{
//                playerLevel.setBlockAndUpdate(positiveXPos[i].below(), Blocks.OAK_PLANKS.defaultBlockState());
//                playerLevel.setBlockAndUpdate(negativeXPos[i].below(), Blocks.OAK_PLANKS.defaultBlockState());

                        VoxelShape blockShapeXPos=playerLevel.getBlockState(positiveXPos[i]).getCollisionShape(playerLevel,positiveXPos[i]);
                        if(!blockShapeXPos.isEmpty())
                        {
                            blockShapeXPos=blockShapeXPos.move(positiveXPos[i].getX(),positiveXPos[i].getY(),positiveXPos[i].getZ());
                            if(playerRightBB.intersects(blockShapeXPos.bounds()))
                            {
                                intersectedRight=true;
                            }
                        }
                    }
                }

                for(int i = -1; !intersectedLeft && i<negativeXPos.length; i++)
                {
                    if(i==-1) {
                        VoxelShape blockShapeXNeg = playerLevel.getBlockState(feetBlock).getCollisionShape(playerLevel, feetBlock);
                        if (!blockShapeXNeg.isEmpty())
                        {
                            blockShapeXNeg= blockShapeXNeg.move(feetBlock.getX(), feetBlock.getY(), feetBlock.getZ());
                            if (playerLeftBB.intersects(blockShapeXNeg.bounds())) {
                                intersectedLeft = true;
                            }
                        }

                    }
                    else{
                        VoxelShape blockShapeXNeg=playerLevel.getBlockState(negativeXPos[i]).getCollisionShape(playerLevel,negativeXPos[i]);
                        if(!blockShapeXNeg.isEmpty())
                        {
                            blockShapeXNeg=blockShapeXNeg.move(negativeXPos[i].getX(), negativeXPos[i].getY(), negativeXPos[i].getZ());
                            if (playerLeftBB.intersects(blockShapeXNeg.bounds())) {
                                intersectedLeft = true;
                            }
                        }
                    }

                }


                if(!(intersectedRight && intersectedLeft))
                {
                    noOpenSpace=false;
                    break;
                }

                //Start of updating the Y logic
                feetBlock=feetBlock.above();

                for(int e=0;e<positiveXPos.length;e++)
                {
                    positiveXPos[e]=positiveXPos[e].offset(0,1,0);
                    negativeXPos[e]=negativeXPos[e].offset(0,1,0);
                }



            }

            if(noOpenSpace)
            {
                MobEffectInstance wedgeSlowness=new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100,4);
                player.addEffect(wedgeSlowness);
            }


        }

        public static void zWedgeCheck(Player player, Level playerLevel, double addedHitboxSize)
        {
            BlockPos feetBlock=player.blockPosition();
            AABB playerBoundBox=player.getBoundingBox();
            double playerDepth=playerBoundBox.getZsize();
            int totalBlocksZ=(int)Math.ceil(playerDepth);

            double playerHeight=playerBoundBox.getYsize();
            int totalBlocksY=(int)Math.ceil(playerHeight);



            BlockPos positiveXPos[]= new BlockPos[Math.max(1,(totalBlocksZ))];
            BlockPos negativeXPos[]= new BlockPos[Math.max(1,(totalBlocksZ))];

            for(int i=0;i<positiveXPos.length;i++)
            {
                positiveXPos[i]=feetBlock.offset(0,0,i+1);
                negativeXPos[i]=feetBlock.offset(0,0,-(i+1));
            }

            //todo in order to solve the issue of trying to figure out if something is on the left or right of the player when your inside a block
            //make two hitboxes that are half the players hitbox, and move them so they are on either side of the center of the player
            //by doing that you can have a marker saying that if the rightside has intersectd ANYTHING in range it's all good
            //then it has to check the other side.

            playerBoundBox=playerBoundBox.inflate(addedHitboxSize);
            AABB playerRightBB=playerBoundBox.move(0,0,playerBoundBox.getZsize()/2);
            AABB playerLeftBB=playerBoundBox.move(0,0,-playerBoundBox.getXsize()/2);

            //Testing Code creates a platform of brich wood
//        for(int i=0;i<10;i++)
//        {
//            for(int j=0;j<10;j++)
//            {
//                BlockPos copy=feetBlock;
//                copy=copy.offset(i-5,-1,j-5);
//                playerLevel.setBlockAndUpdate(copy,Blocks.BIRCH_PLANKS.defaultBlockState());
//            }
//        }


            //this effectively starts at the center, and checks to see if First the right side of the player is intersecting with
            //the block they are standing in, then moves outward till it finds some interection or goes beyond the width being
            //tested


            boolean noOpenSpace=true;
            for(int y=0;y<totalBlocksY && noOpenSpace ;y++)
            {
                boolean intersectedRight=false;
                boolean intersectedLeft=false;

                for(int i = -1; !intersectedRight && i<positiveXPos.length; i++)
                {
                    if(i==-1)
                    {
                        VoxelShape blockShapeZPos=playerLevel.getBlockState(feetBlock).getCollisionShape(playerLevel,feetBlock);
//                playerLevel.setBlockAndUpdate(feetBlock.below(), Blocks.OAK_PLANKS.defaultBlockState());

                        if(!blockShapeZPos.isEmpty())
                        {

                            blockShapeZPos=blockShapeZPos.move(feetBlock.getX(),feetBlock.getY(),feetBlock.getZ());
                            if(playerRightBB.intersects(blockShapeZPos.bounds()))
                            {
                                intersectedRight=true;
                            }
                        }
                    }
                    else{
//                playerLevel.setBlockAndUpdate(positiveXPos[i].below(), Blocks.OAK_PLANKS.defaultBlockState());
//                playerLevel.setBlockAndUpdate(negativeXPos[i].below(), Blocks.OAK_PLANKS.defaultBlockState());

                        VoxelShape blockShapeZPos=playerLevel.getBlockState(positiveXPos[i]).getCollisionShape(playerLevel,positiveXPos[i]);
                        if(!blockShapeZPos.isEmpty())
                        {
                            blockShapeZPos=blockShapeZPos.move(positiveXPos[i].getX(),positiveXPos[i].getY(),positiveXPos[i].getZ());
                            if(playerRightBB.intersects(blockShapeZPos.bounds()))
                            {
                                intersectedRight=true;
                            }
                        }
                    }
                }

                for(int i = -1; !intersectedLeft && i<negativeXPos.length; i++)
                {
                    if(i==-1) {
                        VoxelShape blockShapeZNeg = playerLevel.getBlockState(feetBlock).getCollisionShape(playerLevel, feetBlock);
                        if (!blockShapeZNeg.isEmpty())
                        {
                            blockShapeZNeg= blockShapeZNeg.move(feetBlock.getX(), feetBlock.getY(), feetBlock.getZ());
                            if (playerLeftBB.intersects(blockShapeZNeg.bounds())) {
                                intersectedLeft = true;
                            }
                        }

                    }
                    else{
                        VoxelShape blockShapeZNeg=playerLevel.getBlockState(negativeXPos[i]).getCollisionShape(playerLevel,negativeXPos[i]);
                        if(!blockShapeZNeg.isEmpty())
                        {
                            blockShapeZNeg=blockShapeZNeg.move(negativeXPos[i].getX(), negativeXPos[i].getY(), negativeXPos[i].getZ());
                            if (playerLeftBB.intersects(blockShapeZNeg.bounds())) {
                                intersectedLeft = true;
                            }
                        }
                    }

                }


                if(!(intersectedRight && intersectedLeft))
                {
                    noOpenSpace=false;
                    break;
                }

                //Start of updating the Y logic
                feetBlock=feetBlock.above();

                for(int e=0;e<positiveXPos.length;e++)
                {
                    positiveXPos[e]=positiveXPos[e].offset(0,1,0);
                    negativeXPos[e]=negativeXPos[e].offset(0,1,0);
                }



            }

            if(noOpenSpace)
            {
                ModMessages.sendToServer(new OverstuffedEffectC2SPacket(1,100,4));
            }




        }


        @SubscribeEvent
        public static void commandRegister(RegisterCommandsEvent event)
        {
            registerCommands(event);
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
            event.registerAbove(VanillaGuiOverlay.VIGNETTE.id(),"stuffedbar", HudOverlay.HUD_STUFFEDBAR);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            //RENDERERS ARE CREATED CLIENT SIDE, GOOD TO KNOW


            //Block Entities
            event.registerBlockEntityRenderer(ModEntities.SCALE.get(), ScaleBER::new);

        }


    }

    public static void registerCommands(RegisterCommandsEvent event)
    {
        CommonEventMethods.registerCommands(event);
    }
}
