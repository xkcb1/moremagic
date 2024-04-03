package com.custom.magic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BrightLightBlockNoSchedule  extends Block {

    public BrightLightBlockNoSchedule(Settings settings) {
        super(settings);
    }
    private final int durationTicks = 20; // 光源持续的时间（以游戏tick为单位）

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
