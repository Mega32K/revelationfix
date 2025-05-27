package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.util.SummonApostle;
import com.mega.revelationfix.safe.GRSavedDataEC;
import com.mega.revelationfix.safe.GRSavedDataExpandedContext;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;
import z1gned.goetyrevelation.entitiy.SummonApollyon;

import java.util.Iterator;

@Mixin(SummonApollyon.class)
public class SummonApollyonMixin extends SummonApostle {
    public SummonApollyonMixin(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {
            DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(serverLevel);
            GRSavedDataEC savedDataEC = (GRSavedDataEC) state;
            GRSavedDataExpandedContext context = savedDataEC.revelationfix$dataEC();
            if (this.level.dimension() != Level.NETHER)
                context.setSummonedApollyonOverworld(true);
        }
        if (this.tickCount == 150) {
            this.playSound(SoundEvents.AMBIENT_NETHER_WASTES_MOOD.get(), 1.0F, 1.0F);
            Iterator var1 = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(32.0)).iterator();
            while (var1.hasNext()) {
                Player player = (Player) var1.next();
                player.displayClientMessage(Component.translatable("info.goety.apollyon.summon"), true);
            }
        }
    }
}
