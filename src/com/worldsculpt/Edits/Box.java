package com.worldsculpt.Edits;

import com.worldsculpt.Variables.BlockChange;
import com.worldsculpt.Variables.CommandHelper;
import com.worldsculpt.Variables.CommandStarter;
import com.worldsculpt.Variables.ImprovedMapEdit;
import com.worldsculpt.Variables.Sculptable;

import PluginReference.BlockHelper;
import PluginReference.MC_Block;
import PluginReference.MC_BlockType;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_World;
import WorldSculpt.MyPlugin;

public class Box extends ImprovedMapEdit implements Sculptable {
	private boolean filled;
	private int x, y, z;
	private int rx, ry, rz;
	private MC_Block block;

	private void start(MC_World world, int x, int y, int z, int cx, int cy, int cz, boolean filled, MC_Block block) {
		setup(world, true);
		this.filled = filled;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rx = cx / 2;
		this.ry = cy / 2;
		this.rz = cz / 2;
		this.block = block;
		setupBoxIterator(x - rx, y - ry, z - rz, x + rx, y + ry, z + rz);
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
					return CommandHelper.INTEGER;
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
					return 3;
				if (argumentNumber == 3)
					return 3;
				if (argumentNumber == 4)
					return true;
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
		if (filled || (Math.abs(i[0] - x) == rx || Math.abs(i[1] - y) == ry || Math.abs(i[2] - z) == rz))
			cal.block = block;
		return false;
	}

	public String getName() {
		return "box";
	}

	public String getHelpLine() {
		return "/box [block(name)] [length x(#)] [length y(#)] [length z(#)] [filled(true/false)]";
	}

	public void run(MC_World world, MC_Location location, MC_Player player, Object[] requested) {
		start(world, (int) location.x, (int) location.y, (int) location.z, (int) requested[1], (int) requested[2],
				(int) requested[3], (boolean) requested[4], (MC_Block) requested[0]);
	}

	public Sculptable newInstance() {
		return new Box();
	}

	public void leftClick(MC_Block block) {
	}
}