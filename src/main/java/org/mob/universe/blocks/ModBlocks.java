package org.mob.universe.blocks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.mob.universe.Universe;
import org.mob.universe.blocks.custom.UniverseBlock;
import org.mob.universe.item.ModItems;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Universe.MOD_ID);

    public static final RegistryObject<Block> UNIVERSE_BLOCK = registerBlock("universe_block",
            ()-> new UniverseBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.universe_open"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });

    public static final RegistryObject<Block> INFERIUM_UNIVERSE = registerBlock("inferium_universe",
            ()->new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.catalyst"));
                    pTooltip.add(Component.translatable("tooltip.universe.remove"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });

    public static final RegistryObject<Block> PRUDENTIUM_UNIVERSE = registerBlock("prudentium_universe",
            ()->new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.catalyst"));
                    pTooltip.add(Component.translatable("tooltip.universe.remove"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });

    public static final RegistryObject<Block> TERTIUM_UNIVERSE = registerBlock("tertium_universe",
            ()->new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.catalyst"));
                    pTooltip.add(Component.translatable("tooltip.universe.remove"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });

    public static final RegistryObject<Block> IMPERIUM_UNIVERSE = registerBlock("imperium_universe",
            ()->new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.catalyst"));
                    pTooltip.add(Component.translatable("tooltip.universe.remove"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });

    public static final RegistryObject<Block> SUPREMIUM_UNIVERSE = registerBlock("supremium_universe",
            ()->new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.catalyst"));
                    pTooltip.add(Component.translatable("tooltip.universe.remove"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });

    public static final RegistryObject<Block> INSANIUM_UNIVERSE = registerBlock("insanium_universe",
            ()->new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.catalyst"));
                    pTooltip.add(Component.translatable("tooltip.universe.remove"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });

    public static final RegistryObject<Block> CREATIVE_UNIVERSE = registerBlock("creative_universe",
            ()->new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)){
                @Override
                public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                    pTooltip.add(Component.translatable("tooltip.universe.catalyst"));
                    pTooltip.add(Component.translatable("tooltip.universe.remove"));
                    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
                }
            });


    public static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block){
        return BLOCKS.register(name,block);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name,RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus){BLOCKS.register(eventBus);}
}