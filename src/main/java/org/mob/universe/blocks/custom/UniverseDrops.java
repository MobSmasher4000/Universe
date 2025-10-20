package org.mob.universe.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.mob.universe.Config;
import org.mob.universe.item.ModItems;

import java.util.List;

public class UniverseDrops {
    private static int bonus = Config.bonus + 1;
    public static void spawn(Level level, List<Item> items, Player player, BlockPos pos){
        Item drop_specific = player.getMainHandItem().getItem();
        if (!level.isClientSide()) {
            if (drop_specific == ModItems.SPECIFIER_WAND.get()){
                if (specific_drop(items,player)){
                    int drop = Config.specific_drop;
                    ItemEntity itemEntity = new ItemEntity(level,
                            pos.getX() + 0.5,
                            pos.getY() + 0.8,
                            pos.getZ() + 0.5,
                            player.getOffhandItem().copyWithCount(drop));
                    itemEntity.setDefaultPickUpDelay();
                    level.addFreshEntity(itemEntity);
                    int damage = player.getMainHandItem().getDamageValue();
                    player.getMainHandItem().setDamageValue(damage+1);
                }
            }else {
                int size = items.size();
                for (Item value : items) {
                    ItemEntity item = new ItemEntity(level,
                            pos.getX() + 0.5,
                            pos.getY() + 0.8,
                            pos.getZ() + 0.5,
                            new ItemStack(value));
                    for (int j = 1; j <= bonus; j++) {
                        item.setDefaultPickUpDelay();
                        level.addFreshEntity(item);
                    }
                }
            }
        }
    }

    private static Boolean specific_drop(List<Item> items,Player player){
        Item item = player.getOffhandItem().getItem();
        return items.contains(player.getOffhandItem().copy().getItem());
    }

}
