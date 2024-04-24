package net.willsbr.overstuffed.potion;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.willsbr.overstuffed.OverStuffed;

public class ModPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, OverStuffed.MODID);



    public static void register(IEventBus eventBus)
    {
        POTIONS.register(eventBus);
    }
}
