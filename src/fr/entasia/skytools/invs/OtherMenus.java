package fr.entasia.skytools.invs;

import fr.entasia.apis.menus.MenuClickEvent;
import fr.entasia.apis.menus.MenuCreator;
import fr.entasia.apis.menus.MenuFlag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OtherMenus {

	// MENU WARPS

	public static MenuCreator toolsMenu = new MenuCreator(new MenuFlag[]{MenuFlag.AllItemsTrigger}){

		public void onMenuClick(MenuClickEvent e){
			e.player.setItemOnCursor(e.item);
		}
	};

	public static void WarpsOpen(Player p){
		Inventory inv = toolsMenu.createInv(6, "§cOutils :");

//		ItemStack item = new ItemStack(Material.SNOW_BALL);
//		inv.setItem();

		p.openInventory(inv);
	}
}
