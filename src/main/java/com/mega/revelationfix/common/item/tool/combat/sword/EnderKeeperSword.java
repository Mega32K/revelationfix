package com.mega.revelationfix.common.item.tool.combat.sword;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.mega.endinglib.common.network.PacketHandler;
import com.mega.endinglib.common.network.s2c.rotation.S2CSetRotationPacket;
import com.mega.endinglib.mixin.accessor.AccessorEntity;
import com.mega.endinglib.mixin.accessor.AccessorLivingEntity;
import com.mega.endinglib.util.mc.entity.MobEffectUtils;
import com.mega.endinglib.util.mc.entity.RaycastHelper;
import com.mega.endinglib.util.mc.entity.RotationUtils;
import com.mega.revelationfix.api.item.combat.ICustomHurtWeapon;
import com.mega.revelationfix.client.renderer.item.KeeperSwordItemRenderer;
import com.mega.revelationfix.client.renderer.trail.TrailRenderTask;
import com.mega.revelationfix.common.config.GoetyModificationClientConfig;
import com.mega.revelationfix.common.entity.ShadowPlayerEntity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnderKeeperSword extends ModSwordItem implements ICustomHurtWeapon, ISoulRepair {
    public static final UUID ATTACK_SPEED_EXTRA = UUID.fromString("910f74a9-9ae3-492d-a97b-f0e65ff5bd6f");
    public EnderKeeperSword() {
        super(Tiers.NETHERITE, 20, -3.5F, new Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity living, @NotNull LivingEntity source) {
        MobEffectUtils.forceAdd(living, new MobEffectInstance(GoetyEffects.VOID_TOUCHED.get(), 100, 1), source);
        return super.hurtEnemy(stack, living, source);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final Supplier<KeeperSwordItemRenderer> rendererSupplier = Suppliers.memoize(KeeperSwordItemRenderer::new);
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return rendererSupplier.get();
            }
        });
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player instanceof ServerPlayer sp) {
                ServerLevel serverLevel = sp.serverLevel();
                Vec3 motion;
                if (player.isSprinting()) {
                    PacketHandler.playSound(sp, ModSounds.ENDERLING_TELEPORT_IN.get(), 1.0F, 1.0F);
                    player.getCooldowns().addCooldown(itemStack.getItem(), 25);
                    motion = player.getLookAngle().scale(2.8F);
                } else {
                    PacketHandler.playSound(sp, ModSounds.ENDERLING_TELEPORT_OUT.get(), 1.0F, 1.0F);
                    motion = player.getLookAngle().scale(-1.5F);
                    player.getCooldowns().addCooldown(itemStack.getItem(), 10);
                }
                player.push(motion.x, 0.0, motion.z);
                player.hurtMarked = true;
                sp.connection.send(new ClientboundAnimatePacket(player, hand == InteractionHand.MAIN_HAND ? ClientboundAnimatePacket.SWING_MAIN_HAND : ClientboundAnimatePacket.SWING_OFF_HAND));

                sp.connection.send(new ClientboundSetEntityMotionPacket(player));
                ShadowPlayerEntity shadowPlayer = new ShadowPlayerEntity(player);
                shadowPlayer.setPos(player.position());
                shadowPlayer.setPosData(player.position());
                level.addFreshEntity(shadowPlayer);
                ColorUtil color = new ColorUtil(0x8854d1);
                for (int i=0;i<3;i++) {
                    ServerParticleUtil.windParticle(serverLevel, color, 0.8F + serverLevel.random.nextFloat() * 0.5F, 0.0F, 15 + player.getRandom().nextInt(15), player.getId(), player.position());
                }
                color = new ColorUtil(0x9d4dc8);
                for (int i=0;i<5;i++) {
                    ServerParticleUtil.circularParticles(serverLevel, ParticleTypes.PORTAL, player, 1.5F);
                    ServerParticleUtil.windParticle(serverLevel, color, 1.1F + serverLevel.random.nextFloat() * 0.5F, 0.0F, 15 + player.getRandom().nextInt(15), player.getId(), player.position().add(
                            Math.random() * 0.2F - 0.1F,
                            Math.random() * 0.2F - 0.1F,
                            Math.random() * 0.2F - 0.1F
                    ));
                }
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity.isShiftKeyDown()) {
            if (entity instanceof ServerPlayer serverPlayer) {
                if (!serverPlayer.getCooldowns().isOnCooldown(stack.getItem())) {
                    if (RaycastHelper.findCrosshairTarget(serverPlayer, Math.max(serverPlayer.getBlockReach(), serverPlayer.getEntityReach()) * 2F) instanceof EntityHitResult entityHitResult) {
                        if (entityHitResult.getEntity() instanceof LivingEntity target) {
                            Level level = serverPlayer.level();
                            ((AccessorLivingEntity) serverPlayer).setAttackStrengthTicker((int) (serverPlayer.getCurrentItemAttackStrengthDelay()) + 1);
                            double xSize = target.getBoundingBox().getXsize();
                            double zSize = target.getBoundingBox().getZsize();
                            Vec3 targetPos = target.getBoundingBox().getCenter()
                                    .add(((AccessorEntity) target)
                                            .invokeCalculateViewVector(0F, target.getYRot())
                                            .scale(-1F * Math.max(1F, Math.sqrt(xSize * xSize + zSize * zSize)))
                                    );
                            targetPos.add(((AccessorEntity) target).invokeCalculateViewVector(0F, target.getYRot()));
                            {
                                double distance = targetPos.distanceTo(serverPlayer.position());
                                int times = Math.max(1, (int) (distance / 3.0F));

                                for (int i=0;i<times;i++) {
                                    float percent = (float) i / times;
                                    ShadowPlayerEntity shadowPlayer = new ShadowPlayerEntity(serverPlayer);
                                    shadowPlayer.setPos(
                                            Mth.lerp(percent, serverPlayer.position().x, targetPos.x),
                                            Mth.lerp(percent, serverPlayer.position().y, targetPos.y),
                                            Mth.lerp(percent, serverPlayer.position().z, targetPos.z)
                                    );
                                    shadowPlayer.setPosData(
                                            (float) Mth.lerp(percent, serverPlayer.position().x, targetPos.x),
                                            (float) Mth.lerp(percent, serverPlayer.position().y, targetPos.y),
                                            (float) Mth.lerp(percent, serverPlayer.position().z, targetPos.z)
                                    );
                                    level.addFreshEntity(shadowPlayer);
                                }
                            }
                            //serverPlayer.move(MoverType.PLAYER, targetPos );
                            serverPlayer.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                            RotationUtils.rotateAtoB(serverPlayer, target.getBoundingBox().getCenter());
                            PacketHandler.sendToPlayer(new S2CSetRotationPacket(serverPlayer.getXRot(), serverPlayer.getYRot(), serverPlayer.getId()), serverPlayer);
                            //serverPlayer.connection.teleport(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot(), RelativeMovement.ROTATION);

                            UUID modifierID = UUID.fromString("912cecd5-d89d-43be-baee-392721739ff2");
                            AttributeInstance attributeInstance = serverPlayer.getAttribute(Attributes.ATTACK_DAMAGE);
                            if (attributeInstance != null)
                                attributeInstance.addTransientModifier(new AttributeModifier(
                                        modifierID,
                                        "Weapon Attributes",
                                        0.25,
                                        AttributeModifier.Operation.MULTIPLY_TOTAL
                                ));
                            serverPlayer.attack(target);
                            if (attributeInstance != null)
                                attributeInstance.removeModifier(modifierID);
                            serverPlayer.getCooldowns().addCooldown(stack.getItem(), 20);
                        }
                    }
                }
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public void injectExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.fromString("910f74a9-9ae3-492d-a97b-f0e65ff5bd6f"), "Weapon Attributes", 3.0, AttributeModifier.Operation.ADDITION));
    }
}
