package fr.entasia.skytools.commands.custom;

import fr.entasia.apis.other.ChatComponent;
import fr.entasia.skytools.objs.custom.CustomArrows;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomArrowCmd implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player))return false;
		Player p = (Player)sender;
		if(p.hasPermission("skyblock.customarrow")){

			if (args.length==0){
				p.sendMessage("§cEnchantements disponibles : ");
				ChatComponent cc;
				for(CustomArrows ca : CustomArrows.values()){
					cc = new ChatComponent("§c - "+ca.name()+" | ("+ca.name+")");
					cc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/customarrow "+ca.name()+" "));
					cc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponent.create("§cClique pour appliquer !")));
					p.sendMessage(cc.create());
				}
			}else{
				ItemStack item = p.getInventory().getItemInMainHand();
				if (item == null || item.getType() == Material.AIR) {
					p.sendMessage("§cItem invalide !");
				} else {
					try{
						CustomArrows ca = CustomArrows.valueOf(args[0].toUpperCase());
						ca.enchant(item);
						p.sendMessage("§aEnchantement fait avec succès !");
					}catch(IllegalArgumentException e){
						p.sendMessage("§cEnchantement invalide !");
					}
				}
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}
