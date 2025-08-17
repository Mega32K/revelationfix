package com.mega.revelationfix.client.screen.post.custom;

import com.mega.endinglib.api.client.Easing;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.MyGuiGraphics;
import com.mega.revelationfix.client.screen.CustomScreenEffect;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.odamane.common.TheEndPuzzleItems;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PuzzleEffect implements CustomScreenEffect {
    static Minecraft mc = Minecraft.getInstance();
    static int tickCount = 0;
    static int tickCountO = 0;
    static float rotationStar;
    static GuiGraphics guiGraphics;
    static PuzzleEffect INSTANCE;
    float saturation = 1.0F;

    public PuzzleEffect() {
        PuzzleEffect.INSTANCE = this;
    }

    public static float getRenderRotation() {
        float time = (float) Blaze3D.getTime() * 40F;
        rotationStar = Easing.OUT_ELASTIC.interpolate((time % 90.0F) / 90f, 0F, 1F) * 90F + (int) (time / 90.0F) * 90.0F + time / 2.5F;

        return rotationStar;
    }

    @SubscribeEvent
    public static void clientProgramTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = mc.player;
        if (player == null || event.phase == TickEvent.Phase.END)
            return;
        tickCountO = tickCount;
        if (player.isUsingItem() && player.getUseItem().is(GRItems.PUZZLE_ITEM.get())) {
            if (tickCount < 95)
                tickCount++;
        } else if (tickCount > 0) tickCount--;
    }

    @SubscribeEvent
    public static void onHotBarOverlay(RenderGuiOverlayEvent.Pre event) {
        if (mc.player == null || mc.player.getUseItem().isEmpty()) return;
        if (PuzzleEffect.INSTANCE.canUse()) {
            ItemStack itemStack = mc.player.getUseItem();
            if (!itemStack.is(GRItems.PUZZLE_ITEM.get())) return;
            else event.setCanceled(true);
        }
        if (event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id()) && PuzzleEffect.INSTANCE.canUse()) {
            ItemStack itemStack = mc.player.getUseItem();
            if (!itemStack.is(GRItems.PUZZLE_ITEM.get())) return;
            Item item = Items.AIR;
            if (itemStack.hasTag()) {
                if (itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES))
                    item = TheEndRitualItemContext.PUZZLE1;
                else if (itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES2))
                    item = TheEndRitualItemContext.PUZZLE2;
                else if (itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES3))
                    item = TheEndRitualItemContext.PUZZLE3;
                else if (itemStack.getTag().getBoolean(GRItems.NBT_PUZZLES4))
                    item = TheEndRitualItemContext.PUZZLE4;

            }
            RandomSource randomSource = RandomSource.create();
            float sec = Mth.lerp(event.getPartialTick(), tickCountO, tickCount) / 9;
            if (!TheEndPuzzleItems.puzzleItems.containsKey(item)) return;
            String srcPuzzle = Component.translatable("item.goety_revelation.puzzle." + TheEndPuzzleItems.puzzleItems.get(item).index).getString();
            //10 -> 0
            int toReplaceCount = 10 - (int) sec;
            for (int i = 0; i < toReplaceCount; i++) {
                int index = randomSource.nextInt(0, srcPuzzle.length());
                char indexOf = srcPuzzle.charAt(index);
                if (indexOf != '§' && indexOf != 'k') {
                    StringBuilder stringBuilder = new StringBuilder(srcPuzzle);
                    stringBuilder.insert(index + 1, "§k" + (char) (short) (randomSource.nextInt('A', 'Z')) + "§o§8");
                    srcPuzzle = stringBuilder.toString();
                }
            }
            Component fill = Component.literal(srcPuzzle).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY);
            ((ForgeGui) (mc.gui)).setupOverlayRenderState(true, false);
            MyGuiGraphics guiGraphics = MyGuiGraphics.create();
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            PoseStack stack = guiGraphics.pose;
            stack.pushPose();
            float p = Math.min(sec / 10F, 1.0F);
            float p2 = Math.min(sec / 10F, 1.0F);
            guiGraphics.enableScissor(0, 0, (int) (screenWidth * p), screenHeight);
            guiGraphics.drawCenteredString8x8(mc.font, Component.translatable("info.goety_revelation.puzzle.title").getString(), screenWidth / 2F, screenHeight / 2F, 20, 11141120);
            guiGraphics.disableScissor();
            guiGraphics.enableScissor(0, 0, (int) (screenWidth * p2), screenHeight);
            guiGraphics.drawCenteredString(mc.font, Component.translatable("info.goety_revelation.puzzle.desc", fill), screenWidth / 2F, screenHeight / 2F + mc.font.lineHeight + 1, 0);
            guiGraphics.disableScissor();
            stack.popPose();
        }
    }

    @Override
    public String getName() {
        return "overwhelming";
    }

    @Override
    public ResourceLocation getShaderLocation() {
        return new ResourceLocation(Revelationfix.MODID, "shaders/post/overwhelming.json");
    }

    @Override
    public void onRenderTick(float partialTicks) {
        float ticks = Mth.lerp(partialTicks, tickCountO, tickCount) - 1.0F;
        PostEffectHandler.updateUniform_post(this, "Saturation", Math.max(0.05F, saturation - ticks * 0.01F));
    }

    @Override
    public boolean canUse() {
        if (tickCount <= 0 && tickCountO <= 0)
            return false;
        return tickCount > 1;
    }
}
