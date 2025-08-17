package com.mega.revelationfix.common.data.brew;

import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.effects.brew.PotionBrewEffect;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import com.Polarice3.Goety.common.effects.brew.modifiers.CapacityModifier;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mega.revelationfix.api.event.register.CustomBrewRegisterEvent;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.kjs.KjsSafeClass;
import com.mega.revelationfix.safe.mixinpart.goety.BrewEffectsInvoker;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
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
import java.util.function.Supplier;

public class BrewData {
    public static final int[] CAPACITY_ADDITION_EXCEPT = new int[]{0, 2, 2, 2, 2, 4, 8, 8};
    public static final Int2ObjectOpenHashMap<ObjectArrayList<Item>> LEVEL_TO_CAPACITY_MAP = Util.make(() -> createAndInitI2OMap(8, ObjectArrayList::new));
    public static final List<String> TEMP_ALL_ITEM_CATALYSTS = new ObjectArrayList<>();
    public static final List<String> TEMP_ALL_ENTITY_CATALYSTS = new ObjectArrayList<>();
    public static final Object2ObjectArrayMap<String, Int2ObjectOpenHashMap<ObjectArrayList<Item>>> TYPE_TO_LEVEL_AUGMENTATIONS = Util.make(() -> {
        Object2ObjectArrayMap<String, Int2ObjectOpenHashMap<ObjectArrayList<Item>>> map = new Object2ObjectArrayMap<>();
        map.put(BrewModifier.DURATION, createAndInitI2OMap(5, ObjectArrayList::new));
        map.put(BrewModifier.AMPLIFIER, createAndInitI2OMap(5, ObjectArrayList::new));
        map.put(BrewModifier.AOE, createAndInitI2OMap(5, ObjectArrayList::new));
        map.put(BrewModifier.LINGER, createAndInitI2OMap(5, ObjectArrayList::new));
        map.put(BrewModifier.QUAFF, createAndInitI2OMap(5, ObjectArrayList::new));
        map.put(BrewModifier.VELOCITY, createAndInitI2OMap(5, ObjectArrayList::new));
        map.put(BrewModifier.AQUATIC, createAndInitI2OMap(1, ObjectArrayList::new));
        map.put(BrewModifier.FIRE_PROOF, createAndInitI2OMap(1, ObjectArrayList::new));
        return map;
    });
    private static final Map<String, BrewData> REGISTRIES = new Object2ObjectOpenHashMap<>();
    public String pluginName = "";
    public List<Capacity> capacities = new ObjectArrayList<>();
    public List<Catalysts<?>> catalysts = new ObjectArrayList<>();
    public List<Augmentation> augmentations = new ObjectArrayList<>();

    public static void clearData() {
        REGISTRIES.clear();
    }

    public static void reRegister() {
        for (var entry : REGISTRIES.entrySet())
            register(entry.getKey(), entry.getValue());
        MinecraftForge.EVENT_BUS.post(new CustomBrewRegisterEvent(CustomBrewRegisterEvent.Phase.CHECK));
        if (SafeClass.isKJSLoaded()) {
            KjsSafeClass.postBrewEvent_0();
        }
    }

    public static void register(String id, BrewData data) {
        REGISTRIES.put(id, data);
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
        return REGISTRIES.get(id);
    }

    public static Collection<BrewData> allData() {
        return REGISTRIES.values();
    }

    public static <T> Int2ObjectOpenHashMap<T> createAndInitI2OMap(int initDomainSize, Supplier<T> supplier) {
        Int2ObjectOpenHashMap<T> map = new Int2ObjectOpenHashMap<>();
        for (int i = 0; i < initDomainSize; i++) {
            map.put(i, supplier.get());
        }
        return map;
    }

    public static class Capacity {
        public String itemName;
        public int level;
        private Item item;

        public Capacity(String itemName, int level) {
            this.itemName = itemName;
            this.level = level;
        }

        public static Capacity readFromJO(JsonObject jsonObject) {
            return new Capacity(jsonObject.get("item").getAsString(), jsonObject.get("level").getAsInt());
        }

        public static void injectPatchouliJson(JsonElement element) {
            //找到页面j array
            JsonArray pages = element.getAsJsonObject().getAsJsonArray("pages");
            //找到描述所在jsonObject
            JsonObject part = pages.get(2).getAsJsonObject();
            if (part != null) {
                if (part.get("text") instanceof JsonPrimitive text) {
                    String srcText = text.getAsString();
                    //从标题开始截取
                    StringBuilder builder = new StringBuilder(srcText.split("\\$\\(\\)\\$\\(br\\)")[0]);
                    //换行
                    builder.append("$()$(br)");
                    //遍历容量剂添加
                    //以及添加的行目数量
                    int counts = 0;
                    List<String> pageText = new ObjectArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        List<Item> list = LEVEL_TO_CAPACITY_MAP.get(i);
                        for (int j = 0; j < list.size(); j++) {
                            builder.append("$(li)$(item)(lv.%d)%s$() - +%d ".formatted(i, I18n.get(list.get(j).getDescriptionId()), CAPACITY_ADDITION_EXCEPT[i]));
                            counts++;
                            if (counts > 11) {
                                counts = 0;
                                pageText.add(builder.toString());
                                builder = new StringBuilder();
                            }
                        }
                    }
                    if (pageText.isEmpty())
                        pageText.add(builder.toString());
                    if (!pageText.isEmpty()) {
                        part.addProperty("text", pageText.get(0));
                        if (pageText.size() > 1) {
                            for (int i = 1; i < pageText.size(); i++)
                                createTextPage(pages, pageText.get(i));
                        }
                    }
                }
            }
            //释放内存
            //大概没问题
            LEVEL_TO_CAPACITY_MAP.values().forEach(List::clear);
        }

        static void createTextPage(JsonArray parent, String text) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", "patchouli:text");
            jsonObject.addProperty("text", text);
            parent.add(jsonObject);
        }

        public @Nullable Item getItem() {
            if (item == null)
                item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            return item;
        }
    }

    public static abstract class Catalysts<T> {
        public static final String NORMAL_ITEM_TYPE = "normal_item";
        public static final String ENTITY_BREW_TYPE = "entity_brew";
        public static final Map<String, Function<JsonObject, Catalysts<?>>> readers = new HashMap<>();
        private static final String JSON_CATALYST = "goety:catalyst";
        private static final String JSON_SACRIFICE = "goety:sacrifice";
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

        public static void initReaders() {
            readers.put(NORMAL_ITEM_TYPE, NormalItemCatalysts::readFromJO);
            readers.put(ENTITY_BREW_TYPE, EntityBrewCatalysts::readFromJO);
        }

        public static void injectPatchouliJson(JsonElement element) {
            //找到描述所在的J Array
            JsonArray pages = element.getAsJsonObject().getAsJsonArray("pages");
            //若有存在的覆盖描述，进行覆盖
            for (JsonElement jsonElement : pages) {
                if (jsonElement instanceof JsonObject page) {
                    String jsonEffect = GsonHelper.getAsString(page, "text", "");
                    if (!jsonEffect.isEmpty()) {
                        Optional<String> optionalText = getText(jsonEffect);
                        optionalText.ifPresent(s -> page.addProperty("text", s));
                    }
                }
            }
            //遍历创建催化剂
            for (String effect : TEMP_ALL_ITEM_CATALYSTS) {
                createCatalystPage(pages, true, effect);
            }
            for (String effect : TEMP_ALL_ENTITY_CATALYSTS) {
                createCatalystPage(pages, false, effect);
            }
            //释放内存
            //大概没问题
            TEMP_ALL_ITEM_CATALYSTS.clear();
            TEMP_ALL_ENTITY_CATALYSTS.clear();
        }

        static void createCatalystPage(JsonArray pages, boolean isItem, String effect) {
            JsonObject page = new JsonObject();
            page.addProperty("type", isItem ? JSON_CATALYST : JSON_SACRIFICE);
            page.addProperty("recipe", effect);
            page.addProperty("text", getText(effect).orElse(""));
            pages.add(page);
        }

        static Optional<String> getText(String effect) {
            if (I18n.exists("patchouli.witch_brew." + effect)) {
                return Optional.of(I18n.get("patchouli.witch_brew." + effect));
            } else return Optional.empty();
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

        public static NormalItemCatalysts readFromJO(JsonObject jsonObject) {
            return new NormalItemCatalysts(
                    jsonObject.get("item").getAsString(),
                    jsonObject.get("effect").getAsString(),
                    GsonHelper.getAsInt(jsonObject, "capacityExtra", 0),
                    jsonObject.get("soulCost").getAsInt(),
                    GsonHelper.getAsInt(jsonObject, "duration", 1)
            );
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
    }

    public static class EntityBrewCatalysts extends Catalysts<EntityType<?>> {
        public String entityID;
        public EntityType<?> entityType;
        private BrewEffect brewEffect;

        public EntityBrewCatalysts(String entityID, String effectID, int capacityExtra, int soulCost, int duration) {
            super(effectID, capacityExtra, soulCost, duration);
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
    }

    public static class Augmentation {
        private final BrewModifier brewModifier;
        public String itemName;
        private Item item;

        public Augmentation(String itemName, String id, int level) {
            this.itemName = itemName;
            this.brewModifier = new BrewModifier(id, level);
        }

        public Augmentation(String itemName, BrewModifier modifier) {
            this.itemName = itemName;
            this.brewModifier = new BrewModifier(modifier.id, modifier.level);
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

        public static void injectPatchouliJson(JsonElement element) {
            //TODO 添加内容
            TYPE_TO_LEVEL_AUGMENTATIONS.values().forEach(map -> map.values().forEach(List::clear));
        }

        public BrewModifier getBrewModifier() {
            return brewModifier;
        }

        public @Nullable Item getItem() {
            if (item == null)
                item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            return item;
        }
    }
}
