package com.custom.witchcraft;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WitchcraftItem {
    // 物品组图标
    public static final Logger LOGGER = LoggerFactory.getLogger("witchcraft");
    public static Item WitchcraftGroupItem = new Item(new FabricItemSettings().maxCount(1));
    public static ItemGroup WitchcraftItemGroup = FabricItemGroup.builder()
            .icon(() -> new ItemStack(WitchcraftGroupItem))
            .displayName(Text.translatable("巫师"))
            .entries((context, entries) -> {
                //
                entries.add(WitchcraftGroupItem);
            })
            .build();
    public void register(){
        LOGGER.info("register : witchcraft");
        Registry.register(Registries.ITEM,new Identifier("witchcraft","icon"),WitchcraftGroupItem);
        Registry.register(Registries.ITEM_GROUP,new Identifier("witchcraft","item"),WitchcraftItemGroup);
    }
}
