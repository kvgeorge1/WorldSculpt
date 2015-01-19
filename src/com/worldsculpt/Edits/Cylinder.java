package com.worldsculpt.Edits;

import PluginReference.BlockHelper;
import PluginReference.MC_Block;
import PluginReference.MC_BlockType;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.ImprovedMapEdit;
import com.worldsculpt.Variables.Sculptable;

public class Cylinder extends ImprovedMapEdit implements Sculptable {
	private boolean filled;
	private int x, y, z;
	private int radius, axis, height;
	private MC_Block block;

	private void start(MC_World world, int x, int y, int z, int radius, int height, int axis, boolean filled, MC_Block block) {
		setup(world, true);
		this.filled = filled;
		this.axis = CommandHelper.axisDirectionToAxis(axis);
		this.radius = radius;
		this.block = block;
		height -= 1;
		this.height = height;
		this.x = x;
		this.y = y;
		this.z = z;
		int lx, ly, lz, hx, hy, hz;
		lx = (axis == 1 || axis == 2 ? -radius : (axis == 0 ? 0 : -height)) + x;
		ly = (axis == 0 || axis == 2 ? -radius : (axis == 1 ? 0 : -height)) + y;
		lz = (axis == 0 || axis == 1 ? -radius : (axis == 2 ? 0 : -height)) + z;
		hx = (axis == 1 || axis == 2 ? radius : (axis == 0 ? height : 0)) + x;
		hy = (axis == 0 || axis == 2 ? radius : (axis == 1 ? height : 0)) + y;
		hz = (axis == 0 || axis == 1 ? radius : (axis == 2 ? height : 0)) + z;
		setupBoxIterator(lx, ly, lz, hx, hy, hz);
	}

	public boolean calculateNextBlock(BlockChange cal) {
		int[] i = getNextNextBoxIteration();
		if (i == null) {
			block = null;
			return true;
		}
		int sx = i[0] - x;
		int sy = i[1] - y;
		int sz = i[2] - z;
		boolean place;
		if (filled) {
			if (axis == 0)
				place = Math.round(Math.sqrt(sy * sy + sz * sz)) <= radius;
			else if (axis == 1)
				place = Math.round(Math.sqrt(sx * sx + sz * sz)) <= radius;
			else
				place = Math.round(Math.sqrt(sx * sx + sy * sy)) <= radius;
		} else {
			if (axis == 0) {
				if (sx == 0 || sx == height || sx == -height)
					place = Math.round(Math.sqrt(sy * sy + sz * sz)) <= radius;
				else
					place = Math.round(Math.sqrt(sy * sy + sz * sz)) == radius;
			} else if (axis == 1) {
				if (sy == 0 || sy == height || sy == -height)
					place = Math.round(Math.sqrt(sx * sx + sz * sz)) <= radius;
				else
					place = Math.round(Math.sqrt(sx * sx + sz * sz)) == radius;
			} else {
				if (sz == 0 || sz == height || sz == -height)
					place = Math.round(Math.sqrt(sx * sx + sy * sy)) <= radius;
				else
					place = Math.round(Math.sqrt(sx * sx + sy * sy)) == radius;
			}
		}
		if (place) {
			cal.block = block;
			cal.x = i[0];
			cal.y = i[1];
			cal.z = i[2];
		}
		return false;
	}

	public void getCommandHelper(MC_Player player, String[] args, final CommandStarter run) {
		new CommandHelper(player, args) {
			protected int getArgumentType(int argumentNumber) {
				if (argumentNumber == 0)
					return CommandHelper.BLOCK_NAME;
				if (argumentNumber == 1)
					return CommandHelper.INTEGER;
				if (argumentNumber == 2)
					return CommandHelper.INTEGER;
				if (argumentNumber == 3)
					return CommandHelper.AXIS_DIRECTION;
				if (argumentNumber == 4)
					return CommandHelper.BOOLEAN;
				return -1;
			}

			protected Object getDefault(int argumentNumber) {
				if (argumentNumber == 0)
					return MyPlugin.server.getBlockFromName(BlockHelper.getBlockName(MC_BlockType.STONE));
				if (argumentNumber == 1)
					return 3;
				if (argumentNumber == 2)
					return 1;
				if (argumentNumber == 3)
					return 1;
				if (argumentNumber == 4)
					return true;
				return null;
			}

			protected void run(MC_World world, MC_Location location, Object[] requested) {
				run.run(requested);
			}

			protected int getArgumentLength() {
				return 5;
			}
		};
	}

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested) {
		start(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), (int) requested[1], (int) requested[2],
				(int) requested[3], (boolean) requested[4], (MC_Block) requested[0]);
	}

	public String getName() {
		return "cylinder";
	}

	public String getHelpLine() {
		return "/cylinder [block(name)] [radius(#)] [height[#] [axis(x+/y+/z+/x-/y-/z-)] [filled(true/false)]";
	}

	public Sculptable newInstance() {
		return new Cylinder();
	}

	public void leftClick(MC_Block block) {
	}
}