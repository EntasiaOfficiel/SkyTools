package fr.entasia.skytools.objs;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public enum DirectionUtils {

	// attention : metas inversés
	DOWN(new Vector(0, -1, 0), BlockFace.DOWN),
	NORTH(new Vector(0, 0, -1), BlockFace.NORTH),
	EAST(new Vector(1, 0, 0), BlockFace.EAST),
	SOUTH(new Vector(0, 0, 1), BlockFace.SOUTH),
	WEST(new Vector(-1, 0, 0), BlockFace.WEST),

	;

	public Vector vector;
	public BlockFace face;

	DirectionUtils(Vector v, BlockFace face) {
		this.vector = v;
		this.face = face;
	}

	public static DirectionUtils get(BlockFace bf) {
		for (DirectionUtils d : DirectionUtils.values()) {
			if (bf.getModX() == (int) d.vector.getX() && bf.getModY() == (int) d.vector.getY() && bf.getModZ() == (int) d.vector.getZ()) {
				return d;
			}
		}
		return null;
	}
}
