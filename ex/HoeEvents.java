package fr.entasia.skytools.events.cenchants;

import fr.entasia.apis.utils.ItemUtils;
import fr.entasia.skycore.apis.BaseAPI;
import fr.entasia.skycore.apis.BaseIsland;
import fr.entasia.skycore.apis.CooManager;
import fr.entasia.skycore.apis.OthersAPI;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoeEvents implements Listener {

	public static HashMap<Material, CanoonBlock> seeds = new HashMap<>();
	public static HashMap<Material, CanoonBlock> seeds2 = new HashMap<>();
	public static Vector[] vectors;

	public static class CanoonBlock {
		public Material material;
		public Material[] growBlocks;
		public byte maxdata;
		public boolean hasRadius;

		public CanoonBlock(Material material, int maxdata, boolean hasRadius, Material... growBlocks){
			this.material = material;
			this.maxdata = (byte) maxdata;
			this.hasRadius = hasRadius;
			this.growBlocks = growBlocks;
		}

		public boolean canGrow(Material block){
			for(Material m : growBlocks){
				if(m==block)return true;
			}
			return false;

		}
	}

	static{
		seeds.put(Material.SEEDS, new CanoonBlock(Material.CROPS, 7,true, Material.SOIL));
		seeds.put(Material.CARROT_ITEM, new CanoonBlock(Material.CARROT, 7, true, Material.SOIL));
		seeds.put(Material.POTATO_ITEM, new CanoonBlock(Material.POTATO, 7, true, Material.SOIL));
		seeds.put(Material.MELON_SEEDS, new CanoonBlock(Material.MELON_STEM, 7, true, Material.SOIL));
		seeds.put(Material.PUMPKIN_SEEDS, new CanoonBlock(Material.PUMPKIN_STEM, 7, true, Material.SOIL));
		seeds.put(Material.BEETROOT_SEEDS, new CanoonBlock(Material.BEETROOT_BLOCK, 3, true, Material.SOIL));

		seeds2.put(Material.CACTUS, new CanoonBlock(Material.CACTUS, 0,false, Material.SAND));
		seeds2.put(Material.NETHER_STALK, new CanoonBlock(Material.NETHER_WARTS, 3, true, Material.SOUL_SAND));
		seeds2.put(Material.CHORUS_FLOWER, new CanoonBlock(Material.CHORUS_FLOWER, 0,false, Material.ENDER_STONE));
		seeds2.put(Material.SAPLING, new CanoonBlock(Material.SAPLING, 0, false, Material.GRASS, Material.DIRT));

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
			if(e.getKey()==m){
				return e.getValue();
			}
		}
		return null;
	}

	@EventHandler
	public void a(PlayerInteractEvent e){
		if(e.getItem()!=null&&(e.getAction()== Action.RIGHT_CLICK_AIR||e.getAction()== Action.RIGHT_CLICK_BLOCK)){
			int lvl = CustomEnchants.SEEDS_CANOON.getLevel(e.getItem());
			if(lvl==0)return;

			Player p = e.getPlayer();

			List<MetadataValue> list = p.getMetadata("cdCannon");
			if(list.size()==0||System.currentTimeMillis()-list.get(0).asLong()>400){
				p.setMetadata("cdCannon", new FixedMetadataValue(Main.main, System.currentTimeMillis()));
			}else return;

			BaseIsland is = BaseAPI.getIsland(CooManager.getIslandID(p.getLocation()));
			if(is.getMember(p.getUniqueId())==null&&!OthersAPI.isMasterEdit(p)){
				p.sendMessage("§cTu n'es pas membre de cette île !");
				return;
			}

			ItemStack item = null;
			CanoonBlock cb = null;

			for(ItemStack litem : p.getInventory().getStorageContents()){
				if(litem==null||litem.getType()==Material.AIR)continue;
				cb = getSeedBlock(litem.getType());
				if(cb==null&&lvl==2){
					cb = getSeed2Block(litem.getType());
				}
				if(cb!=null){
					item = litem;
					break;
				}
			}

			if(item==null)return;
			Vector v = p.getLocation().getDirection();

			byte data;
			if(cb.material==Material.SAPLING)data = (byte) item.getDurability();
			else data = cb.maxdata;

			Location loc = p.getLocation().add(0 ,1, 0).add(v);
			FallingBlock fb = p.getWorld().spawnFallingBlock(loc, new MaterialData(cb.material, data));
			fb.setDropItem(false);
			fb.setHurtEntities(false);
			fb.setInvulnerable(true);
			fb.setVelocity(v);
			fb.setMetadata("canoonShoot", new FixedMetadataValue(Main.main, true));

			ItemStack finalItem = item;
			CanoonBlock finalCb = cb;
			new BukkitRunnable() {
				@Override
				public void run() {
					Block aa = loc.getBlock();
					if(aa.getType()==Material.SOIL)aa = aa.getRelative(BlockFace.UP);
					aa.getState().update(true, true);
				}
			}.runTask(Main.main);
			new BukkitRunnable() { // detect if gone

				@Override
				public void run() {

					if (fb.getTicksLived() > 20 * 10) {
						cancel();
						fb.remove();
					} else {
						if (!fb.isValid()) {
							cancel();
							fBBreak(fb, e.getItem(), finalItem, finalCb);
						}
					}
				}
			}.runTaskTimerAsynchronously(Main.main, 0, 2);
		}
	}

	@EventHandler
	public void a(EntityChangeBlockEvent e) {
		if (e.getEntityType() == EntityType.FALLING_BLOCK) {
			FallingBlock fb = (FallingBlock) e.getEntity();
			if(fb.hasMetadata("canoonShoot"))e.setCancelled(true);
		}
	}

//	public static boolean cocoaFBBreak(FallingBlock fb, ItemStack hoeItem, ItemStack seedsItem){
//		Block lb;
//		boolean go = false;
//		Location loc = fb.getLocation();
//		for (Vector v : vectors) {
//			lb = loc.clone().add(v).getBlock();
//			if (lb.getType() == Material.LOG) {
//				if (TreeSpecies.getByData(lb.getData()) == TreeSpecies.JUNGLE) {
//					go = true;
//					break;
//				}
//			}
//		}
//		if (!go) return false;
//		fb.remove();
//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				short amount = (short) seedsItem.getAmount();
//				Block lb, lb2;
//				for (Vector v : vectors) {
//					lb = loc.clone().add(v).getBlock();
//					if (lb.getType() == Material.AIR) {
//						for (Directions cd : Directions.values()) {
//							lb2 = lb.getRelative(cd.face);
//							if (lb2.getType() == Material.LOG) {
//								if (TreeSpecies.getByData(lb2.getData()) == TreeSpecies.JUNGLE) {
//									lb.setType(Material.COCOA);
//									lb.setData(cd.data);
//									amount -= 1;
//									if (amount == 0) break;
//									break;
//								}
//							}
//						}
//					}
//					if (amount == 0) break;
//				}
//				if (amount != seedsItem.getAmount()) {
//					seedsItem.setAmount(amount);
//					ItemUtils.damage(hoeItem, 1);
//				}
//			}
//		}.runTask(Main.main);
//		return true;
//	}

	public static void fBBreak(FallingBlock fb, ItemStack hoeItem, ItemStack seedsItem, CanoonBlock exCb){


		CanoonBlock cb;
		CanoonBlock tempCb = getSeedBlock(seedsItem.getType());
		if(tempCb==null)cb = getSeed2Block(seedsItem.getType());
		else cb = tempCb;
		if (exCb.equals(cb)) {
			Location loc = fb.getLocation();
			if (loc.getBlock().getType() == Material.AIR) loc.add(0, -1, 0);
			new BukkitRunnable() {
				@Override
				public void run() {
					int amount = seedsItem.getAmount();
					Block baseBlock = loc.getBlock();
					Block lb;
					if(cb.hasRadius){
						for(Vector v : vectors){
							lb = loc.clone().add(v).getBlock();
							if (cb.canGrow(lb.getType())) {
								lb = lb.getRelative(BlockFace.UP);
								if (lb.getType() == Material.AIR) {
									lb.setType(cb.material);
									amount -= 1;
									if (amount == 0) break;
								}
							}
						}
					}else{
						if (cb.canGrow(baseBlock.getType())) {
							baseBlock = baseBlock.getRelative(BlockFace.UP);
							if (baseBlock.getType() == Material.AIR) {
								baseBlock.setType(cb.material);
								if(cb.material==Material.SAPLING){
									baseBlock.setData((byte) seedsItem.getDurability());
								}
								amount -= 1;
							}
						}
					}
					if(amount!=seedsItem.getAmount()){
						seedsItem.setAmount(amount);
						ItemUtils.damage(hoeItem, 1);
					}
				}
			}.runTask(Main.main);
		}

	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void a(BlockBreakEvent e){
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		int lvl = CustomEnchants.TONDEUSE.getLevel(item);
		if(lvl==0)return;
		Material m = e.getBlock().getType();
		boolean nop = true;
		ArrayList<CanoonBlock> list = new ArrayList<>(seeds.values());
		list.addAll(seeds2.values());
		for(CanoonBlock cb : list){
			if(cb.material==m){
				nop = false;
				break;
			}
		}
		if(nop)return;

		Location loc = e.getBlock().getLocation().add(0.5, 0.5, 0.5);

		Vector v = e.getPlayer().getLocation().getDirection().setY(0);
//		Vector v = loc.toVector().subtract(e.getPlayer().getLocation().toVector()).setY(0);

		double highest = Math.max(Math.abs(v.getX()), Math.abs(v.getZ()));
		v.divide(new Vector(highest, 1, highest));

		ItemUtils.damage(item, lvl*2);

		for(int i=0;i<(lvl*3)-1;i++){
			loc.add(v);
			Location loc2 = loc.clone();
			Block b = loc2.getBlock();
			if(b.getType()==m){
				new BukkitRunnable() {
					@Override
					public void run() {
						if(b.getType()==m) {
							b.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc2, 150, 0.5, 0.5, 0.5, 1, new MaterialData(m, b.getData()));
							b.getWorld().playSound(loc2, Sound.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 0.8f);
							b.breakNaturally();
						}
					}
				}.runTaskLater(Main.main, i);
			}else if(b.getType()!=Material.AIR)break;
		}
	}
}
