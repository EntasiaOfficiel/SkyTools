package fr.entasia.skytools.objs;


import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public enum Direction { // a voir pour suppr ?

	// attention : metas inversés
	DOWN(new Vector(0, -1, 0), 1),
	NORTH(new Vector(0, 0, -1), 2),
	EAST(new Vector(1, 0, 0), 5),
	SOUTH(new Vector(0, 0, 1), 3),
	WEST(new Vector(-1, 0, 0), 4),

	;

	public Vector vector;
	public byte data;

	Direction(Vector v, int data) {
		this.vector = v;
		this.data = (byte) data;
	}

	public static Direction get(BlockFace bf) {
		for (Direction d : Direction.values()) {
			if (bf.getModX() == (int) d.vector.getX() && bf.getModY() == (int) d.vector.getY() && bf.getModZ() == (int) d.vector.getZ()) {
				return d;
			}
		}
		return null;
	}
}