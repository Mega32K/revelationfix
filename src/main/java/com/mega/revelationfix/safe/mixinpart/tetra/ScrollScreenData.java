package com.mega.revelationfix.safe.mixinpart.tetra;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.compat.tetra.client.*;
import com.mega.revelationfix.mixin.tetra.ScrollScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.scroll.gui.ScrollPageButtonGui;
import se.mickelus.tetra.blocks.scroll.gui.ScrollScreen;

import java.util.ArrayList;
import java.util.List;

public class ScrollScreenData {
    public static final ResourceLocation AEGLOS_TEX = new ResourceLocation(Revelationfix.MODID, "textures/ui/aeglos.png");
    public static final ResourceLocation ARROW_TEX = new ResourceLocation(Revelationfix.MODID, "textures/ui/arrow.png");
    public static final ResourceLocation ARROW2_TEX = new ResourceLocation(Revelationfix.MODID, "textures/ui/arrow2.png");
    public static final ResourceLocation AMIDA_ICON_TEX = new ResourceLocation(Revelationfix.MODID, "textures/ui/amida-icon.png");
    public static final ResourceLocation KETER_ICON_TEX = new ResourceLocation(Revelationfix.MODID, "textures/ui/keter-icon.png");
    public static final ResourceLocation NOTICE_ICON_TEX = new ResourceLocation(Revelationfix.MODID, "textures/ui/notice-icon.png");
    public static final ResourceLocation THAUMIEL_ICON_TEX = new ResourceLocation(Revelationfix.MODID, "textures/ui/thaumiel-icon.png");
    private final List<GuiElement> animationsElement = new ArrayList<>();
    ScrollScreenAccessor accessor;
    ScrollScreenEC screenEC;
    ScrollScreen instance;
    private GuiTexture aeglos;
    private GuiRectEntry left;
    private GuiRectEntry mid_1;
    private GuiRectEntry mid_2;
    private AnimationGuiRect mid_3;
    private GuiTexture right;
    private AnimationGuiRect lineTop;
    private GuiScpLevel scpLevel;
    private ScaledGuiText item;
    private ScaledGuiText level;
    private ScaledGuiText level_name;
    private ScaledGuiText containment_class_name;
    private ScaledGuiText containment_class;
    private ScaledGuiText sub_class_name;
    private ScaledGuiText sub_class;
    private ScaledGuiText disruption_class_name;
    private ScaledGuiText disruption_class;
    private ScaledGuiText risk_class_name;
    private ScaledGuiText risk_class;
    private GuiTexture keter_tex;
    private GuiTexture thaumiel_tex;
    private GuiTexture amida_tex;
    private GuiTexture notice_tex;
    private boolean isSCP = false;
    private SCP type = SCP.NONE;

    public ScrollScreenData(ScrollScreen instance) {
        this.instance = instance;
        this.screenEC = (ScrollScreenEC) instance;
        this.accessor = (ScrollScreenAccessor) instance;
    }

    public void init(String key) {
        Minecraft mc = Minecraft.getInstance();
        if (key.equals("sword/aeglos")) {
            isSCP = true;
            type = SCP.AEGLOS;
            accessor.setGui(new GuiElement(0, 0, instance.width, instance.height));
            int xOff = -9;
            int yOff = 5;
            {
                aeglos = (GuiTexture) new GuiTexture(0, -45, 80, 80, AEGLOS_TEX).setAttachment(GuiAttachment.topCenter);
                aeglos.setSpriteSize(80, 80);
                accessor.gui().addChild(aeglos);
                animationsElement.add(aeglos);
            }
            {
                int prefix = 32 + (int) (mc.font.width(I18n.get("screen.goety_revelation.scp.item") + ": ") * 1.3F + mc.font.width("Aeglos") * 1.7F);
                scpLevel = (GuiScpLevel) new GuiScpLevel(prefix + 5, 35 + yOff, 45, 25).setAttachment(GuiAttachment.topLeft);
                scpLevel.level = 6;
                accessor.gui().addChild(scpLevel);
                animationsElement.add(scpLevel);
                level = new ScaledGuiText(47, 2, 128, "LEVEL 6");
                level.setScale(1.4F);
                level.setColor(0xff333333);
                scpLevel.addChild(level);
                level_name = new ScaledGuiText(47, (int) (2 + mc.font.lineHeight * 1.4F), 128, I18n.get("screen.goety_revelation.scp.level.6"));
                level_name.setScale(1.4F);
                level_name.setColor(0xff333333);
                scpLevel.addChild(level_name);
            }
            {
                String text = I18n.get("screen.goety_revelation.scp.item") + ": ";
                item = (ScaledGuiText) new ScaledGuiText(32, 50 + yOff, 128, text).setAttachment(GuiAttachment.topLeft);
                item.setScale(1.3F);
                item.setColor(0xffb6b6b6);
                accessor.gui().addChild(item);
                ScaledGuiText name = (ScaledGuiText) new ScaledGuiText((int) (32 + mc.font.width(text) * 1.3F) + 1, 50 + yOff - (int) (mc.font.lineHeight * (1.7F - 1.3F)), 128, "Aeglos").setAttachment(GuiAttachment.topLeft);
                name.setScale(1.7F);
                name.setColor(0xffd7c3b2);
                item.addChild(name);
            }
            {
                lineTop = (AnimationGuiRect) new AnimationGuiRect(44 + xOff, 66 + yOff, 235, 4, 0x0c0c0c).setAnimation(AnimationGuiRect.Animation.ALPHA_TO_RIGHT).setAttachment(GuiAttachment.topLeft);
                accessor.gui().addChild(lineTop);
                animationsElement.add(lineTop);
            }
            {
                left = (GuiRectEntry) new GuiRectEntry(50 + xOff, 72 + yOff, 112, 28, -1, 0x424248, new CuttingShapeData(0xbdbdbd, 0xe4b6c2, CuttingShape.TRAPEZIUM)).setHeaderWidth(5).setAttachment(GuiAttachment.topLeft);
                left.setAnimation(AnimationGuiRect.Animation.ALPHA_TO_TOP);
                accessor.gui().addChild(left);
                animationsElement.add(left);
                containment_class_name = new ScaledGuiText(4, 2, 128, I18n.get("screen.goety_revelation.scp.containment_class") + ":");
                containment_class_name.setColor(0xff333333);
                containment_class_name.setScale(0.5F);
                left.addChild(containment_class_name);
                int yOffset0 = (int) (2 + mc.font.lineHeight / 2F + 1);
                containment_class = new ScaledGuiText(4, yOffset0, 128, "KETER");
                containment_class.setColor(0xff333333);
                containment_class.setScale(0.75F);
                left.addChild(containment_class);
                int yOffset1 = yOffset0 + (int) (mc.font.lineHeight * 0.75F);
                sub_class_name = new ScaledGuiText(4, yOffset1, 128, I18n.get("screen.goety_revelation.scp.sub_class") + ":");
                sub_class_name.setColor(0xff333333);
                sub_class_name.setScale(0.5F);
                left.addChild(sub_class_name);
                int yOffset2 = (int) (yOffset1 + mc.font.lineHeight / 2F + 1);
                sub_class = new ScaledGuiText(4, yOffset2, 128, "THAUMIEL");
                sub_class.setColor(0xff333333);
                sub_class.setScale(0.75F);
                left.addChild(sub_class);
                keter_tex = new ZGuiTexture(81, 4, 14, 14, KETER_ICON_TEX, 100);
                keter_tex.setSpriteSize(14, 14);
                left.addChild(keter_tex);
                thaumiel_tex = new ZGuiTexture(92, 2, 18, 18, THAUMIEL_ICON_TEX, 100);
                thaumiel_tex.setSpriteSize(18, 18);
                left.addChild(thaumiel_tex);
            }
            {
                mid_1 = (GuiRectEntry) new GuiRectEntry(169 + xOff, 72 + yOff, 77, 13, 0xf6d9e1, 0xc40233, GuiRectEntry.EMPTY).setHeaderWidth(3).setAttachment(GuiAttachment.topLeft);
                mid_1.setAnimation(AnimationGuiRect.Animation.ALPHA_TO_RIGHT);
                accessor.gui().addChild(mid_1);
                animationsElement.add(mid_1);
                disruption_class_name = new ScaledGuiText(4, 1, 128, I18n.get("screen.goety_revelation.scp.disruption_class") + ":");
                disruption_class_name.setColor(0xff333333);
                disruption_class_name.setScale(0.5F);
                mid_1.addChild(disruption_class_name);
                int yOffset0 = (int) (1 + mc.font.lineHeight / 2F + 1);
                disruption_class = new ScaledGuiText(4, yOffset0, 128, I18n.get("screen.goety_revelation.scp.disruption_class.amida"));
                disruption_class.setColor(0xff333333);
                disruption_class.setScale(0.75F);
                mid_1.addChild(disruption_class);
                amida_tex = new ZGuiTexture(59, -2, 16, 16, AMIDA_ICON_TEX, 100);
                amida_tex.setSpriteSize(16, 16);
                mid_1.addChild(amida_tex);
            }
            {
                mid_2 = (GuiRectEntry) new GuiRectEntry(169 + xOff, 87 + yOff, 77, 13, 0xd9f1e9, 0xB0009f6b, GuiRectEntry.EMPTY).setHeaderWidth(3).setAttachment(GuiAttachment.topLeft);
                mid_2.setAnimation(AnimationGuiRect.Animation.ALPHA_TO_RIGHT);
                accessor.gui().addChild(mid_2);
                animationsElement.add(mid_2);
                risk_class_name = new ScaledGuiText(4, 1, 128, I18n.get("screen.goety_revelation.scp.risk_class") + ":");
                risk_class_name.setColor(0xff333333);
                risk_class_name.setScale(0.5F);
                mid_2.addChild(risk_class_name);
                int yOffset0 = (int) (1 + mc.font.lineHeight / 2F + 1);
                risk_class = new ScaledGuiText(4, yOffset0, 128, I18n.get("screen.goety_revelation.scp.risk_class.notce"));
                risk_class.setColor(0xff333333);
                risk_class.setScale(0.75F);
                mid_2.addChild(risk_class);
                notice_tex = new ZGuiTexture(59, -2, 16, 16, NOTICE_ICON_TEX, 100);
                notice_tex.setSpriteSize(16, 16);
                mid_2.addChild(notice_tex);
            }
            {
                mid_3 = (AnimationGuiRect) new AnimationGuiRect(246 + xOff, 72 + yOff, 3, 28, 0x0).setAnimation(AnimationGuiRect.Animation.ALPHA_TO_BOTTOM).setAttachment(GuiAttachment.topLeft);
                accessor.gui().addChild(mid_3);
                animationsElement.add(mid_3);
            }
            {
                right = (GuiTexture) new GuiTexture(251 + xOff, 72 + yOff, 28, 28, ARROW_TEX).setAttachment(GuiAttachment.topLeft);
                right.setSpriteSize(28, 28);
                accessor.gui().addChild(right);
                animationsElement.add(right);
            }
            {
                ScaledGuiText guiText = new ScaledGuiText(4, -75 + 60 + yOff, 124, "");
                //guiText.setCenter(true);
                accessor.setText(guiText);
                accessor.text().setAttachmentAnchor(GuiAttachment.middleCenter);
                accessor.text().setAttachmentPoint(GuiAttachment.topCenter);
                accessor.text().setColor(0xffb9b9b9);
                accessor.gui().addChild(accessor.text());
            }
            accessor.gui().addChild((new ScrollPageButtonGui(70, 0, true, () -> {
                screenEC.revelationfix$changePage(ScrollScreenAccessor.currentPage() - 1);
            })).setAttachment(GuiAttachment.bottomLeft));
            accessor.gui().addChild((new ScrollPageButtonGui(-70, 0, false, () -> {
                screenEC.revelationfix$changePage(ScrollScreenAccessor.currentPage() + 1);
            })).setAttachment(GuiAttachment.bottomRight));
            screenEC.revelationfix$changePage(ScrollScreenAccessor.currentPage());
        } else if (key.equals("sword/longinus")) {
            isSCP = true;
            type = SCP.LONGINUS;
            accessor.setGui(new GuiElement(0, 0, instance.width, instance.height));
            int xOff = -9;
            int yOff = 5;
            {
                int prefix = 32 + (int) (mc.font.width(I18n.get("screen.goety_revelation.scp.item") + ": ") * 1.3F + mc.font.width("Longinus") * 1.7F);
                scpLevel = (GuiScpLevel) new GuiScpLevel(prefix + 5, 39 + yOff, 45, 25).setAttachment(GuiAttachment.topLeft);
                scpLevel.level = 5;
                accessor.gui().addChild(scpLevel);
                animationsElement.add(scpLevel);
                level = new ScaledGuiText(47, 1, 128, "LEVEL 5");
                level.setScale(1.4F);
                level.setColor(0xffb6b6b6);
                scpLevel.addChild(level);
                level_name = new ScaledGuiText(47, (int) (1 + mc.font.lineHeight * 1.4F), 128, I18n.get("screen.goety_revelation.scp.level.5"));
                level_name.setScale(1.4F);
                level_name.setColor(0xffb6b6b6);
                scpLevel.addChild(level_name);
            }
            {
                String text = I18n.get("screen.goety_revelation.scp.item") + ": ";
                item = (ScaledGuiText) new ScaledGuiText(32, 50 + yOff, 128, text).setAttachment(GuiAttachment.topLeft);
                item.setScale(1.3F);
                item.setColor(0xffb6b6b6);
                accessor.gui().addChild(item);
                ScaledGuiText name = (ScaledGuiText) new ScaledGuiText((int) (32 + mc.font.width(text) * 1.3F) + 1, 50 + yOff - (int) (mc.font.lineHeight * (1.7F - 1.3F)), 128, "Longinus").setAttachment(GuiAttachment.topLeft);
                name.setScale(1.7F);
                name.setColor(0xffd7c3b2);
                item.addChild(name);
            }
            {
                lineTop = (AnimationGuiRect) new AnimationGuiRect(44 + xOff, 66 + yOff, 235, 4, 0x0c0c0c).setAnimation(AnimationGuiRect.Animation.ALPHA_TO_RIGHT).setAttachment(GuiAttachment.topLeft);
                accessor.gui().addChild(lineTop);
                animationsElement.add(lineTop);
            }
            {
                left = (GuiRectEntry) new GuiRectEntry(50 + xOff, 72 + yOff, 112, 28, -1, 0x424248, new CuttingShapeData(0xbdbdbd, 0xe4b6c2, CuttingShape.TRAPEZIUM)).setHeaderWidth(5).setAttachment(GuiAttachment.topLeft);
                left.setAnimation(AnimationGuiRect.Animation.ALPHA_TO_TOP);
                accessor.gui().addChild(left);
                animationsElement.add(left);
                containment_class_name = new ScaledGuiText(4, 2, 128, I18n.get("screen.goety_revelation.scp.containment_class") + ":");
                containment_class_name.setColor(0xff333333);
                containment_class_name.setScale(0.5F);
                left.addChild(containment_class_name);
                int yOffset0 = (int) (2 + mc.font.lineHeight / 2F + 1);
                containment_class = new ScaledGuiText(4, yOffset0, 128, "N/A");
                containment_class.setColor(0xff333333);
                containment_class.setScale(0.75F);
                left.addChild(containment_class);
                int yOffset1 = yOffset0 + (int) (mc.font.lineHeight * 0.75F);
                sub_class_name = new ScaledGuiText(4, yOffset1, 128, I18n.get("screen.goety_revelation.scp.sub_class") + ":");
                sub_class_name.setColor(0xff333333);
                sub_class_name.setScale(0.5F);
                left.addChild(sub_class_name);
                int yOffset2 = (int) (yOffset1 + mc.font.lineHeight / 2F + 1);
                sub_class = new ScaledGuiText(4, yOffset2, 128, "THAUMIEL");
                sub_class.setColor(0xff333333);
                sub_class.setScale(0.75F);
                left.addChild(sub_class);
                thaumiel_tex = new ZGuiTexture(92, 2, 18, 18, THAUMIEL_ICON_TEX, 100);
                thaumiel_tex.setSpriteSize(18, 18);
                left.addChild(thaumiel_tex);
            }
            {
                mid_1 = (GuiRectEntry) new GuiRectEntry(169 + xOff, 72 + yOff, 77, 13, 0xf6d9e1, 0xc40233, GuiRectEntry.EMPTY).setHeaderWidth(3).setAttachment(GuiAttachment.topLeft);
                mid_1.setAnimation(AnimationGuiRect.Animation.ALPHA_TO_RIGHT);
                accessor.gui().addChild(mid_1);
                animationsElement.add(mid_1);
                disruption_class_name = new ScaledGuiText(4, 1, 128, I18n.get("screen.goety_revelation.scp.disruption_class") + ":");
                disruption_class_name.setColor(0xff333333);
                disruption_class_name.setScale(0.5F);
                mid_1.addChild(disruption_class_name);
                int yOffset0 = (int) (1 + mc.font.lineHeight / 2F + 1);
                disruption_class = new ScaledGuiText(4, yOffset0, 128, "N/A");
                disruption_class.setColor(0xff333333);
                disruption_class.setScale(0.75F);
                mid_1.addChild(disruption_class);
            }
            {
                mid_2 = (GuiRectEntry) new GuiRectEntry(169 + xOff, 87 + yOff, 77, 13, 0xd9f1e9, 0xB0009f6b, GuiRectEntry.EMPTY).setHeaderWidth(3).setAttachment(GuiAttachment.topLeft);
                mid_2.setAnimation(AnimationGuiRect.Animation.ALPHA_TO_RIGHT);
                accessor.gui().addChild(mid_2);
                animationsElement.add(mid_2);
                risk_class_name = new ScaledGuiText(4, 1, 128, I18n.get("screen.goety_revelation.scp.risk_class") + ":");
                risk_class_name.setColor(0xff333333);
                risk_class_name.setScale(0.5F);
                mid_2.addChild(risk_class_name);
                int yOffset0 = (int) (1 + mc.font.lineHeight / 2F + 1);
                risk_class = new ScaledGuiText(4, yOffset0, 128, "N/A");
                risk_class.setColor(0xff333333);
                risk_class.setScale(0.75F);
                mid_2.addChild(risk_class);
            }
            {
                mid_3 = (AnimationGuiRect) new AnimationGuiRect(246 + xOff, 72 + yOff, 3, 28, 0x0).setAnimation(AnimationGuiRect.Animation.ALPHA_TO_BOTTOM).setAttachment(GuiAttachment.topLeft);
                accessor.gui().addChild(mid_3);
                animationsElement.add(mid_3);
            }
            {
                right = (GuiTexture) new GuiTexture(251 + xOff, 72 + yOff, 28, 28, ARROW2_TEX).setAttachment(GuiAttachment.topLeft);
                right.setSpriteSize(28, 28);
                accessor.gui().addChild(right);
                animationsElement.add(right);
            }
            {
                ScaledGuiText guiText = new ScaledGuiText(4, -75 + 60 + yOff, 128, "");
                //guiText.setCenter(true);
                accessor.setText(guiText);
                accessor.text().setAttachmentAnchor(GuiAttachment.middleCenter);
                accessor.text().setAttachmentPoint(GuiAttachment.topCenter);
                accessor.text().setColor(0xffb9b9b9);
                accessor.gui().addChild(accessor.text());
            }
            accessor.gui().addChild((new ScrollPageButtonGui(70, 0, true, () -> {
                screenEC.revelationfix$changePage(ScrollScreenAccessor.currentPage() - 1);
            })).setAttachment(GuiAttachment.bottomLeft));
            accessor.gui().addChild((new ScrollPageButtonGui(-70, 0, false, () -> {
                screenEC.revelationfix$changePage(ScrollScreenAccessor.currentPage() + 1);
            })).setAttachment(GuiAttachment.bottomRight));
            screenEC.revelationfix$changePage(ScrollScreenAccessor.currentPage());
        }
    }

    public void screenInit() {
        if (isSCP) {
            if (type == SCP.AEGLOS) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                (new KeyframeAnimation(300, aeglos)).withDelay(100).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(10.0F, 0.0F, true)).start();
                (new KeyframeAnimation(300, scpLevel)).withDelay(100).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(10.0F, 0.0F, true)).start();

                (new KeyframeAnimation(200, left)).withDelay(500 + (int) (Math.random() * 200)).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                int delay1 = 500 + (int) (Math.random() * 200);
                (new KeyframeAnimation(400, lineTop)).withDelay(100).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();
                (new KeyframeAnimation(200, mid_1)).withDelay(delay1).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                (new KeyframeAnimation(200, mid_2)).withDelay(delay1 + 100).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                (new KeyframeAnimation(200, mid_3)).withDelay(delay1).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                (new KeyframeAnimation(400, right)).withDelay(600 + (int) (Math.random() * 200)).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();

                (new KeyframeAnimation(400, accessor.text())).withDelay(600).applyTo(new Applier.Opacity(-3.0F, 1.0F)).start();
                (new KeyframeAnimation(400, level)).withDelay(100).applyTo(new Applier.Opacity(-3.0F, 1.0F)).start();

            } else if (type == SCP.LONGINUS) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                (new KeyframeAnimation(300, scpLevel)).withDelay(100).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(10.0F, 0.0F, true)).start();

                (new KeyframeAnimation(200, left)).withDelay(500 + (int) (Math.random() * 200)).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                int delay1 = 500 + (int) (Math.random() * 200);
                (new KeyframeAnimation(400, lineTop)).withDelay(100).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();
                (new KeyframeAnimation(200, mid_1)).withDelay(delay1).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                (new KeyframeAnimation(200, mid_2)).withDelay(delay1 + 100).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                (new KeyframeAnimation(200, mid_3)).withDelay(delay1).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-6.0F, 0.0F, true)).start();
                (new KeyframeAnimation(400, right)).withDelay(600 + (int) (Math.random() * 200)).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();

                (new KeyframeAnimation(400, accessor.text())).withDelay(600).applyTo(new Applier.Opacity(-3.0F, 1.0F)).start();
                (new KeyframeAnimation(400, level)).withDelay(100).applyTo(new Applier.Opacity(-3.0F, 1.0F)).start();

            }
        }
    }

    public void changePage(int page) {
        if (isSCP) {
            if (type == SCP.AEGLOS) {
            }
        }

    }

    enum SCP {
        NONE, LONGINUS, AEGLOS, GUNGNIR
    }
}
