package com.mega.revelationfix.common.init;

import com.Polarice3.Goety.api.magic.SpellType;
import com.mega.revelationfix.common.entity.cultists.HereticServant;
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

@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER)
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, ModMain.MODID);
    public static final RegistryObject<Attribute> DAMAGE_RESISTANCE = ATTRIBUTES.register("resistance", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".resistance", 1.0D, 0.0D, 2.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ARMOR_PENETRATION = ATTRIBUTES.register("armor_penetration", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".armor_penetration", 1.0D, 0.0D, 2.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ENCHANTMENT_PIERCING = ATTRIBUTES.register("enchantment_piercing", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".enchantment_piercing", 1.0D, 0.0D, 2.0D).setSyncable(true));
    public static final RegistryObject<Attribute> SPELL_POWER_MULTIPLIER = ATTRIBUTES.register("spell_power_multiplier", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_power_multiplier", 1, 0, 32767D).setSyncable(true));
    public static final RegistryObject<Attribute> SPELL_COOLDOWN = ATTRIBUTES.register("spell_cooldown", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_cooldown", 1, 0, 32767D).setSyncable(true));
    public static final RegistryObject<Attribute> SPELL_POWER = ATTRIBUTES.register("spell_power", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".spell_power", 0, 0, 32767D).setSyncable(true));
    public static final RegistryObject<Attribute> CAST_DURATION = ATTRIBUTES.register("cast_duration", () -> new RangedAttribute("attribute.name." + ModMain.MODID + ".cast_duration", 1, 0, 32767D).setSyncable(true));
    public static Object2ObjectOpenHashMap<SpellType, RegistryObject<Attribute>> spellAttributes = new Object2ObjectOpenHashMap<>();

    static {
        for (SpellType spellType : SpellType.values())
            registerSpellPowerAttribute(spellType, ModMain.MODID);
    }

    public static void addAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entityType -> e.add(entityType, DAMAGE_RESISTANCE.get()));
        for (RegistryObject<Attribute> ro : spellAttributes.values()) {
            Attribute attribute = ro.get();
            e.add(EntityType.PLAYER, attribute);
            e.add(ModEntities.FAKE_SPELLER.get(), attribute);
        }
        e.add(EntityType.PLAYER, SPELL_POWER_MULTIPLIER.get());
        e.add(EntityType.PLAYER, SPELL_POWER.get());
        e.add(EntityType.PLAYER, SPELL_COOLDOWN.get());
        e.add(EntityType.PLAYER, CAST_DURATION.get());
        e.add(EntityType.PLAYER, ARMOR_PENETRATION.get());
        e.add(EntityType.PLAYER, ENCHANTMENT_PIERCING.get());

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
