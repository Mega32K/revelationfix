package com.mega.revelationfix.mixin.oculus;

import com.mega.revelationfix.safe.mixinpart.DeprecatedMixin;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import net.irisshaders.iris.pipeline.VanillaRenderingPipeline;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = VanillaRenderingPipeline.class, remap = false)
@ModDependsMixin("oculus")
@DeprecatedMixin
public class VanillaRenderingPipelineMixin {

}
