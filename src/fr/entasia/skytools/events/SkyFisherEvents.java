package fr.entasia.skytools.events;

import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.Color;
import fr.entasia.skytools.tasks.AirHooksTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyFisherEvents implements Listener {




	@EventHandler
	public void a(PlayerFishEvent e){
		if(e.getState()==PlayerFishEvent.State.FISHING) {

			AirHooksTask aht = new AirHooksTask();

			aht.w = e.getHook().getWorld();
			if(aht.w.getTime()>13000){
				aht.fish = Particle.SMOKE_NORMAL;
				aht.fishColor = new Color(0, 0, 0);
				aht.background = Particle.REDSTONE;
				aht.backgroundColor = new Color(128, 128, 128);
			}else{
				aht.fish = Particle.END_ROD;
				aht.fishColor = new Color(0, 0, 0);
				aht.background = Particle.REDSTONE;
				aht.backgroundColor = new Color(210, 240, 255);
			}

			if(aht.w.hasStorm())aht.background = null;

			new BukkitRunnable() {

				public double y;
				public int counter;

				@Override
				public void run() {
					if (e.getHook().isValid()) {

						Location baseLoc = e.getHook().getLocation();

						if(aht.background!=null){
							aht.w.spawnParticle(aht.background, baseLoc, 0, aht.backgroundColor.r, aht.backgroundColor.g, aht.backgroundColor.b, 1);
						}
						if(counter!=10){
							counter++;
							return;
						}
						y = e.getHook().getVelocity().getY();
						if (y >= 0)return;

						Location loc = e.getHook().getLocation();

						for(int x=-2;x<=2;x++) {
							loc.setX(baseLoc.getX()+x);
							for (int y = -2; y <= 2; y++) {
								loc.setY(baseLoc.getX()+y);
								for (int z = -2; z <= 2; z++) {
									loc.setZ(baseLoc.getZ()+z);
									if(loc.getBlock().getType()!=Material.AIR)return;
								}
							}
						}


						aht.w.spawnParticle(aht.fish, baseLoc, 3000, AirHooksTask.radius, AirHooksTask.radius, AirHooksTask.radius, 0.1);


						aht.hook = e.getHook();
						aht.armorStand = (ArmorStand) e.getHook().getWorld().spawnEntity(baseLoc.add(0, -1, 0), EntityType.ARMOR_STAND);
						aht.armorStand.setMarker(true);
						aht.armorStand.setInvulnerable(true);
						aht.armorStand.setVisible(false);
						aht.armorStand.setGravity(false);
						aht.armorStand.addScoreboardTag("SkyFisherAS");
						aht.armorStand.addPassenger(aht.hook);

						Utils.airHookTasks.add(aht);
						aht.init();
					}
					cancel();
				}
			}.runTaskTimer(Main.main, 2, 2);
		}else if(e.getState()==PlayerFishEvent.State.CAUGHT_ENTITY||e.getState()==PlayerFishEvent.State.FAILED_ATTEMPT){
			for(AirHooksTask aht : Utils.airHookTasks){
				if(aht.hook==e.getHook()){
					if(aht.isTrapped()){
						Bukkit.broadcastMessage("Poisson attrapé !");
					}
					aht.cancel();
					aht.hook.remove();
					aht.armorStand.remove();
					break;
				}
			}
		}
	}

}
