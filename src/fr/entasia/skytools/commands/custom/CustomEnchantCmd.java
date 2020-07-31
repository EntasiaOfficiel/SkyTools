package fr.entasia.skytools.commands.custom;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantCmd implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player))return false;
		Player p = (Player)sender;
		if(p.hasPermission("skyblock.customenchant")){

			if (args.length==0){
				p.sendMessage("§cEnchantements disponibles : ");
				ChatComponent cc;
				for(CustomEnchants ca : CustomEnchants.values()){
					cc = new ChatComponent("§c - "+ca.name()+" | ("+ca.name+")");
					cc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/customenchant "+ca.name()+" "));
					cc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponent.create("§cClique pour appliquer !")));
					p.sendMessage(cc.create());
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
						if(ca.enchant(item, level)){
							p.sendMessage("§aEnchantement fait avec succès !");
						}else{
							p.sendMessage("§cNiveau maximal possible : "+ca.maxlvl);
						}
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
