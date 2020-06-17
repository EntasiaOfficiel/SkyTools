package fr.entasia.skytools.tasks;

import fr.entasia.skytools.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SWTask extends BukkitRunnable {

	public void run(){
		int n;
		if(Main.guessWord==null){
			if((n=Bukkit.getOnlinePlayers().size())<2)return;
			StringBuilder a = new StringBuilder();
			for(int i=0;i<7+n/3;i++){
				if(Main.random.nextInt(4)==0){
					a.append((char) (Main.random.nextInt(26) + 'A'));
				}else{
					a.append((char) (Main.random.nextInt(26) + 'a'));
				}
				if(i==15)break;
			}
			Main.guessWord = a.toString();
		}
		newWord();
	}

	public static void newWord(){
		Bukkit.broadcastMessage("§eSpeedWriter §7» §6Nouveau mot à écrire ! §d"+Main.guessWord+"§6\nMarquez le dans le chat pour gagner une récompense !");
	}
}
