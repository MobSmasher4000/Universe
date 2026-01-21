package org.mob.universe.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.mob.universe.item.ModItems;
import org.mob.universe.recipe.UniverseRecipe;
import org.mob.universe.screen.menu.UniverseBlockMenu;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;

public class UniverseBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean isCrafting = false;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Controller 1: The Ring (Always Playing)
        controllers.add(new AnimationController<>(this, "ring_controller", 0, state -> {
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.universe.ring"));
        }));

        // Controller 2: The Center (Only if the machine is currently active)
        controllers.add(new AnimationController<>(this, "center_controller", 40, state -> {
            // Check if the machine is currently active
            if (this.data.get(2) == 1) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.universe.center"));
            }
            return PlayState.STOP;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(64){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (slot == INPUT_SLOT) {
                updateRecipeCache();
            }
            setChanged();
        }
    };

    private UniverseRecipe cachedRecipe;

    private static final int INPUT_SLOT = 0;
    private static final int[] OUTPUT_SLOT = IntStream.rangeClosed(1,63).toArray();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 20;

    public UniverseBlockEntity(BlockPos pos, BlockState state){
        super(ModBlockEntities.UNIVERSE_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> UniverseBlockEntity.this.progress;
                    case 1 -> UniverseBlockEntity.this.maxProgress;
                    case 2 -> UniverseBlockEntity.this.isCrafting ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> UniverseBlockEntity.this.progress = pValue;
                    case 1 -> UniverseBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    // Helper to refresh the cache
    private void updateRecipeCache() {
        if (this.level == null || this.level.isClientSide) return;

        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, this.itemHandler.getStackInSlot(INPUT_SLOT));

        this.cachedRecipe = this.level.getRecipeManager()
                .getRecipeFor(UniverseRecipe.Type.INSTANCE, inventory, level)
                .orElse(null);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.universe.universe_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new UniverseBlockMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("universe.progress", progress);
        pTag.putBoolean("universe.is_crafting", isCrafting);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("universe.progress");
        isCrafting = pTag.getBoolean("universe.is_crafting");
    }

    // Update cache whenever the item in slot 0 changes
    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.cachedRecipe = getCurrentRecipe().orElse(null);
        }
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide()) return;

        boolean wasCrafting = this.isCrafting;
        this.isCrafting = !itemHandler.getStackInSlot(INPUT_SLOT).isEmpty() && getCurrentRecipe().isPresent();

        // Only sync when the machine actually starts or stops
        if (wasCrafting != this.isCrafting) {
            pLevel.sendBlockUpdated(pPos, pState, pState, 3);
        }

        // Initialize cache if it somehow stayed null
        if (cachedRecipe == null && !itemHandler.getStackInSlot(INPUT_SLOT).isEmpty()) {
            updateRecipeCache();
        }

        if (cachedRecipe != null) {
            increaseCraftingProgress();
            setChanged();

            if (hasProgressFinished()) {
                Optional<UniverseRecipe> recipe = getCurrentRecipe();
                craftItem(recipe.get());
                resetProgress();
            }
        } else {
            resetProgress();
        }

        exportToBelow();
    }

    private void resetProgress() {
        progress = 0;
    }

    private Optional<UniverseRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        // Looks for the recipe of type "universe"
        return this.level.getRecipeManager()
                .getRecipeFor(UniverseRecipe.Type.INSTANCE, inventory, level);
    }

    private void craftItem(UniverseRecipe recipe) {
        // It's a permanent catalyst
        // this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        for (UniverseRecipe.UniverseDrop drop : recipe.getDrops()) {
            // Check the chance (e.g., 0.15 for 15%)
            if (this.level.random.nextDouble() <= drop.chance()) {

                // Calculate roll count
                int count = drop.minRolls();
                if (drop.maxRolls() > drop.minRolls()) {
                    count += this.level.random.nextInt(drop.maxRolls() - drop.minRolls() + 1);
                }

                ItemStack stackToInsert = drop.stack().copy();
                stackToInsert.setCount(count);

                // Insert into the output slots (1 to 25)
                exportItem(stackToInsert);
            }
        }
    }

    private void exportItem(ItemStack stack) {
        // Iterate through slots 1 to 25 to find space
        for (int i : OUTPUT_SLOT) {
            stack = itemHandler.insertItem(i, stack, false);
            if (stack.isEmpty()) break; // Item fully inserted
        }
    }

    private boolean hasRecipe() {
        Optional<UniverseRecipe> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        // Check if there is room for at least one potential drop
        return IntStream.of(OUTPUT_SLOT).anyMatch(slot ->
                itemHandler.getStackInSlot(slot).getCount() < itemHandler.getStackInSlot(slot).getMaxStackSize()
        );
    }

    private void exportToBelow() {
        if (this.level == null || this.level.isClientSide) return;

        // 1. Get the capability of the block below (Direction.DOWN)
        BlockEntity belowBe = level.getBlockEntity(worldPosition.below());
        if (belowBe == null) return;

        belowBe.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).ifPresent(belowHandler -> {
            // 2. Loop through your OUTPUT slots (1 to 25)
            for (int i : OUTPUT_SLOT) {
                ItemStack stackInSlot = itemHandler.getStackInSlot(i);

                if (!stackInSlot.isEmpty()) {
                    // 3. Try to simulate putting the item into the block below
                    ItemStack remainder = ItemHandlerHelper.insertItemStacked(belowHandler, stackInSlot, true);

                    // 4. If any items were successfully "accepted" in the simulation
                    if (remainder.getCount() < stackInSlot.getCount()) {
                        int amountToMove = stackInSlot.getCount() - remainder.getCount();

                        // Actually extract from your internal inventory
                        ItemStack extracted = itemHandler.extractItem(i, amountToMove, false);

                        // Actually insert into the storage below
                        ItemHandlerHelper.insertItemStacked(belowHandler, extracted, false);

                        // Optional: Only move one stack per tick to be performance friendly
                        // break;
                    }
                }
            }
        });
    }

    public void spawnDropsFromRecipe(Player player, ItemStack mainHand, ItemStack offHand) {
        if (this.level == null || this.level.isClientSide || cachedRecipe == null) return;

        // 1. Check if the player is holding the correct tool to perform a "Forced Extraction"
        boolean isUsingSpecifier = mainHand.is(ModItems.SPECIFIER_WAND.get());
        boolean hasTargetInOffhand = !offHand.isEmpty();

        if (isUsingSpecifier && hasTargetInOffhand) {
            // --- FORCED EXTRACTION MODE ---
            // Verify the item in offhand is actually a possible drop from this recipe
            boolean isValidTarget = cachedRecipe.getDrops().stream()
                    .anyMatch(drop -> drop.stack().is(offHand.getItem()));

            if (isValidTarget) {
                ItemStack forcedDrop = offHand.copy();
                forcedDrop.setCount(1); // Exactly 1 roll, 100% chance

                Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.1, worldPosition.getZ() + 0.5, forcedDrop);

                // Apply durability damage to the specifier tool
                mainHand.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            }
        } else {
            // --- DEFAULT RANDOM MODE ---
            // Runs if: Not holding a specifier OR offhand is empty
            for (UniverseRecipe.UniverseDrop drop : cachedRecipe.getDrops()) {
                if (this.level.random.nextDouble() <= drop.chance()) {
                    int count = drop.minRolls();
                    if (drop.maxRolls() > drop.minRolls()) {
                        count += this.level.random.nextInt(drop.maxRolls() - drop.minRolls() + 1);
                    }

                    ItemStack stackToSpawn = drop.stack().copy();
                    stackToSpawn.setCount(count);
                    Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.1, worldPosition.getZ() + 0.5, stackToSpawn);
                }
            }
        }
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    // 1. Send data to the client when they arrive at the block
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt); // This ensures 'progress' is included
        return nbt;
    }

    // 2. Receive the data on the client side
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    // 3. Sync data whenever the block is loaded or changes
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
