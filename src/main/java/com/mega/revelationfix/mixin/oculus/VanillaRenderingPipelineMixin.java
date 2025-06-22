package com.mega.revelationfix.mixin.oculus;

import com.mega.endinglib.util.annotation.ModDependsMixin;
import net.irisshaders.iris.pipeline.VanillaRenderingPipeline;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = VanillaRenderingPipeline.class, remap = false)
@ModDependsMixin("oculus")
public class VanillaRenderingPipelineMixin {

}
