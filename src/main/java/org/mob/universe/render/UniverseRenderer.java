package org.mob.universe.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.mob.universe.blocks.entities.UniverseBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class UniverseRenderer extends GeoBlockRenderer<UniverseBlockEntity> {
    public UniverseRenderer(BlockEntityRendererProvider.Context context) {
        super(new UniverseModel());
    }
}