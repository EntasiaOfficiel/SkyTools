package fr.entasia.skytools.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTManager;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.utils.ItemUtils;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.apis.utils.TextUtils;
import fr.entasia.skycore.apis.BaseAPI;
import fr.entasia.skycore.apis.ISPLink;
import fr.entasia.skycore.apis.OthersAPI;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.ToolPlayer;
import fr.entasia.skytools.objs.Warp;
import fr.entasia.skytools.objs.villagers.Villagers;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

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
	public void a(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			boolean canDoIt = false;
			for(ISPLink link : BaseAPI.getIsland(e.getEntity().getLocation()).getMembers()){
				if(link.sp.equals(BaseAPI.getSkyPlayer(p.getUniqueId()))) canDoIt=true;
			}
			if(!canDoIt&&!OthersAPI.isMasterEdit(p) && !p.getWorld().getName().equalsIgnoreCase("spawn")){
				p.sendMessage("§cTu n'es pas membre de cette ile !");
				e.setCancelled(true);
			}

		}
		if(e.getDamager() instanceof Arrow){
			ProjectileSource projectileSource = ((Arrow) e.getDamager()).getShooter();
			if(projectileSource instanceof Player){
				Player p = (Player) projectileSource;
				boolean damagerIsland = false;
				boolean victimIsland = false;

				if(e.getEntity() instanceof Player){
					Player victim = (Player) e.getEntity();
					for(ISPLink link : BaseAPI.getIsland(e.getEntity().getLocation()).getMembers()){

						if(link.sp.equals(BaseAPI.getSkyPlayer(p.getUniqueId()))) damagerIsland=true;
						if(link.sp.equals(BaseAPI.getOnlineSP(victim))) victimIsland=true;
					}
					if((!victimIsland || !damagerIsland )&&!OthersAPI.isMasterEdit(p) && !p.getWorld().getName().equalsIgnoreCase("spawn") && p!=victim){

						p.sendMessage("§cTu ne peux pas faire de dégat à cette personne !");
						e.setCancelled(true);
					}


				}


			}
		}
	}

	@EventHandler
	public static void onJump(PlayerJumpEvent e){
		if(e.getPlayer().isSneaking()&&e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType()==Material.FIREWORK_ROCKET){
			ItemStack a = e.getPlayer().getInventory().getHelmet();
			FireworkMeta meta = (FireworkMeta) a.getItemMeta();
			a.subtract();
			e.getPlayer().getInventory().setHelmet(a);

			Firework fw = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), Firework.class);
			fw.setFireworkMeta(meta);

			final float vec;
			int life;
			switch(meta.getPower()){
				case 3:
					life = 15;
					vec = 3.7f;
					break;
				case 2:
					life = 10;
					vec = 2.4f;
					break;
				default:
					life = 5;
					vec = 1.7f;
					break;
			}
			NBTComponent nbt = new NBTComponent();
			nbt.setValue(NBTTypes.Int, "LifeTime", life);
			EntityNBT.addNBT(fw, nbt);
			e.getPlayer().addPassenger(fw);

			new BukkitRunnable() {
				public void run() {
					e.getPlayer().setVelocity(new Vector(0, vec, 0));
					e.getPlayer().setMetadata("fwJump", new FixedMetadataValue(Main.main, System.currentTimeMillis()));
				}
			}.runTask(Main.main);
		}
	}

	@EventHandler
	public void a(EntityDamageEvent e){
		if(e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL){
			List<MetadataValue> m = e.getEntity().getMetadata("fwJump");
			if(m.size()==0)return;
			if(System.currentTimeMillis()-m.get(0).asLong()<20000){
				e.setCancelled(true);
			}
			e.getEntity().removeMetadata("fwJump", Main.main);
		}
	}

	@EventHandler
	public static void onInteract(PlayerInteractEvent e) {
		if(e.hasBlock()){

			if(Tag.SIGNS.isTagged(e.getClickedBlock().getType())){
				Player p = e.getPlayer();
				Sign s = (Sign)e.getClickedBlock().getState();
				if(s.getLine(0).equals("§9[§7Warp§9]")) {
					Warp w = Warp.getWarp(s.getLine(1).substring(2).toLowerCase());
					if (w == null) {
						p.sendMessage("§cCe warp n'existe plus !");
						e.getClickedBlock().setType(Material.AIR);
						e.getClickedBlock().getWorld().dropItem(e.getClickedBlock().getLocation(), new ItemStack(e.getClickedBlock().getType()));
					} else {
						if(e.getAction()== Action.RIGHT_CLICK_BLOCK) w.teleport(p, true);
						else if(e.getAction()== Action.LEFT_CLICK_BLOCK){
							p.sendMessage("\n§eWarp "+ TextUtils.firstLetterUpper(w.name)+
									"\n"+String.join("\n", w.desc)+
									"\n§eClic droit pour se téléporter !\n ");
						}
					}
				}else if (s.getLine(0).equals("§8[§7Poubelle§8]")){
					e.setCancelled(true);
					p.openInventory(Bukkit.createInventory(null, 54, "§8Poubelle"));
				}
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
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(e.getBlock().getType()));
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

//	@EventHandler
//	public void a(EntitySpawnEvent e){
//		if(e.getEntity().getType()==EntityType.VILLAGER){
//			Villager v = (Villager)e.getEntity();
//			Villagers vi = Villagers.getOne();
//			vi.apply(v);
//		}
//	}

	@EventHandler
	public void a(VillagerAcquireTradeEvent e){
		e.setCancelled(true);

		testUpgrade(e.getEntity());

	}




	@EventHandler
	public void a(VillagerCareerChangeEvent e){
		Villager v = e.getEntity();
		boolean asProfession=false;
		for(String s : v.getScoreboardTags()){
			if (s.equalsIgnoreCase("asProfession")) {
				asProfession = true;
				break;
			}
		}

		if(asProfession){
			e.setCancelled(true);
		}else{


			ArrayList<Villagers> villagersList = new ArrayList<>();


			for(Villagers vi : Villagers.values()){
				if(vi.p == e.getProfession()){

					villagersList.add(vi);
				}
			}
			if(villagersList.isEmpty()){
				e.setCancelled(true);
			}else{
				Random random = new Random();
				Villagers vi = villagersList.get(random.nextInt(villagersList.size()));
				vi.apply(v);

			}



		}
	}

	public static void testUpgrade(AbstractVillager v) {


		List<MetadataValue> meta = v.getMetadata("lastUpgrade");
		if(meta.size()!=0){
			if(System.currentTimeMillis()-meta.get(0).asLong()<1000){
				return;
			}
		}
		v.setMetadata("lastUpgrade", new FixedMetadataValue(Main.main, System.currentTimeMillis()));


		int current=0;
		for(String s : v.getScoreboardTags()){
			if(s.startsWith("CareerLevel-")){
				current= Integer.parseInt(s.split("-")[1]);
			}
		}
		//Object o = nbt.getValue(NBTTypes.Int, "CareerLevel");
		//int current;
		//if (o == null) current = 0;
		//else current = (int) o;
		Villagers vi = Villagers.getType(v);

		if (vi == null) {
			ServerUtils.permMsg("log.upgradenpc", "§cUne erreur s'est produite lors de l'upgrade d'un villageois ! (career not found)." +
					" Infos :§6" + v.getLocation());
		} else {




			int newLvl = current + 1;
			if (newLvl >= vi.levels.length) return;
			List<MerchantRecipe> list2 = new ArrayList<>(v.getRecipes());
			vi.addToList(list2, newLvl);
			v.getScoreboardTags().clear();
			v.addScoreboardTag("npctype-"+vi.id);
			v.addScoreboardTag("asProfession");
			v.addScoreboardTag("CareerLevel-"+newLvl);
			v.setRecipes(list2);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void netherWart(PlayerInteractEvent e) {
		ItemStack i = e.getItem();
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.NETHER_WART) {
				if (i != null && i.getType() == Material.BLAZE_POWDER) {
					Ageable age = (Ageable) e.getClickedBlock().getBlockData();
					int a = age.getAge();
					if (a != 3){
						age.setAge(a+1);
						e.getClickedBlock().setBlockData(age);
						if (p.getGameMode() != GameMode.CREATIVE) {
							p.getInventory().getItemInMainHand().subtract(1);
						}
					}
				}
			}
		}
	}


//	@EventHandler
//	public void a(PlayerInteractEntityEvent e) {
//		if(e.getHand()!= EquipmentSlot.HAND)return;
//		if(e.getRightClicked().getType()==EntityType.VILLAGER){
//			if(Main.r.nextInt(50)==0){
//				e.getPlayer().sendMessage("§bMerci à §3wishdrow§b pour les trades custom des PNJ ! :)");
//			}
//		}
//	}

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
