package com.mega.revelationfix.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class DarkIngotHammerImproveTrigger extends SimpleCriterionTrigger<DarkIngotHammerImproveTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("revelationfix:dark_ingot_hammer_improve");

    public DarkIngotHammerImproveTrigger() {
    }

    public ResourceLocation getId() {
        return ID;
    }

    public DarkIngotHammerImproveTrigger.TriggerInstance createInstance(JsonObject p_286555_, ContextAwarePredicate p_286704_, DeserializationContext p_286270_) {
        return new DarkIngotHammerImproveTrigger.TriggerInstance(p_286704_);
    }

    public void trigger(ServerPlayer serverPlayer) {
        this.trigger(serverPlayer, (p_43166_) -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(ContextAwarePredicate p_286286_) {
            super(ID, p_286286_);
        }

        public JsonObject serializeToJson(SerializationContext p_43196_) {
            JsonObject $$1 = super.serializeToJson(p_43196_);
            return $$1;
        }
    }
}
