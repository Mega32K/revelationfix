package com.mega.revelationfix.common.data.brew;

import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.effects.brew.PotionBrewEffect;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import com.Polarice3.Goety.common.effects.brew.modifiers.CapacityModifier;
import com.google.gson.JsonObject;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.api.event.register.CustomBrewRegisterEvent;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.kjs.KjsSafeClass;
import com.mega.revelationfix.common.compat.kjs.events.KjsEvents;
import com.mega.revelationfix.common.compat.kjs.events.WitchBrewRegisterKjsEvent;
import com.mega.revelationfix.mixin.goety.BrewCauldronBlockEntityMixin;
import com.mega.revelationfix.safe.mixinpart.goety.BrewEffectsInvoker;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import com.mojang.datafixers.types.Func;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class BrewData {
    private static final Map<String, BrewData> registered = new HashMap<>();
    public String pluginName = "";
    public List<Capacity> capacities = new ArrayList<>();
    public List<Catalysts<?>> catalysts = new ArrayList<>();
    public List<Augmentation> augmentations = new ArrayList<>();
    public static void clearData() {
        registered.clear();
    }
    public static void reRegister() {
        for (var entry : registered.entrySet())
            register(entry.getKey(), entry.getValue());
        MinecraftForge.EVENT_BUS.post(new CustomBrewRegisterEvent(CustomBrewRegisterEvent.Phase.CHECK));
        if (SafeClass.isKJSLoaded()) {
            KjsSafeClass.postBrewEvent_0();
        }
    }
    public static void register(String id, BrewData data) {
        registered.put(id, data);
        {
            Logger logger = RevelationFixMixinPlugin.LOGGER;
            BrewEffectsInvoker invoker = (BrewEffectsInvoker) BrewEffects.INSTANCE;
            logger.debug("Goety brew registering Plugin: {}", id);
            for (BrewData.Capacity capacity : data.capacities) {
                logger.debug(" -New Capacity:{}->level{}", capacity.getItem(), capacity.level);
                invoker.forceModifierRegister_(new CapacityModifier(capacity.level), capacity.getItem());

            }
            for (BrewData.Augmentation augmentation : data.augmentations) {
                logger.debug(" -New Augmentation: BrewModifier(id->{}, level->{}), Item:{}", augmentation.getBrewModifier().id, augmentation.getBrewModifier().level, augmentation.getItem());
                invoker.forceModifierRegister_(augmentation.getBrewModifier(), augmentation.getItem());
            }
            for (BrewData.Catalysts<?> catalysts : data.catalysts) {
                if (catalysts instanceof BrewData.NormalItemCatalysts normalItemCatalysts) {
                    BrewEffect brewEffect = catalysts.getBrewEffect();
                    logger.debug(" -New Item Catalysts: BrewEffect(id->{}, capacityExtra->{}, soulCost->{}, duration->{}ticks), Item:{}", brewEffect.getEffectID(), brewEffect.getCapacityExtra(), brewEffect.getSoulCost(), brewEffect.getDuration(), normalItemCatalysts.getObjOfType());
                    invoker.forceRegister_(brewEffect, normalItemCatalysts.getObjOfType());
                } else if (catalysts instanceof BrewData.EntityBrewCatalysts entityBrewCatalysts) {
                    BrewEffect brewEffect = catalysts.getBrewEffect();
                    logger.debug(" -New Entity Catalysts: BrewEffect(id->{}, capacityExtra->{}, soulCost->{}, duration->{}ticks), Entity:{}", brewEffect.getEffectID(), brewEffect.getCapacityExtra(), brewEffect.getSoulCost(), brewEffect.getDuration(), entityBrewCatalysts.getObjOfType().getDescription().getString());

                    invoker.forceRegister_(brewEffect, entityBrewCatalysts.getObjOfType());
                }
            }
        }
    }
    public static BrewData getValue(String id) {
        return registered.get(id);
    }
    public static Collection<BrewData> allData() {
        return registered.values();
    }
    public static class Capacity {
        private Item item;
        public String itemName;
        public int level;
        public @Nullable Item getItem() {
            if (item == null)
                item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            return item;
        }
        public Capacity(String itemName, int level) {
            this.itemName = itemName;
            this.level = level;
        }

        public static Capacity readFromJO(JsonObject jsonObject) {
            return new Capacity(jsonObject.get("item").getAsString(), jsonObject.get("level").getAsInt());
        }
    }
    public static abstract class Catalysts<T> {
        public static final String NORMAL_ITEM_TYPE = "normal_item";
        public static final String ENTITY_BREW_TYPE = "entity_brew";
        public static final Map<String, Function<JsonObject, Catalysts<?>>> readers = new HashMap<>();
        public static void initReaders() {
            readers.put(NORMAL_ITEM_TYPE, NormalItemCatalysts::readFromJO);
            readers.put(ENTITY_BREW_TYPE, EntityBrewCatalysts::readFromJO);
        }
        public String effectID;
        public int capacityExtra;
        public int soulCost;
        public int duration;
        public MobEffect mobEffect;
        public Catalysts(String effectID, int capacityExtra, int soulCost, int duration) {
            this.effectID = effectID;
            this.capacityExtra = capacityExtra;
            this.soulCost = soulCost;
            this.duration = duration;
            this.mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectID));

        }
        public abstract BrewEffect getBrewEffect();
        public abstract T getObjOfType();
    }
    public static class NormalItemCatalysts extends Catalysts<Item> {
        public String itemName;
        private BrewEffect brewEffect;
        private Item item;
        public NormalItemCatalysts(String itemName, String effectID, int capacityExtra, int soulCost, int duration) {
            super(effectID, capacityExtra, soulCost, duration);
            this.itemName = itemName;
        }

        @Override
        public BrewEffect getBrewEffect() {
            if (brewEffect == null && this.mobEffect != null)
                brewEffect = new PotionBrewEffect(this.mobEffect, this.soulCost, this.capacityExtra, this.duration);
            return brewEffect;
        }

        @Override
        public Item getObjOfType() {
            if (item == null)
                item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            return item;
        }
        public static NormalItemCatalysts readFromJO(JsonObject jsonObject) {
            return new NormalItemCatalysts(
                    jsonObject.get("item").getAsString(),
                    jsonObject.get("effect").getAsString(),
                    GsonHelper.getAsInt(jsonObject, "capacityExtra", 0),
                    jsonObject.get("soulCost").getAsInt(),
                    GsonHelper.getAsInt(jsonObject, "duration", 1)
            );
        }
    }
    public static class EntityBrewCatalysts extends Catalysts<EntityType<?>> {
        public String entityID;
        private BrewEffect brewEffect;
        public EntityType<?> entityType;

        public EntityBrewCatalysts(String entityID, String effectID, int capacityExtra, int soulCost, int duration) {
            super(effectID, capacityExtra, soulCost, duration);
        }

        @Override
        public BrewEffect getBrewEffect() {
            if (brewEffect == null && this.mobEffect != null)
                brewEffect = new PotionBrewEffect(this.mobEffect, this.soulCost, this.capacityExtra, this.duration);
            return brewEffect;
        }

        @Override
        public EntityType<?> getObjOfType() {
            if (this.entityType == null)
                this.entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityID));
            return entityType;
        }
        public static EntityBrewCatalysts readFromJO(JsonObject jsonObject) {
            return new EntityBrewCatalysts(
                    jsonObject.get("entity").getAsString(),
                    jsonObject.get("effect").getAsString(),
                    GsonHelper.getAsInt(jsonObject, "capacityExtra", 0),
                    jsonObject.get("soulCost").getAsInt(),
                    GsonHelper.getAsInt(jsonObject, "duration", 1)
            );
        }
    }
    public static class Augmentation {
        private Item item;
        public String itemName;
        private final BrewModifier brewModifier;
        public Augmentation(String itemName, String id, int level) {
            this.itemName = itemName;
            this.brewModifier = new BrewModifier(id, level);
        }

        public BrewModifier getBrewModifier() {
            return brewModifier;
        }

        public @Nullable Item getItem() {
            if (item == null)
                item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            return item;
        }
        public static Augmentation readFromJO(JsonObject jsonObject) {
            return new Augmentation(jsonObject.get("item").getAsString(), jsonObject.get("modifier").getAsString(), jsonObject.get("level").getAsInt());
        }
        public static boolean isInvalidID(String id) {
            //持续时间
            return !BrewModifier.DURATION.equals(id) &&
                    //buff等级+1
                    !BrewModifier.AMPLIFIER.equals(id) &&
                    //滞留型
                    !BrewModifier.LINGER.equals(id) &&
                    //饮用速度
                    !BrewModifier.QUAFF.equals(id) &&
                    //投掷力度
                    !BrewModifier.VELOCITY.equals(id) &&
                    //水栖
                    !BrewModifier.AQUATIC.equals(id) &&
                    //防火
                    !BrewModifier.FIRE_PROOF.equals(id);
                    //隐藏粒子效果
                    //!BrewModifier.HIDDEN.equals(id) &&
                    //喷溅型
                    //!BrewModifier.SPLASH.equals(id) &&
                    //滞留型
                    //!BrewModifier.LINGERING.equals(id) &&
                    //气态
                    //!BrewModifier.GAS.equals(id);
        }
    }
}
