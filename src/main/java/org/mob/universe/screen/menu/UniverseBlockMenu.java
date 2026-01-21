package org.mob.universe.screen.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.mob.mob_lib.inventory.slot.SingleSlot;
import org.mob.mob_lib.inventory.slot.OutputSlot;
import org.mob.universe.blocks.ModBlocks;
import org.mob.universe.blocks.entities.UniverseBlockEntity;
import org.mob.universe.screen.ModMenuTypes;

public class UniverseBlockMenu extends AbstractContainerMenu {
    public final UniverseBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public UniverseBlockMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public UniverseBlockMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.UNIVERSE_BLOCK_MENU.get(), pContainerId);
        checkContainerSize(inv, 2);
        blockEntity = ((UniverseBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SingleSlot(iItemHandler, 0, 32, 52-10));
            // 1st row of output
            this.addSlot(new OutputSlot(iItemHandler, 1, 80, 16-10));
            this.addSlot(new OutputSlot(iItemHandler, 2, 98, 16-10));
            this.addSlot(new OutputSlot(iItemHandler, 3, 116, 16-10));
            this.addSlot(new OutputSlot(iItemHandler, 4, 134, 16-10));
            this.addSlot(new OutputSlot(iItemHandler, 5, 152, 16-10));
            // 2nd row of output
            this.addSlot(new OutputSlot(iItemHandler, 6, 80, 34-10));
            this.addSlot(new OutputSlot(iItemHandler, 7, 98, 34-10));
            this.addSlot(new OutputSlot(iItemHandler, 8, 116, 34-10));
            this.addSlot(new OutputSlot(iItemHandler, 9, 134, 34-10));
            this.addSlot(new OutputSlot(iItemHandler, 10, 152, 34-10));
            // 3rd row of output
            this.addSlot(new OutputSlot(iItemHandler, 11, 80, 52-10));
            this.addSlot(new OutputSlot(iItemHandler, 12, 98, 52-10));
            this.addSlot(new OutputSlot(iItemHandler, 13, 116, 52-10));
            this.addSlot(new OutputSlot(iItemHandler, 14, 134, 52-10));
            this.addSlot(new OutputSlot(iItemHandler, 15, 152, 52-10));
            // 4th row of output
            this.addSlot(new OutputSlot(iItemHandler, 16, 80, 70-10));
            this.addSlot(new OutputSlot(iItemHandler, 17, 98, 70-10));
            this.addSlot(new OutputSlot(iItemHandler, 18, 116, 70-10));
            this.addSlot(new OutputSlot(iItemHandler, 19, 134, 70-10));
            this.addSlot(new OutputSlot(iItemHandler, 20, 152, 70-10));
            // 5th row of output
            this.addSlot(new OutputSlot(iItemHandler, 21, 80, 88-10));
            this.addSlot(new OutputSlot(iItemHandler, 22, 98, 88-10));
            this.addSlot(new OutputSlot(iItemHandler, 23, 116, 88-10));
            this.addSlot(new OutputSlot(iItemHandler, 24, 134, 88-10));
            this.addSlot(new OutputSlot(iItemHandler, 25, 152, 88-10));

        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 26;  // must be the number of slots you have!
//    @Override
//    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
//        Slot sourceSlot = slots.get(pIndex);
//        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
//        ItemStack sourceStack = sourceSlot.getItem();
//        ItemStack copyOfSourceStack = sourceStack.copy();
//
//        // Check if the slot clicked is one of the vanilla container slots
//        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
//            // This is a vanilla container slot so merge the stack into the tile inventory
//            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
//                    + TE_INVENTORY_SLOT_COUNT, false)) {
//                return ItemStack.EMPTY;  // EMPTY_ITEM
//            }
//        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
//            // This is a TE slot so merge the stack into the players inventory
//            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
//                return ItemStack.EMPTY;
//            }
//        } else {
//            System.out.println("Invalid slotIndex:" + pIndex);
//            return ItemStack.EMPTY;
//        }
//        // If stack size == 0 (the entire stack was moved) set slot contents to null
//        if (sourceStack.getCount() == 0) {
//            sourceSlot.set(ItemStack.EMPTY);
//        } else {
//            sourceSlot.setChanged();
//        }
//        sourceSlot.onTake(playerIn, sourceStack);
//        return copyOfSourceStack;
//    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // 1. CLICKED IN PLAYER INVENTORY
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // CHANGE THIS: Only move to the INPUT slot (TE_INVENTORY_FIRST_SLOT_INDEX)
            // We set the 'end' index to TE_INVENTORY_FIRST_SLOT_INDEX + 1 to only include slot 0
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + 1, false)) {
                return ItemStack.EMPTY;
            }
        }
        // 2. CLICKED IN TILE ENTITY INVENTORY (Input or Output)
        else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // Move from TE slots back to player inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.UNIVERSE_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 102 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 170-10));
        }
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

}