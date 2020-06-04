package fr.entasia.skytools.events.cenchants;

import fr.entasia.apis.other.InstantFirework;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.tasks.FWArrowTask;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowEvents implements Listener {


	@EventHandler
	public void a(EntityShootBowEvent e){
		if(e.getEntity() instanceof Player){
			if(CustomArrows.EXPLOSION.hasEnchant(e.getArrowItem())) {
				e.getProjectile().setCustomName("$explode");
			}
			if(CustomArrows.FIREWORKS.hasEnchant(e.getArrowItem())) {
				FWArrowTask.start(e.getProjectile());
			}

			if(CustomArrows.FIRE.hasEnchant(e.getArrowItem())) {
				e.getProjectile().setCustomName("$fire");
			}else if(CustomArrows.DRAGON.hasEnchant(e.getArrowItem())) {
				e.getProjectile().setCustomName("$dragon");
			}
		}
	}

	@EventHandler
	public void a(AreaEffectCloudApplyEvent e) {
		if(e.getAffectedEntities().size()==0)return;
		String name = e.getEntity().getCustomName();
		int type;
		if (name == null||!name.startsWith("$")) return;
		if(name.equals("$fire")) type=1;
		else if(name.equals("$dragon")) type=2;
		else return;
		for(LivingEntity ent : e.getAffectedEntities()){
			if(type==1){
				ent.setFireTicks(70);
			}else if(type==2) {
				ent.damage(4);
			}
//			System.out.println(2);
//			String name = e.getDamager().getCustomName();
//			System.out.println(3);
//			if (name == null||!name.startsWith("$")) return;
//			if(name.equals("$fire")) {
//				e.getEntity().setFireTicks(70);
//			}else if(name.equals("dragon")) {
//				((LivingEntity) e.getEntity()).damage(0.5);
//			}
		}
	}

//	@EventHandler
//	public void a(EntityDamageByEntityEvent e) {
//		System.out.println(1);
//		if(e.getDamager() instanceof AreaEffectCloud){
//			System.out.println(2);
//			String name = e.getDamager().getCustomName();
//			System.out.println(3);
//			if (name == null||!name.startsWith("$")) return;
//			if(name.equals("$fire")) {
//				e.getEntity().setFireTicks(70);
//			}else if(name.equals("dragon")) {
//				((LivingEntity) e.getEntity()).damage(0.5);
//			}
//		}
//	}

	@EventHandler
	public void a(ProjectileHitEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getShooter() instanceof Player && pr.getType() == EntityType.ARROW) {
			String name = pr.getCustomName();
			if (name == null||!name.startsWith("$")) return;
			if(name.equals("$explode")) {
				InstantFirework.explode(pr.getLocation(), Utils.blackmeta);
				for (LivingEntity ent : pr.getLocation().getNearbyEntitiesByType(LivingEntity.class, 4.5, 4.5, 4.5)) {
					if (!(ent instanceof Player)) {
						ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 4*20, 1));
						ent.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 0));
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
							a.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 1, 1), true);
							a.setParticle(Particle.FLAME);
							a.setRadiusPerTick(-0.01f);
							a.setWaitTime(10);
							a.setDurationOnUse(10);
							a.setReapplicationDelay(20);
						}else if(name.equals("$dragon")){
							a = (AreaEffectCloud) pr.getWorld().spawnEntity(pr.getLocation(), EntityType.AREA_EFFECT_CLOUD);
							a.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 1, 1), true);
							a.setParticle(Particle.DRAGON_BREATH);
							a.setRadiusPerTick(-0.01f);
							a.setWaitTime(2);
							a.setReapplicationDelay(5);
						}else return;
						a.setCustomName(name);

					}
				}.runTaskLater(Main.main, 1);
			}
		}
	}
}
