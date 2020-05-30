package fr.entasia.skytools.commands.custom;

import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomEnchantCmd implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player))return false;
		Player p = (Player)sender;
		if(p.hasPermission("skyblock.customenchant")){

			if (args.length==0){
				p.sendMessage("§cEnchantements disponibles : ");
				for(CustomEnchants ca : CustomEnchants.values()){
					p.sendMessage("§c - "+ca.name()+" | ("+ca.name+")");
				}
			}else {
				ItemStack item = p.getInventory().getItemInMainHand();
				if (item == null || item.getType() == Material.AIR) {
					p.sendMessage("§cItem invalide !");
				} else {
					try{
						int level = 1;
						if(args.length>1){
							level = Integer.parseInt(args[1]);
							if(level<1||level>5)throw new NumberFormatException();
						}
						CustomEnchants ca = CustomEnchants.valueOf(args[0].toUpperCase());
						ItemMeta meta = item.getItemMeta();
						ca.enchant(meta, level);
						item.setItemMeta(meta);
					}catch(NumberFormatException e) {
						p.sendMessage("§cNiveau "+args[1]+" invalide !");
					}catch(IllegalArgumentException e) {
						p.sendMessage("§cEnchantement invalide !");
					}
				}
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}
