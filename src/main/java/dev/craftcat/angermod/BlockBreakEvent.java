package dev.craftcat.angermod;


import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AngerMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockBreakEvent {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

        if (event.getPlayer() == null) return;
        if (event.getPlayer() instanceof FakePlayer) return;


        Block block = event.getState().getBlock();

        try {

            for (Block banned_block : Config.blocks) {
                if (block.equals(banned_block)) {
                    if (event.getPlayer().level().dimension() == Level.NETHER) {
                        aggroPiglinsInRange(event.getPlayer(), Config.piglinRange);
                    } else if (event.getPlayer().level().dimension() == Level.END) {
                        aggroEndermenInRange(event.getPlayer(), Config.endermanRange);
                    }
                }
            }
        } catch (Exception e) {
            AngerMod.LOGGER.warn("BlockBreakEvent.onBreakBlock.Error",
                    "An error occoured while processing onBreakBlock. Please report");
            AngerMod.LOGGER.warn(e.toString());
        }
    }

    private static void aggroPiglinsInRange(Player player, int range) {
        double x = player.position().x;
        double y = player.position().y;
        double z = player.position().z;

        List<Piglin> piglins = player.level().getNearbyEntities(Piglin.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().expandTowards(x + range, y + range, z + range));
        List<ZombifiedPiglin> zombifiedPiglins = player.level().getNearbyEntities(ZombifiedPiglin.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().expandTowards(x + range, y + range, z + range));


        for (Piglin piglin : piglins) {
            player.doHurtTarget(piglin);
            piglin.setTarget(player);
            piglin.setAggressive(true);
            piglin.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, player);
            piglin.getBrain().setMemory(MemoryModuleType.NEAREST_HOSTILE, player);
            piglin.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, player);
            piglin.getBrain().setMemory(MemoryModuleType.ANGRY_AT, player.getUUID());
            piglin.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
            AngerMod.LOGGER.info("angered piglin at " + piglin.position());
        }
        for (ZombifiedPiglin zombifiedPiglin : zombifiedPiglins) {
            player.doHurtTarget(zombifiedPiglin);
            zombifiedPiglin.setTarget(player);
            zombifiedPiglin.setAggressive(true);
            zombifiedPiglin.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, player);
            zombifiedPiglin.getBrain().setMemory(MemoryModuleType.NEAREST_HOSTILE, player);
            zombifiedPiglin.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, player);
            zombifiedPiglin.getBrain().setMemory(MemoryModuleType.ANGRY_AT, player.getUUID());
            zombifiedPiglin.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
            AngerMod.LOGGER.info("angered zombified piglin at " + zombifiedPiglin.position());
        }


    }

    private static void aggroEndermenInRange(Player player, int range) {
        double x = player.position().x;
        double y = player.position().y;
        double z = player.position().z;


        List<EnderMan> mobs = player.level().getNearbyEntities(EnderMan.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().expandTowards(x + range, y + range, z + range));

        for (EnderMan enderman : mobs) {
            enderman.setTarget(player);
            enderman.setAggressive(true);
        }
    }
}
