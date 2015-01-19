package com.worldsculpt.Edits;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.ImprovedMapEdit;
import com.worldsculpt.Variables.Sculptable;

import PluginReference.MC_Block;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public class Torus extends ImprovedMapEdit implements Sculptable {
	private int majorRadius;
	private int minorRadius;
	private boolean filled;
	private int axis;
	private int x, y, z;
	private MC_Block block;

	private void start(MC_World world, int x, int y, int z, int majorRadius, int minorRadius, boolean filled, int axis,
			MC_Block block) {
		setup(world, true);
		this.majorRadius = majorRadius;
		this.minorRadius = minorRadius;
		this.filled = filled;
		this.axis = axis;
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
		if (axis == 0)
			setupBoxIterator(x - minorRadius, y - (majorRadius + minorRadius), z - (majorRadius + minorRadius), x + minorRadius, y
					+ (majorRadius + minorRadius), z + (majorRadius + minorRadius));
		else if (axis == 1)
			setupBoxIterator(x - (majorRadius + minorRadius), y - minorRadius, z - (majorRadius + minorRadius), x
					+ (majorRadius + minorRadius), y + minorRadius, z + (majorRadius + minorRadius));
		else
			setupBoxIterator(x - (majorRadius + minorRadius), y - (majorRadius + minorRadius), z - minorRadius, x
					+ (majorRadius + minorRadius), y + (majorRadius + minorRadius), z + minorRadius);
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
					return CommandHelper.BOOLEAN;
				if (argumentNumber == 4)
					return CommandHelper.AXIS;
				return -1;
			}

			protected Object getDefault(int argumentNumber) {
				if (argumentNumber == 0)
					return MyPlugin.server.getBlockFromName("stone");
				if (argumentNumber == 1)
					return 15;
				if (argumentNumber == 2)
					return 5;
				if (argumentNumber == 3)
					return true;
				if (argumentNumber == 4)
					return 1;
				return null;
			}

			protected int getArgumentLength() {
				return 5;
			}

			protected void run(MC_World world, MC_Location location, Object[] requested) {
				run.run(requested);
			}
		};
	}

	public boolean calculateNextBlock(BlockChange cal) {
		int[] i = getNextNextBoxIteration();
		if (i == null) {
			block = null;
			return true;
		}
		cal.x = i[0];
		cal.y = i[1];
		cal.z = i[2];
		int sx = i[0] - x;
		int sy = i[1] - y;
		int sz = i[2] - z;
		boolean place;
		if (filled) {
			if (axis == 0)
				place = Math.round(Math.sqrt(sx * sx + Math.pow(Math.sqrt(sy * sy + sz * sz) - majorRadius, 2))) <= minorRadius;
			else if (axis == 1)
				place = Math.round(Math.sqrt(sy * sy + Math.pow(Math.sqrt(sx * sx + sz * sz) - majorRadius, 2))) <= minorRadius;
			else
				place = Math.round(Math.sqrt(sz * sz + Math.pow(Math.sqrt(sx * sx + sy * sy) - majorRadius, 2))) <= minorRadius;
		} else {
			if (axis == 0)
				place = Math.round(Math.sqrt(sx * sx + Math.pow(Math.sqrt(sy * sy + sz * sz) - majorRadius, 2))) == minorRadius;
			else if (axis == 1)
				place = Math.round(Math.sqrt(sy * sy + Math.pow(Math.sqrt(sx * sx + sz * sz) - majorRadius, 2))) == minorRadius;
			else
				place = Math.round(Math.sqrt(sz * sz + Math.pow(Math.sqrt(sx * sx + sy * sy) - majorRadius, 2))) == minorRadius;
		}
		if (place)
			cal.block = block;
		return false;
	}

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested) {
		start(world, (int) location.x, (int) location.y, (int) location.z, (int) requested[1], (int) requested[2],
				(boolean) requested[3], (int) requested[4], (MC_Block) requested[0]);
	}

	public String getName() {
		return "torus";
	}

	public String getHelpLine() {
		return "/torus [block(name)] [major radius(#)] [minor radius(#)] [filled(true/false)] [axis(x/y/z)]";
	}

	public Sculptable newInstance() {
		return new Torus();
	}

	public void leftClick(MC_Block block) {
	}
}