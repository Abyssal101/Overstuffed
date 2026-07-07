package net.willsbr.gluttonousgrowth.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.gluttonousgrowth.Block.ModBlocks;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;

import java.util.function.Consumer;

public class ModFluids {
    public static ResourceLocation CALORIUM_STILL = new ResourceLocation(GluttonousGrowth.MODID, "block/nutrientpaste");
    //These do not exist yet, as they are not needed
    public static ResourceLocation CALORIUM_FLOW = new ResourceLocation(GluttonousGrowth.MODID, "block/nutrientpaste");
    public static ResourceLocation CALORIUM_OVERLAY = new ResourceLocation(GluttonousGrowth.MODID, "block/nutrientpaste");

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, GluttonousGrowth.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, GluttonousGrowth.MODID);

    // Fluid Type: controls sounds, density, temperature, and client-side textures/colors.
    public static final RegistryObject<FluidType> CALORIUM_TYPE = FLUID_TYPES.register("calorium", () -> new FluidType(FluidType.Properties.create()
            .canDrown(true)
            .canPushEntity(true)
            .density(1500)
            .viscosity(3000)
            .temperature(350)
            .supportsBoating(false)
    ) {
        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return CALORIUM_STILL;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return CALORIUM_FLOW;
                }

                @Override
                public ResourceLocation getOverlayTexture() {
                    return CALORIUM_OVERLAY; // optional, can be null
                }

                @Override
                public int getTintColor() {
                    // ABGR format; 0xAABBGGRR. Slightly orange with some transparency.
                    return 0xC0_40_80_FF; // alpha=0xC0, blue=0x40, green=0x80, red=0xFF
                }
            });
        }
    });

    // Forward declarations for properties
    public static final RegistryObject<ForgeFlowingFluid> CALORIUM = FLUIDS.register("calorium",
            () -> new ForgeFlowingFluid.Source(ModFluids.CALORIUM_PROPERTIES));
    public static final RegistryObject<ForgeFlowingFluid> FLOWING_CALORIUM = FLUIDS.register("flowing_calorium",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CALORIUM_PROPERTIES));

    // The block that appears in the world when the fluid is placed (not shown on creative tab by default).
    public static final RegistryObject<LiquidBlock> CALORIUM_BLOCK = ModBlocks.BLOCKS.register("calorium",
            () -> new LiquidBlock(CALORIUM, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    // Properties connect everything together. Bucket and block are wired up from other registries.
    public static final ForgeFlowingFluid.Properties CALORIUM_PROPERTIES = new ForgeFlowingFluid.Properties(
            CALORIUM_TYPE, CALORIUM, FLOWING_CALORIUM)
            .slopeFindDistance(2).levelDecreasePerBlock(2)
            .block(CALORIUM_BLOCK)
            .bucket(() -> net.willsbr.gluttonousgrowth.Item.ModItems.CALORIUM_BUCKET.get());

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
        // The block is registered via ModBlocks.BLOCKS, which is already registered in the main class.
    }
}
