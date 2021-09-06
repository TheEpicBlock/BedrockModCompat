package com.github.olivermakescode.bedrockmodcompat.populators;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.nbt.NbtType;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geysermc.connector.registry.type.ItemMapping;
import org.geysermc.platform.fabric.GeyserFabricLogger;
import org.geysermc.relocate.fastutil.ints.Int2ObjectMap;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExtendedItemPopulator {
    private static final Logger LOGGER = LogManager.getLogger("bedrockmodcompat-item-populator");

    public static void extend(Int2ObjectMap<ItemMapping> itemMappings, Set<String> javaOnlyItems, List<ItemData> creativeItems, List<ComponentItemData> componentItemData) {
        HashSet<Identifier> alreadyMapped = new HashSet<>();
        itemMappings.forEach((integer, itemMapping) -> alreadyMapped.add(new Identifier(itemMapping.getJavaIdentifier())));

        var bedrockIdCounter = 0;
        for (ItemMapping mapping : itemMappings.values()) {
            // set the counter to the highest bedrock id that's in use
            if (mapping.getBedrockId() > bedrockIdCounter) bedrockIdCounter = mapping.getBedrockId();
        }

        var netIdCounter = 0;
        for (var itemData : creativeItems) {
            // set the counter to the highest net id that's in use
            if (itemData.getNetId() > netIdCounter) netIdCounter = itemData.getNetId();
        }

        LOGGER.info("Extending an item registry, current bedrock id is %d, net id is %d and %d items are already mapped with %d in the vanilla registry".formatted(bedrockIdCounter, netIdCounter, itemMappings.size(), Registry.ITEM.stream().count()));
        for (var item : Registry.ITEM) {
            var id = Registry.ITEM.getId(item);
            var idString = id.toString();
            var rawId = Registry.ITEM.getRawId(item);
            if (alreadyMapped.contains(id)) continue;

            // Item Mappings
            var mapping = ItemMapping.builder();

            mapping.javaId(rawId);
            mapping.bedrockId(rawId);
            mapping.javaIdentifier(id.toString());
            mapping.bedrockIdentifier(id.toString());

            mapping.stackSize(item.getMaxCount());
            mapping.translationString(item.getTranslationKey());

            mapping.bedrockData(0);
            mapping.bedrockBlockId(-1);

            itemMappings.put(rawId, mapping.build());

            // Java only items list
            javaOnlyItems.add(idString);

            // Creative items
            var creaItem = ItemData.builder();
            creaItem.netId(netIdCounter);
            creaItem.id(rawId);
            creaItem.count(1);
            creativeItems.add(creaItem.build());

            // Component item data
            NbtMapBuilder builder = NbtMap.builder();
            builder.putString("name", idString);
            builder.putInt("id", rawId);

            NbtMapBuilder componentBuilder = NbtMap.builder();
            componentBuilder.putCompound("minecraft:icon", NbtMap.builder().putString("texture", "todo").build());
            componentBuilder.putCompound("minecraft:display_name", NbtMap.builder().putString("value", "item.minecartFurnace.name").build());
            NbtMapBuilder itemProperties = NbtMap.builder();
            itemProperties.putBoolean("allow_off_hand", true);
            itemProperties.putBoolean("hand_equipped", false);
            itemProperties.putInt("max_stack_size", item.getMaxCount());
            itemProperties.putInt("creative_category", 4);
            componentBuilder.putCompound("item_properties", itemProperties.build());
            builder.putCompound("components", componentBuilder.build());
            componentItemData.add(new ComponentItemData(idString, builder.build()));

            // Counters
            bedrockIdCounter++;
            netIdCounter++;
        }
        LOGGER.info("Finished extending item registry, %d items are now mapped; %d java only items; %d creative items; %d component item data entries".formatted(itemMappings.size(), javaOnlyItems.size(), creativeItems.size(), componentItemData.size()));
    }
}
