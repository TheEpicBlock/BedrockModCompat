package com.github.olivermakescode.bedrockmodcompat;

import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;

import java.util.List;

/**
 * Component data stores info about items
 */
public interface ComponentDataStorage {
    List<ComponentItemData> bmc$getComponentData();
    void bmc$setComponentData(List<ComponentItemData> v);
}
