package com.raptor.sdu;

import static com.jaquadro.minecraft.storagedrawers.core.ModBlocks.*;
import static com.raptor.sdu.StorageDrawersUnlimited.*;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.raptor.sdu.type.StreamableIterable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;

public class SDUBlocks {
	
	@EventBusSubscriber(modid = StorageDrawersUnlimited.MODID, bus = Bus.MOD)
	public static class Registration {
		
		private static final Logger logger = LogManager.getLogger(SDUBlocks.Registration.class);
		
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			logger.info("Loading storage drawers for " + ENABLED_MODS.stream().count() + " mods");
			IForgeRegistry<Block> registry = event.getRegistry();
			
			int count = 0;
			for(Block block : wrapInIdentityHashSet(BLOCKS)) {
				registry.register(block);
				count++;
			}
			logger.info("Loaded " + count + " storage drawer(s)");
		}
		
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
			
			reregisterTileEntity(event, StorageDrawers.MOD_ID, "standard_drawers_1", TileEntityDrawersStandard.Slot1::new, 
					Streams.concat(
							DRAWERS_FULL_1.stream(), 
							DRAWERS_HALF_1.stream(), 
							Stream.of(
								OAK_FULL_DRAWERS_1, 
								OAK_HALF_DRAWERS_1,
								SPRUCE_FULL_DRAWERS_1,
								SPRUCE_HALF_DRAWERS_1,
								BIRCH_FULL_DRAWERS_1,
								BIRCH_HALF_DRAWERS_1,
								JUNGLE_FULL_DRAWERS_1,
								JUNGLE_HALF_DRAWERS_1,
								ACACIA_FULL_DRAWERS_1,
								ACACIA_HALF_DRAWERS_1,
								DARK_OAK_FULL_DRAWERS_1,
								DARK_OAK_HALF_DRAWERS_1
							)
					));
			
			reregisterTileEntity(event, StorageDrawers.MOD_ID, "standard_drawers_2", TileEntityDrawersStandard.Slot2::new, 
					Streams.concat(
							DRAWERS_FULL_2.stream(), 
							DRAWERS_HALF_2.stream(),
							Stream.of(
								OAK_FULL_DRAWERS_2,
								OAK_HALF_DRAWERS_2,
								SPRUCE_FULL_DRAWERS_2,
								SPRUCE_HALF_DRAWERS_2,
								BIRCH_FULL_DRAWERS_2,
								BIRCH_HALF_DRAWERS_2,
								JUNGLE_FULL_DRAWERS_2,
								JUNGLE_HALF_DRAWERS_2,
								ACACIA_FULL_DRAWERS_2,
								ACACIA_HALF_DRAWERS_2,
								DARK_OAK_FULL_DRAWERS_2,
								DARK_OAK_HALF_DRAWERS_2
							)
					));
			
			reregisterTileEntity(event, StorageDrawers.MOD_ID, "standard_drawers_4", TileEntityDrawersStandard.Slot4::new, 
					Streams.concat(
							DRAWERS_FULL_4.stream(), 
							DRAWERS_HALF_4.stream(),
							Stream.of(
								OAK_FULL_DRAWERS_4,
				                OAK_HALF_DRAWERS_4,
				                SPRUCE_FULL_DRAWERS_4,
				                SPRUCE_HALF_DRAWERS_4,
				                BIRCH_FULL_DRAWERS_4,
				                BIRCH_HALF_DRAWERS_4,
				                JUNGLE_FULL_DRAWERS_4,
				                JUNGLE_HALF_DRAWERS_4,
				                ACACIA_FULL_DRAWERS_4,
				                ACACIA_HALF_DRAWERS_4,
				                DARK_OAK_FULL_DRAWERS_4,
				                DARK_OAK_HALF_DRAWERS_4
							)
					));
		}

		// reregisters a tile entity defined by Storage Drawers
		private static <T extends TileEntity> void reregisterTileEntity(RegistryEvent.Register<TileEntityType<?>> event, String modid, String name, Supplier<? extends T> factory, Stream<? extends Block> blocks) {
			TileEntityType<?> type = event.getRegistry().getValue(new ResourceLocation(modid, name));
			int oldSize = type.validBlocks.size();
			type.validBlocks = ImmutableSet.copyOf(Stream.concat(type.validBlocks.stream(), blocks).collect(Collectors.toSet()));
			logger.info("reregistering tile entity " + modid + ":" + name + " with " + type.validBlocks.size() + " blocks (" + (type.validBlocks.size() - oldSize) + " new blocks)");
		}
		
		// reregisters a tile entity defined by Storage Drawers
		private static <T extends TileEntity> void reregisterTileEntity(RegistryEvent.Register<TileEntityType<?>> event, String modid, String name, Supplier<? extends T> factory, Block... blocks) {
			reregisterTileEntity(event, modid, name, factory, Stream.of(blocks));
        }
		
		private static <T extends TileEntity> void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event, String name, Supplier<? extends T> factory, Block... blocks) {
            event.getRegistry().register(TileEntityType.Builder.create(factory, blocks)
                .build(null).setRegistryName(StorageDrawersUnlimited.MODID, name));
        }
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();
			
			for(Item item : wrapInIdentityHashSet(ITEMS)) {
				registry.register(item);
			}
		}
		
//		@SubscribeEvent(priority = EventPriority.HIGHEST)
//        @OnlyIn(Dist.CLIENT)
//        public static void registerModels(ModelBakeEvent event) {
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDrawersStandard.Slot1.class, new SDUTileEntityDrawersRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDrawersStandard.Slot2.class, new SDUTileEntityDrawersRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDrawersStandard.Slot4.class, new SDUTileEntityDrawersRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDrawersComp.Slot3.class, new TileEntityDrawersRenderer());
//        }
		
		private static <T> Set<T> wrapInIdentityHashSet(StreamableIterable<? extends T> iterable) {
			return iterable.stream().collect(Collectors.toCollection(() -> Collections.newSetFromMap(new IdentityHashMap<T,Boolean>())));
		}
		
	}
	
}
