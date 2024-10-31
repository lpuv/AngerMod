package dev.craftcat.angermod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfig;
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

    // list of banned blocks
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_BLOCKS = BUILDER
            .comment("A list of all blocks which cause anger in mobs")
            .defineListAllowEmpty("blocks", List.of("minecraft:iron_ore"), Config::validateBlockName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int piglinRange;
    public static int endermanRange;
    public static Set<Block> blocks;

    private static boolean validateBlockName(final Object obj)
    {
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

        piglinRange = PIGLIN_RANGE.get();
        endermanRange = ENDERMEN_RANGE.get();

        // convert the list of strings into a set of blocks
        blocks = BANNED_BLOCKS.get().stream()
                .map(blockName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName)))
                .collect(Collectors.toSet());
    }
}
