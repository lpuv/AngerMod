package dev.craftcat.angermod.events;

import dev.craftcat.angermod.AngerMod;
import dev.craftcat.angermod.Config;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;


@Mod.EventBusSubscriber(modid = AngerMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnluckyKills {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        try {
            if (!(event.getSource().getEntity() instanceof Player) || event.getSource().getEntity() instanceof FakePlayer)
                return;

            LivingEntity entity = event.getEntity();

            if (Config.explodingMobs.contains(entity.getType())) {
                Player player = (Player) event.getSource().getEntity();
                if (Config.antiExplodingItems.contains(player.getMainHandItem().getItem()))
                    return;

                Random random = new Random();

                if (random.nextDouble(1) < Config.explodingChance) {


                    Level level = player.level();

                    // check mobGriefing gamerule
                    boolean doGrief = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
                    boolean flag = (doGrief && Config.doGriefing);

                    AngerMod.LOGGER.info("[angermod] exploding mob at " + entity.position());

                    if (flag) {
                        level.explode(entity, entity.position().x, entity.position().y, entity.position().z, (float) Config.explosionRadius, Level.ExplosionInteraction.TNT);
                    } else {
                        level.explode(entity, entity.position().x, entity.position().y, entity.position().z, (float) Config.explosionRadius, Level.ExplosionInteraction.MOB);
                    }
                }

            }
        } catch (Exception e) {
            AngerMod.LOGGER.warn("LivingDeathEvent.Error",
                    "An error occoured while processing onEntityDeath.");
            AngerMod.LOGGER.warn(e.toString());
        }

    }
}
