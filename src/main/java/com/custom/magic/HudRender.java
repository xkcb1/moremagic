package com.custom.magic;


import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.SnowballItem;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.custom.MoreMagic;

import java.text.DecimalFormat;
import java.util.*;


public class HudRender implements HudRenderCallback {
    public static final Identifier Texture_BG_1 = new Identifier("magic:textures/gui/main_bg.png");
    public static final Identifier SearchPanel = new Identifier("magic:textures/gui/panel_3.png");
    public static final Identifier NormalPanel = new Identifier("magic:textures/gui/widgets_1.png");
    public static final Identifier SelectPanel = new Identifier("magic:textures/gui/widgets_2.png");
    public static final Identifier SoltPanel = new Identifier("magic:textures/gui/solt.png");
    public static final Identifier BookPanel = new Identifier("magic:textures/gui/book_5.png");
    public static final Identifier Texture_BG_Lumos = new Identifier("magic:textures/gui/magic_lumos.png");
    public static final Identifier Texture_BG_Stupefy = new Identifier("magic:textures/gui/magic_stupefy.png");
    public static final Identifier Texture_BG_Expelliarmus = new Identifier("magic:textures/gui/magic_expelliarmus.png");
    public static final Identifier Texture_BG_Nox = new Identifier("magic:textures/gui/magic_nox.png");

    public static final Identifier Texture_BG_Protego = new Identifier("magic:textures/gui/magic_protego.png");
    // 图标 1
    public static final Identifier Magic_icon_1 = new Identifier("magic:textures/gui/magic.png");
    // 加载所有的魔咒文本
    public static final Identifier Magic_Text_Lumos = new Identifier("magic:textures/gui/magic_text/lumos.png");
    public static final Identifier Magic_Text_Stupefy = new Identifier("magic:textures/gui/magic_text/stupefy.png");
    public static final Identifier Magic_Text_Expelliarmus = new Identifier("magic:textures/gui/magic_text/expelliarmus.png");
    public static final Identifier Magic_Text_Nox = new Identifier("magic:textures/gui/magic_text/nox.png");
    public static final Identifier Magic_Text_Protego = new Identifier("magic:textures/gui/magic_text/protego.png");

    public static final Map<String, Identifier> Magic_Dict = new HashMap<>();
    public static final Map<String, Identifier> Magic_Text_Dict = new HashMap<>();
    public static final Map<String, String[]> Magic_Text_Info_List = new HashMap<>();
    public static final Map<String,String> MagicHit_BASE_Damage_Info = new HashMap<>(); // 魔法的基础伤害
    public static final Map<String,Double> MagicHit_EXP_Damage_Info = new HashMap<>(); // 魔法的经验等级附加伤害 EXP : Level * Magic
    public static final Map<String, Integer> MagicHit_Use_Mana_Info = new HashMap<>();
    public static final DecimalFormat A_x = new DecimalFormat("#.#");

    /*
    Mana 魔法值一共有5个等级，方别为:
    200 法力值 - 学徒术士
    1000 法力值 - 资深巫师
    2000 法力值 - 高阶魔法师
    5000 法力值 - 传奇魔法师
    100000 法力值 - 至尊法师
    */

    public HudRender(){
        Magic_Dict.put("Lumos",Texture_BG_Lumos);
        Magic_Dict.put("Stupefy",Texture_BG_Stupefy);
        Magic_Dict.put("Expelliarmus",Texture_BG_Expelliarmus);
        Magic_Dict.put("Nox",Texture_BG_Nox);
        Magic_Dict.put("Protego",Texture_BG_Protego);
        // 放入魔咒文本
        Magic_Text_Dict.put("Lumos",Magic_Text_Lumos);
        Magic_Text_Dict.put("Stupefy",Magic_Text_Stupefy);
        Magic_Text_Dict.put("Expelliarmus",Magic_Text_Expelliarmus);
        Magic_Text_Dict.put("Nox",Magic_Text_Nox);
        Magic_Text_Dict.put("Protego",Magic_Text_Protego);
        // 放入咒语文本介绍
        // 最多允许4行文本，空白文本用空字符填充
        String[] Lumos_Text = {"Briefly","illuminates","darkness.","短暂照亮黑暗"};
        Magic_Text_Info_List.put("Lumos",Lumos_Text);
        String[] Stupefy_Text = {"Renders","target","unconscious","使目标失去知觉"};
        Magic_Text_Info_List.put("Stupefy",Stupefy_Text);
        String[] Expelliarmus_Text = {"Disarms","target","解除目标武装",""};
        Magic_Text_Info_List.put("Expelliarmus",Expelliarmus_Text);
        String[] Nox_Text = {"makes","wands","glow","使魔杖发光"};
        Magic_Text_Info_List.put("Nox",Nox_Text);

        String[] Protego_Text = {"Defensive","magic","shield","防御魔法"};
        Magic_Text_Info_List.put("Protego",Protego_Text);
        // 咒语命中效果
        MagicHit_BASE_Damage_Info.put("Stupefy","3");
        MagicHit_EXP_Damage_Info.put("Stupefy",0.2);
        MagicHit_Use_Mana_Info.put("Stupefy",20);

        MagicHit_BASE_Damage_Info.put("Lumos","0.5");
        MagicHit_EXP_Damage_Info.put("Lumos",0.1);
        MagicHit_Use_Mana_Info.put("Lumos",15);


        MagicHit_BASE_Damage_Info.put("Expelliarmus","1.5");
        MagicHit_EXP_Damage_Info.put("Expelliarmus",0.15);
        MagicHit_Use_Mana_Info.put("Expelliarmus",15);


        MagicHit_BASE_Damage_Info.put("Nox","0");
        MagicHit_EXP_Damage_Info.put("Nox",0.0);
        MagicHit_Use_Mana_Info.put("Nox",0);

        MagicHit_BASE_Damage_Info.put("Protego","1");
        MagicHit_EXP_Damage_Info.put("Protego",1.0);
        MagicHit_Use_Mana_Info.put("Protego",10);

    }

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!MoreMagic.ifPressed_Key_R){
            // 如果没有按下R
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                Item MainHandItem = player.getMainHandStack().getItem();
                if(MoreMagic.CanListenItemArray.contains(MainHandItem)) {
                    if(player.getAbilities().creativeMode){
                        int width = client.getWindow().getScaledWidth();
                        int height = client.getWindow().getScaledHeight();
                        context.drawTexture(Texture_BG_1,(int)width/2 - 50,height-45 - 1,90,0,0,100,22,100,22);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45 - 1,90,0,0,100,19,100,19);
                        //
                        context.drawTexture(SoltPanel,(int)width/2- 50 - 41,height-45 - 1,90,0,0,22,22,22,22);
                        context.drawItem(player,MoreMagic.BackGroundImage_Item.getDefaultStack(),(int)width/2- 50 - 37,height-42 - 1,0);


                    }else {
                        int width = client.getWindow().getScaledWidth();
                        int height = client.getWindow().getScaledHeight();
                        context.drawTexture(Texture_BG_1,(int)width/2 - 50,height-42-17 - 3,90,0,0,100,22,100,22);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45 - 15 - 2,90,0,0,100,19,100,19);

                        context.drawTexture(SoltPanel,(int)width/2- 50 - 41,height-45 - 15 - 2,90,0,0,22,22,22,22);
                        context.drawItem(player,MoreMagic.BackGroundImage_Item.getDefaultStack(),(int)width/2- 50 - 37,height-45 - 12 - 2,0);
                    }
                }
            }
        }else {
            // 如果按下R
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            Item MainHandItem = player.getMainHandStack().getItem();
            if(!MoreMagic.CanListenItemArray.contains(MainHandItem)) {
                return;
            }
            if(player.getAbilities().creativeMode){
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight() - 1;
                context.drawTexture(Texture_BG_1,(int)width/2 - 50,height-45,90,0,0,100,22,100,22);
                context.drawTexture(SearchPanel,(int)width/2 - 50,height-45-85,90,0,0,100,85,100,85);
                // 咒语项面板生成
                if ( (MoreMagic.AllMagicArray.size() - MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) - 1) >= 3 ) {
                    // 如果剩下的项大于等于4，则只加载此项之后的3个（包括此项为4个）
                    // [3]
                    context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                    // [2]
                    context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 1)),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                    // [1]
                    context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 2)),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                    // [0]
                    context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 3)),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                } else {
                    // 如果剩下的项小于4，则限制加载项
                    // 0, 1, 2, 3
                    //
                    if ((MoreMagic.AllMagicArray.size() - MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) - 1) == 2){
                        // 如果此项处在倒数第三个的位置
                        // [3]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -1)),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                        // [2]
                        context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                        // [1]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 1)),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                        // [0]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 2)),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                    }else if ((MoreMagic.AllMagicArray.size() - MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) - 1) == 1) {
                        // 如果此项后只有1个，此项处在倒数第二个
                        // [3]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -2)),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                        // [2]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -1)),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                        // [1]
                        context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                        // [0]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 1)),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                    }else {
                        //此项处在最后一个
                        // [3]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -3)),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                        // [2]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -2)),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                        // [1]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -1)),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                        // [0]
                        context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                    }

                }
                // 绘制卷轴
                int x = (int)width/2 - 40 + 95;
                int y = height-25-105 + 106;
                Matrix4f positionMatrix = context.getMatrices().peek().getPositionMatrix();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                buffer.vertex(positionMatrix, x, y - 106, 0).color(0f, 0f, 0f, 0f).texture(0f, 0f).next();
                buffer.vertex(positionMatrix, x, y, 0).color(0f, 0f, 0f, 0f).texture(0f, 1f).next();
                buffer.vertex(positionMatrix, x+90, y, 0).color(0f, 0f, 0f, 0f).texture(1f, 1f).next();
                buffer.vertex(positionMatrix, x+90, y - 106, 0).color(0f, 0f, 0f, 0f).texture(1f, 0f).next();
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderTexture(0, BookPanel);
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                tessellator.draw();
                // 绘制MagicName
                context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45,90,0,0,100,19,100,19);
                context.drawTexture(SoltPanel,(int)width/2- 50 - 41,height-45,90,0,0,22,22,22,22);
                //
                context.drawTexture(Magic_Text_Dict.get(MoreMagic.Magic_Name),
                        (int)width/2 - 40 + 95,height-15-105 - 5,90,0,0,90,100,90,100);
                context.drawItem(player,MoreMagic.BackGroundImage_Item.getDefaultStack(),(int)width/2- 50 - 37,height-42,0);
                // 绘制咒语文本
                String[] getMagicInfo = Magic_Text_Info_List.get(MoreMagic.Magic_Name);
                context.drawText(client.textRenderer,getMagicInfo[0],(int)width/2 - 40 + 95 + 10,height-15-105 + 20,0,false);
                context.drawText(client.textRenderer,getMagicInfo[1],(int)width/2 - 40 + 95 + 10,height-15-105 + 30,0,false);
                context.drawText(client.textRenderer,getMagicInfo[2],(int)width/2 - 40 + 95 + 10,height-15-105 + 40,0,false);
                context.drawText(client.textRenderer,getMagicInfo[3],(int)width/2 - 40 + 95 + 10,height-15-105 + 50,0,false);
                // 绘制咒语攻击力计算
                // 划线
                context.drawHorizontalLine((int)width/2 - 40 + 95 + 10,(int)width/2 - 40 + 95 + 75,height-15-105 + 61,0xEE111111);
                // 计算
                int damage_length = client.textRenderer.getWidth("伤害: ");
                String damage = MagicHit_BASE_Damage_Info.get(MoreMagic.Magic_Name);
                String EXP_damage =  "+"+A_x.format( player.experienceLevel*MagicHit_EXP_Damage_Info.get(MoreMagic.Magic_Name));
                context.drawText(client.textRenderer,"伤害: ",(int)width/2 - 40 + 95 + 10,height-15-105 + 65,0xFFd75ce1,false);
                //
                context.drawText(client.textRenderer,damage,(int)width/2 - 40 + 95 + 10+damage_length,height-15-105 + 65,0,false);
                context.drawText(client.textRenderer, EXP_damage,(int)width/2 - 40 + 95 + 10 + client.textRenderer.getWidth(damage)+damage_length,height-15-105 + 65,0xFF62dc4a,false);
                context.drawText(client.textRenderer, "+1",(int)width/2 - 40 + 95 + 10 + client.textRenderer.getWidth(damage)+client.textRenderer.getWidth(EXP_damage)+damage_length,
                        height-15-105 + 65,0xFF41d7ed,false);

                context.drawText(client.textRenderer,"法力值: ",(int)width/2 - 40 + 95 + 10,height-15-105 + 75,0xFFd75ce1,false);
                context.drawText(client.textRenderer,String.valueOf(MagicHit_Use_Mana_Info.get(MoreMagic.Magic_Name)),(int)width/2 - 40 + 95 + 10+client.textRenderer.getWidth("法力值: "),height-15-105 + 75,0,false);
                // String.valueOf(MagicHit_Use_Mana_Info.get(MoreMagic.Magic_Name))



            }else {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight() - 2 - 15;
                context.drawTexture(Texture_BG_1,(int)width/2 - 50,height-45,90,0,0,100,22,100,22);
                context.drawTexture(SearchPanel,(int)width/2 - 50,height-45-85,90,0,0,100,85,100,85);
                // 咒语项面板生成
                if ( (MoreMagic.AllMagicArray.size() - MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) - 1) >= 3 ) {
                    // 如果剩下的项大于等于4，则只加载此项之后的3个（包括此项为4个）
                    // [3]
                    context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                    // [2]
                    context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 1)),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                    // [1]
                    context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 2)),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                    // [0]
                    context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                    context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 3)),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                } else {
                    // 如果剩下的项小于4，则限制加载项
                    // 0, 1, 2, 3
                    //
                    if ((MoreMagic.AllMagicArray.size() - MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) - 1) == 2){
                        // 如果此项处在倒数第三个的位置
                        // [3]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -1)),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                        // [2]
                        context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                        // [1]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 1)),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                        // [0]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 2)),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                    }else if ((MoreMagic.AllMagicArray.size() - MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) - 1) == 1) {
                        // 如果此项后只有1个，此项处在倒数第二个
                        // [3]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -2)),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                        // [2]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -1)),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                        // [1]
                        context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                        // [0]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) + 1)),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                    }else {
                        //此项处在最后一个
                        // [3]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-84,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -3)),(int)width/2 - 47,height-45-86,90,0,0,100,20,100,20);
                        // [2]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-63,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -2)),(int)width/2 - 47,height-45-65,90,0,0,100,20,100,20);
                        // [1]
                        context.drawTexture(NormalPanel,(int)width/2 - 50,height-45-42,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.AllMagicArray.get(MoreMagic.AllMagicArray.indexOf(MoreMagic.Magic_Name) -1)),(int)width/2 - 47,height-45-44,90,0,0,100,20,100,20);
                        // [0]
                        context.drawTexture(SelectPanel,(int)width/2 - 50,height-45-21,90,0,0,100,20,100,20);
                        context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45-23,90,0,0,100,20,100,20);
                    }

                }
                // 绘制卷轴
                int x = (int)width/2 - 40 + 95;
                int y = height-25-105 + 106;
                Matrix4f positionMatrix = context.getMatrices().peek().getPositionMatrix();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                buffer.vertex(positionMatrix, x, y - 106, 0).color(0f, 0f, 0f, 0f).texture(0f, 0f).next();
                buffer.vertex(positionMatrix, x, y, 0).color(0f, 0f, 0f, 0f).texture(0f, 1f).next();
                buffer.vertex(positionMatrix, x+90, y, 0).color(0f, 0f, 0f, 0f).texture(1f, 1f).next();
                buffer.vertex(positionMatrix, x+90, y - 106, 0).color(0f, 0f, 0f, 0f).texture(1f, 0f).next();
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderTexture(0, BookPanel);
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                tessellator.draw();
                // 绘制MagicName
                context.drawTexture(Magic_Dict.get(MoreMagic.Magic_Name),(int)width/2 - 47,height-45,90,0,0,100,19,100,19);
                context.drawTexture(SoltPanel,(int)width/2- 50 - 41,height-45,90,0,0,22,22,22,22);
                context.drawTexture(Magic_Text_Dict.get(MoreMagic.Magic_Name),
                        (int)width/2 - 40 + 95,height-15-105,90,0,0,90,100,90,100);
                context.drawItem(player,MoreMagic.BackGroundImage_Item.getDefaultStack(),(int)width/2- 50 - 37,height-42,0);
                // 绘制咒语文本
            }
        }
    }
}