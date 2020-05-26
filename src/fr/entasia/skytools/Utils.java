package fr.entasia.skytools;

import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.objs.ToolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class Utils {

	public static HashMap<UUID, ToolPlayer> playerCache = new HashMap<>();
	public static FireworkMeta[] metas;
	public static FireworkMeta blackmeta;

	static{
		blackmeta = (FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK);
		blackmeta.addEffect(FireworkEffect.builder().withColor(Color.BLACK).build());
	}

	public static void checkEffects(Player p){
		ToolPlayer tp = playerCache.get(p.getUniqueId());
		PlayerInventory inv = tp.p.getInventory();

		checkEffect(tp, inv.getHelmet(), CustomEnchants.VISION, PotionEffectType.NIGHT_VISION);
		checkEffect(tp, inv.getBoots(), CustomEnchants.SPEED, PotionEffectType.SPEED);
		checkEffect(tp, inv.getBoots(), CustomEnchants.JUMP, PotionEffectType.JUMP);
	}

	public static void checkEffect(ToolPlayer tp, ItemStack item, CustomEnchants enchant, PotionEffectType potionType){
		int l = enchant.getLevel(item);
		if((l==0)==tp.vision){
			if(tp.vision){
				tp.vision = false;
				tp.p.removePotionEffect(potionType);
			}else{
				tp.vision = true;
				tp.p.addPotionEffect(new PotionEffect(potionType, 99999, 1));
			}
		}
	}
}
