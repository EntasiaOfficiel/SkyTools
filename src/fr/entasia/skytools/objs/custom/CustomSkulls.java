package fr.entasia.skytools.objs.custom;

import com.mojang.authlib.GameProfile;
import fr.entasia.apis.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public enum CustomSkulls {

	SALADE("477dd842c975d8fb03b1add66db8377a18ba987052161f22591e6a4ede7f5", "skull:salad", "§aSalade"),
	TOMATE("69147172072f072483529767fe47554a95a0e0fd9b6cc531b25958a399ef3", "skull:tomato", "§cTomate"),
	POMME("cbb311f3ba1c07c3d1147cd210d81fe11fd8ae9e3db212a0fa748946c3633", "skull:apple")

	;

	public String texture;
	public UUID uuid;
	public GameProfile profile;

	private SkullMeta skullMeta;
	public String name;

	CustomSkulls(String texture, String uuidstr, String name){
		this(texture, uuidstr);

		this.skullMeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		ItemUtils.setTexture(this.skullMeta, profile);
		this.skullMeta.setDisplayName(name);
		this.name = name;
	}

	CustomSkulls(String texture, String uuidstr){
		this.texture = texture;
		this.uuid = UUID.nameUUIDFromBytes(uuidstr.getBytes());
		this.profile = ItemUtils.genProfile(this.uuid, texture);
	}


	public static CustomSkulls getByUUID(UUID uuid){
		for(CustomSkulls cs : values()){
			if(cs.uuid.equals(uuid))return cs;
		}
		return null;
	}

	public ItemStack genItem(int count){
		ItemStack item = new ItemStack(Material.SKULL_ITEM, count, (short)3);
		item.setItemMeta(skullMeta.clone());
		return item;
	}



}
