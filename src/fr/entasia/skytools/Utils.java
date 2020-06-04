package fr.entasia.skytools;

import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.skycore.objs.CodePasser;
import fr.entasia.skytools.objs.ToolPlayer;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.tasks.AirHooksTask;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Utils {


	public static HashMap<UUID, ToolPlayer> playerCache = new HashMap<>();
	public static FireworkMeta[] metas;
	public static FireworkMeta blackmeta;

	public static ArrayList<AirHooksTask> airHookTasks = new ArrayList<>();
	public static HashMap<Entity, NBTComponent> fireworks = new HashMap<>();

	static{
		blackmeta = (FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK);
		blackmeta.addEffect(FireworkEffect.builder().withColor(Color.BLACK).build());
	}

	public static ToolPlayer getPlayer(Player p){
		ToolPlayer tp = playerCache.get(p.getUniqueId());
		if(tp==null){
			tp = new ToolPlayer(p);
			playerCache.put(p.getUniqueId(), tp);
		}
		return tp;
	}


	private static class Bool{
		boolean value;
	}

	public static void updateEffects(Player p){
		ToolPlayer tp = getPlayer(p);
		PlayerInventory inv = p.getInventory();

		updateEffect(p, tp.vision, inv.getHelmet(), CustomEnchants.VISION, PotionEffectType.NIGHT_VISION);
		updateEffect(p, tp.speed, inv.getBoots(), CustomEnchants.SPEED, PotionEffectType.SPEED);
		updateEffect(p, tp.jump, inv.getBoots(), CustomEnchants.JUMP, PotionEffectType.JUMP);
	}

	public static void updateEffect(Player p, MutableBoolean bool, ItemStack item, CustomEnchants enchant, PotionEffectType potionType){
		int l = enchant.getLevel(item);
		if((l==0)==bool.booleanValue()){
			if(bool.booleanValue()){
				bool.setValue(false);
				p.removePotionEffect(potionType);
			}else{
				bool.setValue(true);
				p.addPotionEffect(new PotionEffect(potionType, 99999, 1));
			}
		}
	}
}
