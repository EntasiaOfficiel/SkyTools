package fr.entasia.skytools.events;

import fr.entasia.apis.other.InstantFirework;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.tasks.FWArrowTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class EnchantEvents2 implements Listener {


	@EventHandler
	public void a(EntityShootBowEvent e){
		if(e.getEntity() instanceof Player){
			if(CustomArrows.EXPLOSION.is(e.getArrowItem())) {
				Utils.explosions.add(e.getProjectile());
			}else if(CustomArrows.FIREWORKS.is(e.getArrowItem())) {
				FWArrowTask.start(e.getProjectile());
			}
		}
	}

	@EventHandler
	public void a(ProjectileHitEvent e){
		Projectile pr = e.getEntity();
		if (pr.getShooter() instanceof Player && pr.getType() == EntityType.ARROW) {
			if(Utils.explosions.remove(pr)){
				InstantFirework.explode(pr.getLocation(), Utils.blackmeta);
				for(LivingEntity ent : pr.getLocation().getNearbyEntitiesByType(LivingEntity.class, 3, 3, 3)){
					if(!(ent instanceof Player)){
						ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 4, 0));
					}
					ent.damage(2);
				}
			}
		}
	}

	public static HashMap<Material, Material> seeds = new HashMap<>();
	public static Vector[] vectors;

	static{
		seeds.put(Material.SEEDS, Material.CROPS);
		seeds.put(Material.CARROT_ITEM, Material.CARROT);
		seeds.put(Material.POTATO_ITEM, Material.POTATO);
		seeds.put(Material.BEETROOT_SEEDS, Material.BEETROOT_BLOCK);
		seeds.put(Material.MELON_SEEDS, Material.MELON_STEM);
		seeds.put(Material.PUMPKIN_SEEDS, Material.PUMPKIN_STEM);

		vectors = new Vector[5];
		vectors[0] = new Vector(0, 0, 0);
		vectors[1] = new Vector(1, 0, 0);
		vectors[2] = new Vector(-1, 0, 0);
		vectors[3] = new Vector(0, 0, 1);
		vectors[4] = new Vector(0, 0, -1);

	}

	public static Material getSeedBlock(Material m){
		for(Map.Entry<Material, Material> e : seeds.entrySet()){
			if(e.getKey()==m)return e.getValue();
		}
		return null;
	}

	@EventHandler
	public void a(PlayerInteractEvent e){
		if(e.getItem()!=null&&CustomEnchants.SEEDS_CANOON.hasEnchant(e.getItem())){
			if(e.getAction()== Action.RIGHT_CLICK_AIR||(e.getAction()== Action.RIGHT_CLICK_BLOCK&&e.getClickedBlock().getType()==Material.SOIL)){
				Player p = e.getPlayer();
				ItemStack item = null;
				Material m=null;
				for(ItemStack li : p.getInventory().getStorageContents()){
					if(li==null||li.getType()==Material.AIR)continue;
					m = getSeedBlock(li.getType());
					if(m!=null){
						item = li;
						break;
					}
				}
				if(item==null)return;
				Vector v = p.getLocation().getDirection();
				byte data;
				if(m== Material.BEETROOT_BLOCK)data= 3;
				else data = 0x7;
				FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation().add(0 ,1, 0).add(v), m, data);
				fb.setDropItem(false);
				fb.setHurtEntities(false);
				fb.setInvulnerable(true);
				fb.setVelocity(v);

				ItemStack finalItem = item;
				new BukkitRunnable() {
					@Override
					public void run() {
						if (fb.getTicksLived() > 200) {
							cancel();
							fb.remove();
						} else if (!fb.isValid()) {
							cancel();

							Material m2 = getSeedBlock(finalItem.getType());
							if (m2 != null) {
								Location loc = fb.getLocation();
								if (loc.getBlock().getType() == Material.AIR) loc.add(0, -1, 0);
								new BukkitRunnable() {
									@Override
									public void run() {
										int amount = finalItem.getAmount();
										Block lb;
										Location loc2;
										for (Vector v : vectors) {
											loc2 = loc.clone().add(v);
											if (loc2.getBlock().getType() == Material.SOIL) {
												lb = loc2.add(0, 1, 0).getBlock();
												if (lb.getType() == Material.AIR) {
													lb.setType(m2);
													amount -= 1;
													if (amount == 0) break;
												}
											}
										}
										finalItem.setAmount(amount);
									}
								}.runTask(Main.main);
							}
						}
					}
				}.runTaskTimerAsynchronously(Main.main, 0, 5);
			}
		}
	}
}
