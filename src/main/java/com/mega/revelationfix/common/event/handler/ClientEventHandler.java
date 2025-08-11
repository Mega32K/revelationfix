package com.mega.revelationfix.common.event.handler;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.mega.endinglib.mixin.accessor.AccessorLevelRenderer;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.api.event.render.RenderTooltipPostEvent;
import com.mega.revelationfix.client.font.OdamaneFont;
import com.mega.revelationfix.client.spell.SpellClientContext;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.common.apollyon.common.PlayerTickrateExecutor;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.data.ingrident.PuzzleIngredient;
import com.mega.revelationfix.common.data.ingrident.TheEndCraftingIngredient;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.curios.OdamaneHalo;
import com.mega.revelationfix.common.odamane.common.TheEndPuzzleItems;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Options;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
    public static final ResourceLocation RAIN_LOCATION = new ResourceLocation(Revelationfix.MODID, "textures/environment/rain.png");
    private static final RandomSource rand = RandomSource.create();
    public static VFRBuilders.WorldVFRTrailBuilder normalStarTrailsBuilder;
    public static int tickCount;
    public static Item randomDisplayItem = Items.END_CRYSTAL;
    public static Item randomPuzzleDisplayItem = Items.END_CRYSTAL;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltipNow(RenderTooltipEvent.GatherComponents event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.is(GRItems.HALO_OF_THE_END) && Screen.hasAltDown()) {
                OdamaneHalo.shouldTick = OdamaneHalo.tickCount < OdamaneHalo.maxTickCount;
                OdamaneHalo.apollyonTypeshouldTick = OdamaneHalo.apollyonTypeTickCount < OdamaneHalo.maxTickCount + 15;
            } else {
                OdamaneHalo.shouldTick = false;
                OdamaneHalo.apollyonTypeshouldTick = false;
            }
            if (PlayerTickrateExecutor.isInDoom(player)) {
                if (!CommonConfig.inWhitelist(itemStack.getItem()) && isInInventory(player, itemStack)) {
                    ChatFormatting chatFormatting = ChatFormatting.getByCode('q');
                    if (chatFormatting == null) chatFormatting = ChatFormatting.RED;
                    List<Either<FormattedText, TooltipComponent>> list = new ArrayList<>();
                    List<Either<FormattedText, TooltipComponent>> src = event.getTooltipElements();
                    FormattedText name = event.getTooltipElements().get(0).left().orElseGet(Component::empty);
                    if (name instanceof MutableComponent mutableComponent)
                        name = Component.translatable("tooltip.revelationfix.doom_banned_name").withStyle(ChatFormatting.DARK_RED).append(mutableComponent.withStyle(ChatFormatting.STRIKETHROUGH));
                    src.clear();
                    src.add(Either.left(name));
                    src.add(Either.left(Component.translatable("tooltip.revelationfix.doom_banned").withStyle(chatFormatting)));
                }
            }
        }
    }
    /*
    @SubscribeEvent
    public static void odamaneHaloTooltip(RenderTooltipEvent.Pre event) {
        if (event.getItemStack().getItem() instanceof ICenterDescItem) {
            event.setFont(OdamaneFont.INSTANCE);
            if (event.getFont() instanceof OdamaneFont odamaneFont) {
                event.setFont(OdamaneFont.INSTANCE);
                int i = 0;
                for (ClientTooltipComponent clienttooltipcomponent : event.getComponents()) {
                    int k = clienttooltipcomponent.getWidth(odamaneFont);
                    if (k > i) {
                        i = k;
                    }
                }
                odamaneFont.push(i);
            }
        }
    }

    @SubscribeEvent
    public static void odamaneHaloTooltip(RenderTooltipPostEvent event) {
        if (event.getItemStack().getItem() instanceof ICenterDescItem) {
            OdamaneFont.INSTANCE.pop();
        }
    }
     */

    @SubscribeEvent
    public static void addTECraftingTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (TheEndCraftingIngredient.INSTANCE.test(itemStack)) {
            event.getToolTip().add(Component.translatable("tooltip.revelationfix.t_e_crafting").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        } else if (itemStack.hasTag()) {
            Item item = null;
            boolean add = false;
            if (PuzzleIngredient.puzzle(0).testPuzzleItem(itemStack)) {
                add = true;
                item = TheEndRitualItemContext.PUZZLE1;
            } else if (PuzzleIngredient.puzzle(1).testPuzzleItem(itemStack)) {
                add = true;
                item = TheEndRitualItemContext.PUZZLE2;
            } else if (PuzzleIngredient.puzzle(2).testPuzzleItem(itemStack)) {
                add = true;
                item = TheEndRitualItemContext.PUZZLE3;
            } else if (PuzzleIngredient.puzzle(3).testPuzzleItem(itemStack)) {
                add = true;
                item = TheEndRitualItemContext.PUZZLE4;
            }
            if (add && item != null && itemStack.is(GRItems.PUZZLE_ITEM.get())) {
                try {
                    event.getToolTip().add(Component.translatable("item.goety_revelation.puzzle." + TheEndPuzzleItems.puzzleItems2.get(item)).withStyle(ATAHelper2.hasOdamane(Minecraft.getInstance().player) ? new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY} : new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY, ChatFormatting.OBFUSCATED}));
                } catch (Throwable throwable) {
                    RevelationFixMixinPlugin.LOGGER.debug("Item:" + item.getDescriptionId());
                    RevelationFixMixinPlugin.LOGGER.debug("ItemTag:" + item.builtInRegistryHolder().tags().toList());
                    throwable.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && event.phase == TickEvent.Phase.START) {
            mc.getProfiler().push("gr_clientTickEvent");
            tickCount++;
            if (tickCount >= 255)
                tickCount = 0;
            if (tickCount % 10 == 0) {
                if (TheEndPuzzleItems.vanillaItems == null) {
                    TheEndPuzzleItems.vanillaItems = new ArrayList<>();
                    for (Item item : ForgeRegistries.ITEMS.getValues()) {
                        if (item.getDefaultInstance().isEmpty()) continue;
                        if (item == Items.DRAGON_EGG) continue;
                        if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft"))
                            TheEndPuzzleItems.vanillaItems.add(item);
                    }
                }
                {
                    int i = rand.nextInt(0, TheEndPuzzleItems.vanillaItems.size());
                    Item item = (Item) TheEndPuzzleItems.vanillaItems.toArray()[i];
                    randomDisplayItem = item;
                }
                {
                    int i = rand.nextInt(0, TheEndPuzzleItems.puzzleItems.size());
                    Item item = (Item) TheEndPuzzleItems.puzzleItems.keySet().toArray()[i];
                    randomPuzzleDisplayItem = item;
                }
            }
            if (SpellClientContext.circle != null && !mc.isPaused())
                SpellClientContext.circle.tick();
            mc.getProfiler().pop();
        }
    }

    private static boolean isInInventory(Player player, ItemStack stack) {
        if (player.getInventory().contains(stack))
            return true;
        else return CuriosFinder.hasCurio(player, stack.getItem());
    }

    public static float cosFromSin(float sin, float angle) {
        if (Options.FASTMATH)
            return Mth.sin(angle + Mth.HALF_PI);
        return cosFromSinInternal(sin, angle);
    }

    private static float cosFromSinInternal(float sin, float angle) {
        // sin(x)^2 + cos(x)^2 = 1
        float cos = Mth.sqrt(1.0f - sin * sin);
        float a = angle + Mth.HALF_PI;
        float b = a - (int) (a / Mth.TWO_PI) * Mth.TWO_PI;
        if (b < 0.0)
            b = Mth.TWO_PI + b;
        if (b >= Mth.TWO_PI)
            return -cos;
        return cos;
    }

    public static Quaternionf rotateAxis(Quaternionf base, float angle, float axisX, float axisY, float axisZ, Quaternionf dest) {
        float hangle = angle / 2.0f;
        float sinAngle = Math.sin(hangle);
        float invVLength = Math.invsqrt(Math.fma(axisX, axisX, Math.fma(axisY, axisY, axisZ * axisZ)));
        float rx = axisX * invVLength * sinAngle;
        float ry = axisY * invVLength * sinAngle;
        float rz = axisZ * invVLength * sinAngle;
        float rw = cosFromSin(sinAngle, hangle);
        return dest.set(Math.fma(base.w, rx, Math.fma(base.x, rw, Math.fma(base.y, rz, -base.z * ry))),
                Math.fma(base.w, ry, Math.fma(-base.x, rz, Math.fma(base.y, rw, base.z * rx))),
                Math.fma(base.w, rz, Math.fma(base.x, ry, Math.fma(-base.y, rx, base.z * rw))),
                Math.fma(base.w, rw, Math.fma(-base.x, rx, Math.fma(-base.y, ry, -base.z * rz))));
    }

    @SubscribeEvent
    public static void renderNetherScarletRain(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            ClientLevelExpandedContext ecContext = ClientLevelExpandedContext.get();
            ClientLevel level = ecContext.clientLevel;
            float partialTicks = event.getPartialTick();
            {
                if (ecContext.isScarletRaining()) {
                    float rainLevel = ecContext.getRainLevel(partialTicks);
                    GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
                    LevelRenderer levelRenderer = Minecraft.getInstance().levelRenderer;
                    AccessorLevelRenderer accessorLevelRenderer = (AccessorLevelRenderer) levelRenderer;
                    LightTexture lightTexture = gameRenderer.lightTexture();
                    Camera camera = gameRenderer.getMainCamera();

                    Vec3 cameraPos = camera.getPosition();
                    double cameraX = cameraPos.x;
                    double cameraY = cameraPos.y;
                    double cameraZ = cameraPos.z;

                    lightTexture.turnOnLightLayer();
                    int i = Mth.floor(cameraX);
                    int j = Mth.floor(cameraY);
                    int k = Mth.floor(cameraZ);
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder bufferbuilder = tesselator.getBuilder();
                    RenderSystem.disableCull();
                    RenderSystem.enableBlend();
                    RenderSystem.enableDepthTest();
                    int l = 5;
                    if (Minecraft.useFancyGraphics()) {
                        l = 10;
                    }

                    RenderSystem.depthMask(Minecraft.useShaderTransparency());
                    int i1 = -1;
                    float f1 = (float) accessorLevelRenderer.getTicks() + partialTicks;
                    RenderSystem.setShader(GameRenderer::getParticleShader);
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                    //雨水斜下

                    Matrix4f matrix4f = null;
                    boolean flag = ClientConfig.enableDynamicScarletRain;
                    if (!flag) {
                        Quaternionf quaternionf = new Quaternionf();
                        rotateAxis(quaternionf, -12.5F * Mth.DEG_TO_RAD, 1F, 0F, 1F, quaternionf);
                        matrix4f = new Matrix4f().set(quaternionf);
                    }
                    for (int j1 = k - l; j1 <= k + l; ++j1) {
                        for (int k1 = i - l; k1 <= i + l; ++k1) {
                            int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                            double d0 = (double) accessorLevelRenderer.getRainSizeX()[l1] * 0.5D;
                            double d1 = (double) accessorLevelRenderer.getRainSizeZ()[l1] * 0.5D;
                            blockpos$mutableblockpos.set(k1, cameraY, j1);
                            int j2 = j - l;
                            int k2 = j + l;

                            if (j2 != k2) {
                                RandomSource randomsource = RandomSource.create((long) k1 * k1 * 3121 + k1 * 45238971L ^ (long) j1 * j1 * 418711 + j1 * 13761L);
                                blockpos$mutableblockpos.set(k1, j2, j1);
                                if (i1 != 0) {

                                    i1 = 0;
                                    RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }

                                int i3 = accessorLevelRenderer.getTicks() + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                                float f2 = -((float) i3 + partialTicks) / 32.0F * (3.0F + randomsource.nextFloat());
                                double d2 = (double) k1 + 0.5D - cameraX;
                                double d4 = (double) j1 + 0.5D - cameraZ;
                                float f3 = (float) Math.sqrt(d2 * d2 + d4 * d4) / (float) l;
                                float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * rainLevel;
                                blockpos$mutableblockpos.set(k1, j, j1);
                                int j3 = (int) (LevelRenderer.getLightColor(level, blockpos$mutableblockpos) * 1.5F);
                                if (flag) {
                                    Quaternionf quaternionf = new Quaternionf();
                                    matrix4f = new Matrix4f().set(rotateAxis(quaternionf, (-22.5F + (i3 + partialTicks) / 2.0F) * Mth.DEG_TO_RAD, 1F, 0F, 1F, quaternionf));
                                }
                                bufferbuilder.vertex(matrix4f, (float) (k1 - cameraX - d0 + 0.5D), (float) (k2 - cameraY), (float) (j1 - cameraZ - d1 + 0.5D)).uv(0.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex(matrix4f, (float) (k1 - cameraX + d0 + 0.5D), (float) (k2 - cameraY), (float) (j1 - cameraZ + d1 + 0.5D)).uv(1.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex(matrix4f, (float) (k1 - cameraX + d0 + 0.5D), (float) (j2 - cameraY), (float) (j1 - cameraZ + d1 + 0.5D)).uv(1.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex(matrix4f, (float) (k1 - cameraX - d0 + 0.5D), (float) (j2 - cameraY), (float) (j1 - cameraZ - d1 + 0.5D)).uv(0.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();

                            }
                        }
                    }

                    if (i1 == 0) {
                        tesselator.end();
                    }

                    RenderSystem.enableCull();
                    RenderSystem.disableBlend();
                    lightTexture.turnOffLightLayer();
                }
            }
        }
    }

    @SubscribeEvent
    public static void drawTrails(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            if (ClientConfig.enableTrailRenderer) {
                Minecraft mc = Minecraft.getInstance();
                ProfilerFiller profilerFiller = mc.getProfiler();
                MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
                PoseStack matrix = event.getPoseStack();
                LightTexture lightTexture = mc.gameRenderer.lightTexture();
                Camera camera = (Minecraft.getInstance()).gameRenderer.getMainCamera();
                double d3 = camera.getPosition().x;
                double d4 = camera.getPosition().y;
                double d5 = camera.getPosition().z;
                if (normalStarTrailsBuilder == null) {
                    normalStarTrailsBuilder = VFRBuilders.WorldVFRTrailBuilder.create();
                }
                lightTexture.turnOnLightLayer();
                lightTexture.updateLightTexture(mc.getPartialTick());
                profilerFiller.push("CIL_trail");
                if (!normalStarTrailsBuilder.toRender.isEmpty()) {
                    matrix.translate(-d3, -d4, -d5);
                    normalStarTrailsBuilder.renderTrails(matrix);
                    matrix.translate(d3, d4, d5);
                }
                profilerFiller.pop();
                lightTexture.turnOffLightLayer();
            }
        }
    }

    @SubscribeEvent
    public static void onTailTick(TickEvent.ClientTickEvent event) {
        if (ClientConfig.enableTrailRenderer && normalStarTrailsBuilder != null) {
            if (event.phase == TickEvent.Phase.START) {
                if (!normalStarTrailsBuilder.toRender.isEmpty())
                    normalStarTrailsBuilder.toRender.forEach(VFRBuilders.WorldVFRTrailBuilder.TrailRenderTask::tick);
            }
        }
    }
    @SubscribeEvent
    public static void renderSpellCircle(RenderPlayerEvent event) {
        //if (SafeClass.isYSMLoaded()) return;
        Minecraft mc = Minecraft.getInstance();
        if (SpellClientContext.circle != null) {
            SpellClientContext.circle.render(event.getPartialTick(), event.getPoseStack());
        }
    }
}
