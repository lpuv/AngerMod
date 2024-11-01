package dev.craftcat.angermod.events;

import dev.craftcat.angermod.AngerMod;
import dev.craftcat.angermod.Config;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber(modid = AngerMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerUseEvent {

    @SubscribeEvent
    public static void onPlayerUse(LivingEntityUseItemEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getEntity() instanceof FakePlayer)
            return;

        Item heldItem = event.getItem().getItem();
        Player player = (Player) event.getEntity();

        if (Config.foodPigTrigger.contains(heldItem)) {
            double x = player.position().x;
            double y = player.position().y;
            double z = player.position().z;

            List<Pig> pigs = player.level().getNearbyEntities(Pig.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().expandTowards(x + Config.foodRange, y + Config.foodRange, z + Config.foodRange));
            for (Pig pig : pigs) {
                AngerMod.LOGGER.info("[angermod] hurting pig at " + pig.position());
                pig.hurt(pig.damageSources().playerAttack(player), 0);
            }

            List<Cow> cows = player.level().getNearbyEntities(Cow.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().expandTowards(x + Config.foodRange, y + Config.foodRange, z + Config.foodRange));
            for (Cow cow : cows) {
                AngerMod.LOGGER.info("[angermod] hurting cow at " + cow.position());
                cow.hurt(cow.damageSources().playerAttack(player), 0);
            }

            List<Chicken> chickens = player.level().getNearbyEntities(Chicken.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().expandTowards(x + Config.foodRange, y + Config.foodRange, z + Config.foodRange));
            for (Chicken chicken : chickens) {
                AngerMod.LOGGER.info("[angermod] hurting chicken at " + chicken.position());
                chicken.hurt(chicken.damageSources().playerAttack(player), 0);
            }

            List<Sheep> sheeps = player.level().getNearbyEntities(Sheep.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().expandTowards(x + Config.foodRange, y + Config.foodRange, z + Config.foodRange));
            for (Sheep sheep : sheeps) {
                AngerMod.LOGGER.info("[angermod] hurting sheep at " + sheep.position());
                sheep.hurt(sheep.damageSources().playerAttack(player), 0);
            }
        }


    }


}
