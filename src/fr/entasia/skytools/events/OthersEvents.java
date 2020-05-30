package fr.entasia.skytools.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import fr.entasia.apis.TextUtils;
import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.ToolPlayer;
import fr.entasia.skytools.objs.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class OthersEvents implements Listener {

	@EventHandler
	public void firework(PlayerJoinEvent e){
		ToolPlayer tp = Utils.playerCache.get(e.getPlayer().getUniqueId());
		if(tp==null) Utils.playerCache.put(e.getPlayer().getUniqueId(), new ToolPlayer(e.getPlayer()));
		else tp.p = e.getPlayer();
	}


	@EventHandler
	public void firework(InventoryClickEvent e){
		if(e.getWhoClicked().getOpenInventory().getType() == InventoryType.CRAFTING){
			if(e.getClick()==ClickType.LEFT||e.getClick()==ClickType.RIGHT) {
				if (e.getSlot() == 39){
					if(e.getCursor().getType() == Material.FIREWORK){
						e.setCancelled(true);
						if(e.getWhoClicked().getInventory().getHelmet()==null){ // aucun item en casque , on set juste le curseur
							e.getWhoClicked().getInventory().setHelmet(e.getCursor());
							e.setCursor(new ItemStack(Material.AIR));
						}else{
							if(e.getCursor().getItemMeta().equals(e.getWhoClicked().getInventory().getHelmet().getItemMeta())){
								// mise en place du système de reste
								int total = e.getCursor().getAmount()+e.getWhoClicked().getInventory().getHelmet().getAmount();
								ItemStack helmet = e.getWhoClicked().getInventory().getHelmet();
								ItemStack reste = new ItemStack(Material.AIR);
								if(total>64){ // pour ne pas avoir des stacks de + de 64
									helmet.setAmount(64);
									reste = helmet.clone();
									reste.setAmount(total-64);
								}else helmet.setAmount(total);
								e.getWhoClicked().getInventory().setHelmet(helmet);
								e.setCursor(reste);
								// fin du système de reste

							}else{
								ItemStack a = e.getWhoClicked().getInventory().getHelmet();
								e.getWhoClicked().getInventory().setHelmet(e.getCursor());
								e.setCursor(a);
							}
						}
					}
				}
			}else if(e.getClick()==ClickType.SHIFT_LEFT) {
				if (e.getCurrentItem()!=null&&e.getCurrentItem().getType() == Material.FIREWORK && e.getSlot() != 39){
					// mise en place du système de reste ( geant size )
					ItemStack helmet = e.getWhoClicked().getInventory().getHelmet();
					ItemStack reste = new ItemStack(Material.AIR);
					if(helmet==null){ // cas 1 du système de reste
						e.setCancelled(true);
						helmet = e.getCurrentItem();
					}else{
						if(e.getCurrentItem().getItemMeta().equals(helmet.getItemMeta())){
							// cas 2 du système de reste
							e.setCancelled(true);
							int total = e.getWhoClicked().getInventory().getHelmet().getAmount()+e.getCurrentItem().getAmount();
							if(total>64){
								reste = e.getCurrentItem().clone();
								reste.setAmount(total-64);
								helmet.setAmount(64);
							}else{
								helmet.setAmount(total);
							}
						}else return; // shift click quand les deux items sont différents , on fait rien on laisse mc opérer
					}
					e.getWhoClicked().getInventory().setHelmet(helmet);
					e.setCurrentItem(reste);
					// fin du système de reste
				}
			}
		}
	}

	@EventHandler
	public static void onJump(PlayerJumpEvent e){
		if(e.getPlayer().isSneaking()&&e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType()==Material.FIREWORK){
			ItemStack a = e.getPlayer().getInventory().getHelmet();
			FireworkMeta meta = (FireworkMeta) a.getItemMeta();
			a.setAmount(a.getAmount()-1);
			e.getPlayer().getInventory().setHelmet(a);

			Firework fw = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), Firework.class);
			fw.setFireworkMeta(meta);

			final float vec;
			int life;
			switch(meta.getPower()){
				case 1:
					life = 5;
					vec = 1.5f;
					break;
				case 2:
					life = 10;
					vec = 2.2f;
					break;
				case 3:
					life = 15;
					vec = 2.7f;
					break;
				default:
					vec = 0;
					life = 0;
			}
			EntityNBT.addNBT(fw, new NBTComponent("{LifeTime:\"+life+\"}"));
			e.getPlayer().setPassenger(fw);

			new BukkitRunnable() {
				public void run() {
					e.getPlayer().setVelocity(new Vector(0, vec, 0));
				}
			}.runTask(Main.main);
		}
	}




	@EventHandler
	public static void onInteract(PlayerInteractEvent e) {
		if(e.hasBlock()){
			if(e.getClickedBlock().getType()==Material.SIGN_POST||e.getClickedBlock().getType()==Material.WALL_SIGN){
				Player p = e.getPlayer();
				Sign s = (Sign)e.getClickedBlock().getState();
				if(s.getLine(0).equals("§9[§7Warp§9]")) {
					Warp w = Warp.getWarp(s.getLine(1).substring(2).toLowerCase());
					if (w == null) {
						p.sendMessage("§cCe warp n'existe plus !");
						e.getClickedBlock().setType(Material.AIR);
						e.getClickedBlock().getWorld().dropItem(e.getClickedBlock().getLocation(), new ItemStack(Material.SIGN));
					} else {
						if(e.getAction()== Action.RIGHT_CLICK_BLOCK) w.teleport(p, true);
						else if(e.getAction()== Action.LEFT_CLICK_BLOCK){
							p.sendMessage("\n§eWarp "+ TextUtils.firstLetterUpper(w.name)+
									"\n"+String.join("\n", w.desc)+
									"\n§eClic droit pour se téléporter !\n ");
						}
					}
				}else if (s.getLine(0).equals("§8[§7Poubelle§8]"))
					p.openInventory(Bukkit.createInventory(null, 54, "§8Poubelle"));
			}
		}
	}

	@EventHandler
	public static void onSignChange(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("warp")||e.getLine(0).equalsIgnoreCase("[warp]")) {
			if (Warp.getWarp(e.getLine(1).toLowerCase()) == null) {
				e.getPlayer().sendMessage("§cErreur §7» §cCe warp n'existe pas !");
				e.getBlock().setType(Material.AIR);
				e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.SIGN));
			} else {
				e.setLine(0, "§9[§7Warp§9]");
				e.setLine(1, "§e" + TextUtils.firstLetterUpper(e.getLine(1)));
			}
		}else if(e.getLine(0).equalsIgnoreCase("poubelle")||e.getLine(0).equalsIgnoreCase("[poubelle]"))
			e.setLine(0, "§8[§7Poubelle§8]");
	}


	@EventHandler
	public void a(ProjectileHitEvent e) {
		if (e.getHitBlock() != null) {
			Projectile pr = e.getEntity();
			if (pr instanceof Arrow && pr.getTicksLived() > 1) {
				if (e.getHitBlock().getType() == Material.SLIME_BLOCK) {
					/*
					paper 1.12.2
					direction inversée
					vélocité OK
					 */
					Vector direction = pr.getLocation().getDirection().multiply(-1);
					Vector velocity = pr.getVelocity().multiply(1.1);

					int score = 0;
					String s = "";
					for (String ls : pr.getScoreboardTags()) {
						if (ls.startsWith("$")) {
							score = ls.length();
							s = ls;
							break;
						}
					}

					if (e.getHitBlockFace().getModX() != 0) {
						direction.setX(direction.getX() * -1);
						velocity.setX(velocity.getX() * -1);
					} else if (e.getHitBlockFace().getModY() != 0) {
						direction.setY(direction.getY() * -1);
						velocity.setY(velocity.getY() * -1);
					} else if (e.getHitBlockFace().getModZ() != 0) {
						direction.setZ(direction.getZ() * -1);
						velocity.setZ(velocity.getZ() * -1);
					}

					if (score == 5) return;
					World w = pr.getWorld();
					Location loc = pr.getLocation().add(direction.clone().multiply(1));
//					loc.setDirection(direction);
					pr.remove();

					Arrow a = (Arrow) w.spawnEntity(loc, EntityType.ARROW);

					a.addScoreboardTag(s + "$");


					a.setPickupStatus(Arrow.PickupStatus.ALLOWED);
					a.setVelocity(velocity);
				}
			}
		}
	}
}
