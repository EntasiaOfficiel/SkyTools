package fr.entasia.skytools.events;

import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.tasks.AirHooksTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
			new BukkitRunnable() {

				public double y;

				@Override
				public void run() {
					if (e.getHook().isValid()) {
						y = e.getHook().getVelocity().getY();
						if (y >= 0)return;
						if(e.getHook().getLocation().getBlock().getType()==Material.AIR){
							Location loc = e.getHook().getLocation().add(0, -2, 0);
							if(loc.getBlock().getType()==Material.AIR){
								AirHooksTask aht = new AirHooksTask();
								aht.hook = e.getHook();
								aht.armorStand = (ArmorStand) e.getHook().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
								aht.armorStand.setMarker(true);
								aht.armorStand.setInvulnerable(true);
								aht.armorStand.setVisible(false);
								aht.armorStand.setGravity(false);
								aht.armorStand.addScoreboardTag("SkyFisherAS");
								aht.armorStand.addPassenger(aht.hook);

								Utils.airHookTasks.add(aht);
								aht.init();
							}
						}
					}
					cancel();
				}
			}.runTaskTimer(Main.main, 20, 3);
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
