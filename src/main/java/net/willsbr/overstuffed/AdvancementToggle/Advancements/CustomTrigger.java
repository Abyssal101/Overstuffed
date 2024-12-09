//package net.willsbr.overstuffed.AdvancementToggle.Advancements;
//
//import com.google.gson.JsonObject;
//import net.minecraft.advancements.CriteriaTriggers;
//import net.minecraft.advancements.CriterionTrigger;
//import net.minecraft.advancements.CriterionTriggerInstance;
//import net.minecraft.advancements.critereon.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.PlayerAdvancements;
//import net.minecraft.server.commands.AdvancementCommands;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.level.storage.loot.LootContext;
//
//public class CustomTrigger extends SimpleCriterionTrigger {
//    @Override
//    public ResourceLocation getId() {
//        return new ResourceLocation("overstuffedweightcheck");
//    }
//
//    @Override
//    protected AbstractCriterionTriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite composite, DeserializationContext deserializationContext) {
//        return null;
//    }
//
//    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
//
//        public TriggerInstance(ResourceLocation pCriterion, EntityPredicate.Composite pPlayer) {
//            super(pCriterion, pPlayer);
//        }
//
////        public static CustomTrigger.TriggerInstance weightCheck() {
////            return new CustomTrigger.TriggerInstance(new ResourceLocation("overstuffedweightcheck"),EntityPredicate.Composite pPlayer );
////        }
//
//
//        public JsonObject serializeToJson(SerializationContext pConditions) {
//            JsonObject jsonobject = super.serializeToJson(pConditions);
//           // jsonobject.add("entity", this.entityPredicate.toJson(pConditions));
//           // jsonobject.add("killing_blow", this.killingBlow.serializeToJson());
//            return jsonobject;
//        }
//    }
//
//}
