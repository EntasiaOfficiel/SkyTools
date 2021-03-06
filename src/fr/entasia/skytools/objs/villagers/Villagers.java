package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.other.Randomiser;
import fr.entasia.errors.EntasiaException;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.entity.Villager.Profession;

public enum Villagers {

	PRETRE(0, Profession.CLERIC, 10, 1, 0,
		new TradeLevel(
			new Trade(new TradeItem(Material.BREAD).name("§7Pain divin")).need(new TradeItem(Material.EMERALD)),
			new Trade(new TradeItem(Material.EXPERIENCE_BOTTLE, 5)).need(new TradeItem(Material.GOLD_INGOT, 2)),
			new Trade(new TradeItem(Material.BREWING_STAND)).need(new TradeItem(Material.GLASS_BOTTLE, 64)).need(new TradeItem(Material.GOLD_INGOT, 24)),
			new Trade(new TradeItem(Material.BLAZE_POWDER, 5)).need(new TradeItem(Material.BREAD, 64))
		),
		new TradeLevel(
			new Trade(new TradeItem(Material.GLASS_BOTTLE, 32)).need(new TradeItem(Material.BREAD, 32)),
			new Trade(new TradeItem(Material.DRAGON_BREATH)).need(new TradeItem(Material.EXPERIENCE_BOTTLE, 24)).need(new TradeItem(Material.GOLD_INGOT, 16))
		),
		new TradeLevel(
			new Trade(new TradeItem(Material.CLOCK)).need(new TradeItem(Material.GOLD_INGOT, 32)).need(new TradeItem(Material.EXPERIENCE_BOTTLE, 16)),
			new Trade(new TradeItem(Material.EXPERIENCE_BOTTLE, 64)).need(new TradeItem(Material.GLASS_BOTTLE, 64)).need(new TradeItem(Material.GOLD_INGOT, 8)),
			new Trade(new TradeItem(Material.RED_DYE).name("§cCoeur")).need(new TradeItem(Material.ROTTEN_FLESH, 64)).need(new TradeItem(Material.GOLDEN_APPLE))
		),
		new TradeLevel(
			new Trade(new TradeItem(Material.TOTEM_OF_UNDYING)).need(new TradeItem(Material.EMERALD, 24)),
			new Trade(new TradeItem(Material.EXPERIENCE_BOTTLE, 64)).need(new TradeItem(Material.GLASS_BOTTLE, 64)).need(new TradeItem(Material.LAPIS_ORE, 8))
		)
	),
	FORGERON_OUTILS(1, Profession.TOOLSMITH, 10, 1, 1,
		new TradeLevel(
			new Trade(new TradeItem(Material.IRON_PICKAXE)).need(new TradeItem(Material.COBBLESTONE, 64)).need(new TradeItem(Material.IRON_INGOT)),
			new Trade(new TradeItem(Material.DIAMOND_PICKAXE)).need(new TradeItem(Material.EMERALD, 2)),
			new Trade(new TradeItem(Material.IRON_SHOVEL)).need(new TradeItem(Material.COBBLESTONE, 64, false)),
			new Trade(new TradeItem(Material.DIAMOND_SHOVEL)).need(new TradeItem(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_PICKAXE)).need(new TradeItem(Material.IRON_INGOT, 16)),
				new Trade(new TradeItem(Material.DIAMOND_PICKAXE).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.IRON_INGOT, 24)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL)).need(new TradeItem(Material.IRON_INGOT, 16)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.IRON_INGOT, 24))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_PICKAXE).enchant(Enchantment.DIG_SPEED, 3)).need(new TradeItem(Material.IRON_INGOT, 16)),
				new Trade(new TradeItem(Material.IRON_PICKAXE).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 16)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 24)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL).enchant(Enchantment.DIG_SPEED, 3)).need(new TradeItem(Material.IRON_INGOT, 16)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 16)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL).enchant(Enchantment.DIG_SPEED, 2)).need(new TradeItem(Material.IRON_INGOT, 24))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 2)).need(new TradeItem(Material.EMERALD)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL).enchant(Enchantment.DIG_SPEED, 1)).need(new TradeItem(Material.EMERALD)),
				new Trade(new TradeItem(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 2).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.EMERALD, 3)),
				new Trade(new TradeItem(Material.DIAMOND_SHOVEL).enchant(Enchantment.DIG_SPEED, 2).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.EMERALD)).need(new TradeItem(Material.DIAMOND_PICKAXE)),
				new Trade(new TradeItem(Material.EMERALD)).need(new TradeItem(Material.DIAMOND_SHOVEL)),
				new Trade(new TradeItem(Material.IRON_INGOT, 24)).need(new TradeItem(Material.DIAMOND_PICKAXE)),
				new Trade(new TradeItem(Material.IRON_INGOT, 24)).need(new TradeItem(Material.DIAMOND_SHOVEL))
		)
	),




	GOOGLE_MAP(2, Profession.LIBRARIAN, 10, 1, 0,
		new TradeLevel(
				new Trade(new TradeItem(Material.PAPER, 24)).need(new TradeItem(Material.GOLD_INGOT)),
				new Trade(new TradeItem(Material.PAPER, 24)).need(new TradeItem(Material.IRON_INGOT, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.PAPER, 16)).need(new TradeItem(Material.IRON_INGOT)),
				new Trade(new TradeItem(Material.PAPER, 64)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.MAP)).need(new TradeItem(Material.IRON_INGOT))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_INGOT)).need(new TradeItem(Material.MAP, 2)),
				new Trade(new TradeItem(Material.MAP, 4)).need(new TradeItem(Material.IRON_INGOT, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.EMERALD)).need(new TradeItem(Material.MAP, 16)),
				new Trade(new TradeItem(Material.IRON_INGOT, 8)).need(new TradeItem(Material.MAP, 16))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.EMERALD, 2)).need(new TradeItem(Material.PAPER, 64)).need(new TradeItem(Material.MAP, 32)),
				new Trade(new TradeItem(Material.EMERALD, 4)).need(new TradeItem(Material.MAP, 64))
		)
	),
	FISHEMAN(3, Profession.FARMER, 10, 1, 1,
		new TradeLevel(
				new Trade(new TradeItem(Material.TROPICAL_FISH)).need(new TradeItem(Material.EMERALD)),
				new Trade(new TradeItem(Material.PUFFERFISH)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.COD, 8)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.SALMON, 8)).need(new TradeItem(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.TROPICAL_FISH, 5)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.PUFFERFISH, 10)).need(new TradeItem(Material.EMERALD, 6)),
				new Trade(new TradeItem(Material.COD, 32)).need(new TradeItem(Material.EMERALD, 5)),
				new Trade(new TradeItem(Material.SALMON, 32)).need(new TradeItem(Material.EMERALD, 5))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_INGOT, 4)).need(new TradeItem(Material.COD, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 4)).need(new TradeItem(Material.SALMON, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 5)).need(new TradeItem(Material.COOKED_SALMON, 48)),
				new Trade(new TradeItem(Material.IRON_INGOT, 5)).need(new TradeItem(Material.COOKED_COD, 48))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_INGOT, 6)).need(new TradeItem(Material.COD, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 6)).need(new TradeItem(Material.SALMON, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 6)).need(new TradeItem(Material.COOKED_COD, 16)).need(new TradeItem(Material.COOKED_SALMON))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.EMERALD, 2)).need(new TradeItem(Material.TROPICAL_FISH, 64)),
				new Trade(new TradeItem(Material.EMERALD, 2)).need(new TradeItem(Material.PUFFERFISH, 64)),
				new Trade(new TradeItem(Material.EMERALD, 5)).need(new TradeItem(Material.TROPICAL_FISH, 64)).need(new TradeItem(Material.PUFFERFISH, 64))
		)
	),
	CHASSEUR(4, Profession.BUTCHER, 10, 1, 1,
		new TradeLevel(
				new Trade(new TradeItem(Material.LEATHER, 16)).need(new TradeItem(Material.EMERALD)),
				new Trade(new TradeItem(Material.LEATHER, 16)).need(new TradeItem(Material.IRON_INGOT, 6)),
				new Trade(new TradeItem(Material.RABBIT_HIDE, 32)).need(new TradeItem(Material.EMERALD)),
				new Trade(new TradeItem(Material.RABBIT_HIDE, 32)).need(new TradeItem(Material.IRON_INGOT, 6))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.LEATHER, 24)).need(new TradeItem(Material.EMERALD)),
				new Trade(new TradeItem(Material.LEATHER, 24)).need(new TradeItem(Material.IRON_INGOT, 6)),
				new Trade(new TradeItem(Material.RABBIT_HIDE, 48)).need(new TradeItem(Material.EMERALD)),
				new Trade(new TradeItem(Material.RABBIT_HIDE, 48)).need(new TradeItem(Material.IRON_INGOT, 6))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.RABBIT_FOOT)).need(new TradeItem(Material.EMERALD, 6)),
				new Trade(new TradeItem(Material.LEATHER_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 10)),
				new Trade(new TradeItem(Material.LEATHER_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 10)),
				new Trade(new TradeItem(Material.LEATHER_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 10)),
				new Trade(new TradeItem(Material.LEATHER_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 10))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.EMERALD, 2)).need(new TradeItem(Material.RABBIT_FOOT)),
				new Trade(new TradeItem(Material.IRON_INGOT, 4)).need(new TradeItem(Material.LEATHER, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.RABBIT_HIDE, 64))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.LEATHER_HELMET).enchant(Enchantment.OXYGEN, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 15)),
				new Trade(new TradeItem(Material.LEATHER_CHESTPLATE).enchant(Enchantment.THORNS, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 15)),
				new Trade(new TradeItem(Material.LEATHER_LEGGINGS).enchant(Enchantment.THORNS, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 15)),
				new Trade(new TradeItem(Material.LEATHER_BOOTS).enchant(Enchantment.WATER_WORKER, 4).enchant(Enchantment.PROTECTION_FALL, 4).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new TradeItem(Material.EMERALD, 15)),
				new Trade(new TradeItem(Material.RABBIT_FOOT, 32)).need(new TradeItem(Material.EMERALD, 16))
		)
	),
	CHARCUTIER(5, Profession.BUTCHER, 10, 1, 1,
		new TradeLevel(
				new Trade(new TradeItem(Material.COOKED_CHICKEN, 4)).need(new TradeItem(Material.CHICKEN, 8)),
				new Trade(new TradeItem(Material.COOKED_RABBIT, 4)).need(new TradeItem(Material.RABBIT, 8)),
				new Trade(new TradeItem(Material.COOKED_MUTTON, 4)).need(new TradeItem(Material.MUTTON, 8)),
				new Trade(new TradeItem(Material.COOKED_BEEF, 4)).need(new TradeItem(Material.BEEF, 8)),
				new Trade(new TradeItem(Material.COOKED_PORKCHOP, 4)).need(new TradeItem(Material.PORKCHOP, 8))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.CHICKEN)).need(new TradeItem(Material.ROTTEN_FLESH, 32)),
				new Trade(new TradeItem(Material.BEEF)).need(new TradeItem(Material.ROTTEN_FLESH, 32)),
				new Trade(new TradeItem(Material.RABBIT)).need(new TradeItem(Material.ROTTEN_FLESH, 32)),
				new Trade(new TradeItem(Material.COOKED_PORKCHOP)).need(new TradeItem(Material.ROTTEN_FLESH, 32)),
				new Trade(new TradeItem(Material.MUTTON)).need(new TradeItem(Material.ROTTEN_FLESH, 32))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.COOKED_CHICKEN)).need(new TradeItem(Material.ROTTEN_FLESH, 64)),
				new Trade(new TradeItem(Material.COOKED_RABBIT)).need(new TradeItem(Material.ROTTEN_FLESH, 64)),
				new Trade(new TradeItem(Material.COOKED_MUTTON)).need(new TradeItem(Material.ROTTEN_FLESH, 64))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.COOKED_BEEF)).need(new TradeItem(Material.ROTTEN_FLESH, 64)),
				new Trade(new TradeItem(Material.COOKED_PORKCHOP)).need(new TradeItem(Material.ROTTEN_FLESH, 64)),
				new Trade(new TradeItem(Material.COOKED_CHICKEN).name("§6KFC")).need(new TradeItem(Material.IRON_INGOT, 6)),
				new Trade(new TradeItem(Material.BAKED_POTATO).name("§6Frites McDo")).need(new TradeItem(Material.IRON_INGOT, 6))
		)
	),
	FLECHIER(6, Profession.FARMER, 10, 1, 1,
		new TradeLevel(
				new Trade(new TradeItem(Material.BOW)).need(new TradeItem(Material.IRON_INGOT, 2)),
				new Trade(new TradeItem(Material.ARROW, 32)).need(new TradeItem(Material.IRON_INGOT, 2)),
				new Trade(new TradeItem(Material.IRON_INGOT)).need(new TradeItem(Material.ARROW, 64)),
				new Trade(new TradeItem(Material.BOW)).need(new TradeItem(Material.ARROW, 48))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.INSTANT_DAMAGE)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.STRENGTH)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.WEAKNESS)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.INSTANT_HEAL)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.JUMP)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.SPEED)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.SLOWNESS)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.SPECTRAL_ARROW)).need(new TradeItem(Material.GOLD_INGOT, 8)).need(new TradeItem(Material.ARROW, 64)),
				new Trade(new TradeItem(Material.BOW)).need(new TradeItem(Material.ARROW, 48)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.NIGHT_VISION)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.INVISIBILITY)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.FIRE_RESISTANCE)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.POISON)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.REGEN)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.LUCK)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.TIPPED_ARROW).effect(PotionType.WATER_BREATHING)).need(new TradeItem(Material.ARROW, 8)).need(new TradeItem(Material.IRON_INGOT, 4))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.ARROW, 16).lore(CustomArrows.FIREWORKS.str())).need(new TradeItem(Material.IRON_INGOT, 2)),
				new Trade(new TradeItem(Material.ARROW, 4).lore(CustomArrows.EXPLOSION.str())).need(new TradeItem(Material.GOLD_INGOT, 1)),
				new Trade(new TradeItem(Material.ARROW, 5).lore(CustomArrows.FIRE.str())).need(new TradeItem(Material.EMERALD, 1)),
				new Trade(new TradeItem(Material.ARROW, 5).lore(CustomArrows.DRAGON.str())).need(new TradeItem(Material.EMERALD, 1))
		)
	),

	LIBRAIRE(7, Profession.LIBRARIAN, 10, 1, 1, // TODO A REFAIRE
		new TradeLevel(
				new Trade(new TradeItem(Material.PAPER, 24)).need(new TradeItem(Material.GOLD_INGOT, 1)),
				new Trade(new TradeItem(Material.PAPER, 24)).need(new TradeItem(Material.IRON_INGOT, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.BOOK, 16)).need(new TradeItem(Material.GOLD_INGOT, 2)),
				new Trade(new TradeItem(Material.BOOK, 16)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.PAPER, 64)).need(new TradeItem(Material.GOLD_INGOT, 2)),
				new Trade(new TradeItem(Material.PAPER, 64)).need(new TradeItem(Material.IRON_INGOT, 4))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.BOOK, 32)).need(new TradeItem(Material.GOLD_INGOT, 3)),
				new Trade(new TradeItem(Material.BOOK, 32)).need(new TradeItem(Material.IRON_INGOT, 6))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.GOLD_INGOT, 2)).need(new TradeItem(Material.BOOK, 32)),
				new Trade(new TradeItem(Material.IRON_INGOT)).need(new TradeItem(Material.PAPER, 64)),
				new Trade(new TradeItem(Material.EXPERIENCE_BOTTLE, 64)).need(new TradeItem(Material.EMERALD, 4))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.GOLD_INGOT, 2)).need(new TradeItem(Material.BOOK, 32)),
				new Trade(new TradeItem(Material.IRON_INGOT)).need(new TradeItem(Material.PAPER, 64)),
				new Trade(new TradeItem(Material.EXPERIENCE_BOTTLE, 64)).need(new TradeItem(Material.EMERALD, 4))
		)
	),

	ARMURIER(8, Profession.ARMORER, 10, 1, 2,
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_HELMET)).need(new TradeItem(Material.COBBLESTONE, 64)).need(new TradeItem(Material.IRON_INGOT, 2)),
				new Trade(new TradeItem(Material.IRON_CHESTPLATE)).need(new TradeItem(Material.COBBLESTONE, 64)).need(new TradeItem(Material.IRON_INGOT, 4)),
				new Trade(new TradeItem(Material.IRON_LEGGINGS)).need(new TradeItem(Material.COBBLESTONE, 64)).need(new TradeItem(Material.IRON_INGOT, 3)),
				new Trade(new TradeItem(Material.IRON_BOOTS)).need(new TradeItem(Material.COBBLESTONE, 64)).need(new TradeItem(Material.IRON_INGOT, 1)),

				new Trade(new TradeItem(Material.DIAMOND_HELMET)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.DIAMOND_CHESTPLATE)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.DIAMOND_LEGGINGS)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.DIAMOND_BOOTS)).need(new TradeItem(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_HELMET)).need(new TradeItem(Material.IRON_INGOT, 24)),
				new Trade(new TradeItem(Material.DIAMOND_CHESTPLATE)).need(new TradeItem(Material.IRON_INGOT, 24)),
				new Trade(new TradeItem(Material.DIAMOND_LEGGINGS)).need(new TradeItem(Material.IRON_INGOT, 24)),
				new Trade(new TradeItem(Material.DIAMOND_BOOTS)).need(new TradeItem(Material.IRON_INGOT, 24)),

				new Trade(new TradeItem(Material.DIAMOND_HELMET).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.IRON_INGOT, 32)),
				new Trade(new TradeItem(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.IRON_INGOT, 32)),
				new Trade(new TradeItem(Material.DIAMOND_LEGGINGS).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.IRON_INGOT, 32)),
				new Trade(new TradeItem(Material.DIAMOND_BOOTS).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.IRON_INGOT, 32))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),

				new Trade(new TradeItem(Material.IRON_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.IRON_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.IRON_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.IRON_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 48))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.EMERALD, 3)),
				new Trade(new TradeItem(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.EMERALD, 3)),
				new Trade(new TradeItem(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.EMERALD, 3)),
				new Trade(new TradeItem(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 1)).need(new TradeItem(Material.EMERALD, 3))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_HELMET)).need(new TradeItem(Material.EMERALD, 1)),
				new Trade(new TradeItem(Material.DIAMOND_CHESTPLATE)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.DIAMOND_LEGGINGS)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.DIAMOND_BOOTS)).need(new TradeItem(Material.EMERALD, 1)),

				new Trade(new TradeItem(Material.DIAMOND_HELMET)).need(new TradeItem(Material.IRON_INGOT, 32)),
				new Trade(new TradeItem(Material.DIAMOND_CHESTPLATE)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.DIAMOND_LEGGINGS)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.DIAMOND_BOOTS)).need(new TradeItem(Material.IRON_INGOT, 32))
		)
	),

	ARMES(9, Profession.WEAPONSMITH, 10, 1, 1, // TODO A REFAIRE
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_SWORD)).need(new TradeItem(Material.COBBLESTONE, 64)).need(new TradeItem(Material.IRON_INGOT)),
				new Trade(new TradeItem(Material.DIAMOND_SWORD)).need(new TradeItem(Material.EMERALD, 2)),
				new Trade(new TradeItem(Material.IRON_AXE)).need(new TradeItem(Material.COBBLESTONE, 64)).need(new TradeItem(Material.IRON_INGOT)),
				new Trade(new TradeItem(Material.DIAMOND_AXE)).need(new TradeItem(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_SWORD).enchant(Enchantment.DURABILITY)).need(new TradeItem(Material.IRON_INGOT, 32)),
				new Trade(new TradeItem(Material.DIAMOND_AXE).enchant(Enchantment.DURABILITY)).need(new TradeItem(Material.IRON_INGOT, 32)),
				new Trade(new TradeItem(Material.DIAMOND_SWORD)).need(new TradeItem(Material.IRON_INGOT, 24)),
				new Trade(new TradeItem(Material.DIAMOND_AXE)).need(new TradeItem(Material.IRON_INGOT, 24))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.DIAMOND_AXE).enchant(Enchantment.DAMAGE_ALL, 2)).need(new TradeItem(Material.IRON_INGOT, 48)),
				new Trade(new TradeItem(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 3).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 40)),
				new Trade(new TradeItem(Material.DIAMOND_AXE).enchant(Enchantment.DAMAGE_ALL, 3).enchant(Enchantment.DURABILITY, 2)).need(new TradeItem(Material.IRON_INGOT, 40))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 2).enchant(Enchantment.DURABILITY)).need(new TradeItem(Material.EMERALD, 3)),
				new Trade(new TradeItem(Material.DIAMOND_AXE).enchant(Enchantment.DAMAGE_ALL, 2).enchant(Enchantment.DURABILITY)).need(new TradeItem(Material.EMERALD, 3))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.EMERALD)).need(new TradeItem(Material.DIAMOND_SWORD)),
				new Trade(new TradeItem(Material.EMERALD)).need(new TradeItem(Material.DIAMOND_AXE)),
				new Trade(new TradeItem(Material.IRON_INGOT, 24)).need(new TradeItem(Material.DIAMOND_SWORD)),
				new Trade(new TradeItem(Material.IRON_INGOT, 24)).need(new TradeItem(Material.DIAMOND_AXE))
		)
	),

	FERMIER(10, Profession.FARMER, 10, 1, 1,
		new TradeLevel(
				new Trade(new TradeItem(Material.WHEAT, 6)).need(new TradeItem(Material.WHEAT_SEEDS, 64)),
				new Trade(new TradeItem(Material.MELON, 6)).need(new TradeItem(Material.MELON_SEEDS, 64)),
				new Trade(new TradeItem(Material.PUMPKIN, 6)).need(new TradeItem(Material.PUMPKIN_SEEDS, 64)),
				new Trade(new TradeItem(Material.BEETROOT, 6)).need(new TradeItem(Material.BEETROOT_SEEDS, 64)),
				new Trade(new TradeItem(Material.SUGAR, 40)).need(new TradeItem(Material.SUGAR_CANE, 32))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.WHEAT, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.MELON, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.PUMPKIN, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.BEETROOT_SEEDS, 64))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.POTATO, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.CARROT, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 4)).need(new TradeItem(Material.CHORUS_FRUIT, 64)),
				new Trade(new TradeItem(Material.GOLD_INGOT, 2)).need(new TradeItem(Material.CHORUS_FRUIT, 64))
		),
		new TradeLevel(
				new Trade(new TradeItem(Material.GOLD_INGOT)).need(new TradeItem(Material.CARROT, 64)),
				new Trade(new TradeItem(Material.IRON_INGOT, 2)).need(new TradeItem(Material.POISONOUS_POTATO, 32)),
				new Trade(new TradeItem(Material.IRON_HOE).lore(CustomEnchants.TONDEUSE.str(Main.r.nextInt(2)+1))).need(new TradeItem(Material.EMERALD, 10))
		)
	),

	TERRORISTE(11, Profession.TOOLSMITH, 10, 1, 1,
			new TradeLevel(
					new Trade(new TradeItem(Material.GUNPOWDER, 32)).need(new TradeItem(Material.IRON_INGOT, 5)),
					new Trade(new TradeItem(Material.FLINT, 32)).need(new TradeItem(Material.IRON_INGOT, 6)),
					new Trade(new TradeItem(Material.FLINT, 32)).need(new TradeItem(Material.GOLD_INGOT, 3)),
					new Trade(new TradeItem(Material.FLINT_AND_STEEL)).need(new TradeItem(Material.IRON_NUGGET, 5)).need(new TradeItem(Material.FLINT, 2))
			),
			new TradeLevel(
					new Trade(new TradeItem(Material.TNT)).need(new TradeItem(Material.FIREWORK_ROCKET, 16)).need(new TradeItem(Material.REDSTONE, 4)),
					new Trade(new TradeItem(Material.FIREWORK_ROCKET,20)).need(new TradeItem(Material.PAPER, 2)).need(new TradeItem(Material.GUNPOWDER)),
					new Trade(new TradeItem(Material.FIREWORK_ROCKET,30)).need(new TradeItem(Material.PAPER)).need(new TradeItem(Material.GUNPOWDER, 2)),
					new Trade(new TradeItem(Material.EMERALD)).need(new TradeItem(Material.TNT, 16))
			),
			new TradeLevel(
					new Trade(new TradeItem(Material.GOLD_INGOT, 4)).need(new TradeItem(Material.TNT, 16)),
					new Trade(new TradeItem(Material.IRON_INGOT, 8)).need(new TradeItem(Material.TNT, 16)),
					new Trade(new TradeItem(Material.FIREWORK_ROCKET, 8)).need(new TradeItem(Material.FLINT_AND_STEEL)),
					new Trade(new TradeItem(Material.REDSTONE, 64)).need(new TradeItem(Material.TNT, 16))
			),
			new TradeLevel(
					new Trade(new TradeItem(Material.TNT, 12)).need(new TradeItem(Material.REDSTONE_BLOCK, 32)).need(new TradeItem(Material.REDSTONE, 64)),
					new Trade(new TradeItem(Material.ARROW, 16).lore(CustomArrows.EXPLOSION.str())).need(new TradeItem(Material.TNT, 48)).need(new TradeItem(Material.EMERALD, 4)),
					new Trade(new TradeItem(Material.END_CRYSTAL)).need(new TradeItem(Material.REDSTONE_BLOCK, 32)).need(new TradeItem(Material.REDSTONE, 64))
			)
	),

	ARTISTE(12, Profession.CLERIC, 10, 1, 1,
			new TradeLevel(
					new Trade(new TradeItem(Material.PAINTING)).need(new TradeItem(Material.GREEN_DYE, 8)),
					new Trade(new TradeItem(Material.BOOK)).need(new TradeItem(Material.IRON_INGOT)),
					new Trade(new TradeItem(Material.BOOK).name("§7Livre dédicacé par iTrooz_")).need(new TradeItem(Material.BOOK, 8)),
					new Trade(new TradeItem(Material.PAINTING).name("§7Peinture dédicacée par iTrooz_")).need(new TradeItem(Material.PAINTING, 8))
			),
			new TradeLevel(
					new Trade(new TradeItem(Material.GOLD_INGOT, 2)).need(new TradeItem(Material.BOOK, 24)),
					new Trade(new TradeItem(Material.GOLD_INGOT, 2)).need(new TradeItem(Material.PAINTING, 24)),
					new Trade(new TradeItem(Material.WHITE_BANNER, 1, false, Utils.bmeta)).need(new TradeItem(Material.WHITE_BANNER)),
					new Trade(new TradeItem(Material.IRON_INGOT)).need(new TradeItem(Material.CYAN_DYE, 32))
			),
			new TradeLevel(
					new Trade(new TradeItem(Material.WRITABLE_BOOK, 1)).need(new TradeItem(Material.EMERALD, 2)),
					new Trade(new TradeItem(Material.PAINTING)).need(new TradeItem(Material.EMERALD, 2))
			)
	),
	MYSTERE(13, Profession.CLERIC, 1, 1, 0,
			new TradeLevel(
					new Trade(new TradeItem(Material.GOLDEN_APPLE)).need(new TradeItem(Material.BREAD).name("§rPain divin")),
					new Trade(new TradeItem(Material.ENCHANTED_GOLDEN_APPLE)).need(new TradeItem(Material.BREAD, 64).name("§rPain divin"))
			)
	),


	;


	static{
		Utils.bmeta = (BannerMeta) Bukkit.getItemFactory().getItemMeta(Material.WHITE_BANNER);
		Utils.bmeta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_LEFT));
		Utils.bmeta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_MIDDLE));
		Utils.bmeta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP));
		Utils.bmeta.addPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM));
		Utils.bmeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
	}


	public static Randomiser r = new Randomiser(0, false);

	static{
		for(Villagers v : values())r.max+=v.chance;
	}

	public int id; // custom career
	public Profession p;
	public int chance;
	public TradeLevel[] levels;

	Villagers(int id, Profession p, int chance, int dmin, int drandom, TradeLevel... levels){
		this.id = id;
		this.p = p;
		this.chance = chance;
		for(TradeLevel tl : levels){
			if(tl.min==0&&tl.random==0){
				tl.min = dmin;
				tl.random = drandom;
			}
		}
		this.levels = levels;
	}

	public static Villagers getByID(int id){
		for(Villagers vi : values()){
			if(vi.id==id)return vi;
		}
		return null;
	}

	public static Villagers getType(AbstractVillager v){



		int npcType=0;

		for(String s : v.getScoreboardTags()){
			if(s.startsWith("npctype")){
				npcType = Integer.parseInt(s.split("-")[1]);
			}
		}


		for(Villagers vi : values()){


			if(npcType == vi.id){
				return vi;
			}
		}
		return null;
	}

	public static Villagers getOne(){
		r.regen();
		for(Villagers v : values()){
			if(r.isInNext(v.chance))return v;
		}
		throw new EntasiaException();
	}

	public void apply(Villager v){





		v.setMetadata("lastUpgrade", new FixedMetadataValue(Main.main, System.currentTimeMillis()));
		v.addScoreboardTag("npctype-"+id);
		v.addScoreboardTag("asProfession");
		v.addScoreboardTag("CareerLevel-"+0);

		List<MerchantRecipe> list = new ArrayList<>();
		addToList(list, 1);
		v.setRecipes(list);
		v.setProfession(p);
//		EntityNBT.addNBT(v, new NBTComponent(String.format("{Career:%s}", id))); // est override par le jeu quand upgrade







	}

	public void addToList(List<MerchantRecipe> list, int from){ // current 1-5 (max=5)
		TradeLevel lvl = levels[from];
		int max = lvl.min + Main.r.nextInt(lvl.random+1);
		ArrayList<Trade> tempTrades = new ArrayList<>(Arrays.asList(lvl.trades));
		for(int i=0;i<max;i++){
			if(tempTrades.size()==0)break;
			Trade t = tempTrades.remove(Main.r.nextInt(tempTrades.size()));
			list.add(t.buildOne());
		}

	}

// EN CAS DE % DE CHANCE DANS LES TRADES DANS EUX MÊMES
//	private void addToList(List<MerchantRecipe> list, int current){
//		int max = levels[current].min + Main.r.nextInt(levels[current].random +1);
//		Randomiser r = levels[current].getRandomiser();
//		Trade[] trades = Arrays.copyOf(levels[current].trades, levels[current].trades.length);
//		for(int i=0;i<max;i++){
//			r.regen();
//			for(int j=0;j<trades.length;j++){
//				if(trades[j]!=null&&r.isInNext(trades[j].percent)){
//					r.max-=trades[j].percent;
//					trades[j] = null;
//					list.add(trades[j]);
//					break;
//				}
//			}
//			// ne devrait pas être atteint
//			ServerUtils.permMsg("log.upgradenpc","§cErreur lors du chois des nouveaux tades d'un PNJ ! Infos : "+name()+" "+current);
//		}
//	}
}
