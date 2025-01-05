package net.willsbr.overstuffed.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.willsbr.overstuffed.client.ClientStuffedBarData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=Player.class)
public abstract class MixinCanEat {


    @Shadow public abstract FoodData getFoodData();

    @Inject(method = "canEat",at =@At("RETURN"),cancellable = true)
    protected void canEat(boolean pCanAlwaysEat, CallbackInfoReturnable<Boolean> cir)
    {
        //this.abilities.invulnerable ||

         if(ClientStuffedBarData.getPlayerStuffedBar()<(ClientStuffedBarData.getSoftLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getHardLimit()))
         {

//             if(this.getFoodData().getFoodLevel()>=20)
//             {
//                 Object conversionPlayer=(Object)this;
//                 Player thisPlayer=(Player)conversionPlayer;
//                 Level level=thisPlayer.getLevel();
//                 if(level.isClientSide)
//                 {
//                     ModMessages.sendToServer(new OverfullFoodC2SPacket());
//                 }
//             }
             //ModMessages.sendToServer(new OverfullFoodC2SPacket());

             cir.setReturnValue(true);
         }

    }
}
