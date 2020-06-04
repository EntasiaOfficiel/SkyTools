package fr.entasia.skytools.events.cenchants;

import fr.entasia.apis.ItemUtils;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class EnchantEvents2 implements Listener {

	public static HashMap<Material, CanoonBlock> seeds = new HashMap<>();
	public static HashMap<Material, CanoonBlock> seeds2 = new HashMap<>();
	public static Vector[] vectors;

	public static class CanoonBlock {
		public Material material;
		public Material[] growBlocks;
		public byte maxdata;

		public CanoonBlock(Material material, int maxdata, Material... growBlocks){
			this.material = material;
			this.maxdata = (byte) maxdata;
			this.growBlocks = growBlocks;
		}

		public boolean canGrow(Material block){
			for(Material m : growBlocks){
				if(m==block)return true;
			}
			return false;

		}
	}

	public enum Directions {

		NORTH(BlockFace.NORTH, 0),
		EAST(BlockFace.EAST, 1),
		SOUTH(BlockFace.SOUTH, 2),
		WEST(BlockFace.WEST, 3),


		;

		public BlockFace face;
		public byte data;

		Directions(BlockFace face, int data) {
			this.face = face;
			this.data = (byte) data;
		}
	}

	static{
		seeds.put(Material.SEEDS, new CanoonBlock(Material.CROPS, 7, Material.SOIL));
		seeds.put(Material.CARROT_ITEM, new CanoonBlock(Material.CARROT, 7, Material.SOIL));
		seeds.put(Material.POTATO_ITEM, new CanoonBlock(Material.POTATO, 7, Material.SOIL));
		seeds.put(Material.MELON_SEEDS, new CanoonBlock(Material.MELON_STEM, 7, Material.SOIL));
		seeds.put(Material.PUMPKIN_SEEDS, new CanoonBlock(Material.PUMPKIN_STEM, 7, Material.SOIL));
		seeds.put(Material.BEETROOT_SEEDS, new CanoonBlock(Material.BEETROOT_BLOCK, 3, Material.SOIL));

		seeds2.put(Material.CACTUS, new CanoonBlock(Material.CACTUS, 0, Material.SAND));
		seeds2.put(Material.INK_SACK, new CanoonBlock(Material.COCOA, 2, Material.LOG)); // a voir pour hardcoder à 100%
		seeds2.put(Material.NETHER_STALK, new CanoonBlock(Material.NETHER_WARTS, 3, Material.SOUL_SAND));
		seeds2.put(Material.CHORUS_FLOWER, new CanoonBlock(Material.CHORUS_FLOWER, 0, Material.ENDER_STONE));
		seeds2.put(Material.SAPLING, new CanoonBlock(Material.SAPLING, 0, Material.GRASS, Material.DIRT));

		vectors = new Vector[5];
		vectors[0] = new Vector(0, 0, 0);
		vectors[1] = new Vector(1, 0, 0);
		vectors[2] = new Vector(-1, 0, 0);
		vectors[3] = new Vector(0, 0, 1);
		vectors[4] = new Vector(0, 0, -1);

	}

	public static CanoonBlock getSeedBlock(Material m){
		for(Map.Entry<Material, CanoonBlock> e : seeds.entrySet()){
			if(e.getKey()==m)return e.getValue();
		}
		return null;
	}
	public static CanoonBlock getSeed2Block(Material m){
		for(Map.Entry<Material, CanoonBlock> e : seeds2.entrySet()){
			if(e.getKey()==m) return e.getValue();
		}
		return null;
	}

	@EventHandler
	public void a(PlayerInteractEvent e){
		if(e.getItem()!=null&&e.getAction()== Action.RIGHT_CLICK_AIR||(e.getAction()== Action.RIGHT_CLICK_BLOCK&&e.getClickedBlock().getType()==Material.SOIL)){
			int lvl = CustomEnchants.SEEDS_CANOON.getLevel(e.getItem());
			if(lvl==0)return;
			Player p = e.getPlayer();
			ItemStack item = null;
			CanoonBlock cb=null;
			for(ItemStack litem : p.getInventory().getStorageContents()){
				if(litem==null||litem.getType()==Material.AIR)continue;
				cb = getSeedBlock(litem.getType());
				if(cb==null&&lvl==2){
					cb = getSeed2Block(litem.getType());
					if(cb!=null&&cb.material==Material.COCOA&&litem.getDurability()!=3)cb = null;
				}
				if(cb!=null){
					item = litem;
					break;
				}
			}
			if(item==null)return;
			Vector v = p.getLocation().getDirection();

			FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation().add(0 ,1, 0).add(v), cb.material, cb.maxdata);
			fb.setDropItem(false);
			fb.setHurtEntities(false);
			fb.setInvulnerable(true);
			fb.setVelocity(v);

			ItemStack finalItem = item;
			CanoonBlock finalCb = cb;
			new BukkitRunnable() { // detect if gone
				@Override
				public void run() {
					if (fb.getTicksLived() > 200) {
						cancel();
						fb.remove();
					} else if (!fb.isValid()) {
						cancel();
						fallingBlockBreak()

						CanoonBlock cb2 = getSeedBlock(finalItem.getType());
						if (finalCb.equals(cb2)) {
							Location loc = fb.getLocation();
							if (loc.getBlock().getType() == Material.AIR) loc.add(0, -1, 0);
							new BukkitRunnable() {
								@Override
								public void run() {
									int amount = finalItem.getAmount();
									Block baseBlock = loc.getBlock();
									Block lb;
									Block lb2;
									if(cb2.material==Material.COCOA){
										for(Directions cd : Directions.values()){
											lb = baseBlock.getRelative(cd.face);
											for(Directions cd2 : Directions.values()){
												lb2 = lb.getRelative(cd2.face);
												if(lb2.getType()==Material.LOG){
													if(TreeSpecies.getByData(lb2.getData())==TreeSpecies.JUNGLE){
														lb.setType(Material.COCOA);
														lb.setData(cd2.data);
														amount -= 1;
														break;
													}
												}
											}
											if (amount == 0) break;
										}
									}else{
										for(Directions cd : Directions.values()){
											lb = loc.clone().getBlock().getRelative(cd.face);
											if (cb2.canGrow(lb.getType())) {
												lb = lb.getRelative(BlockFace.UP);
												if (lb.getType() == Material.AIR) {
													lb.setType(cb2.material);
													amount -= 1;
													if (amount == 0) break;
												}
											}
										}
									}
									if(amount!=finalItem.getAmount()){
										finalItem.setAmount(amount);
										ItemUtils.damage(e.getItem(), 1);
									}
								}
							}.runTask(Main.main);
						}
					}
				}
			}.runTaskTimerAsynchronously(Main.main, 0, 5);
		}
	}

	public static void fallingBlockBreak(ItemStack item, CanoonBlock cb, FallingBlock fb){

		CanoonBlock cb2 = getSeedBlock(finalItem.getType());
		if (cb.equals(cb2)) {
			Location loc = fb.getLocation();
			if (loc.getBlock().getType() == Material.AIR) loc.add(0, -1, 0);
			new BukkitRunnable() {
				@Override
				public void run() {
					int amount = finalItem.getAmount();
					Block baseBlock = loc.getBlock();
					Block lb;
					Block lb2;
					if(cb2.material==Material.COCOA){
						for(Directions cd : Directions.values()){
							lb = baseBlock.getRelative(cd.face);
							for(Directions cd2 : Directions.values()){
								lb2 = lb.getRelative(cd2.face);
								if(lb2.getType()==Material.LOG){
									if(TreeSpecies.getByData(lb2.getData())==TreeSpecies.JUNGLE){
										lb.setType(Material.COCOA);
										lb.setData(cd2.data);
										amount -= 1;
										break;
									}
								}
							}
							if (amount == 0) break;
						}
					}else{
						for(Directions cd : Directions.values()){
							lb = loc.clone().getBlock().getRelative(cd.face);
							if (cb2.canGrow(lb.getType())) {
								lb = lb.getRelative(BlockFace.UP);
								if (lb.getType() == Material.AIR) {
									lb.setType(cb2.material);
									amount -= 1;
									if (amount == 0) break;
								}
							}
						}
					}
					if(amount!=finalItem.getAmount()){
						finalItem.setAmount(amount);
						ItemUtils.damage(e.getItem(), 1);
					}
				}
			}.runTask(Main.main);
		}

	}

//	@EventHandler
//	public void a(BlockBreakEvent e){
//		if(e.getBlock().getType()==Material.CROPS
//	}
}
