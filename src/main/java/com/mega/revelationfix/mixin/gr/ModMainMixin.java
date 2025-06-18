package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.compat.patchouli.PatchouliIntegration;
import com.Polarice3.Goety.compat.patchouli.PatchouliLoaded;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.util.java.Self;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.item.ModItems;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mixin(ModMain.class)
public class ModMainMixin {
    @Unique
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "goety_revelation");
    @Unique
    private static RegistryObject<CreativeModeTab> TAB;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        TAB = CREATIVE_TABS.register("goety_revelation", () -> CreativeModeTab.builder()
                .icon(() -> ModItems.WITHER_QUIETUS.get().getDefaultInstance())
                .title(Component.translatable("itemGroup.goety_revelation"))
                .hideTitle()
                .withBackgroundLocation(new ResourceLocation(ModMain.MODID, "textures/ui/tab_items.png"))
                .displayItems((parameters, output) -> {
                    if (PatchouliLoaded.PATCHOULI.isLoaded()) {
                        output.accept(PatchouliIntegration.getBlackBook());
                        output.accept(PatchouliIntegration.getWitchesBrew());
                    }
                    //output.accept(GRItems.HALO_OF_THE_END);
                    output.accept(ModItems.ASCENSION_HALO.get());
                    output.accept(ModItems.BROKEN_HALO.get());
                    for (Field field : GRItems.class.getDeclaredFields()) {
                        if (!field.isAnnotationPresent(Self.class) && Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(RegistryObject.class)) {
                            try {
                                RegistryObject<Item> ro = ((RegistryObject<Item>) field.get(null));
                                if (ro != null) {
                                    output.accept(ro.get());
                                    if (GRItems.insertAfterTabMap.containsKey(ro)) {
                                        for (ItemStack stack : GRItems.insertAfterTabMap.get(ro).get())
                                            output.accept(stack);
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                })
                .build()
        );
        CREATIVE_TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
