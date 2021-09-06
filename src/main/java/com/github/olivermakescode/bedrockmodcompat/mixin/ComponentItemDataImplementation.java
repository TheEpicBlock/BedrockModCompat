package com.github.olivermakescode.bedrockmodcompat.mixin;

import com.github.olivermakescode.bedrockmodcompat.ComponentDataStorage;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.ItemComponentPacket;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.registry.type.ItemMappings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = GeyserSession.class, remap = false)
public class ComponentItemDataImplementation {
    @Shadow private ItemMappings itemMappings;

    @Redirect(method = "connect", at = @At(value = "INVOKE", target = "Lcom/nukkitx/protocol/bedrock/packet/ItemComponentPacket;getItems()Ljava/util/List;"))
    private List<ComponentItemData> itemComponentDataGetter(ItemComponentPacket itemComponentPacket) {
        var items = ((ComponentDataStorage)(Object)this.itemMappings).bmc$getComponentData();
        itemComponentPacket.getItems().addAll(items);
        return itemComponentPacket.getItems();
    }
}
