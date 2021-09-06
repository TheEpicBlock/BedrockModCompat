package com.github.olivermakescode.bedrockmodcompat.mixin.populators;

import com.github.olivermakescode.bedrockmodcompat.ComponentDataStorage;
import com.github.olivermakescode.bedrockmodcompat.mixin.ItemMappingsBuilderAccessor;
import com.github.olivermakescode.bedrockmodcompat.populators.ExtendedItemPopulator;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import org.geysermc.connector.registry.populator.ItemRegistryPopulator;
import org.geysermc.connector.registry.type.ItemMappings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ItemRegistryPopulator.class)
public class MixinItemRegistryPopulator {
    @Redirect(method = "populate", at = @At(value = "INVOKE", target = "Lorg/geysermc/connector/registry/type/ItemMappings$ItemMappingsBuilder;build()Lorg/geysermc/connector/registry/type/ItemMappings;"), remap = false)
    private static ItemMappings redirectInsertItems(ItemMappings.ItemMappingsBuilder itemMappingsBuilder) {
        var accessor = (ItemMappingsBuilderAccessor)itemMappingsBuilder;

        var creativeItems = accessor.getCreativeItems();
        var creativeItemsList = new ArrayList<ItemData>(creativeItems.length);
        Collections.addAll(creativeItemsList, creativeItems);

        List<ComponentItemData> componentItemData = new ArrayList<>();
        ExtendedItemPopulator.extend(accessor.getItems(), accessor.getJavaOnlyItems(), creativeItemsList, componentItemData);

        itemMappingsBuilder.creativeItems(creativeItemsList.toArray(new ItemData[0]));
        ((ComponentDataStorage)itemMappingsBuilder).bmc$setComponentData(componentItemData);

        return itemMappingsBuilder.build();
    }
}
