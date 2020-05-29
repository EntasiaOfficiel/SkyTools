package fr.entasia.skytools.commands.custom;

import fr.entasia.skytools.objs.custom.CustomArrows;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomArrowCmd implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player))return false;
		Player p = (Player)sender;
		if(p.hasPermission("skyblock.customarrow")){

			if (args.length==0){
				p.sendMessage("§cEnchantements disponibles : ");
				for(CustomArrows ca : CustomArrows.values()){
					p.sendMessage("§c - "+ca.name()+" | ("+ca.name+")");
				}
			}else{
				try{
					CustomArrows ca = CustomArrows.valueOf(args[0]);
					ItemStack item = new ItemStack(Material.ARROW);
					ItemMeta meta = item.getItemMeta();
					ca.enchant(meta);
					item.setItemMeta(meta);
					p.getInventory().addItem(item);
				}catch(IllegalArgumentException e){
					p.sendMessage("§cEnchantement invalide !");
				}
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}
