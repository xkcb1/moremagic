package com.custom.magic;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

class MagicBookItem_1 extends Item {
    public final ArrayList<String> ToolTipList;
    public final String ToolTip_1;

    public MagicBookItem_1(Settings settings,String ToolTip_1, ArrayList<String> ToolTipList) {
        super(settings);
        this.ToolTip_1 = ToolTip_1;
        this.ToolTipList = ToolTipList;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.ToolTip_1).formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("学习 : 主手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("施法 : 副手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("拿在副手时 + 10% 魔法攻击").formatted(Formatting.DARK_PURPLE));
        for (String item : ToolTipList) {
            tooltip.add(Text.translatable(item).formatted(Formatting.GRAY));
        }
    }
}
class MagicBookItem_2 extends Item {
    public final ArrayList<String> ToolTipList;
    public final String ToolTip_1;

    public MagicBookItem_2(Settings settings,String ToolTip_1, ArrayList<String> ToolTipList) {
        super(settings);
        this.ToolTip_1 = ToolTip_1;
        this.ToolTipList = ToolTipList;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.ToolTip_1).formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("学习 : 主手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("施法 : 副手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("拿在副手时 + 15% 魔法攻击").formatted(Formatting.DARK_PURPLE));
        for (String item : ToolTipList) {
            tooltip.add(Text.translatable(item).formatted(Formatting.GRAY));
        }
    }
}
class MagicBookItem_3 extends Item {
    public final ArrayList<String> ToolTipList;
    public final String ToolTip_1;

    public MagicBookItem_3(Settings settings,String ToolTip_1, ArrayList<String> ToolTipList) {
        super(settings);
        this.ToolTip_1 = ToolTip_1;
        this.ToolTipList = ToolTipList;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.ToolTip_1).formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("学习 : 主手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("施法 : 副手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("拿在副手时 + 20% 魔法攻击").formatted(Formatting.DARK_PURPLE));
        for (String item : ToolTipList) {
            tooltip.add(Text.translatable(item).formatted(Formatting.GRAY));
        }
    }
}
class MagicBookItem_4 extends Item {
    public final ArrayList<String> ToolTipList;
    public final String ToolTip_1;

    public MagicBookItem_4(Settings settings,String ToolTip_1, ArrayList<String> ToolTipList) {
        super(settings);
        this.ToolTip_1 = ToolTip_1;
        this.ToolTipList = ToolTipList;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.ToolTip_1).formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("学习 : 主手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("施法 : 副手右键").formatted(Formatting.WHITE));
        tooltip.add(Text.translatable("拿在副手时 + 30% 魔法攻击").formatted(Formatting.DARK_PURPLE));
        for (String item : ToolTipList) {
            tooltip.add(Text.translatable(item).formatted(Formatting.GRAY));
        }
    }
}
//LegendaryMagicBookItem
class LegendaryMagicBookItem extends Item {
    public final ArrayList<String> ToolTipList;
    public final String ToolTip_1;

    public LegendaryMagicBookItem(Settings settings,String ToolTip_1, ArrayList<String> ToolTipList) {
        super(settings);
        this.ToolTip_1 = ToolTip_1;
        this.ToolTipList = ToolTipList;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.ToolTip_1).formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("学习：主手右键").formatted(Formatting.BLUE));
        tooltip.add(Text.translatable("施法：副手右键").formatted(Formatting.BLUE));
        tooltip.add(Text.translatable("拿在副手时 + 50% 魔法攻击").formatted(Formatting.DARK_PURPLE));
        for (String item : ToolTipList) {
            tooltip.add(Text.translatable(item).formatted(Formatting.GRAY));
        }
    }
}
public class MagicBooks {
    // 学徒魔法书
    public MagicBookItem_1 Apprentice_Magic_Books;
    // 高阶魔法书
    public MagicBookItem_3 Advanced_Magic_Books;
    // 普通魔法书
    public MagicBookItem_2 Normal_Magic_Books;
    // 元素魔法书
    public MagicBookItem_2 Elemental_Magic_Books;
    // 古代魔法书
    public MagicBookItem_4 Ancient_Magic_Books;
    // 神秘魔法书
    public MagicBookItem_4 Mystical_Magic_Books;
    // 禁忌魔法书
    public MagicBookItem_4 Forbidden_Magic_Books;
    // 咒语册
    public LegendaryMagicBookItem Spellbooks;
    // 奥术文集
    public LegendaryMagicBookItem Arcane_Manuscripts;
    // 命运卷轴
    public LegendaryMagicBookItem Scrolls_of_Fate;
    // 神秘典籍
    public LegendaryMagicBookItem Mystical_Codices;
    // 奇幻魔典
    public LegendaryMagicBookItem Fantasy_Grimoires;
    // 奥秘法典
    public LegendaryMagicBookItem Esoteric_Manuals;
    // 水晶法典
    public LegendaryMagicBookItem Crystal_Codices;
    // 炼金手册
    public LegendaryMagicBookItem Alchemy_Manuals;
    // 附魔卷轴
    public LegendaryMagicBookItem Enchantment_Tomes;
    // 死灵魔法书
    public LegendaryMagicBookItem Necromancy_Books;
    // 神秘古卷
    public LegendaryMagicBookItem Mystic_Scrolls;
    // 幽冥之书
    public LegendaryMagicBookItem Book_of_Shadows;
    public MagicBooks(){
        // 学徒魔法书
        ArrayList<String> Apprentice_Magic_Books_toolTip = new ArrayList<>();
        Apprentice_Magic_Books_toolTip.add("这些书籍是年轻法师的最爱,");
        Apprentice_Magic_Books_toolTip.add("充满了基础魔法知识和简单咒语,");
        Apprentice_Magic_Books_toolTip.add("它们教导着学徒们如何点燃一束火焰,");
        Apprentice_Magic_Books_toolTip.add("召唤一阵微风，或者治愈一处轻伤。");
        Apprentice_Magic_Books = new MagicBookItem_1(new FabricItemSettings().maxCount(1),
                "学徒魔法书 (Apprentice Magic Books)",
                Apprentice_Magic_Books_toolTip);
        // 普通魔法书
        ArrayList<String> Normal_Magic_Books_toolTip = new ArrayList<>();
        Normal_Magic_Books_toolTip.add("这些书籍是大多数魔法师们的常备品,");
        Normal_Magic_Books_toolTip.add("包含了各种常见的魔法知识和技巧。");
        Normal_Magic_Books_toolTip.add("它们是魔法师们在日常生活中的指导书,");
        Normal_Magic_Books_toolTip.add("帮助他们解决各种魔法难题和挑战。");
        Normal_Magic_Books = new MagicBookItem_2(new FabricItemSettings().maxCount(1),
                "普通魔法书 (Normal Magic Books)",
                Normal_Magic_Books_toolTip);
        // 高阶魔法书
        ArrayList<String> Advanced_Magic_Books_toolTip = new ArrayList<>();
        Advanced_Magic_Books_toolTip.add("这些书籍是高阶法师们的精华之作,");
        Advanced_Magic_Books_toolTip.add("蕴含了复杂和强大的魔法知识。");
        Advanced_Magic_Books_toolTip.add("它们涵盖了各种高级咒语和魔法仪式,");
        Advanced_Magic_Books_toolTip.add("只有那些具有深厚魔法天赋的人才能够理解和施展。");
        Advanced_Magic_Books = new MagicBookItem_3(new FabricItemSettings().maxCount(1),
                "高阶魔法书 (Advanced Magic Books)",
                Advanced_Magic_Books_toolTip);
        // 元素魔法书
        ArrayList<String> Elemental_Magic_Books_toolTip = new ArrayList<>();
        Elemental_Magic_Books_toolTip.add("这些书籍探索了火焰、冰霜、闪电等不同元素属性的魔法。");
        Elemental_Magic_Books_toolTip.add("它们教导读者如何与自然元素互动，");
        Elemental_Magic_Books_toolTip.add("并掌握各种元素之间的相互作用和变化。");
        Elemental_Magic_Books = new MagicBookItem_2(new FabricItemSettings().maxCount(1),
                "元素魔法书 (Elemental Magic Books)",
                Elemental_Magic_Books_toolTip);
        // 远古魔法书
        ArrayList<String> Ancient_Magic_Books_toolTip = new ArrayList<>();
        Ancient_Magic_Books_toolTip.add("这些书籍拥有神秘而强大的魔法力量，");
        Ancient_Magic_Books_toolTip.add("通常与历史悠久的文明和传说有关。");
        Ancient_Magic_Books_toolTip.add("它们蕴含着古老的智慧和力量，");
        Ancient_Magic_Books_toolTip.add("只有那些勇敢和智慧的人才能够解开它们的秘密。");
        Ancient_Magic_Books = new MagicBookItem_4(new FabricItemSettings().maxCount(1),
                "远古魔法书 (Ancient Magic Books)",
                Ancient_Magic_Books_toolTip);
        // 神秘魔法书
        ArrayList<String> Mystical_Magic_Books_toolTip = new ArrayList<>();
        Mystical_Magic_Books_toolTip.add("这些书籍充满了神秘符号和咒语，");
        Mystical_Magic_Books_toolTip.add("可能与古老的秘密组织或神秘的神灵有关。");
        Mystical_Magic_Books_toolTip.add("它们隐藏着深不可测的力量和智慧，");
        Mystical_Magic_Books_toolTip.add("唤醒读者心中的神秘之火。");
        Mystical_Magic_Books = new MagicBookItem_4(new FabricItemSettings().maxCount(1),
                "神秘魔法书 (Mystical Magic Books)",
                Mystical_Magic_Books_toolTip);
        // 禁忌魔法书
        ArrayList<String> Forbidden_Magic_Books_toolTip = new ArrayList<>();
        Forbidden_Magic_Books_toolTip.add("这些书籍包含了强大而危险的禁忌魔法，");
        Forbidden_Magic_Books_toolTip.add("使用可能带来严重的后果。");
        Forbidden_Magic_Books_toolTip.add("它们被封印在黑暗的角落，");
        Forbidden_Magic_Books_toolTip.add("只有勇敢者或者愚蠢者才会试图破解它们的秘密。");
        Forbidden_Magic_Books = new MagicBookItem_4(new FabricItemSettings().maxCount(1),
                "禁忌魔法书 (Forbidden Magic Books)",
                Forbidden_Magic_Books_toolTip);
        // 咒语册
        ArrayList<String> Spellbooks_toolTip = new ArrayList<>();
        Spellbooks_toolTip.add("这些书籍记录了各种魔法咒语和法术，");
        Spellbooks_toolTip.add("是法师们施展魔法的必备工具。");
        Spellbooks_toolTip.add("教导读者如何念诵咒语、绘画符文，");
        Spellbooks_toolTip.add("以及释放各种奇妙的魔法效果。");
        Spellbooks = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "咒语册 (Spellbooks)",
                Spellbooks_toolTip);
        // 奥术文集
        ArrayList<String> Arcane_Manuscripts_toolTip = new ArrayList<>();
        Arcane_Manuscripts_toolTip.add("这些书籍包含了广泛而深奥的奥术知识，");
        Arcane_Manuscripts_toolTip.add("涉及到魔法学、星相学、符文学等各个方面。");
        Arcane_Manuscripts_toolTip.add("它们是法师们探索奥秘世界的重要参考资料。");
        Arcane_Manuscripts = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "奥术文集 (Arcane Manuscripts)",
                Arcane_Manuscripts_toolTip);
        // 命运卷轴
        ArrayList<String> Scrolls_of_Fate_toolTip = new ArrayList<>();
        Scrolls_of_Fate_toolTip.add("这些卷轴被认为是命运之书，");
        Scrolls_of_Fate_toolTip.add("记录了宿命和未来的预言。");
        Scrolls_of_Fate_toolTip.add("它们是神秘的预言家们和占卜师们的工具，");
        Scrolls_of_Fate_toolTip.add("揭示着人类命运的轨迹和未来的走向。");
        Scrolls_of_Fate = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "命运卷轴 (Scrolls of Fate)",
                Scrolls_of_Fate_toolTip);
        // 神秘典籍
        ArrayList<String> Mystical_Codices_toolTip = new ArrayList<>();
        Mystical_Codices_toolTip.add("这些典籍蕴含着无穷的神秘力量，");
        Mystical_Codices_toolTip.add("记录着古老的魔法仪式、神秘的咒语和奥秘的知识。");
        Mystical_Codices_toolTip.add("它们是神秘学家和奥秘探索者的宝藏，");
        Mystical_Codices_toolTip.add("引领着人们探索未知的边界。");
        Mystical_Codices = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "神秘典籍 (Mystical Codices)",
                Mystical_Codices_toolTip);
        // 奇幻魔典
        ArrayList<String> Fantasy_Grimoires_toolTip = new ArrayList<>();
        Fantasy_Grimoires_toolTip.add("这些魔典充满了奇幻的魔法和惊奇的冒险，");
        Fantasy_Grimoires_toolTip.add("记录着各种神秘生物、魔法国度和魔法物品的传说。");
        Fantasy_Grimoires_toolTip.add("它们是幻想作家和冒险家的灵感源泉，");
        Fantasy_Grimoires_toolTip.add("唤起人们内心深处的奇幻梦想。");
        Fantasy_Grimoires = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "奇幻魔典 (Fantasy Grimoires)",
                Fantasy_Grimoires_toolTip);
        // 奥秘法典
        ArrayList<String> Esoteric_Manuals_toolTip = new ArrayList<>();
        Esoteric_Manuals_toolTip.add("这些法典揭示了深奥而神秘的魔法秘密，");
        Esoteric_Manuals_toolTip.add("包含了各种高深的魔法学理论和实践技巧。");
        Esoteric_Manuals_toolTip.add("它们是奥秘学者和研究者的必备工具，");
        Esoteric_Manuals_toolTip.add("帮助他们探索魔法的本质和奥秘。");
        Esoteric_Manuals = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "奥秘法典 (Esoteric Manuals)",
                Esoteric_Manuals_toolTip);
        // 水晶法典
        ArrayList<String> Crystal_Codices_toolTip = new ArrayList<>();
        Crystal_Codices_toolTip.add("这些法典由水晶制成，");
        Crystal_Codices_toolTip.add("蕴含着神秘的水晶能量和智慧。");
        Crystal_Codices_toolTip.add("它们记录了各种水晶魔法和水晶学知识，");
        Crystal_Codices_toolTip.add("是水晶术士和水晶导师的重要指南。");
        Crystal_Codices = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "水晶法典 (Crystal Codices)",
                Crystal_Codices_toolTip);
        // 炼金手册
        ArrayList<String> Alchemy_Manuals_toolTip = new ArrayList<>();
        Alchemy_Manuals_toolTip.add("这些手册包含了炼金术的精髓和技巧，");
        Alchemy_Manuals_toolTip.add("教导读者如何炼制各种神奇的药剂、合金和魔法物品。");
        Alchemy_Manuals_toolTip.add("它们是远古炼金术士和炼金学者的宝库，");
        Alchemy_Manuals_toolTip.add("开启了人们对于物质转化的神秘探索。");
        Alchemy_Manuals = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "炼金手册 (Alchemy Manuals)",
                Alchemy_Manuals_toolTip);
        // 附魔卷轴
        ArrayList<String> Enchantment_Tomes_toolTip = new ArrayList<>();
        Enchantment_Tomes_toolTip.add("这些卷轴记录了各种附魔法术和咒语，");
        Enchantment_Tomes_toolTip.add("教导读者如何赋予物品魔法能量和特殊效果。");
        Enchantment_Tomes_toolTip.add("它们是远古附魔师和咒术师的遗留之物，");
        Enchantment_Tomes_toolTip.add("为他们的装备赋予了无尽的可能性和力量。");
        Enchantment_Tomes = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "附魔卷轴 (Enchantment Tomes)",
                Enchantment_Tomes_toolTip);
        // 死灵魔法书
        ArrayList<String> Necromancy_Books_toolTip = new ArrayList<>();
        Necromancy_Books_toolTip.add("这些书籍涉及了死灵魔法和亡灵控制的黑暗艺术，");
        Necromancy_Books_toolTip.add("记录了各种召唤亡灵、控制死灵和死灵转化的方法。");
        Necromancy_Books_toolTip.add("它们是死灵法师和黑暗术士的秘密武器，");
        Necromancy_Books_toolTip.add("带来了无穷的恐惧和力量。");
        Necromancy_Books = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "死灵魔法书 (Necromancy Books)",
                Necromancy_Books_toolTip);
        // 神秘古卷
        ArrayList<String> Mystic_Scrolls_toolTip = new ArrayList<>();
        Mystic_Scrolls_toolTip.add("这些古卷传承着神秘的力量和智慧，");
        Mystic_Scrolls_toolTip.add("记录了神秘的咒语、神秘的符号和神秘的仪式。");
        Mystic_Scrolls_toolTip.add("它们是神秘学者和占卜师的宝藏，");
        Mystic_Scrolls_toolTip.add("解开了古老的秘密和谜团。");
        Mystic_Scrolls = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "神秘古卷 (Mystic Scrolls)",
                Mystic_Scrolls_toolTip);
        // 幽冥之书
        ArrayList<String> Book_of_Shadows_toolTip = new ArrayList<>();
        Book_of_Shadows_toolTip.add("这本书是被许多女巫和巫师们使用的一种咒语书。");
        Book_of_Shadows_toolTip.add("它们通常用来记录个人的咒语、仪式、魔法实验和日常活动。");
        Book_of_Shadows = new LegendaryMagicBookItem(new FabricItemSettings().maxCount(1),
                "幽冥之书 (Book of Shadows)",
                Book_of_Shadows_toolTip);
    }

    public void register(){
        // 魔法书
        Registry.register(Registries.ITEM,new Identifier("magic","apprentice_magic_books"),Apprentice_Magic_Books);
        Registry.register(Registries.ITEM,new Identifier("magic","normal_magic_books"),Normal_Magic_Books);
        Registry.register(Registries.ITEM,new Identifier("magic","advanced_magic_books"),Advanced_Magic_Books);
        Registry.register(Registries.ITEM,new Identifier("magic","elemental_magic_books"),Elemental_Magic_Books);
        Registry.register(Registries.ITEM,new Identifier("magic","ancient_magic_books"),Ancient_Magic_Books);
        Registry.register(Registries.ITEM,new Identifier("magic","mystical_magic_books"),Mystical_Magic_Books);
        Registry.register(Registries.ITEM,new Identifier("magic","forbidden_magic_books"),Forbidden_Magic_Books);
        // 传奇魔法书
        Registry.register(Registries.ITEM,new Identifier("magic","spellbooks"),Spellbooks);
        Registry.register(Registries.ITEM,new Identifier("magic","arcane_manuscripts"),Arcane_Manuscripts);
        Registry.register(Registries.ITEM,new Identifier("magic","scrolls_of_fate"),Scrolls_of_Fate);
        Registry.register(Registries.ITEM,new Identifier("magic","mystical_codices"),Mystical_Codices);
        Registry.register(Registries.ITEM,new Identifier("magic","fantasy_grimoires"),Fantasy_Grimoires);
        Registry.register(Registries.ITEM,new Identifier("magic","esoteric_manuals"),Esoteric_Manuals);
        Registry.register(Registries.ITEM,new Identifier("magic","crystal_codices"),Crystal_Codices);
        Registry.register(Registries.ITEM,new Identifier("magic","alchemy_manuals"),Alchemy_Manuals);
        Registry.register(Registries.ITEM,new Identifier("magic","enchantment_tomes"),Enchantment_Tomes);
        Registry.register(Registries.ITEM,new Identifier("magic","necromancy_books"),Necromancy_Books);
        Registry.register(Registries.ITEM,new Identifier("magic","mystic_scrolls"),Mystic_Scrolls);
        Registry.register(Registries.ITEM,new Identifier("magic","book_of_shadows"),Book_of_Shadows);
    }
}
