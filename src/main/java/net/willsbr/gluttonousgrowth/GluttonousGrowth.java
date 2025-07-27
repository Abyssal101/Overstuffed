package net.willsbr.gluttonousgrowth;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.willsbr.gluttonousgrowth.Block.ModBlocks;
import net.willsbr.gluttonousgrowth.CPMCompat.CPMCompat;
import net.willsbr.gluttonousgrowth.Effects.ModEffects;
import net.willsbr.gluttonousgrowth.Entity.ModEntities;
import net.willsbr.gluttonousgrowth.Item.ModCreativeModeTab;
import net.willsbr.gluttonousgrowth.Item.ModItems;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;
import net.willsbr.gluttonousgrowth.config.GluttonousWorldConfig;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.potion.ModPotions;
import net.willsbr.gluttonousgrowth.sound.ModSounds;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GluttonousGrowth.MODID)
public class GluttonousGrowth
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gluttonousgrowth";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace


    public GluttonousGrowth()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTab.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModSounds.createArrays();
        //ModEntities.ENTITY_TYPES.register(modEventBus);
        ModPotions.register(modEventBus);
        ModEffects.register(modEventBus);
        // Register the methods for modloading
        modEventBus.addListener(this::enqueueIMC); // CPM Compat

        modEventBus.addListener(this::commonSetup);

        // generate/read config/overstuffed.toml
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                GluttonousClientConfig.GENERAL_SPEC,
                "gluttonousgrowth_client.toml");
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.SERVER,
                GluttonousWorldConfig.GENERAL_SPEC,
                "gluttonousgrowth_server.toml");

        // register ConfigScreen as Config button in Mods list
//        ModLoadingContext.get().registerExtensionPoint(
//                ConfigScreenHandler.ConfigScreenFactory.class,
//                () -> new ConfigScreenHandler.ConfigScreenFactory(new BiFunction<Minecraft, Screen, Screen>() {
//                    @Override
//                    public Screen apply(Minecraft mc, Screen screen) {
//                        return new ConfigScreen();
//                    }
//                }));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        event.enqueueWork(() ->{
            // ModVillagers.registerPOIS();
        });
        ModMessages.register();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
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
            Minecraft.getInstance().getMainRenderTarget().enableStencil();
            // Some client setup code
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
