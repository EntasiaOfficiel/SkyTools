package fr.entasia.skytools.events.cenchants;

import fr.entasia.apis.ItemUtils;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.ToolPlayer;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
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

	public enum Directions {

		NORTH(BlockFace.NORTH, 2),
		EAST(BlockFace.EAST, 3),
		SOUTH(BlockFace.SOUTH, 0),
		WEST(BlockFace.WEST, 1),


		;

		public BlockFace face;
		public byte data;

		Directions(BlockFace face, int data) {
			this.face = face;
			this.data = (byte) data;
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
		seeds2.put(Material.INK_SACK, new CanoonBlock(Material.COCOA, 2, true, Material.LOG)); // a voir pour hardcoder à 100%
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
		if(e.getItem()!=null&&e.getAction()== Action.RIGHT_CLICK_AIR||(e.getAction()== Action.RIGHT_CLICK_BLOCK&&e.getClickedBlock().getType()==Material.SOIL)){
			int lvl = CustomEnchants.SEEDS_CANOON.getLevel(e.getItem());
			if(lvl==0)return;

			ToolPlayer tp = Utils.getPlayer(e.getPlayer());
			if(System.currentTimeMillis()-tp.cdCanoon<500)return;
			tp.cdCanoon = System.currentTimeMillis();
			ItemStack item = null;
			CanoonBlock cb=null;

			for(ItemStack litem : tp.p.getInventory().getStorageContents()){
				if(litem==null||litem.getType()==Material.AIR)continue;
				cb = getSeedBlock(litem.getType());
				if(cb==null&&lvl==2){
					cb = getSeed2Block(litem.getType());
					if(cb!=null&&cb.material==Material.COCOA&&litem.getDurability()!=3){
						cb = null;
					}
				}
				if(cb!=null){
					item = litem;
					break;
				}
			}

			if(item==null)return;
			Vector v = tp.p.getLocation().getDirection();

			byte data;
			if(cb.material==Material.SAPLING)data = (byte) item.getDurability();
			else if(cb.material==Material.COCOA)data = (byte) (Main.random.nextInt(4)+8);
			else data = cb.maxdata;

			FallingBlock fb = tp.p.getWorld().spawnFallingBlock(tp.p.getLocation().add(0 ,1, 0).add(v), cb.material, data);
			fb.setDropItem(false);
			fb.setHurtEntities(false);
			fb.setInvulnerable(true);
			fb.setVelocity(v);

			ItemStack finalItem = item;
			CanoonBlock finalCb = cb;
			new BukkitRunnable() { // detect if gone
				Location futureLoc;
				@Override
				public void run() {
					if(fb.getTicksLived() > 20*10) {
						cancel();
						fb.remove();
					}else if (finalCb.material==Material.COCOA) {
						if(fb.isValid()){
							Block lb;
							boolean go = false;
							Location loc = fb.getLocation();
							for(Vector v : vectors) {
								lb = loc.clone().add(v).getBlock();
								if(lb.getType()==Material.LOG){
									if (TreeSpecies.getByData(lb.getData()) == TreeSpecies.JUNGLE) {
										go = true;
										break;
									}
								}
							}
							if(go){
								cancel();
								fb.remove();
								new BukkitRunnable() {
									@Override
									public void run() {
										short amount = (short) finalItem.getAmount();
										Block lb, lb2;
										for(Vector v : vectors) {
											lb = loc.clone().add(v).getBlock();
											if (lb.getType() == Material.AIR) {
												for (Directions cd : Directions.values()) {
													lb2 = lb.getRelative(cd.face);
													if (lb2.getType() == Material.LOG) {
														if (TreeSpecies.getByData(lb2.getData()) == TreeSpecies.JUNGLE) {
															lb.setType(Material.COCOA);
															lb.setData(cd.data);
															amount -= 1;
															if (amount == 0) break;
															break;
														}
													}
												}
											}
											if (amount == 0) break;
										}
										if(amount!=finalItem.getAmount()){
											finalItem.setAmount(amount);
											ItemUtils.damage(e.getItem(), 1);
										}
									}
								}.runTask(Main.main);
							}
						}

					}else{
						if(!fb.isValid()){
							cancel();
							fallingBlockBreak(fb, e.getItem(), finalItem, finalCb);
						}
					}
				}
			}.runTaskTimerAsynchronously(Main.main, 0, 2);
		}
	}

	@EventHandler
	public void EntityChangeBlockEvent (EntityChangeBlockEvent e) {
		if (e.getEntityType() == EntityType.FALLING_BLOCK) {
			e.setCancelled(true);
		}
	}

	public static void fallingBlockBreak(FallingBlock fb, ItemStack hoe, ItemStack seeds, CanoonBlock exCb){

		CanoonBlock cb;
		CanoonBlock tempCb = getSeedBlock(seeds.getType());
		if(tempCb==null)cb = getSeed2Block(seeds.getType());
		else cb = tempCb;
		if (exCb.equals(cb)) {
			Location loc = fb.getLocation();
			if (loc.getBlock().getType() == Material.AIR) loc.add(0, -1, 0);
			new BukkitRunnable() {
				@Override
				public void run() {
					int amount = seeds.getAmount();
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
									baseBlock.setData((byte) seeds.getDurability());
								}
								amount -= 1;
							}
						}
					}
					if(amount!=seeds.getAmount()){
						seeds.setAmount(amount);
						ItemUtils.damage(hoe, 1);
					}
				}
			}.runTask(Main.main);
		}

	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void a(BlockBreakEvent e){
		int lvl = CustomEnchants.AURA.getLevel(e.getPlayer().getInventory().getItemInMainHand());
		if(lvl==0)return;
		Material m = e.getBlock().getType();
		boolean nop = true;
		for(CanoonBlock cb : seeds.values()){
			if(cb.material==m){
				nop = false;
				break;
			}
		}
		if(nop)return;
		Location loc = e.getBlock().getLocation();
		Vector v = e.getPlayer().getLocation().getDirection();
		for(int i=0;i<lvl*3;i++){
			loc.add(v);
			Block b = loc.getBlock();
			if(b.getType()==m){
				new BukkitRunnable() {
					@Override
					public void run() {
						b.breakNaturally();
					}
				}.runTaskLater(Main.main, 50 * i);
			}else if(b.getType()!=Material.AIR)break;
		}

	}
}
