package fr.entasia.skytools.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.utils.ItemUtils;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.apis.utils.TextUtils;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.ToolPlayer;
import fr.entasia.skytools.objs.Warp;
import fr.entasia.skytools.objs.villagers.Villagers;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class OthersEvents implements Listener {

	@EventHandler
	public void a(PlayerJoinEvent e){
		ToolPlayer tp = Utils.playerCache.get(e.getPlayer().getUniqueId());
		if(tp!=null)tp.p = e.getPlayer();
	}

	@EventHandler
	public void a(PlayerQuitEvent e){
		Utils.playerCache.remove(e.getPlayer().getUniqueId());
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
				case 3:
				case 2:
					life = 10;
					vec = 2.2f;
					break;
				default: // et case 1 ducoup
					life = 5;
					vec = 1.5f;
					break;
			}
			EntityNBT.addNBT(fw, new NBTComponent(String.format("{LifeTime:%s}", life)));
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
		switch (e.getLine(0).toLowerCase()) {
			case "warp":
			case "[warp]": {
				if (Warp.getWarp(e.getLine(1).toLowerCase()) == null) {
					e.getPlayer().sendMessage("§cErreur §7» §cCe warp n'existe pas !");
					e.getBlock().setType(Material.AIR);
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.SIGN));
				} else {
					e.setLine(0, "§9[§7Warp§9]");
					e.setLine(1, "§e" + TextUtils.firstLetterUpper(e.getLine(1)));
				}
				break;
			}
			case "bin":
			case "[bin]":
			case "poubelle":
			case "[poubelle]": {
				e.setLine(0, "§8[§7Poubelle§8]");
				break;
			}
			case "lock":
			case "[lock]":
			case "protection":
			case "[protection]": {
				e.setLine(0, "§9[§3Protection§9]");
				e.setLine(1, e.getPlayer().getName());
				break;
			}
		}
	}

	// TODO A VOIR POUR SUPPR OU NON
//	@EventHandler
//	public void a(ProjectileHitEvent e) {
//		if (e.getHitBlock() != null) {
//			Projectile pr = e.getEntity();
//			if (pr instanceof Arrow && pr.getTicksLived() > 1) {
//				if (e.getHitBlock().getType() == Material.SLIME_BLOCK) {
//					/*
//					paper 1.12.2
//					direction inversée
//					vélocité OK
//					 */
//					Vector direction = pr.getLocation().getDirection().multiply(-1);
//					Vector velocity = pr.getVelocity().multiply(1.1);
//
//					int score = 0;
//					String s = "";
//					for (String ls : pr.getScoreboardTags()) {
//						if (ls.startsWith("$")) {
//							score = ls.length();
//							s = ls;
//							break;
//						}
//					}
//
//					if (e.getHitBlockFace().getModX() != 0) {
//						direction.setX(direction.getX() * -1);
//						velocity.setX(velocity.getX() * -1);
//					} else if (e.getHitBlockFace().getModY() != 0) {
//						direction.setY(direction.getY() * -1);
//						velocity.setY(velocity.getY() * -1);
//					} else if (e.getHitBlockFace().getModZ() != 0) {
//						direction.setZ(direction.getZ() * -1);
//						velocity.setZ(velocity.getZ() * -1);
//					}
//
//					if (score == 5) return;
//					World w = pr.getWorld();
//					Location loc = pr.getLocation().add(direction.clone().multiply(1));
////					loc.setDirection(direction);
//					pr.remove();
//
//					Arrow a = (Arrow) w.spawnEntity(loc, EntityType.ARROW);
//
//					a.addScoreboardTag(s + "$");
//
//					a.setPickupStatus(Arrow.PickupStatus.ALLOWED);
//					a.setVelocity(velocity);
//				}
//			}
//		}
//	}

	@EventHandler
	public void a(EntitySpawnEvent e){
		if(e.getEntity().getType()==EntityType.VILLAGER){
			Villager v = (Villager)e.getEntity();
			Villagers vi = Villagers.getOne();
			vi.apply(v);
		}
	}

	@EventHandler
	public void a(VillagerAcquireTradeEvent e){
		e.setCancelled(true);
		if(e.getEntity().getTicksLived()<20)return;
		testUpgrade(e.getEntity());
	}

	@EventHandler
	public void a(VillagerReplenishTradeEvent e) {
		e.setCancelled(true);
		testUpgrade(e.getEntity());
	}

	public static void testUpgrade(Villager v) {
		NBTComponent nbt = EntityNBT.getNBT(v);

		List<MetadataValue> meta = v.getMetadata("lastUpgrade");
		if(meta.size()!=0){
			if(System.currentTimeMillis()-meta.get(0).asLong()<1000){
				return;
			}
		}
		v.setMetadata("lastUpgrade", new FixedMetadataValue(Main.main, System.currentTimeMillis()));
//		System.out.println("upgrading...");
		Object o = nbt.getValue(NBTTypes.Int, "CareerLevel");
		int current;
		if (o == null) current = 1;
		else current = (int) o;
		Villagers vi = Villagers.getType(v);
		if (vi == null) {
			ServerUtils.permMsg("log.upgradenpc", "§cUne erreur s'est produite lors de l'upgrade d'un villageois ! (career not found)." +
					" Infos :§6" + v.getLocation());
		}else{
			int newLvl = current+1;
			if (newLvl >= vi.levels.length) return;
			List<MerchantRecipe> list2 = new ArrayList<>(v.getRecipes());
			vi.addToList(list2, newLvl);
			nbt.setValue(NBTTypes.Int, "CareelLevel", newLvl);
			EntityNBT.setNBT(v, nbt);
			v.setRecipes(list2);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void netherWart(PlayerInteractEvent e) {
		ItemStack i = e.getItem();
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.NETHER_WARTS) {
				if (i != null && i.getType() == Material.BLAZE_POWDER) {
					byte b = e.getClickedBlock().getData();
					if (b != 3) {
						e.getClickedBlock().setData((byte) (b + 1));
						if (p.getGameMode() != GameMode.CREATIVE) {
							p.getInventory().getItemInMainHand().subtract(1);
						}
					}
				}
			}
		}
	}


	@EventHandler
	public void a(PlayerInteractEntityEvent e) {
		if(e.getHand()!= EquipmentSlot.HAND)return;
		if(e.getRightClicked().getType()==EntityType.ZOMBIE_VILLAGER){
			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			if(!ItemUtils.hasName(item, "§fÉtoile guérisseuse")) {
				item = e.getPlayer().getInventory().getItemInOffHand();
				if (!ItemUtils.hasName(item, "§fÉtoile guérisseuse")) return;
			}
			item.subtract();
			EntityNBT.addNBT(e.getRightClicked(), new NBTComponent("{ConversionTime:60}"));
		}else if(e.getRightClicked().getType()==EntityType.VILLAGER){
			if(Main.r.nextInt(50)==0){
				e.getPlayer().sendMessage("§bMerci à §3wishdrow§b pour les trades custom des PNJ ! :)");
			}
		}
	}

//	@EventHandler
//	public void a(InventoryOpenEvent e) throws ReflectiveOperationException {
//		e.setCancelled(true);
//
//		InventoryMerchant a = new InventoryMerchant((EntityPlayer)ReflectionUtils.getEntityPlayer(e.getPlayer()))
//
////		 vive mc
//		Inventory inv = e.getInventory();
//		InventoryMerchant mcinv = (InventoryMerchant) (((CraftInventory)inv).getInventory());
//		EntityPlayer ep = (EntityPlayer) ReflectionUtils.getEntityPlayer((Player)e.getPlayer());
//		int counter = ep.nextContainerCounter();
//		ep.playerConnection.sendPacket(new PacketPlayOutOpenWindow(counter, "minecraft:villager", ichatbasecomponent, mcinv.getSize()));
//		MerchantRecipeList merchantrecipelist = imerchant.getOffers(this);
//		if(merchantrecipelist != null)
//		{
//			PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
//			packetdataserializer.writeInt(counter);
//			merchantrecipelist.a(packetdataserializer);
//			ep.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", packetdataserializer));
//		}
//	}


//	@EventHandler
//	public void a(WeatherChangeEvent e){
//		if(e.toWeatherState()){
//			SquidTask.start();
//		}else{
//			SquidTask.stop();
//		}
//	}

}
