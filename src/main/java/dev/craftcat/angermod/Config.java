package dev.craftcat.angermod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = AngerMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // range to aggro piglins
    private static final ForgeConfigSpec.IntValue PIGLIN_RANGE = BUILDER
            .comment("Range to aggro piglins")
            .defineInRange("piglinRange", 10, 0, Integer.MAX_VALUE);

    // range to aggro endermen
    private static final ForgeConfigSpec.IntValue ENDERMEN_RANGE = BUILDER
            .comment("Range to aggro endermen")
            .defineInRange("endermenRange", 10, 0, Integer.MAX_VALUE);

    // chance that the mobs explode
    private static final ForgeConfigSpec.DoubleValue EXPLODING_CHANCE = BUILDER
            .comment("Chance that specified mobs explode")
            .defineInRange("explodingChance", 0.5, 0, 1);

    // explosion radius
    private static final ForgeConfigSpec.DoubleValue EXPLOSION_RADIUS = BUILDER
            .comment("The explosion radius")
            .defineInRange("explosionRadius", 5.0, 0, Double.MAX_VALUE);

    // list of banned blocks
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_BLOCKS = BUILDER
            .comment("A list of all blocks which cause anger in mobs")
            .defineListAllowEmpty("blocks", List.of("minecraft:iron_ore"), Config::validateBlockName);

    // list of mobs that have a chance to explode on death
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXPLODING_MOBS = BUILDER
            .comment("A list of all mobs that have a chance to explode on death")
            .defineListAllowEmpty("explodingMobs", List.of("minecraft:cow"), Config::validateMobName);

    // list of items that bypass mob explosion chance
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ANTIEXPLODING_ITEMS = BUILDER
            .comment("A list of all items that bypass mob explosion chance")
            .defineListAllowEmpty("antiExplodingItems", List.of("minecraft:diamond_sword"), Config::validateItemName);

    // do mobs do griefing?
    private static final ForgeConfigSpec.BooleanValue DO_GRIEFING = BUILDER
            .comment("Do mobs grief blocks upon exploding?")
            .define("doGrief", true);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int piglinRange;
    public static int endermanRange;
    public static double explodingChance;
    public static double explosionRadius;

    public static Set<Block> blocks;
    public static Set<EntityType> explodingMobs;
    public static Set<Item> antiExplodingItems;

    public static boolean doGriefing;

    private static boolean validateBlockName(final Object obj)
    {
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }

    private static boolean validateMobName(final Object obj) {
        return obj instanceof final String mobName && ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(mobName));
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

        piglinRange = PIGLIN_RANGE.get();
        endermanRange = ENDERMEN_RANGE.get();
        explodingChance = EXPLODING_CHANCE.get();
        explosionRadius = EXPLOSION_RADIUS.get();

        // convert the list of strings into a set of blocks
        blocks = BANNED_BLOCKS.get().stream()
                .map(blockName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName)))
                .collect(Collectors.toSet());

        // convert the list of strings into a set of entities
        explodingMobs = EXPLODING_MOBS.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toSet());

        // convert the list of strings into a set of items
        antiExplodingItems = ANTIEXPLODING_ITEMS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        doGriefing = DO_GRIEFING.get();

    }
}
