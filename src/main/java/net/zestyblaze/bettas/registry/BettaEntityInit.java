package net.zestyblaze.bettas.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.zestyblaze.bettas.Bettas;
import net.zestyblaze.bettas.config.BettasModConfig;
import net.zestyblaze.bettas.entity.BettaFishEntity;

@SuppressWarnings("ALL")
public class BettaEntityInit {

    public static final EntityType<BettaFishEntity> BETTA_FISH = Registry.register(Registry.ENTITY_TYPE, new Identifier(Bettas.MODID, "betta_fish"), FabricEntityTypeBuilder.create(SpawnGroup.WATER_AMBIENT, BettaFishEntity::new).dimensions(EntityDimensions.fixed(0.3f, 0.3f)).build());

    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(BETTA_FISH, BettaFishEntity.createAttributes());

        if (BettasModConfig.get().debugMode) {
            Bettas.LOGGER.info("Bettas: Registry - Entities Registered");
        }
    }

}
