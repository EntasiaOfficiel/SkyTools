package fr.entasia.skytools.invs;

import fr.entasia.apis.menus.MenuClickEvent;
import fr.entasia.apis.menus.MenuCreator;
import fr.entasia.skytools.objs.Warp;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class BaseMenus {

	// MENU WARPS

	public static MenuCreator warpsMenu = new MenuCreator(){

		public void onMenuClick(MenuClickEvent e){

			Warp w = null;
			switch(e.item.getType()){
				case ELYTRA:
					w = Warp.getWarp("spawn");
					break;
				case NOTE_BLOCK:
					w = Warp.getWarp("crates");
					break;
				case EMERALD:
					w = Warp.getWarp("shop");
					break;
				case OAK_SAPLING:
					w = Warp.getWarp("islands");
					break;
				case IRON_AXE:
					w = Warp.getWarp("arene");
					break;
				default:
					e.player.sendMessage("§cCette option n'est pas disponible pour le moment !");
			}
			if(w!=null)w.teleport(e.player, true);

			e.player.closeInventory();
		}
	};

	public static void WarpsOpen(Player p){
		Inventory inv = warpsMenu.createInv(6, "§aWarps");
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		for(int i : new int[]{28,30,32,34,38,40,42})inv.setItem(i, item);
		for(int i=0;i<10;i++)inv.setItem(i+9, item);
		item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
		for(int i=0;i<4;i++)inv.setItem(i+12, item);

		item = new ItemStack(Material.ELYTRA);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSpawn");
		meta.setLore(getDesc("spawn"));
		item.setItemMeta(meta);
		inv.setItem(4, item);

		item = new ItemStack(Material.OAK_SAPLING);
		meta = item.getItemMeta();
		meta.setDisplayName("§7Iles");
		meta.setLore(getDesc("islands"));
		item.setItemMeta(meta);
		inv.setItem(13, item);

		p.openInventory(inv);
	}

	private static List<String> getDesc(String warp){
		if(warp.equals("spawn"))return Collections.singletonList("§aLe spawn du Skyblock, tout simplement");
		else{
			Warp a = Warp.getWarp(warp);
			if(a==null)return Collections.singletonList("§cProblème de chargement de ce warp !");
			else return a.desc;
		}
	}
}
