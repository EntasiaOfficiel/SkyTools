package fr.entasia.skytools.events.cenchants;

import fr.entasia.apis.other.InstantFirework;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.tasks.FWArrowTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

public class ArrowEvents implements Listener {


	@EventHandler
	public void a(EntityShootBowEvent e){
		if(e.getEntity() instanceof Player){
			if(CustomArrows.EXPLOSION.is(e.getArrowItem())) {
				e.getProjectile().setCustomName("$explode");
			}
			if(CustomArrows.FIREWORKS.is(e.getArrowItem())) {
				FWArrowTask.start(e.getProjectile());
			}

			if(CustomArrows.FIRE.is(e.getArrowItem())) {
				e.getProjectile().setCustomName("$fire");
			}else if(CustomArrows.DRAGON.is(e.getArrowItem())) {
				e.getProjectile().setCustomName("$dragon");
			}
		}
	}

	@EventHandler
	public void a(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof AreaEffectCloud){
			String name = e.getDamager().getCustomName();
			System.out.println(name);
			if (name == null||!name.startsWith("$")) return;
			if(name.equals("$fire")) {
				e.getEntity().setFireTicks(70);
			}
		}
	}

	@EventHandler
	public void a(ProjectileHitEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getShooter() instanceof Player && pr.getType() == EntityType.ARROW) {
			String name = pr.getCustomName();
			if (name == null||!name.startsWith("$")) return;
			if(name.equals("$explode")) {
				InstantFirework.explode(pr.getLocation(), Utils.blackmeta);
				for (LivingEntity ent : pr.getLocation().getNearbyEntitiesByType(LivingEntity.class, 3, 3, 3)) {
					if (!(ent instanceof Player)) {
						ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 4, 0));
					}
					break;
				}
			}else{
				new BukkitRunnable() {
					@Override
					public void run() {
						AreaEffectCloud a;
						if(name.equals("$fire")) {
							a = (AreaEffectCloud) pr.getWorld().spawnEntity(pr.getLocation(), EntityType.AREA_EFFECT_CLOUD);
							a.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 0, 0), true);
							a.setParticle(Particle.FLAME);
							a.setRadiusPerTick(-0.01f);
							a.setWaitTime(10);
							a.setDurationOnUse(10);
							a.setReapplicationDelay(20);
						}else if(name.equals("$dragon")){
							a = (AreaEffectCloud) pr.getWorld().spawnEntity(pr.getLocation(), EntityType.AREA_EFFECT_CLOUD);
							a.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 1), true);
//							a.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 30, 1), true);
							a.setParticle(Particle.DRAGON_BREATH);
							a.setRadiusPerTick(-0.01f);
							a.setWaitTime(2);
							a.setReapplicationDelay(20);
						}else return;
						a.setCustomName(name);

					}
				}.runTaskLater(Main.main, 1);
			}
		}
	}
}
