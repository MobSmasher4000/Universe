package org.mob.universe.render;

import net.minecraft.resources.ResourceLocation;
import org.mob.universe.Universe;
import org.mob.universe.blocks.entities.UniverseBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class UniverseModel extends GeoModel<UniverseBlockEntity> {
    @Override
    public ResourceLocation getModelResource(UniverseBlockEntity animatable) {
        return new ResourceLocation(Universe.MOD_ID, "geo/universe_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(UniverseBlockEntity animatable) {
        return new ResourceLocation(Universe.MOD_ID, "textures/block/universe_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(UniverseBlockEntity animatable) {
        return new ResourceLocation(Universe.MOD_ID, "animations/universe_block.animation.json");
    }
}