package net.willsbr.overstuffed;

import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.willsbr.overstuffed.Block.ModBlocks;
//import net.willsbr.overstuffed.CPMCompat.CPMCompat;
import net.willsbr.overstuffed.CPMCompat.CPMCompat;
import net.willsbr.overstuffed.Effects.ModEffects;
import net.willsbr.overstuffed.Item.ModItems;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.potion.ModPotions;
import net.willsbr.overstuffed.sound.ModSounds;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OverStuffed.MODID)
public class OverStuffed
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "overstuffed";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public OverStuffed()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModSounds.register(modEventBus);
        ModSounds.createArrays();
        ModPotions.register(modEventBus);
        ModEffects.register(modEventBus);


       // ModEntities.ENTITY_TYPES.register(modEventBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        event.enqueueWork(() ->{
        ModMessages.register();
           // ModVillagers.registerPOIS();
        });
        ModMessages.register();
    }
    private void enqueueIMC(final InterModEnqueueEvent event)  {
          InterModComms.sendTo("cpm", "api", () -> (Supplier<?>) () -> new CPMCompat());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
