package com.custom;

import com.custom.magic.WandItem;
import com.custom.magic.HudRender;
import com.custom.magic.entity.MagicVillagerRenderer;
import com.custom.occult.OccultItem;
import com.custom.witchcraft.WitchcraftItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.custom.MoreMagic;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;


public class MoreMagicClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    public static final MinecraftClient client = MinecraftClient.getInstance();

    public static final KeyBinding MY_KEY = new KeyBinding(
            "key.moremagic.my_key_binding",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.category.moremagic"
    ){
        @Override
        public void setPressed(boolean pressed) {
            super.setPressed(pressed);
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                Item MainHandItem = player.getMainHandStack().getItem();
                if(MoreMagic.CanListenItemArray.contains(MainHandItem)) {
                    if (pressed){
                        MoreMagic.ifPressed_Key_R = true;
                    }else {
                        MoreMagic.ifPressed_Key_R = false;
                    }
                }
            }
        }
    };
    public static final KeyBinding KEY_PAGE_UP = new KeyBinding(
            "key.moremagic.my_key_binding_2",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UP,
            "key.category.moremagic"
    ){
        @Override
        public void setPressed(boolean pressed) {
            super.setPressed(pressed);
            if (pressed) {
                // 当按键被按下时执行操作
                // 检查滚轮向上滚动
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null) {
                    Item MainHandItem = player.getMainHandStack().getItem();
                    if(MoreMagic.CanListenItemArray.contains(MainHandItem)) {
                        //上
                        if(MoreMagic.ifPressed_Key_R){
                            int getIndex = MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name);
                            if(getIndex-1 >= 0){
                                MoreMagic.Magic_Name = MoreMagic.AllMagicArray.get(getIndex-1);
                            }else{
                                MoreMagic.Magic_Name = MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.toArray().length-1);
                            }
                        }
                    }
                }
            }
        }
    };
    public static final KeyBinding KEY_PAGE_DOWN = new KeyBinding(
            "key.moremagic.my_key_binding_3",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_DOWN,
            "key.category.moremagic"
    ){
        @Override
        public void setPressed(boolean pressed) {
            super.setPressed(pressed);
            if (pressed) {
                // 当按键被按下时执行操作
                // 检查滚轮向上滚动
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null) {
                    Item MainHandItem = player.getMainHandStack().getItem();
                    if(MoreMagic.CanListenItemArray.contains(MainHandItem)) {
                        //下
                        if(MoreMagic.ifPressed_Key_R){
                            int getIndex = MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name);
                            if(getIndex+1 <= MoreMagic.AllMagicArray.toArray().length-1){
                                MoreMagic.Magic_Name = MoreMagic.AllMagicArray.get(getIndex+1);
                            }else{
                                MoreMagic.Magic_Name = MoreMagic.AllMagicArray.get(0);
                            }
                        }
                    }
                }else {
                    LOGGER.info("NO PLAYER");
                }
            }
        }
    };
    @Override
    public void onInitializeClient() {
        LOGGER.info("client init");
        EntityRendererRegistry.register(MoreMagic.MagicEntityType, (context) ->
                new FlyingItemEntityRenderer(context));

        EntityRendererRegistry.register(MoreMagic.MagicVillagerType, (context) -> {
            return new MagicVillagerRenderer(context);
        });

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        // 添加一个按键监听器
        KeyBindingHelper.registerKeyBinding(MY_KEY);
        KeyBindingHelper.registerKeyBinding(KEY_PAGE_UP);
        KeyBindingHelper.registerKeyBinding(KEY_PAGE_DOWN);

        HudRenderCallback.EVENT.register(new HudRender());
    }
}