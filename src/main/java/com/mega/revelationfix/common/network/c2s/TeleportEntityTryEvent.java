package com.mega.revelationfix.common.network.c2s;

import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.entity.binding.TeleportEntity;
import com.mega.revelationfix.common.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TeleportEntityTryEvent {

    protected final int user;

    public TeleportEntityTryEvent(int user) {
        this.user = user;
    }

    public static TeleportEntityTryEvent decode(FriendlyByteBuf friendlyByteBuf) {
        return new TeleportEntityTryEvent(friendlyByteBuf.readInt());
    }

    public static void encode(TeleportEntityTryEvent packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packet.user);
    }

    public static void handle(TeleportEntityTryEvent packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TeleportEntityTryEvent packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getSender() == null)
            return;
        ServerPlayer sp = context.get().getSender();
        if (sp == null) return;
        try {
            ServerLevel serverLevel = sp.serverLevel();
            Entity levelEntity = serverLevel.getEntity(packet.user);
            if (levelEntity instanceof ServerPlayer serverPlayer) {
                BlockPos blockPos = serverPlayer.blockPosition().above(-1);
                if (serverLevel.getBlockState(blockPos).is(ModBlocks.RUNE_REACTOR.get())) {
                    if (serverLevel.getBlockEntity(blockPos) instanceof RuneReactorBlockEntity reactorBlockEntity) {
                        List<TeleportEntity> preparation = new ArrayList<>(serverLevel.getEntitiesOfClass(TeleportEntity.class, new AABB(serverPlayer.blockPosition()).inflate(128D)));
                        preparation.sort((e1, e2) -> (int) ((e1.distanceToSqr(serverPlayer) - e2.distanceToSqr(serverPlayer)) * 100));
                        TeleportEntity target = null;
                        for (TeleportEntity teleportEntity : preparation) {
                             if (teleportEntity.isLookingAtMe(serverPlayer) && teleportEntity.distanceToSqr(serverPlayer) > 2) {
                                 target = teleportEntity;
                                 break;
                             }
                        }
                        if (target != null && !target.isRemoved()) {
                            serverPlayer.teleportTo(target.getX() + .5F, target.getY() + 1F, target.getZ() + .5F);
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    public static double calculateRadians(
            Vector3f line1Start, Vector3f line1End,
            Vector3f line2Start, Vector3f line2End) {
        Vector3d dir1 = new Vector3d(line1End);
        dir1.sub(line1Start);
        Vector3d dir2 = new Vector3d(line2End);
        dir2.sub(line2Start);
        Vector3d crossProduct = new Vector3d();
        dir1.cross(dir2, crossProduct);

        if (crossProduct.equals(new Vector3d(0, 0, 0))) {
            return 0;
        }
        Matrix3d matrix = new Matrix3d();
        matrix.setColumn(0, dir1);
        matrix.setColumn(1, dir2);
        matrix.setColumn(2, crossProduct);

        double angle = Math.atan2(matrix.determinant(), dir1.dot(dir2));
        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        return angle;
    }
}
