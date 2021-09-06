package com.github.olivermakescode.bedrockmodcompat.mixin;

import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import org.geysermc.connector.registry.type.ItemMapping;
import org.geysermc.connector.registry.type.ItemMappings;
import org.geysermc.relocate.fastutil.ints.Int2ObjectMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(value = ItemMappings.ItemMappingsBuilder.class, remap = false)
public interface ItemMappingsBuilderAccessor {
    @Accessor
    Int2ObjectMap<ItemMapping> getItems();

    @Accessor
    Set<String> getJavaOnlyItems();

    @Accessor
    ItemData[] getCreativeItems();
}
