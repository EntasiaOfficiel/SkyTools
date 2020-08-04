package fr.entasia.skytools.events;

import com.mojang.authlib.GameProfile;
import fr.entasia.apis.utils.ItemUtils;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.DirectionUtils;
import fr.entasia.skytools.objs.custom.CustomSkulls;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SkullEvents implements Listener {

	public static boolean isLeaves(Material m){
		return m==Material.LEAVES||m==Material.LEAVES_2;
	}

	public static void particle(Location loc){
		loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.getX()+0.5, loc.getY()+0.5, loc.getZ()+0.5, 25, 0.4, 0.4, 0.4, 10);
	}


	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void a(PlayerInteractEvent e){
		if(e.getHand()==EquipmentSlot.HAND&&e.getAction()==Action.RIGHT_CLICK_BLOCK&&e.getItem()!=null&&e.getItem().getType()==Material.INK_SACK&&e.getItem().getDurability()==15){
			if(e.getClickedBlock().getType()==Material.GRASS) {
				if (e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR) {
					e.getItem().subtract();

					Location loc = e.getClickedBlock().getLocation();
					int minx = loc.getBlockX() - 3;
					int miny = loc.getBlockY() - 2;
					int minz = loc.getBlockZ() - 3;

					int maxx = loc.getBlockX() + 3;
					int maxy = loc.getBlockY() + 2;
					int maxz = loc.getBlockZ() + 3;

					Block b;
					for (int x = minx; x < maxx; x++) {
						loc.setX(x);
						for (int y = miny; y < maxy; y++) {
							loc.setY(y);
							for (int z = minz; z < maxz; z++) {
								loc.setZ(z);

								b = loc.getBlock();
								if (b.getType() == Material.AIR) {
									if (loc.clone().add(0, -1, 0).getBlock().getType() == Material.GRASS) {
										int ra = Main.r.nextInt(10);
										if (ra == 0) {
											b.setType(Material.SKULL);
											b.setData((byte) 1);
											ItemUtils.setTexture(b, CustomSkulls.SALADE.profile);
											particle(b.getLocation());
										} else if (ra == 1) {
											b.setType(Material.SKULL);
											b.setData((byte) 1);
											ItemUtils.setTexture(b, CustomSkulls.TOMATE.profile);
											particle(b.getLocation());
										}
									}
								}
							}
						}
					}
				}
			}else if(isLeaves(e.getClickedBlock().getType())){
				e.getItem().subtract();
				Location loc = e.getClickedBlock().getLocation();
				int minx = loc.getBlockX() - 3;
				int miny = loc.getBlockY() - 2;
				int minz = loc.getBlockZ() - 3;

				int maxx = loc.getBlockX() + 3;
				int maxy = loc.getBlockY() + 2;
				int maxz = loc.getBlockZ() + 3;

				Block b;
				for (int x = minx; x < maxx; x++) {
					loc.setX(x);
					for (int y = miny; y < maxy; y++) {
						loc.setY(y);
						for (int z = minz; z < maxz; z++) {
							loc.setZ(z);

							b = loc.getBlock();
							if(isLeaves(b.getType())){
								int ra = Main.r.nextInt(10);
								if (ra == 0) {
									Block lb;
									for(DirectionUtils d : DirectionUtils.values()){
										lb = b.getLocation().clone().add(d.vector).getBlock();
										if(lb.getType()==Material.AIR){
											place(lb, d, CustomSkulls.POMME);
											particle(b.getLocation());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static void place(Block b, DirectionUtils face, CustomSkulls skull){
		b.setType(Material.SKULL);
		ItemUtils.setTexture(b, skull.profile);
		b.setData(face.data);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void b(PlayerInteractEvent e){
		if(e.getHand()== EquipmentSlot.HAND&&e.getAction()==Action.RIGHT_CLICK_BLOCK&&e.getItem()!=null&&e.getItem().getType()==Material.APPLE){
			DirectionUtils d = DirectionUtils.get(e.getBlockFace());
			if(d!=null){
				Block b = e.getClickedBlock().getLocation().add(d.vector).getBlock();
				if(b.getType()==Material.AIR){
					e.getItem().subtract();
					place(b, d, CustomSkulls.POMME);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void b(BlockBreakEvent e){
		if(e.getPlayer().getGameMode()== GameMode.SURVIVAL&&e.getBlock().getType()==Material.SKULL){
			GameProfile profile = ItemUtils.getProfile(e.getBlock());
			if(profile==null)return;
			CustomSkulls cs = CustomSkulls.getByUUID(profile.getId());
			if(cs==null)return;

			e.setDropItems(false);
			if(cs==CustomSkulls.POMME)e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
			else{
				e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), cs.genItem(1));
			}
		}
	}
}