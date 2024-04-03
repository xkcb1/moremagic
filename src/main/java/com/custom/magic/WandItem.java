package com.custom.magic;

import com.custom.MoreMagic;
import com.custom.magic.ProjectileRenderer;
import com.custom.magic.armor.MagicArmor;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.logging.Logger;

import static com.custom.MoreMagic.CanListenItemArray;
import static com.custom.MoreMagic.random;

class WandToolipItem extends SwordItem {
    private final String ToolTip_1;
    private final String ToolTip_2;

    public WandToolipItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings,
                          String ToolTip_1,String ToolTip_2) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.ToolTip_1 = ToolTip_1;
        this.ToolTip_2 = ToolTip_2;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.ToolTip_1).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable(this.ToolTip_2).formatted(Formatting.GRAY));
    }
}
//WandToolipItemGold
class WandToolipItemGold extends SwordItem {
    private final String ToolTip_1;
    private final String ToolTip_2;
    private final String ToolTip_3;

    public static Map<Entity, BlockPos> NoxBlockPos = new HashMap<>();

    public WandToolipItemGold(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings,
                              String ToolTip_1,String ToolTip_2,String ToolTip_3) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.ToolTip_1 = ToolTip_1;
        this.ToolTip_2 = ToolTip_2;
        this.ToolTip_3 = ToolTip_3;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable(this.ToolTip_1).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable(this.ToolTip_2).formatted(Formatting.GOLD));
        tooltip.add(Text.translatable(this.ToolTip_3).formatted(Formatting.DARK_PURPLE));
    }

    public void NoxMagic(Entity player, World world, Hand hand){
        if (!NoxBlockPos.containsKey(player)){
            // 如果没有记录，则记录一下
            NoxBlockPos.put(player,player.getBlockPos().up());
            // System.out.println(NoxBlockPos);
        }else {
            // 如果有，则再处理一下
            if(NoxBlockPos.get(player) == player.getBlockPos().up()){
                // 如果坐标没变
                world.setBlockState(player.getBlockPos().up(),MoreMagic.BRIGHT_LIGHT_BLOCK.getDefaultState());
            }else {
                // 否则删除上一个
                world.setBlockState(NoxBlockPos.get(player), Blocks.AIR.getDefaultState());
                world.setBlockState(player.getBlockPos().up(),MoreMagic.BRIGHT_LIGHT_BLOCK.getDefaultState());
                NoxBlockPos.put(player,player.getBlockPos().up());
            }
        }
    }

    public void ProtegoMagic(PlayerEntity player, World world, Hand hand){
        Box box = player.getBoundingBox().expand(4.0D);

        // 获取区域内的所有实体，排除自身
        List<Entity> entities = world.getEntitiesByClass(Entity.class, box,entity -> entity != player);
        for (Entity entity : entities) {
            if (entity != player && (entity instanceof LivingEntity || entity instanceof ProjectileEntity) ) {
                Vec3d direction = entity.getPos().subtract(player.getPos()).normalize();

                // 计算反弹的力量
                double strength = 1; // 可以根据需要调整力量
                if (entity instanceof LivingEntity){
                    // 计算实体和爆炸中心的相对位置
                    double deltaX = entity.getX() - player.getX();
                    double deltaY = entity.getY() - player.getY();
                    double deltaZ = entity.getZ() - player.getZ();

                    // 计算施加在实体上的力的大小
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                    double force = 1.0 - distance / 5.0; // 强度随距离减小而减小
                    if (force > 0.0) {
                        entity.addVelocity(deltaX / distance * force * strength, deltaY / distance * force * strength, deltaZ / distance * force * strength);
                        entity.damage(new DamageSources(world.getRegistryManager()).create(DamageTypes.GENERIC,player), (float) (0.5F+player.experienceLevel*0.15));
                    }
                } else {

                    // 在飞行方向上应用随机偏移
                    Random random = new Random();
                    double offsetX = random.nextDouble(); // 在 [-1.0, 1.0] 范围内生成随机偏移
                    double offsetY = random.nextDouble();
                    double offsetZ = random.nextDouble();
                    Vec3d offset = new Vec3d(offsetX/1.5, offsetY/1.5, offsetZ/1.5);

                    // 将飞行方向和偏移合并
                    Vec3d modifiedDirection = direction.add(offset).normalize();

                    // 将实体向相反的方向移动，并设置速度
                    Vec3d velocity = modifiedDirection.multiply(strength);

                    ((ProjectileEntity) entity).setVelocity(-entity.getVelocity().getX()+offsetX,
                            -entity.getVelocity().getY() +offsetY,
                            -entity.getVelocity().getZ() +offsetZ,
                            2,1);
                }
                // 将实体向相反的方向移动
                ((LivingEntity) player).swingHand(hand);
                player.getItemCooldownManager().set(player.getStackInHand(hand).getItem(), 5);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        MagicEntity projectile;
        if (Objects.equals(MoreMagic.Magic_Name, "Nox")){
            NoxMagic(user, world, hand);
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        if (Objects.equals(MoreMagic.Magic_Name, "Protego")){
            ProtegoMagic(user, world, hand);
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        if (Objects.equals(MoreMagic.Magic_Name, "Lumos")) {
            projectile = new MagicEntity(world, user,true);
        }else {
            projectile = new MagicEntity(world, user);
        }
        // 设置发射物的位置和速度
        Vec3d look = user.getRotationVec(1.0f);
        projectile.setPos(user.getX() + look.x, user.getEyeY() + look.y, user.getZ() + look.z);
        if (Objects.equals(MoreMagic.Magic_Name, "Lumos")) {
            projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.7F, 0F);
        }
        else {
            projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3.5F, 0F);
        }
        ProjectileRenderer.renderProjectile(projectile,user);
        Identifier soundId = new Identifier("minecraft", "entity.ghast.shoot");
        // 获取声音事件对象
        SoundEvent soundEvent = SoundEvent.of(soundId);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
        // 将发射物添加到世界中
        ((LivingEntity) user).swingHand(hand);
        user.getItemCooldownManager().set(user.getStackInHand(hand).getItem(), 15);
        world.spawnEntity(projectile);
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return this.isFood() ? user.eatFood(world, stack) : stack;
    }
    @Override
    public void onCraft(ItemStack stack, World world) {
        Logger.getLogger("w").info("???");
    }

}
class WandMaterial implements ToolMaterial {
    @Override
    public int getDurability() {
        return 0;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 0;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
};

public class WandItem {
    // 橡木
    public WandToolipItem NoCore_long_wand;
    public WandToolipItem NoCore_oak_wand;
    public WandToolipItem NoCore_birch_wand;
    public WandToolipItem NoCore_acacia_wand;
    public WandToolipItem NoCore_dark_oak_wand;
    public WandToolipItem NoCore_jungle_wand;
    public WandToolipItem NoCore_spruce_wand;
    public Item MagicGroupIcon;
    public ItemGroup MagicItemGroup;
    // (1) 金质杖芯
    public WandToolipItemGold GoldCore_long_wand;
    public WandToolipItemGold GoldCore_oak_wand;
    public WandToolipItemGold GoldCore_birch_wand;
    public WandToolipItemGold GoldCore_acacia_wand;
    public WandToolipItemGold GoldCore_dark_oak_wand;
    public WandToolipItemGold GoldCore_jungle_wand;
    public WandToolipItemGold GoldCore_spruce_wand;

    public MagicBooks MagicBooksItem;

    public Item MagicBall;

    public ArrayList<WandToolipItemGold> GoldWand = new ArrayList<>();


    //(2)紫水晶杖芯

    public WandItem(){
        // 橡木
        NoCore_long_wand = new WandToolipItem(new WandMaterial(),1,0.5F,
                new FabricItemSettings().maxCount(1),"长法杖","魔杖杖芯 : 无杖芯");

        NoCore_oak_wand = new WandToolipItem(new WandMaterial(),1,0.5F,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 橡木","魔杖杖芯 : 无杖芯");
        // 白桦木
        NoCore_birch_wand = new WandToolipItem(new WandMaterial(),1,0.5F,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 白桦木","魔杖杖芯 : 无杖芯");
        // 金合欢木
        NoCore_acacia_wand = new WandToolipItem(new WandMaterial(),1,0.5F,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 金合欢木","魔杖杖芯 : 无杖芯");
        // 黑橡木
        NoCore_dark_oak_wand = new WandToolipItem(new WandMaterial(),1,0.5F,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 黑橡木","魔杖杖芯 : 无杖芯");
        // 丛林木
        NoCore_jungle_wand = new WandToolipItem(new WandMaterial(),1,0.5F,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 丛林木","魔杖杖芯 : 无杖芯");
        // 云杉木
        NoCore_spruce_wand = new WandToolipItem(new WandMaterial(),1,0.5F,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 云杉木","魔杖杖芯 : 无杖芯");
        // 物品组图标
        MagicGroupIcon = new Item(new FabricItemSettings().maxCount(1));

        // 金质杖芯
        GoldCore_long_wand = new WandToolipItemGold(new WandMaterial(),5,0,
                new FabricItemSettings().maxCount(1),"长法杖","魔杖杖芯 : 金杖芯","近战攻击 + 15% 闪电附加");

        GoldCore_oak_wand = new WandToolipItemGold(new WandMaterial(),3,0,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 橡木","魔杖杖芯 : 金杖芯","近战攻击 + 15% 闪电附加");
        // 白桦木
        GoldCore_birch_wand = new WandToolipItemGold(new WandMaterial(),3,0,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 白桦木","魔杖杖芯 : 金杖芯","近战攻击 + 15% 闪电附加");
        // 金合欢木
        GoldCore_acacia_wand = new WandToolipItemGold(new WandMaterial(),3,0,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 金合欢木","魔杖杖芯 : 金杖芯","近战攻击 + 15% 闪电附加");
        // 黑橡木
        GoldCore_dark_oak_wand = new WandToolipItemGold(new WandMaterial(),3,0,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 黑橡木","魔杖杖芯 : 金杖芯","近战攻击 + 15% 闪电附加");
        // 丛林木
        GoldCore_jungle_wand = new WandToolipItemGold(new WandMaterial(),3,0,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 丛林木","魔杖杖芯 : 金杖芯","近战攻击 + 15% 闪电附加");
        // 云杉木
        GoldCore_spruce_wand = new WandToolipItemGold(new WandMaterial(),3,0,
                new FabricItemSettings().maxCount(1),"魔杖杖身 : 云杉木","魔杖杖芯 : 金杖芯","近战攻击 + 15% 闪电附加");

        MagicBall = new Item(new FabricItemSettings().maxCount(1));

        MagicBooksItem = new MagicBooks();

        // 金魔杖列表
        GoldWand.add(GoldCore_long_wand);
        GoldWand.add(GoldCore_oak_wand);
        GoldWand.add(GoldCore_birch_wand);
        GoldWand.add(GoldCore_acacia_wand);
        GoldWand.add(GoldCore_dark_oak_wand);
        GoldWand.add(GoldCore_jungle_wand);
        GoldWand.add(GoldCore_spruce_wand);

        // 注册物品组
        MagicItemGroup = FabricItemGroup.builder()
                .icon(() -> new ItemStack(MagicGroupIcon))
                .displayName(Text.translatable("魔法师"))
                .entries((context, entries) -> {
                    entries.add(MagicBooksItem.Apprentice_Magic_Books);
                    entries.add(MagicBooksItem.Normal_Magic_Books);
                    entries.add(MagicBooksItem.Elemental_Magic_Books);
                    entries.add(MagicBooksItem.Advanced_Magic_Books);
                    entries.add(MagicBooksItem.Ancient_Magic_Books);
                    entries.add(MagicBooksItem.Forbidden_Magic_Books);
                    entries.add(MagicBooksItem.Mystical_Magic_Books);
                    entries.add(MagicBooksItem.Book_of_Shadows);
                    entries.add(MagicBooksItem.Alchemy_Manuals);
                    entries.add(MagicBooksItem.Arcane_Manuscripts);
                    entries.add(MagicBooksItem.Crystal_Codices);
                    entries.add(MagicBooksItem.Enchantment_Tomes);
                    entries.add(MagicBooksItem.Esoteric_Manuals);
                    entries.add(MagicBooksItem.Fantasy_Grimoires);
                    entries.add(MagicBooksItem.Mystic_Scrolls);
                    entries.add(MagicBooksItem.Mystical_Codices);
                    entries.add(MagicBooksItem.Necromancy_Books);
                    entries.add(MagicBooksItem.Scrolls_of_Fate);
                    entries.add(MagicBooksItem.Spellbooks);
                    entries.add(NoCore_oak_wand);
                    entries.add(NoCore_birch_wand);
                    entries.add(NoCore_acacia_wand);
                    entries.add(NoCore_dark_oak_wand);
                    entries.add(NoCore_jungle_wand);
                    entries.add(NoCore_spruce_wand);
                    entries.add(MagicArmor.CUSTOM_HELMET_Leather);
                    entries.add(MagicArmor.CUSTOM_CHESTPLATE_Leather);
                    entries.add(MagicArmor.CUSTOM_LEGGINGS_Leather);
                    entries.add(MagicArmor.CUSTOM_BOOTS_Leather);
                })
                .build();
    }
    public void register(){
        // 无杖芯魔杖
        Registry.register(Registries.ITEM,new Identifier("magic","long_wand"),NoCore_long_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","no_core_oak_wand"),NoCore_oak_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","no_core_birch_wand"),NoCore_birch_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","no_core_acacia_wand"),NoCore_acacia_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","no_core_dark_oak_wand"),NoCore_dark_oak_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","no_core_jungle_wand"),NoCore_jungle_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","no_core_spruce_wand"),NoCore_spruce_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","icon"),MagicGroupIcon);
        // 金杖芯
        Registry.register(Registries.ITEM,new Identifier("magic","gold_core_long_wand"),GoldCore_long_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","gold_core_oak_wand"),GoldCore_oak_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","gold_core_birch_wand"),GoldCore_birch_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","gold_core_acacia_wand"),GoldCore_acacia_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","gold_core_dark_oak_wand"),GoldCore_dark_oak_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","gold_core_jungle_wand"),GoldCore_jungle_wand);
        Registry.register(Registries.ITEM,new Identifier("magic","gold_core_spruce_wand"),GoldCore_spruce_wand);
        // 物品组
        Registry.register(Registries.ITEM_GROUP,new Identifier("magic","item"),MagicItemGroup);

        Registry.register(Registries.ITEM,new Identifier("magic","magic_ball_item"),MagicBall);
        MagicBooksItem.register();
        MagicArmor.registerItems();

        CanListenItemArray.add(GoldCore_oak_wand);
        CanListenItemArray.add(GoldCore_birch_wand);
        CanListenItemArray.add(GoldCore_acacia_wand);
        CanListenItemArray.add(GoldCore_dark_oak_wand);
        CanListenItemArray.add(GoldCore_jungle_wand);
        CanListenItemArray.add(GoldCore_spruce_wand);
        // MagicBooksItem
        CanListenItemArray.add(MagicBooksItem.Apprentice_Magic_Books);
        CanListenItemArray.add(MagicBooksItem.Normal_Magic_Books);
        CanListenItemArray.add(MagicBooksItem.Elemental_Magic_Books);
        CanListenItemArray.add(MagicBooksItem.Advanced_Magic_Books);
        CanListenItemArray.add(MagicBooksItem.Ancient_Magic_Books);
        CanListenItemArray.add(MagicBooksItem.Forbidden_Magic_Books);
        CanListenItemArray.add(MagicBooksItem.Mystical_Magic_Books);
        CanListenItemArray.add(MagicBooksItem.Book_of_Shadows);
        CanListenItemArray.add(MagicBooksItem.Alchemy_Manuals);
        CanListenItemArray.add(MagicBooksItem.Arcane_Manuscripts);
        CanListenItemArray.add(MagicBooksItem.Crystal_Codices);
        CanListenItemArray.add(MagicBooksItem.Enchantment_Tomes);
        CanListenItemArray.add(MagicBooksItem.Esoteric_Manuals);
        CanListenItemArray.add(MagicBooksItem.Fantasy_Grimoires);
        CanListenItemArray.add(MagicBooksItem.Mystic_Scrolls);
        CanListenItemArray.add(MagicBooksItem.Mystical_Codices);
        CanListenItemArray.add(MagicBooksItem.Necromancy_Books);
        CanListenItemArray.add(MagicBooksItem.Scrolls_of_Fate);
        CanListenItemArray.add(MagicBooksItem.Spellbooks);
    }
}
