package net.willsbr.overstuffed.Mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.willsbr.overstuffed.client.ClientStuffedBarData;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.OverfullFoodC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= Item.class)
public abstract class MixinFinishUsingItem {

    //@Shadow public abstract FoodData getFoodData();

    @Shadow public abstract boolean isEdible();

    @Inject(method = "finishUsingItem",at =@At("RETURN"))
    protected void canEat(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, CallbackInfoReturnable<ItemStack> cir)
    {
        //this.abilities.invulnerable ||

        System.out.println("fuck");
        ModMessages.sendToServer(new OverfullFoodC2SPacket());
        if(pStack.getItem().isEdible())
        {
            ModMessages.sendToServer(new OverfullFoodC2SPacket());
            Player player= (Player)pLivingEntity;
            if(((Player)pLivingEntity).getFoodData().getFoodLevel()>=20)
            {
                Level level=player.getLevel();
                if(level.isClientSide)
                {
                    ModMessages.sendToServer(new OverfullFoodC2SPacket());
                }

            }

            //cir.setReturnValue(true);
        }

         }

}

