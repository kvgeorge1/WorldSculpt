package com.worldsculpt.Variables;

import java.util.Arrays;
import java.util.List;
import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;

public class PlayerTargetBlock {
	public static int[] getTargetBlock(MC_Location location, MC_Server server, int range, List<MC_Block> skip) {
		if (skip == null)
			skip = Arrays.asList(server.getBlockFromName("air"));
		double pitch = Math.toRadians(location.pitch + 90);
		double yaw = Math.toRadians(location.yaw + 90);
		double startX = location.x;
		double startY = location.y;
		double startZ = location.z;
		double endX = Math.sin(pitch) * Math.cos(yaw) * range + startX;
		double endY = Math.cos(pitch) * range + startY;
		double endZ = Math.sin(pitch) * Math.sin(yaw) * range + startZ;
		BlockIterator itr = new BlockIterator(startX, startY, startZ, endX, endY, endZ);
		int[] i;
		int x = (int) Math.floor(endX);
		int y = (int) Math.floor(endY);
		int z = (int) Math.floor(endZ);
		while ((i = itr.getNext()) != null) {
			if (!contains(skip, server.getWorld(location.dimension).getBlockAt(i[0], i[1], i[2]))) {
				x = i[0];
				y = i[1];
				z = i[2];
				break;
			}
		}
		return new int[] { x, y, z };
	}

	private static boolean contains(List<MC_Block> skip, MC_Block check) {
		for (MC_Block b : skip)
			if (b.getId() == check.getId() && b.getSubtype() == check.getSubtype())
				return true;
		return false;
	}

	public static int[] getTargetBlock(MC_Player player, int range, List<MC_Block> skip) {
		return getTargetBlock(
				new MC_Location(player.getLocation().x, player.getLocation().y + 1.62, player.getLocation().z,
						player.getLocation().dimension, player.getLocation().yaw, player.getLocation().pitch), player.getServer(),
				range, skip);
	}
}