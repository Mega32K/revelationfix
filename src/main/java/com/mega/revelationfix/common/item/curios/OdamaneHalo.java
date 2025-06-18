package com.mega.revelationfix.common.item.curios;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.google.common.collect.HashMultimap;
import com.mega.revelationfix.common.apollyon.client.effect.CuriosMutableComponent;
import com.mega.revelationfix.common.apollyon.client.effect.LoreHelper;
import com.mega.revelationfix.client.enums.ModChatFormatting;
import com.mega.revelationfix.common.item.FontItemExtensions;
import com.mega.revelationfix.api.item.ICenterDescItem;
import com.mega.revelationfix.api.item.IInvulnerableItem;
import com.mega.revelationfix.api.item.IJEIInvisibleRitualResult;
import com.mega.revelationfix.common.odamane.common.HaloEvents;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author MegaDarkness<br>
 * 隐藏饰品-终末之环<br>
 * 终末玩家法术能力修改:<br>
 * --法术能耗:{@link com.mega.revelationfix.mixin.goety.SEHelperMixin#soulDiscount(LivingEntity, CallbackInfoReturnable)}<br>
 * --吟唱速度:{@link com.mega.revelationfix.util.EventUtil#castDuration(int, ISpell, LivingEntity)}<br>
 * --法术冷却:{@link com.mega.revelationfix.mixin.goety.FocusCooldownMixin#tick(Player, Level, CallbackInfo, Iterator, Map.Entry)}<br>
 * --终末熔岩炸到法术能力(终末之焰){@link com.mega.revelationfix.mixin.gr.LavaballSpellMixin}<br>
 * --终末下界流星法术能力(星辰之矢){@link com.mega.revelationfix.mixin.gr.WitherSkullSpellMixin}<br>
 * --终末火球法术能力(终末之焰){@link com.mega.revelationfix.mixin.gr.FireballSpellMixin}<br><br>
 * 终末之焰部分:<br>
 * 实体,见:{@link com.mega.revelationfix.common.entity.TheEndHellfire}<br>
 * 终末之焰替换狱焰部分,见:
 * {@link com.mega.revelationfix.mixin.goety.HellBoltMixin#onHit(HitResult, CallbackInfo)}<br>
 * {@link com.mega.revelationfix.mixin.goety.HellBlastMixin#onHit(HitResult, CallbackInfo)}<br><br>
 * 终末玩家的箭矢速度*7,见:<br>
 * {@link com.mega.revelationfix.mixin.gr.BowItemMixin#deathShoot(Level, Entity)}<br><br>
 * 终末玩家物品冷却修改,见:<br>
 * {@link OdamanePlayerExpandedContext#odamaneDecreaseCooldowns()}<br>
 * {@link com.mega.revelationfix.common.apollyon.common.CooldownsManager#odamaneDecreaseCooldowns(Player, Item)}<br><br>
 * 饰品特供的玩家数据附加为<br>
 * {@link com.mega.revelationfix.safe.OdamanePlayerExpandedContext}<br>
 * 饰品特供的玩家数据接口方法为<br>
 * {@link PlayerInterface#revelationfix$odamaneHaloExpandedContext()}<br><br>
 * 终末玩家死亡/受伤音效实现,见:<br>
 * {@link com.mega.revelationfix.mixin.gr.PlayerMixin#hurtSound(DamageSource, CallbackInfoReturnable)}<br>
 * {@link com.mega.revelationfix.mixin.gr.PlayerMixin#deathSound(CallbackInfoReturnable)}<br><br>
 * 终末玩家氛围音效实现,见:<br>
 * {@link OdamanePlayerExpandedContext#tryPlayAmbient()}<br><br>
 * 终末玩家黑曜石巨柱无敌,见:<br>
 * {@link com.mega.revelationfix.mixin.gr.ObsidianMonolithMixin#getPlayerOwner(ObsidianMonolith)}<br><br>
 * 免疫负面buff,见:<br>
 * {@link OdamanePlayerExpandedContext#tryCleanDeBuffs()}<br>
 * {@link com.mega.revelationfix.common.apollyon.common.CommonEventHandler#haloNoEffects(MobEffectEvent.Applicable)}<br><br>
 * 关于饰品的减伤,见:<br>
 * {@link HaloEvents#onLivingDamage(LivingDamageEvent)}<br><br>
 * 关于饰品的限伤,见:<br>
 * {@link HaloEvents#onLivingDamage}<br>
 * {@link com.mega.revelationfix.mixin.SynchedEntityDataMixin#odamaneHaloDamageLimit(EntityDataAccessor, Object, boolean, CallbackInfo)}<br>
 * {@link com.mega.revelationfix.mixin.KillCommandMixin#kill(CommandSourceStack, Collection, CallbackInfoReturnable, Iterator, Entity)}<br><br>
 * 终末之环玩家的所有攻击越过低等限伤,见:<br>
 * {@link com.mega.revelationfix.common.odamane.common.BypassHurtMethodDamageEvents}<br><br>
 * 终末之环抵御炼狱结界,见:<br>
 * {@link com.mega.revelationfix.common.apollyon.common.DeathPerformance#perform(Apostle, ApollyonAbilityHelper, int)}<br><br>
 * 关于饰品的其他特殊效果能力,见:<br>
 * {@link OdamanePlayerExpandedContext#tick()}<br>
 * {@link com.mega.revelationfix.common.odamane.common.AbilityEvents}<br><br>
 * 终末之环的时停逻辑:<br>
 * 1.梦幻终焉加载的时候调用梦幻终焉的API。<br>
 * 2.梦幻终焉不加载的时候使用自己实现的时停。<br><br>
 * 调用,见:<br>
 * {@link com.mega.revelationfix.common.odamane.common.AbilityEvents#onOdamanePlayerDeath(LivingDeathEvent)}<br>
 * {@link OdamanePlayerExpandedContext#baseTick(Level)}<br><br>
 */
@SuppressWarnings("JavadocReference")
public class OdamaneHalo extends SimpleDescriptiveCurio implements IInvulnerableItem, IJEIInvisibleRitualResult, ICenterDescItem {
    //c&s
    public static final int ABILITY_DESC_COUNT = 26;
    static final char nextLineChar = '`';
    static final char colorChar = '§';
    static final String COLOR_PURPLE = LoreHelper.codeMode(ChatFormatting.DARK_PURPLE);
    static final String COLOR_APOLLYON = LoreHelper.codeMode(ModChatFormatting.APOLLYON);
    public static int maxTickCount = 64;
    public static boolean shouldTick;
    public static int tickCountO;
    public static int tickCount;
    public static int apollyonTypeTickCount = -15;
    public static boolean apollyonTypeshouldTick;
    private static int apollyonTypeTickCount2 = 0;

    public OdamaneHalo() {
        super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC), "head", () -> {
            HashMultimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            CuriosApi.addSlotModifier(multimap, "head",
                    UUID.fromString("7a5e737b-693d-4f84-bce0-79667c67e7d6"), 1.0D, AttributeModifier.Operation.ADDITION);
            return multimap;
        });
        List<CuriosMutableComponent> tailDesc = new ArrayList<>();
        for (int i = 0; i < ABILITY_DESC_COUNT; i++) {
            tailDesc.add(CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.halo_of_the_end.desc" + i)));
        }
        this.withHead(
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.halo_of_the_end.default_1").withStyle(ChatFormatting.DARK_PURPLE)),
                CuriosMutableComponent.create().appendComponent(Component.translatable("item.goety_revelation.halo_of_the_end.default_2").withStyle(ChatFormatting.DARK_PURPLE))
        ).withTail(
                tailDesc.toArray(new CuriosMutableComponent[]{})
        );
    }

    public static String softDescsBakeBaking(String example, int apSize) {
        //将原字符串转为StringBuilder;
        StringBuilder sourceString = new StringBuilder(example);
        //起始索引 / 时间刻度
        //int start = 13;
        if (apollyonTypeTickCount2 >= example.length()) apollyonTypeTickCount2 = example.length();
        //获取到正确截取起始位置（排除颜色字符块）
        //这里不分配新变量直接赋值给时间的话可以让渲染不卡顿
        apollyonTypeTickCount2 = getStart(example, apollyonTypeTickCount2);
        //初始截取末端
        int end;
        end = getEnd(example, apSize, apollyonTypeTickCount2);
        //复制要替换的索引头索引尾
        int toReplaceSrcStart = Math.min(apollyonTypeTickCount2, end), toReplaceSrcEnd = end;
        StringBuilder toModifyString = new StringBuilder(example.substring(Math.min(apollyonTypeTickCount2, end), end));
        {
            //替换所有颜色字符块为空,替换所有换行字符为换行字符+亚波伦字体颜色字符块
            toModifyString = new StringBuilder(toModifyString.toString().replaceAll("§.", "").replaceAll(String.valueOf(nextLineChar), nextLineChar + COLOR_APOLLYON));
            //在前插入亚波伦颜色字符块
            toModifyString.insert(0, COLOR_APOLLYON);
            //末尾插入原颜色
            toModifyString.insert(toModifyString.length(), COLOR_PURPLE);
        }
        //替换截取部分
        sourceString.replace(toReplaceSrcStart, toReplaceSrcEnd, toModifyString.toString());
        //格式化
        return sourceString.toString();
    }

    public static int getStart(String src, int srcStart) {
        //说明已递归到尽头,返回
        if (srcStart >= src.length()) return src.length();
        //无效的范围
        if (srcStart < 0) return 0;
        if (src.charAt(srcStart) == colorChar)//说明起始位置和下一个索引所在都为错误,所以start+2,向后递归
            return getStart(src, srcStart + 2);
        else if (srcStart > 0 && (src.charAt(srcStart - 1) == colorChar || src.charAt(srcStart - 1) == nextLineChar))//说明起始位置为颜色字符块末尾 或 为换行字符,所以start+1,向后递归
            return getStart(src, srcStart + 1);
        return srcStart;
    }

    public static int getEnd(String src, int sizeLeft, int endToReturn) {
        //说明已递归到尽头,返回
        if (endToReturn >= src.length())
            return src.length();
        //无效的范围
        if (endToReturn < 0) return 0;
        if (src.charAt(endToReturn) == colorChar)//说明终末位置和下一个索引所在都为错误,所以end位置+2,向后递归
            return getEnd(src, sizeLeft, endToReturn + 2);
            //说明终末位置为换行字符 (错误）,所以end位置+1,向后递归
        else if (src.charAt(endToReturn) == nextLineChar)
            return getEnd(src, sizeLeft, endToReturn + 1);
            //说明终末位置为颜色字符块末尾 或 为换行字符 (错误),所以end位置+1,向后递归
        else if (endToReturn > 0 && (src.charAt(endToReturn - 1) == colorChar || src.charAt(endToReturn - 1) == nextLineChar))
            return getEnd(src, sizeLeft, endToReturn + 1);
        //发现正确的可作为筛选后字符串的字符,如果还剩余筛选后字符串席位,剩余大小-1,end位置+1,向后递归
        if (sizeLeft > 0)
            return getEnd(src, sizeLeft - 1, endToReturn + 1);
        return endToReturn;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack p_41453_) {
        return false;
    }

    @Override
    public boolean showHead(List<Component> components, ItemStack stack) {
        showHeader = false;
        if (!Screen.hasAltDown() && !Screen.hasShiftDown()) {
            tickCount = 0;
            apollyonTypeTickCount = -15;
            components.add(Component.literal(""));
            components.add(Component.translatable("tooltip.revelationfix.holdAlt"));
            components.add(Component.translatable("tooltip.revelationfix.holdShiftEffect"));
            return false;
        } else return Screen.hasAltDown();
    }

    @Override
    public List<MutableComponent> getHeadDescriptionLines(ItemStack stack) {
        List<MutableComponent> srcList = super.getHeadDescriptionLines(stack);
        //private static Supplier<>
        if (!apollyonTypeshouldTick)
            return srcList;
        else {
            StringBuilder stringBuilder = new StringBuilder();
            for (MutableComponent component : head.stream().map(cmc -> cmc.build(stack)).toList())
                stringBuilder.append(stringBuilder.isEmpty() ? COLOR_PURPLE : nextLineChar + COLOR_PURPLE).append(component.getString());
            apollyonTypeTickCount2 = apollyonTypeTickCount;
            String baked;
            try {
                baked = softDescsBakeBaking(stringBuilder.toString(), 7);
                int length = baked.length();
                float partialTickCount = Mth.lerp(Minecraft.getInstance().getFrameTime(), tickCountO, tickCount) / 2F;

                List<String> spitLines = new ArrayList<>();
                Collections.addAll(spitLines, (baked.substring(0, (int) Math.min((partialTickCount / (float) maxTickCount * 4F * length), length))).split(String.valueOf(nextLineChar)));
                return spitLines.stream().map(Component::literal).toList();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return List.of();
            }
        }
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        if (Screen.hasShiftDown()) {
            return super.getAttributesTooltip(tooltips, stack);
        } else {
            return List.of();
        }
    }

    @Override
    public boolean canBeHurtBy(DamageSource p_41387_) {
        if (!p_41387_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.canBeHurtBy(p_41387_);
    }

    @Override
    public boolean enableSimpleDesc() {
        return false;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return super.canEquip(slotContext, stack) && !CuriosFinder.hasCurio(slotContext.entity(), this) && slotContext.identifier().equals("head");
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new FontItemExtensions());
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level p_41405_, @NotNull Entity entity, int p_41407_, boolean p_41408_) {
        if (p_41405_.isClientSide) {
            tickCountO = tickCount;
            if (shouldTick)
                tickCount++;
            if (apollyonTypeshouldTick)
                apollyonTypeTickCount++;
        } else if (entity.tickCount % 5 == 0) {
            if (!stack.getAllEnchantments().containsKey(Enchantments.BINDING_CURSE)) {
                Map<Enchantment, Integer> map = stack.getAllEnchantments();
                try {
                    map.put(Enchantments.BINDING_CURSE, 1);
                    EnchantmentHelper.setEnchantments(map, stack);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        super.inventoryTick(stack, p_41405_, entity, p_41407_, p_41408_);
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> list = EnchantmentHelper.getEnchantments(book);
        if (list.size() == 1 && list.containsKey(Enchantments.BINDING_CURSE))
            return true;
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isInvisibleInJEI(ItemStack stack) {
        return true;
    }
}
