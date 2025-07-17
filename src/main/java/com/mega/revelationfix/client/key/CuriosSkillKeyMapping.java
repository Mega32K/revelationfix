package com.mega.revelationfix.client.key;

import com.mega.endinglib.api.client.ClientTaskInstance;
import com.mega.endinglib.api.event.EventItf;
import com.mega.revelationfix.client.screen.post.custom.BarrelDistortionCoordinatesPostEffect;
import com.mega.revelationfix.client.task.TeleportCameraTask;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.entity.binding.TeleportEntity;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.c2s.TeleportEntityTryEvent;
import com.mega.revelationfix.common.network.c2s.TryTimeStopSkill;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CuriosSkillKeyMapping {
    private static final Minecraft mc = Minecraft.getInstance();
    public static KeyMapping ACTIVE_SKILL = new KeyMapping("key.revelationfix.curios_skill", GLFW.GLFW_KEY_K, "key.revelationfix.category");

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onKeyboard(InputEvent.Key event) {
        if (mc.screen == null && event.getAction() == GLFW.GLFW_PRESS && event.getKey() == ACTIVE_SKILL.getKey().getValue()) {
            if (ATAHelper2.hasOdamane(mc.player) || ATAHelper2.hasEternalWatch(mc.player)) {
                PacketHandler.sendToServer(new TryTimeStopSkill(mc.player.getId()));
                ((EventItf)event).el_setEventUnCancelable(true);
            }
        }
    }
    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton event) {
        if (event instanceof InputEvent.MouseButton.Post) {
            Player player = mc.player;
            if (mc.screen == null && event.getAction() == GLFW.GLFW_RELEASE && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                if (player != null && player.isShiftKeyDown() && ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext().teleportStayingTime > 0) {
                    PacketHandler.sendToServer(new TeleportEntityTryEvent(player.getId()));
                    player.swingTime = -1;
                    player.swinging = true;
                    player.swingingArm = InteractionHand.MAIN_HAND;
                    label0:{
                        Level level = mc.level;
                        if (level == null) break label0;
                        BlockPos blockPos = player.blockPosition().above(-1);
                        if (level.getBlockState(blockPos).is(ModBlocks.RUNE_REACTOR.get())) {
                            if (level.getBlockEntity(blockPos) instanceof RuneReactorBlockEntity reactorBlockEntity) {
                                List<TeleportEntity> preparation = new ArrayList<>(level.getEntitiesOfClass(TeleportEntity.class, new AABB(player.blockPosition()).inflate(128D)));
                                preparation.sort((e1, e2) -> (int) ((e1.distanceToSqr(player) - e2.distanceToSqr(player)) * 100));
                                TeleportEntity target = null;
                                for (TeleportEntity teleportEntity : preparation) {
                                    if (teleportEntity.isLookingAtMe(player) && teleportEntity.distanceToSqr(player) > 2) {
                                        target = teleportEntity;
                                        break;
                                    }
                                }
                                if (target != null && !target.isRemoved()) {
                                    mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ENDERMAN_TELEPORT, 1F));
                                    if (BarrelDistortionCoordinatesPostEffect.tickCount <= 0) {
                                        ClientTaskInstance instance = new TeleportCameraTask();
                                        instance.onAddedToWorld();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
