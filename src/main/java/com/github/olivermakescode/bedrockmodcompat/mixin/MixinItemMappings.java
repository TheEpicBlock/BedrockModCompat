package com.github.olivermakescode.bedrockmodcompat.mixin;

import com.github.olivermakescode.bedrockmodcompat.ComponentDataStorage;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import org.geysermc.connector.registry.type.ItemMappings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Adds a field to store item components
 */
@Mixin(ItemMappings.class)
public class MixinItemMappings implements ComponentDataStorage {
    private List<ComponentItemData> bmc$componentDats;

    @Override
    public List<ComponentItemData> bmc$getComponentData() {
        return bmc$componentDats;
    }

    @Override
    public void bmc$setComponentData(List<ComponentItemData> v) {
        bmc$componentDats = v;
    }

    @Mixin(ItemMappings.ItemMappingsBuilder.class)
    public static class MixinItemMappingsBuilder implements ComponentDataStorage {
        private List<ComponentItemData> bmc$componentData;

        @Override
        public List<ComponentItemData> bmc$getComponentData() {
            return bmc$componentData;
        }

        @Override
        public void bmc$setComponentData(List<ComponentItemData> v) {
            bmc$componentData = v;
        }

        @Inject(method = "build", at = @At("RETURN"), remap = false)
        private void buildInject(CallbackInfoReturnable<ItemMappings> cir) {
            var mappings = cir.getReturnValue();
            ((ComponentDataStorage)(Object)mappings).bmc$setComponentData(bmc$componentData);
        }
    }
}
