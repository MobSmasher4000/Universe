package org.mob.universe.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mob.universe.Config;
import org.mob.universe.blocks.entities.ModBlockEntities;
import org.mob.universe.blocks.entities.UniverseBlockEntity;

import java.util.List;

public class UniverseBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

    public UniverseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new UniverseBlockEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof UniverseBlockEntity) {
                ((UniverseBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof UniverseBlockEntity UniverseBE) {
                if (pPlayer.isShiftKeyDown()) {
                    // SHIFT + RIGHT CLICK: Open GUI
                    NetworkHooks.openScreen((ServerPlayer) pPlayer, UniverseBE, pPos);
                } else {
                    // NORMAL RIGHT CLICK: Spawn
                    // Main Hand = Specifier (the tool)
                    ItemStack mainHand = pPlayer.getMainHandItem();
                    // Offhand = Target (the item you want)
                    ItemStack offHand = pPlayer.getOffhandItem();
                    UniverseBE.spawnDropsFromRecipe(pPlayer, mainHand,offHand);
                }
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.UNIVERSE_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Only spawn particles if the block is "Active" (optional check)
        // if (!state.getValue(WORKING_PROPERTY)) return;

        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;

        for (int i = 0; i < 4; i++) {
            // Randomize spawn offset so they don't all start in the exact center
            double offsetX = random.nextGaussian() * 0.2D;
            double offsetY = random.nextGaussian() * 0.2D;
            double offsetZ = random.nextGaussian() * 0.2D;

            level.addParticle(ParticleTypes.DRAGON_BREATH,
                    x + offsetX, y + offsetY, z + offsetZ,
                    offsetX * 0.5D, offsetY * 0.5D, offsetZ * 0.5D);
        }
    }

}
