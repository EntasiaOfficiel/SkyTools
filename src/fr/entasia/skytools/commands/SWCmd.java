package fr.entasia.skytools.commands;

import fr.entasia.skytools.Main;
import fr.entasia.skytools.tasks.SWTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SWCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("skyblock.speedwriter")){
			if(args.length ==0)sender.sendMessage("§cMet un mot !");
			else{
				Main.guessWord = String.join(" ", args);
				SWTask.newWord();
			}

		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}