package fr.entasia.skytools.events;

import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.ItemNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.other.InstantFirework;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;


public class FireworksEvents implements Listener {


	@EventHandler
	public void a(PrepareItemCraftEvent e) {
		Recipe r = e.getRecipe();
		if(r==null)return;
		ItemStack item = r.getResult();
		if (item.getType() == Material.SNOWBALL) {
			char type = '0';
			NBTComponent nbt = null;
			for (ItemStack i : e.getInventory().getContents()) {
				if (i.getType() == Material.FIREWORK_STAR) {
					nbt = new NBTComponent("{Fireworks:{Explosions:[" + ItemNBT.getNBTSafe(i).getComponent("Explosion") + "]}}");
					type = '1';
					break;
				} else if (i.getType() == Material.FIREWORK_ROCKET) {
					nbt = ItemNBT.getNBTSafe(i);
					type = '0';
					break;
				}
			}
			if (nbt != null) {
				NBTComponent injected = new NBTComponent();
				injected.setValue(NBTTypes.String, "type", Character.toString(type));
				injected.setValue(NBTTypes.String, "id", "minecraft:fireworks");
				injected.fusion(new NBTComponent("{Count:1b,Damage:0s}"));
				injected.setComponent("tag", nbt);

				nbt = ItemNBT.getNBTSafe(item);
				if(nbt==null)nbt = new NBTComponent();
				nbt.setComponent("entasia", injected);

				ItemNBT.setNBT(item, nbt);
				e.getInventory().setResult(item);
			}
		}
	}

	@EventHandler
	public void a(ProjectileLaunchEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getType() == EntityType.SNOWBALL&&pr.getShooter() instanceof Player) {
			ItemStack item = ((Player) pr.getShooter()).getInventory().getItemInMainHand();
			if(item.getType()==Material.SNOWBALL){
				NBTComponent nbt = ItemNBT.getNBTSafe(item).getComponent("entasia");
				if(nbt==null)return;

				Player p = (Player)pr.getShooter();
				List<MetadataValue> list = p.getMetadata("cdFirework");
				if(list.size()==0||System.currentTimeMillis()-list.get(0).asLong()>500){
					p.setMetadata("cdFirework", new FixedMetadataValue(Main.main, System.currentTimeMillis()));
					Utils.fireworks.put(pr, nbt);
				}else e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void a(ProjectileHitEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getType() == EntityType.SNOWBALL&&pr.getShooter() instanceof Player) {
			NBTComponent inject = Utils.fireworks.remove(pr);
			if(inject!=null){
				Firework fw = (Firework) pr.getWorld().spawnEntity(pr.getLocation(), EntityType.FIREWORK);
				NBTComponent nbt = EntityNBT.getNBT(fw);
				nbt.setComponent("FireworksItem", inject);
				EntityNBT.setNBT(fw, nbt);
				if("1".equals(inject.getValue(NBTTypes.String, "type"))){
					InstantFirework.explode(fw);
				}
			}
		}
	}



	@EventHandler
	public void firework(InventoryClickEvent e){
		if(e.getWhoClicked().getOpenInventory().getType() == InventoryType.CRAFTING){
			if(e.getClick()== ClickType.LEFT||e.getClick()==ClickType.RIGHT) {
				if (e.getSlot() == 39){
					if(e.getCursor().getType() == Material.FIREWORK_ROCKET){
						e.setCancelled(true);
						if(e.getWhoClicked().getInventory().getHelmet()==null){ // aucun item en casque , on set juste le curseur
							e.getWhoClicked().getInventory().setHelmet(e.getCursor());
							e.setCursor(new ItemStack(Material.AIR));
						}else{
							if(e.getCursor().isSimilar(e.getWhoClicked().getInventory().getHelmet())){
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
				if (e.getCurrentItem()!=null&&e.getCurrentItem().getType() == Material.FIREWORK_ROCKET && e.getSlot() != 39){
					// mise en place du système de reste ( geant size )
					ItemStack helmet = e.getWhoClicked().getInventory().getHelmet();
					ItemStack reste = new ItemStack(Material.AIR);
					if(helmet==null){ // cas 1 du système de reste
						e.setCancelled(true);
						helmet = e.getCurrentItem();
					}else{
						if(e.getCurrentItem().isSimilar(helmet)){
							// cas 2 du système de reste
							int total = e.getWhoClicked().getInventory().getHelmet().getAmount();
							if(total==64)return;
							e.setCancelled(true);
							total += e.getCurrentItem().getAmount();
							if(total>64){
								reste = e.getCurrentItem();
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


}