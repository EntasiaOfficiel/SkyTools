package fr.entasia.skytools.commands;

import fr.entasia.skytools.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkyToolsCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player))return false;
		if(sender.hasPermission("plugin.skytools")){
			Player p = (Player)sender;
			if(args.length==0)p.sendMessage("§cMet un argument !");
			else{
				if(args[0].equalsIgnoreCase("reload")) {
					try{
						Main.loadConfigs();
						p.sendMessage("§aConfiguration rechargée avec succès !");
					}catch(Exception e){
						e.printStackTrace();
						p.sendMessage("§cConfiguration rechargée avec erreur ! ( voir console )");
					}
				}else{
					p.sendMessage("§cArgument invalide ! Arguments disponibles :\n" +
							"- reload\n"+
							"- setwarp");
				}
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}
