package com.custom.occult;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.custom.MoreMagic.CanListenItemArray;

public class OccultItem {
    // 物品组图标
    public static final Logger LOGGER = LoggerFactory.getLogger("occult");
    public static Item OccultGroupIcon = new Item(new FabricItemSettings().maxCount(1));
    public static ItemGroup OccultItemGroup = FabricItemGroup.builder()
            .icon(() -> new ItemStack(OccultGroupIcon))
            .displayName(Text.translatable("奥术师"))
            .entries((context, entries) -> {
                //
                entries.add(OccultGroupIcon);
            })
            .build();
    public void register(){
        LOGGER.info("register : occult");
        Registry.register(Registries.ITEM,new Identifier("occult","icon"),OccultGroupIcon);
        Registry.register(Registries.ITEM_GROUP,new Identifier("occult","item"),OccultItemGroup);
    }
}
