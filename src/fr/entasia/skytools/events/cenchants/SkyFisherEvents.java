package fr.entasia.skytools.events.cenchants;

import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.tasks.AirHooksTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SkyFisherEvents implements Listener {



	public static final int AR = 3;

	@EventHandler
	public void a(PlayerFishEvent e){
		if(e.getState()==PlayerFishEvent.State.FISHING) {

			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			if(!CustomEnchants.SKY_FISHER.hasEnchant(item))return;

			AirHooksTask aht = new AirHooksTask();
			aht.w = e.getHook().getWorld();
			aht.initColors();

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
						for(int x=-AR; x<=AR; x++) {
							loc.setX(baseLoc.getX()+x);
							for (int y=-AR; y<=AR; y++) {
								loc.setY(baseLoc.getX()+y);
								for (int z=-AR; z<=AR; z++) {
									loc.setZ(baseLoc.getZ()+z);
									if(loc.getBlock().getType()!=Material.AIR)return;
								}
							}
						}

						ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
						if(!CustomEnchants.SKY_FISHER.hasEnchant(item))return;
						short dura = (short) (e.getPlayer().getInventory().getItemInMainHand().getDurability()+15);
						if(dura>=item.getType().getMaxDurability()){
							item.setType(Material.AIR);
							return;
						}else item.setDurability(dura);

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
						Entity ent;
						switch(e.getHook().getWorld().getEnvironment()){
							case NORMAL:{
								ent = aht.owLoot();
								break;
							}
							case NETHER:{
								ent = aht.netherLoot();
								break;
							}
							case THE_END:{
								ent = aht.endLoot();
								break;
							}
							default:{
								e.getPlayer().sendMessage("§cCongrats ! T'a cassé tout le système :)\nBon... Tu préviens un membre du Staff stp ?");
								return;
							}
						}
						assert ent != null;
						Vector v = e.getPlayer().getLocation().subtract(aht.centerLoc()).toVector().multiply(0.1).add(new Vector(0, 0.2, 0));
						System.out.println(v);
						ent.setVelocity(v);
					}
					aht.stop();
					break;
				}
			}
		}
	}


	public void a(LingeringPotionSplashEvent e){

	}

}
