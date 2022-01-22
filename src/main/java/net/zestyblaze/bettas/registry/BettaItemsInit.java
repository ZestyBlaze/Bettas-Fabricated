package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.config.BettasModConfig;
import net.zestyblaze.bettas.item.BettasBucketItem;

public class BettaItemsInit {

    public static final Item BETTA_FISH = new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(1).saturationModifier(0.1f).build()).group(ItemGroup.FOOD));
    public static final Item BETTA_FISH_BUCKET = new BettasBucketItem(() -> BettaEntityInit.BETTA_FISH, Fluids.WATER, Items.BUCKET, false, new FabricItemSettings().maxCount(1).group(ItemGroup.MISC));
    public static final Item BLACKWATER_BOTTLE = new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

    public static final Item BETTA_FISH_SPAWN_EGG = new SpawnEggItem(BettaEntityInit.BETTA_FISH, 0x613d2a, 0x2dceff, new FabricItemSettings().group(ItemGroup.MISC));

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(Bettas.MODID, "betta_fish"), BETTA_FISH);
        Registry.register(Registry.ITEM, new Identifier(Bettas.MODID, "betta_fish_bucket"), BETTA_FISH_BUCKET);
        Registry.register(Registry.ITEM, new Identifier(Bettas.MODID, "blackwater_bottle"), BLACKWATER_BOTTLE);

        Registry.register(Registry.ITEM, new Identifier(Bettas.MODID, "betta_fish_spawn_egg"), BETTA_FISH_SPAWN_EGG);

        Registry.register(Registry.ITEM, new Identifier(Bettas.MODID, "moss_ball"), new BlockItem(BettaBlocksInit.MOSS_BALL, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.ITEM, new Identifier(Bettas.MODID, "moss_ball_block"), new BlockItem(BettaBlocksInit.MOSS_BALL_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.ITEM, new Identifier(Bettas.MODID, "dried_leaves"), new BlockItem(BettaBlocksInit.DRIED_LEAVES, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - Items Registered");
        }
    }

}
