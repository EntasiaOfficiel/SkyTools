package fr.entasia.skytools.events.cenchants;

import fr.entasia.apis.utils.ItemUtils;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class FarmEvents implements Listener {

	public static HashMap<Material, SeedBlock> seeds = new HashMap<>();
	public static Vector[] vectors;

	public static class SeedBlock {
		public Material material;
		public Material growBlock;

		public SeedBlock(Material material, Material growBlock){
			this.material = material;
			this.growBlock = growBlock;
		}
	}

	static{
		seeds.put(Material.SEEDS, new SeedBlock(Material.CROPS, Material.SOIL));
		seeds.put(Material.CARROT_ITEM, new SeedBlock(Material.CARROT, Material.SOIL));
		seeds.put(Material.POTATO_ITEM, new SeedBlock(Material.POTATO, Material.SOIL));
		seeds.put(Material.MELON_SEEDS, new SeedBlock(Material.MELON_STEM, Material.SOIL));
		seeds.put(Material.PUMPKIN_SEEDS, new SeedBlock(Material.PUMPKIN_STEM, Material.SOIL));
		seeds.put(Material.BEETROOT_SEEDS, new SeedBlock(Material.BEETROOT_BLOCK, Material.SOIL));
		seeds.put(Material.NETHER_STALK, new SeedBlock(Material.NETHER_WARTS, Material.SOUL_SAND));

		vectors = new Vector[5];
		vectors[0] = new Vector(0, 0, 0);
		vectors[1] = new Vector(1, 0, 0);
		vectors[2] = new Vector(-1, 0, 0);
		vectors[3] = new Vector(0, 0, 1);
		vectors[4] = new Vector(0, 0, -1);

	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void a(BlockBreakEvent e){
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		int lvl = CustomEnchants.TONDEUSE.getLevel(item);
		if(lvl==0)return;
		Material m = e.getBlock().getType();
		boolean nop = true;
		for(SeedBlock cb : seeds.values()){
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

	@EventHandler
	public void a(PlayerMoveEvent e){
		Block b = e.getFrom().clone().add(0, 1, 0).getBlock();
		if(b.getType()==Material.AIR){
			PlayerInventory inv = e.getPlayer().getInventory();
			Material m = e.getFrom().getBlock().getType();
			if(m==Material.SOUL_SAND||m==Material.SOIL){
				if(CustomEnchants.GREENER.hasEnchant(inv.getBoots())){
					for(ItemStack i : inv.getContents()){
						if(i==null||i.getType()==Material.AIR)continue;
						for(Map.Entry<Material, SeedBlock> en : seeds.entrySet()){
							if(i.getType()==en.getKey()&&m==en.getValue().growBlock){
								i.subtract();
								b.setType(en.getValue().material);
								return;
							}
						}
					}
				}
			}
		}
	}

}
