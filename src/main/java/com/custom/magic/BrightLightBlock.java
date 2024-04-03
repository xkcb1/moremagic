package com.custom.magic;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.TickPriority;
import net.minecraft.world.tick.TickScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BrightLightBlock extends Block {
    private final int durationTicks = 20; // 光源持续的时间（以游戏tick为单位）

    public BrightLightBlock(Settings settings) {
        super(settings);
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> {
            // 在一段时间后执行的代码
            try{
                synchronized (world) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            }catch (Throwable t) {

            }
            executorService.shutdown();
            executorService.shutdownNow();

        }, 1, TimeUnit.SECONDS); // 延迟 delay 秒后执行
        // 方块被放置时的逻辑
        // 播放音效、生成粒子效果等操作
        super.onBlockAdded(state, world, pos, oldState, notify);
    }
}