package net.willsbr.overstuffed.Entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.Block.ModBlocks;
import net.willsbr.overstuffed.Entity.BlockEntity.ScaleBlockEntity;
import net.willsbr.overstuffed.OverStuffed;

public class ModEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OverStuffed.MODID);

    public static RegistryObject<BlockEntityType<ScaleBlockEntity>> SCALE=BLOCK_ENTITIES.register("scale_block_entity"
            ,() -> BlockEntityType.Builder.of(ScaleBlockEntity::new, ModBlocks.SCALE.get()).build(null));
  public static void register(IEventBus eventbus)
  {
      BLOCK_ENTITIES.register(eventbus);
  }
}
