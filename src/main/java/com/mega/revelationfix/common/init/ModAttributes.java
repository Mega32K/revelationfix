package com.mega.revelationfix.common.init;

import com.Polarice3.Goety.api.magic.SpellType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

import java.util.Locale;

/**
 * 不标强度的都是加值给实际<br>
 * xx强度,xx持久力,xx乘数的都是乘值给实际
 */
@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER)
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, ModMain.MODID);
    /**
     * 伤害抗性
     */
    public static final RegistryObject<Attribute> DAMAGE_RESISTANCE = ATTRIBUTES.register("resistance", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".resistance", 1.0D, 0.0D, 2.0D).setSyncable(true));
    /**
     * 护甲穿透
     */
    public static final RegistryObject<Attribute> ARMOR_PENETRATION = ATTRIBUTES.register("armor_penetration", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".armor_penetration", 1.0D, 0.0D, 2.0D).setSyncable(true));
    /**
     * 附魔穿透
     */
    public static final RegistryObject<Attribute> ENCHANTMENT_PIERCING = ATTRIBUTES.register("enchantment_piercing", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".enchantment_piercing", 1.0D, 0.0D, 2.0D).setSyncable(true));
    /**
     * 巫法强度乘数
     */
    public static final RegistryObject<Attribute> SPELL_POWER_MULTIPLIER = ATTRIBUTES.register("spell_power_multiplier", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_power_multiplier", 1, 0, 32767D).setSyncable(true));
    /**
     * 冷却缩减
     */
    public static final RegistryObject<Attribute> SPELL_COOLDOWN = ATTRIBUTES.register("spell_cooldown", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_cooldown", 1, -5.0, 32767D).setSyncable(true));
    /**
     * 巫法强度
     */
    public static final RegistryObject<Attribute> SPELL_POWER = ATTRIBUTES.register("spell_power", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_power", 0, -32767D, 32767D).setSyncable(true));
    /**
     * 吟唱缩减
     */
    public static final RegistryObject<Attribute> CAST_DURATION = ATTRIBUTES.register("cast_duration", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".cast_duration", 1, -5.0, 32767D).setSyncable(true));
    /**
     * 灵魂亲和
     */
    public static final RegistryObject<Attribute> SOUL_INCREASE_EFFICIENCY = ATTRIBUTES.register("soul_increase_efficiency", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".soul_increase_efficiency", 1, 0, 32767D).setSyncable(true));
    /**
     * 灵魂洄流
     */
    public static final RegistryObject<Attribute> SOUL_DECREASE_EFFICIENCY = ATTRIBUTES.register("soul_decrease_reduction", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".soul_decrease_reduction", 1, -32767D, 2D).setSyncable(true));
    /**
     * 巫法范围
     */
    public static final RegistryObject<Attribute> SPELL_RANGE = ATTRIBUTES.register("spell_range", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_range", 0, -1000000, 1000000).setSyncable(true));
    /**
     * 巫法半径
     */
    public static final RegistryObject<Attribute> SPELL_RADIUS = ATTRIBUTES.register("spell_radius", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_radius", 0F, -1000000, 1000000).setSyncable(true));
    /**
     * 巫法燃烧值
     */
    public static final RegistryObject<Attribute> SPELL_BURNING = ATTRIBUTES.register("spell_burning_value", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_burning_value", 0, -1000000, 1000000).setSyncable(true));
    /**
     * 巫法持久力
     */
    public static final RegistryObject<Attribute> SPELL_DURATION = ATTRIBUTES.register("spell_duration", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_duration", 1D, -1000000, 32767D).setSyncable(true));
    /**
     * 巫法弹射物速度
     */
    public static final RegistryObject<Attribute> SPELL_VELOCITY = ATTRIBUTES.register("spell_velocity", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_velocity", 1D, -1000000, 32767D).setSyncable(true));
    /**
     * 灵魂豪夺
     */
    public static final RegistryObject<Attribute> SOUL_STEALING = ATTRIBUTES.register("soul_stealing", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".soul_stealing", 1D, 0D, 1000000));
    public static Object2ObjectOpenHashMap<SpellType, RegistryObject<Attribute>> spellAttributes = new Object2ObjectOpenHashMap<>();

    static {
        for (SpellType spellType : SpellType.values())
            registerSpellPowerAttribute(spellType, ModMain.MODID);
    }

    public static void addAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entityType -> {
            e.add(entityType, DAMAGE_RESISTANCE.get());
            e.add(entityType, ARMOR_PENETRATION.get());
            e.add(entityType, ENCHANTMENT_PIERCING.get());
        });
        for (RegistryObject<Attribute> ro : spellAttributes.values()) {
            Attribute attribute = ro.get();
            e.add(EntityType.PLAYER, attribute);
            e.add(ModEntities.FAKE_SPELLER.get(), attribute);
        }
        e.add(EntityType.PLAYER, SPELL_POWER_MULTIPLIER.get());
        e.add(EntityType.PLAYER, SPELL_POWER.get());
        e.add(EntityType.PLAYER, SPELL_COOLDOWN.get());
        e.add(EntityType.PLAYER, CAST_DURATION.get());
        e.add(EntityType.PLAYER, SOUL_INCREASE_EFFICIENCY.get());
        e.add(EntityType.PLAYER, SOUL_DECREASE_EFFICIENCY.get());
        e.add(EntityType.PLAYER, SPELL_RANGE.get());
        e.add(EntityType.PLAYER, SPELL_RADIUS.get());
        e.add(EntityType.PLAYER, SPELL_BURNING.get());
        e.add(EntityType.PLAYER, SPELL_DURATION.get());
        e.add(EntityType.PLAYER, SPELL_VELOCITY.get());
        e.add(EntityType.PLAYER, SOUL_STEALING.get());

        e.add(ModEntities.FAKE_SPELLER.get(), SPELL_POWER_MULTIPLIER.get());
        e.add(ModEntities.FAKE_SPELLER.get(), SPELL_POWER.get());
        e.add(ModEntities.FAKE_SPELLER.get(), SPELL_COOLDOWN.get());
        e.add(ModEntities.FAKE_SPELLER.get(), CAST_DURATION.get());
        e.add(ModEntities.FAKE_SPELLER.get(), ARMOR_PENETRATION.get());
        e.add(ModEntities.FAKE_SPELLER.get(), ENCHANTMENT_PIERCING.get());
    }

    public static RegistryObject<Attribute> registerSpellPowerAttribute(SpellType spellType, String langModID) {
        RegistryObject<Attribute> ro = ATTRIBUTES.register(spellType.name().toLowerCase(Locale.ROOT) + "_power", () -> new RangedAttribute("attribute.name." + langModID + "." + spellType.name().toLowerCase(Locale.ROOT) + "_power", 0, 0, 32767D).setSyncable(true));
        spellAttributes.put(spellType, ro);
        return ro;
    }

    public static Attribute spellAttribute(SpellType spellType) {
        return spellAttributes.get(spellType).get();
    }
}
