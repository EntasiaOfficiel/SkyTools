package fr.entasia.skytools.commands;

import fr.entasia.cosmetiques.utils.CosmAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CosmCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		if(sender instanceof Player){
			CosmAPI.openCosmMenu((Player) sender);
		}
		return false;
	}
}
