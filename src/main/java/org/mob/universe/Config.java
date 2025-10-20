package org.mob.universe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
@Mod.EventBusSubscriber(modid = Universe.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue BONUS = BUILDER
            .comment("Bonus rolls.")
            .defineInRange("Bonus Rolls", 0, 0, 20);

    private static final ForgeConfigSpec.IntValue SPECIFIC_DROP = BUILDER
            .comment("amount of drops using the specifier.")
            .defineInRange("specifier drops", 1, 1, 64);


//     a list of strings that are treated as resource locations for items
//    inferium universe
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> INFERIUM_STRINGS = BUILDER
            .comment("A list of items to drop from universe.")
            .defineListAllowEmpty("Inferium Contained Universe", List.of("minecraft:iron_ingot"), Config::validateItemName);

//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> INFERIUM_WEIGHT = BUILDER
//            .defineListAllowEmpty("Inferium universe weight",List.of(2,3,4),Config::validateWeightList);

//    prudentium universe
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PRUDENTIUM_STRINGS = BUILDER
            .defineListAllowEmpty("Prudentium Contained Universe", List.of("minecraft:iron_ingot"), Config::validateItemName);

//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> PRUDENTIUM_WEIGHT = BUILDER
//            .defineListAllowEmpty("Prudentium universe weight",List.of(2,3,4),Config::validateWeightList);

//    tertium universe
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TERTIUM_STRINGS = BUILDER
            .defineListAllowEmpty("Tertium Contained Universe", List.of("minecraft:iron_ingot"), Config::validateItemName);

//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> TERTIUM_WEIGHT = BUILDER
//            .defineListAllowEmpty("Tertium universe weight",List.of(2,3,4),Config::validateWeightList);

//    imperium universe
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> IMPERIUM_STRINGS = BUILDER.comment()
            .defineListAllowEmpty("Imperium Contained Universe", List.of("minecraft:iron_ingot"), Config::validateItemName);

//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> IMPERIUM_WEIGHT = BUILDER
//            .defineListAllowEmpty("Imperium universe weight",List.of(2,3,4),Config::validateWeightList);

//    supremium universe
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SUPREMIUM_STRINGS = BUILDER.comment()
            .defineListAllowEmpty("Supremium Contained Universe", List.of("minecraft:iron_ingot"), Config::validateItemName);

//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> SUPREMIUM_WEIGHT = BUILDER
//            .defineListAllowEmpty("Supremium universe weight",List.of(2,3,4),Config::validateWeightList);

//    Insanium universe
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> INSANIUM_STRINGS = BUILDER.comment()
            .defineListAllowEmpty("Insanium Contained Universe", List.of("minecraft:iron_ingot"), Config::validateItemName);

//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> INSANIUM_WEIGHT = BUILDER
//            .defineListAllowEmpty("Insanium universe weight",List.of(2,3,4),Config::validateWeightList);

//    creative universe
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> CREATIVE_STRINGS = BUILDER.comment()
            .defineListAllowEmpty("Creative Contained Universe", List.of("minecraft:iron_ingot"), Config::validateItemName);

//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> CREATIVE_WEIGHT = BUILDER
//            .defineListAllowEmpty("Creative universe weight",List.of(2,3,4),Config::validateWeightList);

    static final ForgeConfigSpec SPEC = BUILDER.build();
//
//    public static boolean logDirtBlock;
//    public static int magicNumber;
//    public static String magicNumberIntroduction;
//    public static Set<Item> items;

    public static Set<Item> inferium;
    public static Set<Item> prudentium;
    public static Set<Item> tertium;
    public static Set<Item> imperium;
    public static Set<Item> supremium;
    public static Set<Item> insanium;
    public static Set<Item> creative;
    public static int bonus;
    public static int specific_drop;
    public static Set<Integer> inferium_weight;
    public static Set<Integer> prudentium_weight;
    public static Set<Integer> tertium_weight;
    public static Set<Integer> imperium_weight;
    public static Set<Integer> supremium_weight;
    public static Set<Integer> insanium_weight;
    public static Set<Integer> creative_weight;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    private static boolean validateWeightList(final Object obj){
        return true;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
//         convert the list of strings into a set of items
//        items = ITEM_STRINGS.get().stream()
//                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
//                .collect(Collectors.toSet());
        inferium = INFERIUM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        prudentium = PRUDENTIUM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        tertium = TERTIUM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        imperium = IMPERIUM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        supremium = SUPREMIUM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        insanium = INSANIUM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        creative = CREATIVE_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

        bonus = BONUS.get();
        specific_drop = SPECIFIC_DROP.get();

//        inferium_weight = new HashSet<>(INFERIUM_WEIGHT.get());
//        prudentium_weight = new HashSet<>(PRUDENTIUM_WEIGHT.get());
//        tertium_weight = new HashSet<>(TERTIUM_WEIGHT.get());
//        imperium_weight = new HashSet<>(IMPERIUM_WEIGHT.get());
//        supremium_weight = new HashSet<>(SUPREMIUM_WEIGHT.get());
//        insanium_weight = new HashSet<>(INSANIUM_WEIGHT.get());
//        creative_weight = new HashSet<>(CREATIVE_WEIGHT.get());
    }
}
